package chatroom.model;

public class serverMsgModel {
    public static class s1 {
        private byte type;
        private long userId;

        public s1(long userId) {
            this.type = 'i';
            this.userId = userId;
        }
    }

    public static class s2 {
        private byte type;
        private Long[] userId;
        private String[] usernames;

        public s2(Long[] userId, String[] usernames) {
            this.type = 'u';
            this.userId = userId;
            this.usernames = usernames;
        }
    }

    public static class s3 {
        private byte type;
        private Long[] groupIds;
        private String[] groupNames;
        private Byte[] flags;

        public s3(Long[] groupIds, String[] groupNames, Byte[] flags) {
            this.type = 'g';
            this.groupIds = groupIds;
            this.groupNames = groupNames;
            this.flags = flags;
        }
    }

    public static class s4 {
        private byte type;
        private long groupId, userId, reqId;

        public s4(long groupId, long userId, long reqId) {
            this.type = 'o';
            this.groupId = groupId;
            this.userId = userId;
            this.reqId = reqId;
        }
    }

    public static class s5 {
        private byte type, flags;
        private long userId;

        public s5(byte flags, long userId) {
            this.type = 'j';
            this.flags = flags;
            this.userId = userId;
        }
    }

    public static class s6 {
        private byte type;
        private long groupId;

        public s6(long groupId) {
            this.type = 'e';
            this.groupId = groupId;
        }
    }

    public static class s7 {
        private byte type;
        private long groupId, userId;
        private String msg;

        public s7(long groupId, long userId, String msg) {
            this.type = 'm';
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

        public s8(long groupId, Long[] userIds, String[] usernames, Byte[] flags) {
            this.type = 'b';
            this.groupId = groupId;
            this.userId = userIds;
            this.usernames = usernames;
            this.flags = flags;
        }
    }


}

