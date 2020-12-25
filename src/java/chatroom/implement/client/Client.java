package chatroom.implement.client;

import chatroom.implement.client.ui.UI;
import chatroom.protocol.IClient;
import chatroom.protocol.IClientSocket;
import chatroom.protocol.Protocol;
import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;
import chatroom.util.Log;
import javafx.application.Platform;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Client implements IClient {
    private static Client instance = null;

    public static Client getInstance() {
        return instance;
    }

    private IClientSocket socket;
    private UI            uiCtrl;
    private Integer       myself = null;

    @FunctionalInterface
    public interface SocketOperation {
        void doOperation(IClientSocket socket) throws IOException;
    }

    public IClientSocket getSocket() {
        return socket;
    }

    public int getMyself() {
        return Objects.requireNonNull(myself);
    }

    public void doWithSocket(SocketOperation operation) {
        try {
            operation.doOperation(socket);
        } catch (IOException e) {
            e.printStackTrace();
            Log.client("Socket IO failure, may break normal functionality");
        }
    }

    private final HashMap<Integer, User> users = new HashMap<>();
    private final HashMap<Integer, Chat> chats = new HashMap<>();

    private final HashMap<Integer, Chat>                              myChats = new HashMap<>();
    private final HashMap<Integer, LinkedList<Pair<Integer, String>>> history = new HashMap<>();
    private final HashMap<Integer, LinkedList<String>>                p2pChat = new HashMap<>();

    public Client() {
        instance = this; // there should be only one client
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public HashMap<Integer, Chat> getChats() {
        return chats;
    }

    public HashMap<Integer, Chat> getMyChats() {
        return myChats;
    }

    public HashMap<Integer, LinkedList<Pair<Integer, String>>> getHistory() {
        return history;
    }

    public HashMap<Integer, LinkedList<String>> getP2pChat() {
        return p2pChat;
    }

    @Override
    public void bind(IClientSocket socket) {
        this.socket = socket;
        UI.initializeGUI();
    }

    public void bind(UI uiCtrl) {
        this.uiCtrl = uiCtrl;
    }

    @Override
    public void notifySignInAccepted(int uuid) {
        myself = uuid;
        Platform.runLater(() -> uiCtrl.switchToMain());
    }

    @Override
    public void notifySignInRejected(byte why) {
        final String msg;
        switch (why) {
            case Protocol.REASON_BAD_NAME:
                msg = "用户不存在";
                break;
            case Protocol.REASON_BAD_PASS:
                msg = "密码错误";
                break;
            case Protocol.REASON_ANOTHER_SIGNED_IN:
                msg = "该用户已在另一个客户端上登录";
                break;
            default:
                msg = "登录失败";
                Log.client("Sign in failure reason is unexpected: %0x02x", why);
        }
        Platform.runLater(() -> UI.error(msg));
    }

    @Override
    public void updateUserList(User[] userArray) throws IOException {
        users.clear();
        users.putAll(Arrays.stream(userArray).collect(Collectors.toMap(User::getUuid, Function.identity())));

        for (final User user : userArray)
            p2pChat.putIfAbsent(user.getUuid(), new LinkedList<>());
        for (final int uuid : p2pChat.keySet()) {
            if (!users.containsKey(uuid))
                p2pChat.remove(uuid);
        }

        Platform.runLater(() -> uiCtrl.getMainController().updateUserList(null, userArray));
    }

    @Override
    public void updateChatList(Chat[] chatArray) throws IOException {
        chats.clear();
        chats.putAll(Arrays.stream(chatArray).collect(Collectors.toMap(Chat::getUuid, Function.identity())));

        Platform.runLater(() -> uiCtrl.getMainController().updateChatList(chatArray));
    }

    @Override
    public void updateChatInfo(User[] users) throws IOException {
//        Platform.runLater(() -> uiCtrl.updateChatInfo(users));
    }

    @Override
    public void notifyChatJoined(int chat, int user, byte reason) throws IOException {
        if (user == myself) {
            myChats.put(chat, chats.get(chat));
            history.put(chat, new LinkedList<>());
        }
        Platform.runLater(() -> uiCtrl.getMainController().insertChatIntoMenu(chats.get(chat)));
    }

    @Override
    public void notifyChatQuited(int chat, int user, byte reason) throws IOException {
        if (user == myself) {
            myChats.remove(chat);
            history.remove(chat);
        }
        Platform.runLater(() -> uiCtrl.getMainController().removeChatFromMenu(chats.get(chat)));
    }

    @Override
    public void notifyChatJoinRequest(int chat, int user) throws IOException {
//        Platform.runLater(() -> uiCtrl.notifyChatJoinRequest(chat, user));
    }

    @Override
    public void notifyMessage(int chat, int from, String msg) throws IOException {
        if (chat == from)
            p2pChat.get(chat).add(msg);
        else
            history.get(chat).add(new Pair<>(from, msg));

//        Platform.runLater(() -> uiCtrl.notifyMessage(chat, from, msg));
    }
}
