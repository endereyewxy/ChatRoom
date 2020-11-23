package chatroom.model;

public class clientMsgModel {
    public static class c1 {
        private byte type;
        private String username, passwordMd5;

        public c1(String username, String passwordMd5) {
            this.type = 'i';
            this.username = username;
            this.passwordMd5 = passwordMd5;
        }
    }

    public static class c2 {
        private byte type;

        public c2() {
            this.type = 'u';
        }
    }

    public static class c3 {
        private byte type;

        public c3() {
            this.type = 'g';
        }
    }

    public static class c4 {
        private byte type;
        private String groupName;

        public c4(String groupName) {
            this.type = 'c';
            this.groupName = groupName;
        }
    }

    public static class c5 {
        private byte type;
        private long groupId;

        public c5(long groupId) {
            this.type = 'j';
            this.groupId = groupId;
        }
    }

    public static class c6 {
        private byte type;
        private long groupId, userId;

        public c6(long groupId, long userId) {
            this.type = 'v';
            this.groupId = groupId;
            this.userId = userId;
        }
    }

    public static class c7 {
        private byte type;
        private long groupId;

        public c7(long groupId) {
            this.type = 'e';
            this.groupId = groupId;
        }
    }

    public static class c8 {
        private byte type;
        private Long groupId;
        private String msg;

        public c8(long groupId, String msg) {
            this.type = 'm';
            this.groupId = groupId;
            this.msg = msg;
        }
    }

    public static class c9 {
        private byte type;
        private long reqId;
        private boolean agree;

        public c9(long reqId, boolean agree) {
            this.type = 'o';
            this.reqId = reqId;
            this.agree = agree;
        }
    }

    public static class cA {
        private byte type;
        private Long groupId;

        public cA(long groupId) {
            this.type = 'b';
            this.groupId = groupId;
        }
    }
}
