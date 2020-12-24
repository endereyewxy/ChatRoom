package chatroom.implement.socket;

import chatroom.protocols.IServer;
import chatroom.protocols.IServerSocket;
import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;
import chatroom.util.ByteIStream;
import chatroom.util.ByteOStream;
import chatroom.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ServerSocket extends java.net.ServerSocket implements IServerSocket {
    private IServer server;

    private       int                           generatedId = 0;
    private final HashMap<Integer, ByteIStream> iStreams    = new HashMap<>();
    private final HashMap<Integer, ByteOStream> oStreams    = new HashMap<>();

    public ServerSocket(int port) throws IOException {
        super(port);
    }

    @Override
    public void bind(IServer server) {
        this.server = server;
        while (!this.isClosed()) {
            try {
                final Socket socket = this.accept();
                generatedId++;
                iStreams.put(generatedId, new ByteIStream(socket.getInputStream()));
                oStreams.put(generatedId, new ByteOStream(socket.getOutputStream()));
                createThread(generatedId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void replyUserList(int client, User[] users) throws IOException {
        ByteOStream oStream = oStreams.get(client);
        oStream.writeByte((byte) 0x01);
        oStream.writeArray(users, oStream::writeUser);
        oStream.flush();
    }

    @Override
    public void replyChatList(int client, Chat[] chats) throws IOException {
        ByteOStream oStream = oStreams.get(client);
        oStream.writeByte((byte) 0x02);
        oStream.writeArray(chats, oStream::writeChat);
        oStream.flush();
    }

    @Override
    public void replyChatMemberList(int client, User[] users) throws IOException {
        ByteOStream oStream = oStreams.get(client);
        oStream.writeByte((byte) 0x03);
        oStream.writeArray(users, oStream::writeUser);
        oStream.flush();
    }

    @Override
    public void notifySignInSucceeded(int client, int userUuid) throws IOException {
        ByteOStream oStream = oStreams.get(client);
        oStream.writeByte((byte) 0x04);
        oStream.writeUuid(userUuid);
        oStream.flush();
    }

    @Override
    public void notifyChatJoinRequest(int client, int userUuid, int chatUuid) throws IOException {
        ByteOStream oStream = oStreams.get(client);
        oStream.writeByte((byte) 0x05);
        oStream.writeUuid(userUuid);
        oStream.writeUuid(chatUuid);
        oStream.flush();
    }

    @Override
    public void notifyMessageReceived(int client, int userUuid, int chatUuid, String text) throws IOException {
        ByteOStream oStream = oStreams.get(client);
        oStream.writeByte((byte) 0x07);
        oStream.writeUuid(userUuid);
        oStream.writeUuid(chatUuid);
        oStream.writeString(text);
        oStream.flush();
    }

    private void createThread(int client) {
        final ByteIStream iStream = iStreams.get(client);
        new Thread(() -> {
            try {
                while (true) {
                    final byte control = iStream.readByte();
                    switch (control) {
                        case 0x01:
                            server.acquireUserList(client);
                            break;
                        case 0x02:
                            server.acquireChatList(client);
                            break;
                        case 0x03:
                            final int userUuid = iStream.readUuid();
                            server.acquireChatMemberList(client, userUuid);
                            break;
                        case 0x04:
                            final String username = iStream.readString();
                            final String password = iStream.readString();
                            server.requestSignIn(client, username, password);
                            break;
                        case 0x05:
                            final String name = iStream.readString();
                            server.requestCreateChat(client, name);
                            break;
                        case 0x06:
                            final int userUuid_ = iStream.readUuid();
                            final int chatUuid_ = iStream.readUuid();
                            server.requestJoinChat(client, userUuid_, chatUuid_);
                            break;
                        case 0x07:
                            final int userUuid__ = iStream.readUuid();
                            final int chatUuid__ = iStream.readUuid();
                            server.requestQuitChat(client, userUuid__, chatUuid__);
                            break;
                        case 0x08:
                            final int chatUuid___ = iStream.readUuid();
                            final String text = iStream.readString();
                            server.requestSendMessage(client, chatUuid___, text);
                            break;
                        default:
                            Log.socket("Ignoring unknown control byte %02x", control);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
