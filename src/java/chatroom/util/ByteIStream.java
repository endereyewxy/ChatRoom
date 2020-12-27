package chatroom.util;

import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
        return Log.stream("<= %d", ByteBuffer.wrap(data).getInt());
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

    public byte[] read(int length) throws IOException {
        final ByteBuffer totalBuffer = ByteBuffer.allocate(length);
        final byte[]     batchBuffer = new byte[length];
        for (int i = 0, a; i < length; ) {
            if ((a = inputStream.read(batchBuffer)) == -1)
                throw new EOFException();
            i += a;
            totalBuffer.put(batchBuffer, 0, a);
        }
        return totalBuffer.array();
    }
}
