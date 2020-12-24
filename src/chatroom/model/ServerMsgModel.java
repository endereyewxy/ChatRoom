package chatroom.model;

import java.io.Serializable;

public class ServerMsgModel {
    public static class s1 implements Serializable {
        private final long userId;

        public s1(long userId) {
            this.userId = userId;
        }

        public long getUserId() {
            return userId;
        }
    }

    public static class s2 implements Serializable {
        private final Long[]   userId;
        private final String[] usernames;

        public s2(Long[] userId, String[] usernames) {
            this.userId = userId;
            this.usernames = usernames;
        }

        public Long[] getUserId() {
            return userId;
        }

        public String[] getUsernames() {
            return usernames;
        }
    }

    public static class s3 implements Serializable {
        private final Long[]   groupIds;
        private final String[] groupNames;
        private final Byte[]   flags;

        public s3(Long[] groupIds, String[] groupNames, Byte[] flags) {
            this.groupIds = groupIds;
            this.groupNames = groupNames;
            this.flags = flags;
        }

        public Long[] getGroupIds() {
            return groupIds;
        }

        public Byte[] getFlags() {
            return flags;
        }

        public String[] getGroupNames() {
            return groupNames;
        }
    }

    public static class s4 implements Serializable {
        private final long groupId;
        private final long userId;
        private final long reqId;

        public s4(long groupId, long userId, long reqId) {
            this.groupId = groupId;
            this.userId = userId;
            this.reqId = reqId;
        }

        public long getGroupId() {
            return groupId;
        }

        public long getUserId() {
            return userId;
        }

        public long getReqId() {
            return reqId;
        }
    }

    public static class s5 implements Serializable {
        private final byte flags;
        private final long userId;

        public s5(byte flags, long userId) {
            this.flags = flags;
            this.userId = userId;
        }

        public byte getFlags() {
            return flags;
        }

        public long getUserId() {
            return userId;
        }
    }

    public static class s6 implements Serializable {
        private final long groupId;

        public s6(long groupId) {
            this.groupId = groupId;
        }

        public long getGroupId() {
            return groupId;
        }
    }

    public static class s7 implements Serializable {
        private final long   groupId;
        private final long   userId;
        private final String msg;

        public s7(long groupId, long userId, String msg) {
            this.groupId = groupId;
            this.userId = userId;
            this.msg = msg;
        }

        public long getGroupId() {
            return groupId;
        }

        public long getUserId() {
            return userId;
        }

        public String getMsg() {
            return msg;
        }
    }

    public static class s8 implements Serializable {
        private final Long     groupId;
        private final Long[]   userIds;
        private final String[] usernames;
        private final Byte[]   flags;

        public s8(long groupId, Long[] userIds, String[] usernames, Byte[] flags) {
            this.groupId = groupId;
            this.userIds = userIds;
            this.usernames = usernames;
            this.flags = flags;
        }

        public Long getGroupId() {
            return groupId;
        }

        public Long[] getUserIds() {
            return userIds;
        }

        public String[] getUsernames() {
            return usernames;
        }

        public Byte[] getFlags() {
            return flags;
        }
    }


}

