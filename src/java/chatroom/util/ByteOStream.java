package chatroom.util;

import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteOStream {
    private final OutputStream outputStream;

    public ByteOStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeByte(byte b) throws IOException {
        outputStream.write(b);
        Log.stream("=> 001B %02x", b);
    }

    public void writeUuid(int uuid) throws IOException {
        final byte[] bytes = new byte[]{
                (byte) ((uuid & 0xff000000) >> 0x18),
                (byte) ((uuid & 0x00ff0000) >> 0x10),
                (byte) ((uuid & 0x0000ff00) >> 0x08),
                (byte) ((uuid & 0x000000ff))
        };
        outputStream.write(bytes);
        Log.stream("=> 004B %d", uuid);
    }

    public void writeArrayLength(int length) throws IOException {
        writeUuid(length);
    }

    public void writeFixedString(String str) throws IOException {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 16)
            bytes = Arrays.copyOfRange(bytes, 0, 16);
        outputStream.write(bytes);
        for (int i = bytes.length; i < 16; i++)
            outputStream.write(0);
        Log.stream("=> 016B \"%s\"", str);
    }

    public void writeMessageString(String str) throws IOException {
        final byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        writeArrayLength(bytes.length);
        outputStream.write(bytes);
        Log.stream("=> %03dB \"%s\"", str.length(), str);
    }

    public void writeUser(User user) throws IOException {
        writeUuid(user.getUuid());
        writeByte(user.getFlag());
        writeFixedString(user.getUsername());
    }

    public void writeChat(Chat chat) throws IOException {
        writeUuid(chat.getUuid());
        writeByte(chat.getFlag());
        writeFixedString(chat.getName());
    }

    public void flush() throws IOException {
        outputStream.flush();
    }
}
