package chatroom.util;

import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ByteOStream {
    private final OutputStream outputStream;

    public ByteOStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeByte(byte data) throws IOException {
        outputStream.write(data);
        Log.stream("=> %02x", data);
    }

    public void writeUuid(int data) throws IOException {
        outputStream.write(new byte[]{
                (byte) ((data & 0xff000000) >> 0x18),
                (byte) ((data & 0x00ff0000) >> 0x10),
                (byte) ((data & 0x0000ff00) >> 0x08),
                (byte) ((data & 0x000000ff))
        });
        Log.stream("=> %d", data);
    }

    public void writeString(String data) throws IOException {
        final byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        writeUuid(bytes.length);
        outputStream.write(bytes);
        Log.stream("=> \"%s\"", data);
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

    public void flush() throws IOException {
        outputStream.flush();
    }
}
