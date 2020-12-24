package chatroom.test;

import chatroom.implement.client.Client;
import chatroom.implement.socket.ClientSocket;
import chatroom.protocols.IClient;
import chatroom.protocols.IClientSocket;

import java.io.IOException;

public class TestClient {
    public static void main(String[] args) throws IOException {
        final IClient       client = new Client();
        final IClientSocket socket = new ClientSocket("localhost", 1987);

        socket.bind(client);
        client.bind(socket);
    }
}
