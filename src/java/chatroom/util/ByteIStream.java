package chatroom.util;

import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.IntFunction;

public class ByteIStream {
    private final InputStream inputStream;

    public ByteIStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public byte readByte() throws IOException {
        return read(1)[0];
    }

    public int readUuid() throws IOException {
        final byte[] data = read(4);
        return Log.stream("<= %d", (data[0] << 0x18) |
                                   (data[1] << 0x10) |
                                   (data[2] << 0x08) | data[3]);
    }

    public String readString() throws IOException {
        final int length = readUuid();
        return Log.stream("<= \"%s\"", new String(read(length), StandardCharsets.UTF_8));
    }

    public User readUser() throws IOException {
        final int    uuid = readUuid();
        final String name = readString();
        return new User(uuid, name, "");
    }

    public Chat readChat() throws IOException {
        final int    uuid = readUuid();
        final String name = readString();
        final int    init = readUuid();
        return new Chat(uuid, name, init);
    }

    @FunctionalInterface
    public interface Operation<T> {
        T read() throws IOException;
    }

    public <T> T[] readArray(IntFunction<T[]> newer, Operation<T> operation) throws IOException {
        final T[] array = newer.apply(readUuid());
        for (int i = 0; i < array.length; i++)
            array[i] = operation.read();
        return array;
    }

    private byte[] read(int length) throws IOException {
        final byte[] buffer = new byte[length];
        while (true) {
            final int actual = inputStream.read(buffer);
            if (actual == length)
                break;
            if (actual == -1)
                throw new EOFException();
            Log.stream("<! Ignoring because %d (actual) != %d (expected)", actual, length);
        }
        Log.stream("<= %s", Arrays.toString(buffer));
        return buffer;
    }
}
