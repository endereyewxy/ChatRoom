package chatroom.implement.server;

import chatroom.protocols.IServer;
import chatroom.protocols.IServerSocket;
import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.Flag;
import chatroom.protocols.entity.User;
import chatroom.util.MD5;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

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
            while (scanner.hasNext()) {
                final String username = scanner.next();
                final String password = scanner.next();
                userData.put(username, new User(userList.size() + 1, (byte) 0, username, MD5.md5(password)));
                userList.add(null);
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bind(IServerSocket socket) {
        this.socket = socket;
    }

    @Override
    public void clientClosed(int client) throws IOException {
        userList.set(c2u.get(client).getUuid() - 1, null);
        u2c.remove(c2u.get(client).getUuid());
        c2u.remove(client);
        for (final int cli : c2u.keySet())
            acquireUserList(cli);
    }

    @Override
    public void acquireUserList(int client) throws IOException {
        socket.replyUserList(client, userList.stream()
                                             .filter(Objects::nonNull)
                                             .toArray(User[]::new));
    }

    @Override
    public void acquireChatList(int client) throws IOException {
        final User user = c2u.get(client);
        socket.replyChatList(client, chatList.stream()
                                             .filter(Objects::nonNull)
                                             .map(chat -> chat.shadow(Flag.of(user, chat)))
                                             .toArray(Chat[]::new));
    }

    @Override
    public void acquireChatMemberList(int client, int chatUuid) throws IOException {
        final Chat chat = chatList.get(chatUuid - 1);
        socket.replyChatMemberList(client, chat.getMembers().stream()
                                               .filter(Objects::nonNull)
                                               .map(uuid -> userList.get(uuid - 1))
                                               .map(user -> user.shadow(Flag.of(user, chat)))
                                               .toArray(User[]::new));
    }

    @Override
    public void requestSignIn(int client, String username, String password) throws IOException {
        User matched = userData.get(username);
        if (matched == null && !authMode) {
            matched = new User(userList.size() + 1, (byte) 0, username, password);
            userData.put(matched.getUsername(),
                         matched);
            userList.add(matched);
        }
        if (matched != null && matched.getPassword().equals(password)) {
            u2c.put(matched.getUuid(), client);
            c2u.put(client, matched);
            userList.set(matched.getUuid() - 1, matched);
            socket.notifySignInSucceeded(client, matched.getUuid());
            for (int cli : c2u.keySet())
                acquireUserList(cli);
        } else {
            socket.notifySignInSucceeded(client, 0);
        }
    }

    @Override
    public void requestCreateChat(int client, String name, User[] users) throws IOException {
        final User user = c2u.get(client);
        final Chat chat = new Chat(chatList.size() + 1, (byte) 0, name, user.getUuid(), new HashSet<>());
        chat.getMembers().add(user.getUuid());
        for (final User u : users)
            chat.getMembers().add(u.getUuid());
        chatList.add(chat);
        for (int cli : c2u.keySet())
            acquireChatList(cli);
    }

    @Override
    public void requestJoinChat(int client, int userUuid, int chatUuid) throws IOException {
        final User user = c2u.get(client);
        final Chat chat = chatList.get(chatUuid - 1);

        if (chat.getMembers().contains(userUuid))
            return;

        if (user.getUuid() == chat.getCreator())
            chat.getMembers().add(userUuid);
        else if (user.getUuid() == userUuid)
            socket.notifyChatJoinRequest(u2c.get(chat.getCreator()), userUuid, chatUuid);
    }

    @Override
    public void requestQuitChat(int client, int userUuid, int chatUuid) throws IOException {
        final User user = c2u.get(client);
        final Chat chat = chatList.get(chatUuid - 1);

        if (!chat.getMembers().contains(userUuid))
            return;

        if (user.getUuid() == chat.getCreator() && user.getUuid() == userUuid) {
            chatList.set(chatUuid - 1, null);
            for (int cli : c2u.keySet())
                acquireChatList(cli);
        } else if (user.getUuid() == chat.getCreator() || user.getUuid() == userUuid) {
            chat.getMembers().remove(userUuid);
        }
    }

    @Override
    public void requestSendMessage(int client, int chatUuid, String text) throws IOException {
        for (final int userUuid : chatList.get(chatUuid - 1).getMembers()) {
            if (u2c.containsKey(userUuid))
                socket.notifyMessageReceived(u2c.get(userUuid), c2u.get(client).getUuid(), chatUuid, text);
        }
    }
}