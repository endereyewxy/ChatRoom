package chatroom.implement.server;

import chatroom.protocols.IServer;
import chatroom.protocols.IServerSocket;
import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.Flag;
import chatroom.protocols.entity.User;
import chatroom.util.MD5;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Server implements IServer {
    private IServerSocket socket;

    private final HashMap<String, User> userData = new HashMap<>();
    private final boolean               authMode = Boolean.parseBoolean(System.getProperty("server.authMode"));

    private final ArrayList<User> userList = new ArrayList<>();
    private final ArrayList<Chat> chatList = new ArrayList<>();

    private final HashMap<Integer, Integer> u2c = new HashMap<>();
    private final HashMap<Integer, User>    c2u = new HashMap<>();

    public Server() {
        try (final FileInputStream inputStream = new FileInputStream(System.getProperty("server.userData"))) {
            final Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                final String username = scanner.next();
                final String password = scanner.next();
                userData.put(username,
                             new User(userList.size() + 1, (byte) 0, username, MD5.md5(password)));
                userList.add(new User(userList.size() + 1, (byte) 0, username, ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bind(IServerSocket socket) {
        this.socket = socket;
    }

    @Override
    public void acquireUserList(int client) throws IOException {
        socket.replyUserList(client, userList.toArray(new User[0]));
    }

    @Override
    public void acquireChatList(int client) throws IOException {
        socket.replyChatList(client, chatList.toArray(new Chat[0]));
    }

    @Override
    public void acquireChatMemberList(int client, int chatUuid) throws IOException {
        final Chat chat = chatList.get(chatUuid - 1);
        socket.replyChatMemberList(client, chat.getMembers().stream()
                                               .map(uuid -> userList.get(uuid - 1))
                                               .map(user -> user.shadow(Flag.of(user, chat)))
                                               .toArray(User[]::new));
    }

    @Override
    public void requestSignIn(int client, String username, String password) throws IOException {
        User matched = userData.get(username);
        if (matched == null && !authMode) {
            matched = new User(userList.size() + 1, (byte) 0, username, MD5.md5(password));
            userData.put(matched.getUsername(),
                         matched);
            userList.add(matched);
        }
        if (matched != null) {
            u2c.put(matched.getUuid(), client);
            c2u.put(client, matched);
            socket.notifySignInSucceeded(client, matched.getUuid());
        }
    }

    @Override
    public void requestCreateChat(int client, String name) throws IOException {
        final User user = c2u.get(client);
        final Chat chat = new Chat(chatList.size() + 1, (byte) 0, name, user.getUuid(), new HashSet<>());
        chat.getMembers().add(user.getUuid());
        chatList.add(chat);
        for (int cli : c2u.keySet()) {
            socket.notifyChatMembersChanged(cli,
                                            user.shadow(Flag.ofJoined(user, chat, true)),
                                            chat.shadow(Flag.ofJoined(user, chat, true)));
        }
    }

    @Override
    public void requestJoinChat(int client, int userUuid, int chatUuid) throws IOException {
        final User user = c2u.get(client);
        final User join = userList.get(userUuid - 1);
        final Chat chat = chatList.get(chatUuid - 1);

        if (chat.getMembers().contains(userUuid))
            return;

        if (user.getUuid() == chat.getCreator()) {
            chat.getMembers().add(userUuid);
            for (int cli : c2u.keySet()) {
                socket.notifyChatMembersChanged(cli,
                                                join.shadow(Flag.ofJoined(join, chat, user.getUuid() == userUuid)),
                                                chat.shadow(Flag.ofJoined(join, chat, user.getUuid() == userUuid)));
            }
        } else if (user.getUuid() == userUuid) {
            socket.notifyChatJoinRequest(u2c.get(chat.getCreator()), userUuid, chatUuid);
        }
    }

    @Override
    public void requestQuitChat(int client, int userUuid, int chatUuid) throws IOException {
        final User user = c2u.get(client);
        final User quit = userList.get(userUuid - 1);
        final Chat chat = chatList.get(chatUuid - 1);

        if (!chat.getMembers().contains(userUuid))
            return;

        if (user.getUuid() == chat.getCreator()) {
            if (userUuid == user.getUuid())
                return;
            chat.getMembers().remove(userUuid);
            for (int cli : c2u.keySet()) {
                socket.notifyChatMembersChanged(cli,
                                                quit.shadow(Flag.ofQuited(quit, chat, false)),
                                                chat.shadow(Flag.ofQuited(quit, chat, false)));
            }
        } else if (user.getUuid() == userUuid) {
            chat.getMembers().remove(userUuid);
            for (int cli : c2u.keySet()) {
                socket.notifyChatMembersChanged(cli,
                                                quit.shadow(Flag.ofQuited(quit, chat, true)),
                                                chat.shadow(Flag.ofQuited(quit, chat, true)));
            }
        }
    }

    @Override
    public void requestSendMessage(int client, int chatUuid, String text) throws IOException {
        for (final int userUuid : chatList.get(chatUuid - 1).getMembers())
            socket.notifyMessageReceived(u2c.get(userUuid), c2u.get(client).getUuid(), chatUuid, text);
    }
}