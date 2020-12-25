package chatroom.protocol.entity;

public final class Chat {
    private final int    uuid;
    private final String name;
    private final int    init;

    public Chat(int uuid, String name, int init) {
        this.uuid = uuid;
        this.name = name;
        this.init = init;
    }

    public int getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getInit() {
        return init;
    }
}
