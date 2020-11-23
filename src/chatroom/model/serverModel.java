package chatroom.model;

public class serverModel {
    public static class s1 {
        private byte type;
        private long userId;

        public s1(byte type, long userId) {
            this.type = type;
            this.userId = userId;
        }
    }

    public static class s2 {
        private byte type;
        private Long[] userId;
        private String[] usernames;

        public s2(byte type, Long[] userId, String[] usernames) {
            this.type = type;
            this.userId = userId;
            this.usernames = usernames;
        }
    }

    public static class s3 {
        private byte type;
        private Long[] groupIds;
        private String[] groupNames;
        private Byte[] flags;

        public s3(byte type, Long[] groupIds, String[] groupNames, Byte[] flags) {
            this.type = type;
            this.groupIds = groupIds;
            this.groupNames = groupNames;
            this.flags = flags;
        }
    }

    public static class s4 {
        private byte type;
        private long groupId, userId, reqId;

        public s4(byte type, long groupId, long userId, long reqId) {
            this.type = type;
            this.groupId = groupId;
            this.userId = userId;
            this.reqId = reqId;
        }
    }

    public static class s5 {
        private byte type, flags;
        private long userId;

        public s5(byte type, byte flags, long userId) {
            this.type = type;
            this.flags = flags;
            this.userId = userId;
        }
    }

    public static class s6 {
        private byte type;
        private long groupId;

        public s6(byte type, long groupId) {
            this.type = type;
            this.groupId = groupId;
        }
    }

    public static class s7 {
        private byte type;
        private long groupId, userId;
        private String msg;

        public s7(byte type, long groupId, long userId, String msg) {
            this.type = type;
            this.groupId = groupId;
            this.userId = userId;
            this.msg = msg;
        }
    }

    public static class s8 {
        private byte type;
        private Long groupId;
        private Long userId[];
        private String[] usernames;
        private Byte[] flags;

        public s8(byte type, long groupId, Long[] userIds, String[] usernames, Byte[] flags) {
            this.type = type;
            this.groupId = groupId;
            this.userId = userIds;
            this.usernames = usernames;
            this.flags = flags;
        }
    }


}

