package chatroom.model;

import java.io.Serializable;

public class ClientMsgModel {
    public static class c1 implements Serializable {
        private final String username;
        private final String passwordMd5;

        public c1(String username, String passwordMd5) {
            this.username = username;
            this.passwordMd5 = passwordMd5;
        }

        public String getPasswordMd5() {
            return passwordMd5;
        }

        public String getUsername() {
            return username;
        }
    }

    public static class c2 implements Serializable {
        public c2() {
        }
    }

    public static class c3 implements Serializable {
        public c3() {
        }
    }

    public static class c4 implements Serializable {
        private final String groupName;

        public c4(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupName() {
            return groupName;
        }
    }

    public static class c5 implements Serializable {
        private final long groupId;

        public c5(long groupId) {
            this.groupId = groupId;
        }

        public long getGroupId() {
            return groupId;
        }
    }

    public static class c6 implements Serializable {
        private final long groupId;
        private final long userId;

        public c6(long groupId, long userId) {
            this.groupId = groupId;
            this.userId = userId;
        }

        public long getGroupId() {
            return groupId;
        }

        public long getUserId() {
            return userId;
        }
    }

    public static class c7 implements Serializable {
        private final long groupId;

        public c7(long groupId) {
            this.groupId = groupId;
        }

        public long getGroupId() {
            return groupId;
        }
    }

    public static class c8 implements Serializable {
        private final Long   groupId;
        private final String msg;

        public c8(long groupId, String msg) {
            this.groupId = groupId;
            this.msg = msg;
        }

        public Long getGroupId() {
            return groupId;
        }

        public String getMsg() {
            return msg;
        }
    }

    public static class c9 implements Serializable {
        private final long    reqId;
        private final boolean agree;

        public c9(long reqId, boolean agree) {
            this.reqId = reqId;
            this.agree = agree;
        }

        public long getReqId() {
            return reqId;
        }

        public boolean isAgree() {
            return agree;
        }
    }

    public static class cA implements Serializable {
        private final Long groupId;

        public cA(long groupId) {
            this.groupId = groupId;
        }

        public Long getGroupId() {
            return groupId;
        }
    }
}
