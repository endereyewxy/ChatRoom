package chatroom.protocols.entity;

import java.util.HashSet;

public final class Chat {
    private final int              uuid;
    private final byte             flag;
    private final String           name;
    private final int              creator;
    private final HashSet<Integer> members;

    public Chat(int uuid, byte flag, String name, int creator, HashSet<Integer> members) {
        this.uuid = uuid;
        this.flag = flag;
        this.name = name;
        this.creator = creator;
        this.members = members;
    }

    public int getUuid() {
        return uuid;
    }

    public byte getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }

    public int getCreator() {
        return creator;
    }

    public HashSet<Integer> getMembers() {
        return members;
    }

    public Chat shadow(byte flag) {
        return new Chat(uuid, flag, name, creator, members);
    }
}
