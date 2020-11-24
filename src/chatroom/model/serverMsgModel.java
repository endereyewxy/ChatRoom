package chatroom.model;

import java.io.Serializable;

public class serverMsgModel {
    public static class s1 implements Serializable {
        private byte type;
        private long userId;

        public s1(long userId) {
            this.type = 'i';
            this.userId = userId;
        }

        public long getUserId() {
            return userId;
        }
    }

    public static class s2 implements Serializable{
        private byte type;
        private Long[] userId;
        private String[] usernames;

        public s2(Long[] userId, String[] usernames) {
            this.type = 'u';
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

    public static class s3 implements Serializable{
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

    public static class s4 implements Serializable{
        private byte type;
        private long groupId, userId, reqId;

        public s4(long groupId, long userId, long reqId) {
            this.type = 'o';
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

    public static class s5 implements Serializable{
        private byte type, flags;
        private long userId;

        public s5(byte flags, long userId) {
            this.type = 'j';
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

    public static class s6 implements Serializable{
        private byte type;
        private long groupId;

        public s6(long groupId) {
            this.type = 'e';
            this.groupId = groupId;
        }

        public long getGroupId() {
            return groupId;
        }
    }

    public static class s7 implements Serializable{
        private byte type;
        private long groupId, userId;
        private String msg;

        public s7(long groupId, long userId, String msg) {
            this.type = 'm';
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

    public static class s8 implements Serializable{
        private byte type;
        private Long groupId;
        private Long[] userIds;
        private String[] usernames;
        private Byte[] flags;

        public s8(long groupId, Long[] userIds, String[] usernames, Byte[] flags) {
            this.type = 'b';
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

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public void setUserId(Long[] userIds) {
            this.userIds = userIds;
        }

        public String[] getUsernames() {
            return usernames;
        }

        public void setUsernames(String[] usernames) {
            this.usernames = usernames;
        }

        public Byte[] getFlags() {
            return flags;
        }

        public void setFlags(Byte[] flags) {
            this.flags = flags;
        }
    }


}

