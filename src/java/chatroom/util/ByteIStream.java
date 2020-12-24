package chatroom.util;

import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.IntFunction;

public class ByteIStream {
    private final InputStream inputStream;

    public ByteIStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public byte readByte() throws IOException {
        final int data = inputStream.read();
        if (data == -1)
            throw new EOFException();
        Log.stream("<= 001B %02x", data);
        return (byte) data;
    }

    public int readUuid() throws IOException {
        final byte[] bytes = new byte[4];
        if (inputStream.read(bytes) != 4)
            throw new EOFException();
        final int data = (bytes[0] << 0x18) |
                         (bytes[1] << 0x10) |
                         (bytes[2] << 0x08) | bytes[3];
        Log.stream("<= 004B %d", data);
        return data;
    }

    public int readArrayLength() throws IOException {
        return readUuid();
    }

    public String readFixedString() throws IOException {
        final byte[] bytes = new byte[16];
        if (inputStream.read(bytes) != bytes.length)
            throw new EOFException();
        final String data = new String(bytes, StandardCharsets.UTF_8);
        Log.stream("<= 016B \"%s\"", data);
        return data;
    }

    public String readMessageString() throws IOException {
        final int    length = readArrayLength();
        final byte[] bytes  = new byte[length];
        if (inputStream.read(bytes) != length)
            throw new EOFException();
        final String data = new String(bytes, StandardCharsets.UTF_8);
        Log.stream("<= %03dB \"%s\"", length, data);
        return data;
    }

    public User readUser() throws IOException {
        final int    uuid = readUuid();
        final byte   flag = readByte();
        final String name = readFixedString();
        return new User(uuid, flag, name, "");
    }

    public Chat readChat() throws IOException {
        final int    uuid = readUuid();
        final byte   flag = readByte();
        final String name = readFixedString();
        return new Chat(uuid, flag, name, 0, null);
    }

    @FunctionalInterface
    public interface Operation<T> {
        T read() throws IOException;
    }

    public <T> T[] readArray(IntFunction<T[]> newer, Operation<T> operation) throws IOException {
        final T[] array = newer.apply(readArrayLength());
        for (int i = 0; i < array.length; i++)
            array[i] = operation.read();
        return array;
    }
}
