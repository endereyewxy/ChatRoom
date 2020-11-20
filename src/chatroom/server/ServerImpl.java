package chatroom.server;

import chatroom.protocol.Flags;
import chatroom.protocol.IServerApp;
import chatroom.protocol.IServerSocket;
import chatroom.util.MD5;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.LongStream;

public class ServerImpl implements IServerApp {
    private IServerSocket socket = null;

    private boolean noAuthenticationMode = false;

    private static class User {
        public final String username;
        public final String password;

        private User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    // @formatter:off
    private final HashMap<String, User> usersByUsername = new HashMap<>();
    private final HashMap<Long,   User> usersByClientId = new HashMap<>();
    // @formatter:on

    public static class Group {
        public final String name;
        public final long owner;
        public final HashSet<Long> members = new HashSet<>();

        public Group(String name, long owner) {
            this.name = name;
            this.owner = owner;
        }
    }

    private final ArrayList<Group> groups = new ArrayList<>();

    private static class Request {
        public final long groupId;
        public final long joinerId;

        private Request(long groupId, long joinerId) {
            this.groupId = groupId;
            this.joinerId = joinerId;
        }
    }

    private long reqIdCounter = 0;

    private final HashMap<Long, Request> requests = new HashMap<>();

    public ServerImpl() {
        final String pathAuthentication = System.getProperty("server.path.auth");
        try (final FileInputStream stream = new FileInputStream(new File(pathAuthentication))) {
            final Scanner scanner = new Scanner(stream);
            while (scanner.hasNext()) {
                final String username = scanner.next();
                usersByUsername.put(username, new User(username, MD5.md5(scanner.next())));
            }
        } catch (NullPointerException | IOException e) {
            System.out.println("Warning: Running under no-authentication mode.");
            noAuthenticationMode = true;
        }
    }

    // Implementations of the interface.

    @Override
    public void bind(IServerSocket socket) {
        this.socket = socket;
    }

    @Override
    public void onRequestLogin(long client, String username, String passwordMd5) {
        final User user;
        if (noAuthenticationMode)
            // No-authentication mode allows every login request.
            user = new User(username, passwordMd5);
        else
            user = usersByUsername.get(username);
        if (user != null && passwordMd5.equals(user.password)) {
            usersByClientId.put(client, user);
            socket.replyLogin(client, client);
        } else {
            socket.replyLogin(client, 0L);
        }
    }

    @Override
    public void onRequestUserList(long client) {
        socket.replyUserList(client,
                usersByClientId.keySet()
                        .toArray(new Long[0]),
                // Use keySet().stream() instead of values().stream() to prevent disordering.
                usersByClientId.keySet().stream()
                        .map(id -> usersByClientId.get(id).username)
                        .toArray(String[]::new));
    }

    @Override
    public void onRequestGroupList(long client) {
        socket.replyGroupList(client,
                LongStream.rangeClosed(1, groups.size())
                        .boxed()
                        .toArray(Long[]::new),
                groups.stream()
                        .map(group -> group.name)
                        .toArray(String[]::new),
                groups.stream()
                        .map(group -> Flags.of(group.owner == client, group.members.contains(client)))
                        .toArray(Byte[]::new));
    }

    @Override
    public void onRequestCreateGroup(long client, String groupName) {
        groups.add(new Group(groupName, client));
        socket.replyGroupJoined(client, (byte) 2, groups.size());
    }

    @Override
    public void onRequestJoinGroup(long client, long groupId) {
        if (0 < groupId && groupId <= groups.size()) {
            reqIdCounter += 1;
            requests.put(reqIdCounter, new Request(groupId, client));
            socket.notifyOtherRequestJoinGroup(
                    groups.get((int) (groupId - 1)).owner,
                    groupId,
                    client,
                    reqIdCounter);
        }
    }

    @Override
    public void onRequestInvite(long client, long groupId, long userId) {
        if (0 < groupId && groupId <= groups.size()) {
            final Group group = groups.get((int) (groupId - 1));
            if (group.owner == client && !group.members.contains(userId)) {
                group.members.add(userId);
                socket.replyGroupJoined(userId, (byte) 3, groupId);
            }
        }
    }

    @Override
    public void onRequestLeaveGroup(long client, long groupId) {
        if (0 < groupId && groupId <= groups.size()) {
            final Group group = groups.get((int) (groupId - 1));
            if (group.members.contains(client)) {
                group.members.remove(client);
                socket.notifyGroupLeft(client, groupId);
            }
        }
    }

    @Override
    public void onRequestSendMessage(long client, long groupId, String msg) {
        if (0 < groupId && groupId <= groups.size()) {
            final Group group = groups.get((int) (groupId - 1));
            if (group.members.contains(client))
                group.members.forEach(
                        id -> socket.notifyMessage(id, groupId, client, msg));
        }
    }

    @Override
    public void onOtherRequestJoinGroupReplied(long client, long reqId, boolean agree) {
        if (requests.containsKey(reqId)) {
            final Request request = requests.remove(reqId);
            socket.replyGroupJoined(request.joinerId, (byte) (agree ? 1 : 0), request.groupId);
        }
    }

    @Override
    public void onRequestGroupMemberList(long client, long groupId) {
        if (0 < groupId && groupId <= groups.size()) {
            final Group group = groups.get((int) (groupId - 1));
            socket.replyRequestMemberList(client, groupId,
                    group.members
                            .toArray(new Long[0]),
                    group.members.stream()
                            .map(id -> usersByClientId.get(id).username)
                            .toArray(String[]::new),
                    group.members.stream()
                            .map(id -> Flags.of(group.owner == id, true))
                            .toArray(Byte[]::new));
        }
    }

    @Override
    public void onUserLogout(long client) {
        usersByClientId.remove(client);
    }
}
