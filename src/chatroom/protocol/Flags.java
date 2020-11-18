package chatroom.protocol;

public abstract class Flags {
    public static boolean isJoined(byte flags) {
        return (flags & 1) != 0;
    }

    public static boolean isOwner(byte flags) {
        return (flags & 2) != 0;
    }
}
