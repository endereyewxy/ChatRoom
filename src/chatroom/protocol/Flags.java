package chatroom.protocol;

public abstract class Flags {
    public static boolean isJoined(byte flags) {
        return (flags & 1) != 0;
    }

    public static boolean isOwner(byte flags) {
        return (flags & 2) != 0;
    }

    public static byte of(boolean isOwner, boolean isJoined) {
        return (byte) ((isOwner ? 2 : 0) + (isJoined ? 1 : 0));
    }
}
