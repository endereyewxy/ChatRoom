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
            final String[] cmd = scanner.nextLine().split("\\s+");
            switch (cmd[0]) {
                case "bytes":
                    for (int i = 1; i < cmd.length; i++)
                        oStream.writeByte(Byte.parseByte(cmd[i], 16));
                    oStream.flush();
                    break;
                case "integers":
                    for (int i = 1; i < cmd.length; i++)
                        oStream.writeUuid(Integer.parseInt(cmd[i]));
                    oStream.flush();
                    break;
                case "fixed":
                    oStream.writeFixedString(cmd[1]);
                    oStream.flush();
                    break;
                case "string":
                    oStream.writeMessageString(cmd[1]);
                    oStream.flush();
            }
        }
    }
}
