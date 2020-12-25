package chatroom.protocol.entity;

public final class User {
    private final int    uuid;
    private final String name;
    private final String pMd5;

    public User(int uuid, String name, String pMd5) {
        this.uuid = uuid;
        this.name = name;
        this.pMd5 = pMd5;
    }

    public int getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getPMd5() {
        return pMd5;
    }
}
