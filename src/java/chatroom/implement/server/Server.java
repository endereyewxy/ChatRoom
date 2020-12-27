package chatroom.implement.server;

import chatroom.protocol.IServer;
import chatroom.protocol.IServerSocket;
import chatroom.protocol.Protocol;
import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;
import chatroom.util.Log;
import chatroom.util.MD5;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Server implements IServer {
    private IServerSocket socket;

    private int globalUuidGenerator = 0;

    // @formatter:off
    private final HashMap<String , User   > userN2O = new HashMap<>();
    private final HashMap<Integer, User   > userI2O = new HashMap<>();
    private final HashMap<Integer, User   > userC2O = new HashMap<>();
    private final HashMap<User   , Integer> userO2C = new HashMap<>();
    private final HashMap<Integer, Chat   > chatI2O = new HashMap<>();
    // @formatter:on

    private final HashMap<User, HashSet<Chat>> relationU2C = new HashMap<>();
    private final HashMap<Chat, HashSet<User>> relationC2U = new HashMap<>();

    public Server() {
        try (final FileInputStream inputStream = new FileInputStream(System.getProperty("server.user"))) {
            final Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNext()) {
                final String name = scanner.next();
                final String pass = scanner.next();
                userN2O.put(name, new User(++globalUuidGenerator, name, MD5.md5(pass)));
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            Log.server("Cannot start server because no user authentication data is provided");
            Log.failed();
        }
    }

    @Override
    public void bind(IServerSocket socket) {
        this.socket = socket;
    }

    @Override
    public void requestSignIn(int client, String name, String pMd5) throws IOException {
        final User user = userN2O.get(name);

        if (user == null) {
            socket.notifySignInRejected(client, Protocol.REASON_BAD_NAME);
            Log.server("Client %d failed to sign in because of wrong username", client);
            return;
        }

        if (!user.getPMd5().equals(pMd5)) {
            socket.notifySignInRejected(client, Protocol.REASON_BAD_PASS);
            Log.server("Client %d failed to sign in because of wrong password", client);
            return;
        }

        if (userC2O.containsKey(client)) {
            socket.notifySignInRejected(client, Protocol.REASON_ALREADY_SIGNED_IN);
            Log.server("Client %d failed to sign in because he/she has already signed in", client);
            return;
        }

        if (userI2O.containsKey(user.getUuid())) {
            socket.notifySignInRejected(client, Protocol.REASON_ANOTHER_SIGNED_IN);
            Log.server("Client %d failed to sign in because another client has already signed in", client);
            return;
        }

        // @formatter:off
        userI2O    .put        (user.getUuid(), user);
        userO2C    .put        (user          , client);
        userC2O    .put        (client        , user);
        relationU2C.putIfAbsent(user          , new HashSet<>());
        // @formatter:on
        socket.notifySignInAccepted(client, user.getUuid());
        doUpdateChatList(client);
        for (final int clit : userC2O.keySet())
            doUpdateUserList(clit);

        Log.server("Client %d successfully signed in as %d", client, user.getUuid());
    }

    @Override
    public void clientOffline(int client) throws IOException {
        final User user = userC2O.get(client);

        if (user == null) {
            Log.server("Invalid request");
            return;
        }

        userI2O.remove(user.getUuid());
        userO2C.remove(user);
        userC2O.remove(client);

        for (final int clit : userO2C.values())
            doUpdateUserList(clit);

        for (final Chat chat : relationU2C.get(user))
            doQuitChat(chat, user, Protocol.REASON_OFFLINE);
    }

    @Override
    public void requestChatInfo(int client, int chat) throws IOException {
        final User userObj = userC2O.get(client);
        final Chat chatObj = chatI2O.get(chat);

        if (userObj == null || chatObj == null) {
            Log.server("Invalid request");
            return;
        }

        final HashSet<User> chatInfo = relationC2U.get(chatObj);
        if (chatInfo.contains(userObj))
            socket.updateChatInfo(client, chatInfo.toArray(new User[0]));
    }

    @Override
    public void requestInitChat(int client, String name, Integer[] members) throws IOException {
        final User user = userC2O.get(client);
        final Chat chat = new Chat(++globalUuidGenerator, name, user.getUuid());

        // @formatter:off
        chatI2O    .put(chat.getUuid(), chat);
        relationC2U.put(chat          , new HashSet<>());
        // @formatter:on

        for (final int clit : userO2C.values())
            doUpdateChatList(clit);
        doJoinChat(chat, user, Protocol.REASON_CREATE);
        for (final int member : members)
            doJoinChat(chat, userI2O.get(member), Protocol.REASON_NORMAL);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void requestJoinChat(int client, int chat, int user) throws IOException {
        final User userObj = userC2O.get(client);
        final Chat chatObj = chatI2O.get(chat);
        final User joinObj = userI2O.get(user);

        if (userObj == null || chatObj == null || joinObj == null) {
            Log.server("Invalid request");
            return;
        }

        if (userObj == joinObj) {
            socket.notifyChatJoinRequest(userO2C.get(userI2O.get(chatObj.getInit())), chat, user);
            Log.server("User %d wish to join chat %d, waiting for initiator's agreement", user, chat);
            return;
        }

        if (userObj.getUuid() == chatObj.getInit())
            doJoinChat(chatObj, joinObj, Protocol.REASON_NORMAL);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void requestQuitChat(int client, int chat, int user) throws IOException {
        final User userObj = userC2O.get(client);
        final Chat chatObj = chatI2O.get(chat);
        final User quitObj = userI2O.get(user);

        if (userObj == null || chatObj == null || quitObj == null) {
            Log.server("Invalid request");
            return;
        }

        if (userObj.getUuid() == chatObj.getInit()) {
            if (quitObj == userObj) {
                doQuitChat(chatObj, quitObj, Protocol.REASON_SELF_QUIT);
                for (final User u : relationC2U.get(chatObj))
                    doQuitChat(chatObj, u, Protocol.REASON_CHAT_QUIT);
                chatI2O.remove(chat);
                relationC2U.remove(chatObj);

                for (final int clit : userO2C.values())
                    doUpdateChatList(clit);
            } else {
                doQuitChat(chatObj, quitObj, Protocol.REASON_INIT_QUIT);
            }
            return;
        }

        if (userObj == quitObj)
            doQuitChat(chatObj, quitObj, Protocol.REASON_SELF_QUIT);
    }

    @Override
    public void sendMessage(int client, int uuid, String msg) throws IOException {
        final User user = userI2O.get(uuid);
        final Chat chat = chatI2O.get(uuid);
        final User from = userC2O.get(client);

        if (user != null) {
            socket.notifyMessage(userO2C.get(user), from.getUuid(), from.getUuid(), msg);
            if (user != from)
                socket.notifyMessage(userO2C.get(from), from.getUuid(), from.getUuid(), msg);
            Log.server("Message U2U %d -> %d: \"%s\"", from.getUuid(), uuid, msg);
        }

        if (chat != null) {
            for (final User member : relationC2U.get(chat))
                socket.notifyMessage(userO2C.get(member), chat.getUuid(), from.getUuid(), msg);
            Log.server("Message U2C %d -> %d: \"%s\"", from.getUuid(), uuid, msg);
        }
    }

    @Override
    public void sendFileMsg(int client, int uuid, byte[] data) throws IOException {
        final User user = userI2O.get(uuid);
        final Chat chat = chatI2O.get(uuid);
        final User from = userC2O.get(client);

        if (user != null) {
            socket.notifyFileMsg(userO2C.get(user), from.getUuid(), from.getUuid(), data);
            if (user != from)
                socket.notifyFileMsg(userO2C.get(from), from.getUuid(), from.getUuid(), data);
            Log.server("File message U2U %d -> %d", from.getUuid(), uuid);
        }

        if (chat != null) {
            for (final User member : relationC2U.get(chat))
                socket.notifyFileMsg(userO2C.get(member), chat.getUuid(), from.getUuid(), data);
            Log.server("File message U2C %d -> %d", from.getUuid(), uuid);
        }
    }

    private void doUpdateUserList(int client) throws IOException {
        socket.updateUserList(client, userI2O.values().toArray(new User[0]));
    }

    private void doUpdateChatList(int client) throws IOException {
        socket.updateChatList(client, chatI2O.values().toArray(new Chat[0]));
    }

    private void doJoinChat(Chat chat, User user, byte reason) throws IOException {
        final HashSet<Chat> chats = relationU2C.get(user);
        final HashSet<User> users = relationC2U.get(chat);

        if (users.contains(user)) {
            Log.server("User %d tried to join chat %d but he/she is already in it", user.getUuid(), chat.getUuid());
            return;
        }

        chats.add(chat);
        users.add(user);
        for (final User u : users)
            socket.notifyChatJoined(userO2C.get(u), chat.getUuid(), user.getUuid(), reason);

        Log.server("User %d joined chat %d", user.getUuid(), chat.getUuid());
    }

    private void doQuitChat(Chat chat, User user, byte reason) throws IOException {
        final HashSet<Chat> chats = relationU2C.get(user);
        final HashSet<User> users = relationC2U.get(chat);

        if (!users.contains(user)) {
            Log.server("User %d tried to quit chat %d but he/she isn't in it", user.getUuid(), chat.getUuid());
            return;
        }

        for (final User u : users)
            socket.notifyChatQuited(userO2C.get(u), chat.getUuid(), user.getUuid(), reason);
        chats.remove(chat);
        users.remove(user);

        Log.server("User %d quited chat %d", user.getUuid(), chat.getUuid());
    }
}