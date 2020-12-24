package chatroom.implement.socket;

import chatroom.protocols.IClient;
import chatroom.protocols.IClientSocket;
import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;
import chatroom.util.ByteIStream;
import chatroom.util.ByteOStream;
import chatroom.util.Log;

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
        new Thread(() -> {
            try {
                while (true) {
                    final byte control = iStream.readByte();
                    switch (control) {
                        case 0x01:
                            client.replyUserList(iStream.readArray(User[]::new, iStream::readUser));
                            break;
                        case 0x02:
                            client.replyChatList(iStream.readArray(Chat[]::new, iStream::readChat));
                            break;
                        case 0x03:
                            client.replyChatMemberList(iStream.readArray(User[]::new, iStream::readUser));
                            break;
                        case 0x04:
                            client.notifySignInSucceeded(iStream.readUuid());
                            break;
                        case 0x05:
                            final int userUuid = iStream.readUuid();
                            final int chatUuid = iStream.readUuid();
                            client.notifyChatJoinRequest(userUuid, chatUuid);
                            break;
                        case 0x06:
                            final int userUuid_ = iStream.readUuid();
                            final int chatUuid_ = iStream.readUuid();
                            client.notifyMessageReceived(userUuid_, chatUuid_, iStream.readString());
                            break;
                        default:
                            Log.socket("Ignoring unknown control byte %02x", control);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }).start();
    }

    @Override
    public void bind(IClient client) {
        this.client = client;
    }

    @Override
    public void closeSocket() throws IOException {
        super.close();
    }

    @Override
    public void acquireUserList() throws IOException {
        oStream.writeByte((byte) 0x01);
        oStream.flush();
    }

    @Override
    public void acquireChatList() throws IOException {
        oStream.writeByte((byte) 0x02);
        oStream.flush();
    }

    @Override
    public void acquireChatMemberList(int chatUuid) throws IOException {
        oStream.writeByte((byte) 0x03);
        oStream.writeUuid(chatUuid);
        oStream.flush();

    }

    @Override
    public void requestSignIn(String username, String password) throws IOException {
        oStream.writeByte((byte) 0x04);
        oStream.writeString(username);
        oStream.writeString(password);
        oStream.flush();
    }

    @Override
    public void requestCreateChat(String name, User[] users) throws IOException {
        oStream.writeByte((byte) 0x05);
        oStream.writeString(name);
        oStream.writeArray(users, oStream::writeUser);
        oStream.flush();
    }

    @Override
    public void requestJoinChat(int userUuid, int chatUuid) throws IOException {
        oStream.writeByte((byte) 0x06);
        oStream.writeUuid(userUuid);
        oStream.writeUuid(chatUuid);
        oStream.flush();
    }

    @Override
    public void requestQuitChat(int userUuid, int chatUuid) throws IOException {
        oStream.writeByte((byte) 0x07);
        oStream.writeUuid(userUuid);
        oStream.writeUuid(chatUuid);
        oStream.flush();
    }

    @Override
    public void requestSendMessage(int chatUuid, String text) throws IOException {
        oStream.writeByte((byte) 0x08);
        oStream.writeUuid(chatUuid);
        oStream.writeString(text);
        oStream.flush();
    }
}
