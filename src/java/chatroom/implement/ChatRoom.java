package chatroom.implement;

import chatroom.implement.client.Client;
import chatroom.implement.server.Server;
import chatroom.implement.socket.ClientSocket;
import chatroom.implement.socket.ServerSocket;
import chatroom.protocol.IClient;
import chatroom.protocol.IClientSocket;
import chatroom.protocol.IServer;
import chatroom.protocol.IServerSocket;

import java.io.IOException;

public class ChatRoom {
    public static void main(String[] args) throws IOException {
        switch (args[0]) {
            case "server":
                doServer(Integer.decode(args[1]));
                break;
            case "client":
                doClient(args[1], Integer.decode(args[2]));
        }
    }

    public static void doServer(int port) throws IOException {
        final IServer       server = new Server();
        final IServerSocket socket = new ServerSocket(port);

        socket.bind(server);
        server.bind(socket);
        socket.init();
    }

    public static void doClient(String host, int port) throws IOException {
        final IClient       client = new Client();
        final IClientSocket socket = new ClientSocket(host, port);

        socket.bind(client);
        socket.init();
        client.bind(socket);
    }
}
