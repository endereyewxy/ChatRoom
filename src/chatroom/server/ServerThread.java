package chatroom.server;

import java.net.Socket;
import java.util.concurrent.Callable;

public class ServerThread implements Callable<Void> {
    private final int RESULT_OK = 1;
    private final int RESULT_CONNECTION_CLOSED = 2;
    static long count = 1l;
    private Socket socket;
    private long client;

    public ServerThread(Socket socket) {
        this.socket = socket;
        this.client = count++;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public long getClient() {
        return this.client;
    }

    @Override
    public Void call() {
        return null;
    }
}
