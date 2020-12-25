package chatroom.test;

import chatroom.implement.client.Client;
import chatroom.implement.socket.ClientSocket;
import chatroom.protocol.IClient;
import chatroom.protocol.IClientSocket;

import java.io.IOException;

public class TestClient {
    public static void main(String[] args) throws IOException {
        final IClient       client = new Client();
        final IClientSocket socket = new ClientSocket("localhost", 1987);

        socket.bind(client);
        socket.init();
        client.bind(socket);
    }
}
