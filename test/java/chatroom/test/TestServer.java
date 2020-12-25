package chatroom.test;

import chatroom.implement.server.Server;
import chatroom.implement.socket.ServerSocket;
import chatroom.protocol.IServer;
import chatroom.protocol.IServerSocket;

import java.io.IOException;

public class TestServer {
    public static void main(String[] args) throws IOException {
        final IServer       server = new Server();
        final IServerSocket socket = new ServerSocket(1987);

        socket.bind(server);
        server.bind(socket);
        socket.init();
    }
}
