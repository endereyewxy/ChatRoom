package chatroom.implement.socket;

import chatroom.protocol.IClient;
import chatroom.protocol.IClientSocket;
import chatroom.protocol.Protocol;
import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;
import chatroom.util.ByteIStream;
import chatroom.util.ByteOStream;
import chatroom.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ClientSocket extends Socket implements IClientSocket {
    private IClient client;

    private final ByteIStream iStream;
    private final ByteOStream oStream;

    public ClientSocket(String host, int port) throws IOException {
        super(host, port);
        iStream = new ByteIStream(getInputStream());
        oStream = new ByteOStream(getOutputStream());
    }

    @Override
    public void bind(IClient client) {
        this.client = client;
    }

    @Override
    public void init() {
        new Thread(() -> {
            try {
                while (true) {
                    final byte control = iStream.readByte();
                    switch (control) {
                        case Protocol.S2C_SIGN_IN_ACCEPTED:
                            client.notifySignInAccepted(iStream.readUuid());
                            break;
                        case Protocol.S2C_SIGN_IN_REJECTED:
                            client.notifySignInRejected(iStream.readByte());
                            break;
                        case Protocol.S2C_USER_LIST_UPDATED:
                            client.updateUserList(iStream.readArray(User[]::new, iStream::readUser));
                            break;
                        case Protocol.S2C_CHAT_LIST_UPDATED:
                            client.updateChatList(iStream.readArray(Chat[]::new, iStream::readChat));
                            break;
                        case Protocol.S2C_CHAT_INFO_UPDATED:
                            client.updateChatInfo(iStream.readArray(User[]::new, iStream::readUser));
                            break;
                        case Protocol.S2C_CHAT_JOINED:
                            final int joinChat = iStream.readUuid();
                            final int joinUser = iStream.readUuid();
                            client.notifyChatJoined(joinChat, joinUser, iStream.readByte());
                            break;
                        case Protocol.S2C_CHAT_QUITED:
                            final int quitChat = iStream.readUuid();
                            final int quitUser = iStream.readUuid();
                            client.notifyChatQuited(quitChat, quitUser, iStream.readByte());
                            break;
                        case Protocol.S2C_CHAT_JOIN_REQUESTED:
                            final int reqChat = iStream.readUuid();
                            final int reqUser = iStream.readUuid();
                            client.notifyChatJoinRequest(reqChat, reqUser);
                            break;
                        case Protocol.S2C_MESSAGE:
                            final int uuid = iStream.readUuid();
                            final int from = iStream.readUuid();
                            client.notifyMessage(uuid, from, iStream.readString());
                            break;
                        default:
                            Log.socket("Ignoring unknown control byte %02x", control);
                    }
                }
            } catch (EOFException e) {
                Log.client("Server connection terminated, currently we simply crash the client");
                Log.failed();
            } catch (IOException e) {
                e.printStackTrace();
                Log.client("Socket IO failure, cannot solve this issue");
                Log.failed();
            }
        }).start();
    }

    @Override
    public void stop() {
        try {
            super.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void requestSignIn(String name, String pMd5) throws IOException {
        oStream.writeByte(Protocol.C2S_SIGN_IN);
        oStream.writeString(name);
        oStream.writeString(pMd5);
        oStream.flush();
    }

    @Override
    public void requestChatInfo(int chat) throws IOException {
        oStream.writeByte(Protocol.C2S_CHAT_INFO);
        oStream.writeUuid(chat);
        oStream.flush();
    }

    @Override
    public void requestInitChat(String name, Integer[] users) throws IOException {
        oStream.writeByte(Protocol.C2S_INIT_CHAT);
        oStream.writeString(name);
        oStream.writeArray(users, oStream::writeUuid);
    }

    @Override
    public void requestJoinChat(int chat, int user) throws IOException {
        oStream.writeByte(Protocol.C2S_JOIN_CHAT);
        oStream.writeUuid(chat);
        oStream.writeUuid(user);
        oStream.flush();
    }

    @Override
    public void requestQuitChat(int chat, int user) throws IOException {
        oStream.writeByte(Protocol.C2S_QUIT_CHAT);
        oStream.writeUuid(chat);
        oStream.writeUuid(user);
        oStream.flush();

    }

    @Override
    public void sendMessage(int uuid, String msg) throws IOException {
        oStream.writeByte(Protocol.C2S_MESSAGE);
        oStream.writeUuid(uuid);
        oStream.writeString(msg);
        oStream.flush();

    }
}
