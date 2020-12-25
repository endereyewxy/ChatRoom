package chatroom.protocol;

public abstract class Protocol {
    public static final byte S2C_SIGN_IN_ACCEPTED    = 0x01;
    public static final byte S2C_SIGN_IN_REJECTED    = 0x02;
    public static final byte S2C_USER_LIST_UPDATED   = 0x03;
    public static final byte S2C_CHAT_LIST_UPDATED   = 0x04;
    public static final byte S2C_CHAT_INFO_UPDATED   = 0x05;
    public static final byte S2C_CHAT_JOINED         = 0x06;
    public static final byte S2C_CHAT_QUITED         = 0x07;
    public static final byte S2C_CHAT_JOIN_REQUESTED = 0x08;
    public static final byte S2C_MESSAGE             = 0x09;

    public static final byte C2S_SIGN_IN   = 0x01;
    public static final byte C2S_CHAT_INFO = 0x02;
    public static final byte C2S_INIT_CHAT = 0x03;
    public static final byte C2S_JOIN_CHAT = 0x04;
    public static final byte C2S_QUIT_CHAT = 0x05;
    public static final byte C2S_MESSAGE   = 0x06;

    public static final byte REASON_BAD_NAME          = 0x01;
    public static final byte REASON_BAD_PASS          = 0x02;
    public static final byte REASON_ALREADY_SIGNED_IN = 0x03;
    public static final byte REASON_ANOTHER_SIGNED_IN = 0x04;

    public static final byte REASON_NORMAL = 0x01;
    public static final byte REASON_CREATE = 0x02;

    public static final byte REASON_OFFLINE   = 0x01;
    public static final byte REASON_INIT_QUIT = 0x02;
    public static final byte REASON_SELF_QUIT = 0x03;
    public static final byte REASON_CHAT_QUIT = 0x04;
}
