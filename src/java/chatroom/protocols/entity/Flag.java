package chatroom.protocols.entity;

public abstract class Flag {
    public static byte of(User user, Chat chat, Boolean joined, Boolean quited) {
        byte flag = 0;
        if (chat.getMembers().contains(user.getUuid()))
            flag |= 0x01;
        if (chat.getCreator() == user.getUuid())
            flag |= 0x02;
        if (joined != null) {
            flag |= 0x10;
            flag |= joined ? 0x04 : 0;
        }
        if (quited != null) {
            flag |= 0x20;
            flag |= quited ? 0x08 : 0;
        }
        return flag;
    }

    public static byte of(User user, Chat chat) {
        return of(user, chat, null, null);
    }

    public static byte ofJoined(User user, Chat chat, boolean joined) {
        return of(user, chat, joined, null);
    }

    public static byte ofQuited(User user, Chat chat, boolean quited) {
        return of(user, chat, null, quited);
    }
}
