package chatroom.test;

import chatroom.implement.server.Server;
import chatroom.implement.socket.ServerSocket;
import chatroom.protocols.IServer;
import chatroom.protocols.IServerSocket;

import java.io.IOException;

public class TestServer {
    public static void main(String[] args) throws IOException {
        final IServer       server = new Server();
        final IServerSocket socket = new ServerSocket(1987);

        server.bind(socket);
        socket.bind(server);
    }
}
