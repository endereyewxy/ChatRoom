package chatroom.fake;

import chatroom.implement.socket.ServerSocket;
import chatroom.util.ByteIStream;
import chatroom.util.ByteOStream;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class FakeServer {
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        System.out.println("Waiting for client...");
        final ServerSocket server = new ServerSocket(1987);
        final Socket       socket = server.accept();
        System.out.println("Fake server connected");

        final ByteIStream iStream = new ByteIStream(socket.getInputStream());
        final ByteOStream oStream = new ByteOStream(socket.getOutputStream());

        new Thread(() -> {
            while (true) {
                try {
                    iStream.readByte();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        final Scanner scanner = new Scanner(System.in);
        while (true) {
            String state = "b";
            for (final String cmd : scanner.nextLine().split("\\s+")) {
                if (cmd.startsWith("!")) {
                    state = cmd.substring(1);
                } else {
                    switch (state) {
                        case "b":
                            oStream.writeByte(Byte.decode(cmd));
                            break;
                        case "i":
                            oStream.writeUuid(Integer.decode(cmd));
                            break;
                        case "s":
                            oStream.writeString(cmd);
                            break;
                    }
                }
            }
        }
    }
}
