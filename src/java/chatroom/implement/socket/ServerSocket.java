package chatroom.implement.socket;

import chatroom.protocol.IServer;
import chatroom.protocol.IServerSocket;
import chatroom.protocol.Protocol;
import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;
import chatroom.util.ByteIStream;
import chatroom.util.ByteOStream;
import chatroom.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class ServerSocket extends java.net.ServerSocket implements IServerSocket {
    private IServer server;

    private       int                           globalUuidGenerator = 0;
    private final HashMap<Integer, ByteIStream> iStreams            = new HashMap<>();
    private final HashMap<Integer, ByteOStream> oStreams            = new HashMap<>();

    public ServerSocket(int port) throws IOException {
        super(port);
    }

    @Override
    public void bind(IServer server) {
        this.server = server;
    }

    @Override
    public void init() {
        try {
            while (!this.isClosed()) {
                final Socket socket = this.accept();
                globalUuidGenerator++;
                iStreams.put(globalUuidGenerator, new ByteIStream(socket.getInputStream()));
                oStreams.put(globalUuidGenerator, new ByteOStream(socket.getOutputStream()));
                createThread(globalUuidGenerator);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.socket("Server socket IO failure, cannot resolve this issue");
            Log.failed();
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public void notifySignInAccepted(int client, int uuid) throws IOException {
        final ByteOStream oStream = oStreams.get(client);
        oStream.writeByte(Protocol.S2C_SIGN_IN_ACCEPTED);
        oStream.writeUuid(uuid);
        oStream.flush();
    }

    @Override
    public void notifySignInRejected(int client, byte why) throws IOException {
        final ByteOStream oStream = oStreams.get(client);
        oStream.writeByte(Protocol.S2C_SIGN_IN_REJECTED);
        oStream.writeByte(why);
        oStream.flush();
    }

    @Override
    public void updateUserList(int client, User[] users) throws IOException {
        final ByteOStream oStream = oStreams.get(client);
        oStream.writeByte(Protocol.S2C_USER_LIST_UPDATED);
        oStream.writeArray(users, oStream::writeUser);
        oStream.flush();
    }

    @Override
    public void updateChatList(int client, Chat[] chats) throws IOException {
        final ByteOStream oStream = oStreams.get(client);
        oStream.writeByte(Protocol.S2C_CHAT_LIST_UPDATED);
        oStream.writeArray(chats, oStream::writeChat);
        oStream.flush();
    }

    @Override
    public void updateChatInfo(int client, User[] users) throws IOException {
        final ByteOStream oStream = oStreams.get(client);
        oStream.writeByte(Protocol.S2C_CHAT_INFO_UPDATED);
        oStream.writeArray(users, oStream::writeUser);
        oStream.flush();
    }

    @Override
    public void notifyChatJoined(int client, int chat, int user, byte reason) throws IOException {
        final ByteOStream oStream = oStreams.get(client);
        oStream.writeByte(Protocol.S2C_CHAT_JOINED);
        oStream.writeUuid(chat);
        oStream.writeUuid(user);
        oStream.writeByte(reason);
        oStream.flush();
    }

    @Override
    public void notifyChatQuited(int client, int chat, int user, byte reason) throws IOException {
        final ByteOStream oStream = oStreams.get(client);
        oStream.writeByte(Protocol.S2C_CHAT_QUITED);
        oStream.writeUuid(chat);
        oStream.writeUuid(user);
        oStream.writeByte(reason);
        oStream.flush();
    }

    @Override
    public void notifyChatJoinRequest(int client, int chat, int user) throws IOException {
        final ByteOStream oStream = oStreams.get(client);
        oStream.writeByte(Protocol.S2C_CHAT_JOIN_REQUESTED);
        oStream.writeUuid(chat);
        oStream.writeUuid(user);
        oStream.flush();
    }

    @Override
    public void notifyMessage(int client, int uuid, int from, String msg) throws IOException {
        final ByteOStream oStream = oStreams.get(client);
        oStream.writeByte(Protocol.S2C_MESSAGE);
        oStream.writeUuid(uuid);
        oStream.writeUuid(from);
        oStream.writeString(msg);
        oStream.flush();
    }

    private void createThread(int client) {
        final ByteIStream iStream = iStreams.get(client);
        new Thread(() -> {
            Log.socket("Client %d online", client);
            try {
                while (true) {
                    final byte control = iStream.readByte();
                    switch (control) {
                        case Protocol.C2S_SIGN_IN:
                            final String name = iStream.readString();
                            final String pMd5 = iStream.readString();
                            server.requestSignIn(client, name, pMd5);
                            break;
                        case Protocol.C2S_CHAT_INFO:
                            server.requestChatInfo(client, iStream.readUuid());
                            break;
                        case Protocol.C2S_INIT_CHAT:
                            final String initName = iStream.readString();
                            server.requestInitChat(client, initName, iStream.readArray(Integer[]::new, iStream::readUuid));
                            break;
                        case Protocol.C2S_JOIN_CHAT:
                            final int joinChat = iStream.readUuid();
                            final int joinUser = iStream.readUuid();
                            server.requestJoinChat(client, joinChat, joinUser);
                            break;
                        case Protocol.C2S_QUIT_CHAT:
                            final int quitChat = iStream.readUuid();
                            final int quitUser = iStream.readUuid();
                            server.requestQuitChat(client, quitChat, quitUser);
                            break;
                        case Protocol.C2S_MESSAGE:
                            final int uuid = iStream.readUuid();
                            server.sendMessage(client, uuid, iStream.readString());
                            break;
                        default:
                            Log.socket("Ignoring unknown control byte %02x", control);
                    }
                }
            } catch (EOFException | SocketException e) {
                iStreams.remove(client);
                oStreams.remove(client);
                try {
                    server.clientOffline(Log.socket("Client %d offline", client));
                } catch (IOException ignored) {
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.socket("Server socket IO failure, cannot resolve this issue");
                Log.failed();
            }
        }).start();
    }
}
