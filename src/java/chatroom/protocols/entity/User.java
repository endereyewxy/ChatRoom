package chatroom.protocols.entity;

public final class User {
    private final int    uuid;
    private final byte   flag;
    private final String username;
    private final String password;

    public User(int uuid, byte flag, String username, String password) {
        this.uuid = uuid;
        this.flag = flag;
        this.username = username;
        this.password = password;
    }

    public int getUuid() {
        return uuid;
    }

    public byte getFlag() {
        return flag;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User shadow(byte flag) {
        return new User(uuid, flag, username, "");
    }
}
