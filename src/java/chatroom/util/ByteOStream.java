package chatroom.util;

import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteOStream {
    private final OutputStream outputStream;

    public ByteOStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeByte(byte data) throws IOException {
        outputStream.write(Log.stream("=> %02x", data));
    }

    public void writeUuid(int data) throws IOException {
        Log.stream("=> %d", data);
        outputStream.write(ByteBuffer.allocate(4).putInt(data).array());
    }

    public void writeString(String data) throws IOException {
        final byte[] bytes = Log.stream("=> \"%s\"", data).getBytes(StandardCharsets.UTF_8);
        writeUuid(bytes.length);
        outputStream.write(bytes);
    }

    public void writeUser(User user) throws IOException {
        writeUuid(user.getUuid());
        writeString(user.getName());
    }

    public void writeChat(Chat chat) throws IOException {
        writeUuid(chat.getUuid());
        writeString(chat.getName());
        writeUuid(chat.getInit());
    }

    @FunctionalInterface
    public interface Operation<T> {
        void write(T t) throws IOException;
    }

    public <T> void writeArray(T[] array, Operation<T> operation) throws IOException {
        writeUuid(array.length);
        for (final T t : array)
            operation.write(t);
    }

    public void write(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    public void flush() throws IOException {
        outputStream.flush();
    }
}
