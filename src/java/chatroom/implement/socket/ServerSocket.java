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
    public void replyUserList(int client, User[] users) {
        // TODO
    }

    @Override
    public void replyChatList(int client, Chat[] chats) {
        // TODO
    }

    @Override
    public void replyChatMemberList(int client, User[] users) {
        // TODO
    }

    @Override
    public void notifySignInSucceeded(int client, int userUuid) {
        // TODO
    }

    @Override
    public void notifyChatJoinRequest(int client, int userUuid, int chatUuid) {
        // TODO
    }

    @Override
    public void notifyChatMembersChanged(int client, User user, Chat chat) {
        // TODO
    }

    @Override
    public void notifyMessageReceived(int client, int userUuid, int chatUuid, String text) {
        // TODO
    }

    private void createThread(int client) {
        final ByteIStream iStream = iStreams.get(client);
        new Thread(() -> {
            while (true) {
                try {
                    final byte control = iStream.readByte();
                    switch (control) {
                        // TODO
                        default:
                            Log.socket("Ignoring unknown control byte %02x", control);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
