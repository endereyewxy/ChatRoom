package chatroom.model;

import java.io.Serializable;

public class clientMsgModel {
    public static class c1 implements Serializable {
        private byte type;
        private String username, passwordMd5;

        public c1(String username, String passwordMd5) {
            this.type = 'i';
            this.username = username;
            this.passwordMd5 = passwordMd5;
        }

        public String getPasswordMd5() {
            return passwordMd5;
        }

        public void setPasswordMd5(String passwordMd5) {
            this.passwordMd5 = passwordMd5;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public static class c2 implements Serializable{
        private byte type;

        public c2() {
            this.type = 'u';
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }
    }

    public static class c3 implements Serializable{
        private byte type;

        public c3() {
            this.type = 'g';
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }
    }

    public static class c4 implements Serializable{
        private byte type;
        private String groupName;

        public c4(String groupName) {
            this.type = 'c';
            this.groupName = groupName;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }


        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
    }

    public static class c5 implements Serializable{
        private byte type;
        private long groupId;

        public c5(long groupId) {
            this.type = 'j';
            this.groupId = groupId;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public long getGroupId() {
            return groupId;
        }

        public void setGroupId(long groupId) {
            this.groupId = groupId;
        }
    }

    public static class c6 implements Serializable{
        private byte type;
        private long groupId, userId;

        public c6(long groupId, long userId) {
            this.type = 'v';
            this.groupId = groupId;
            this.userId = userId;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public long getGroupId() {
            return groupId;
        }

        public void setGroupId(long groupId) {
            this.groupId = groupId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }
    }

    public static class c7 implements Serializable{
        private byte type;
        private long groupId;

        public c7(long groupId) {
            this.type = 'e';
            this.groupId = groupId;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public long getGroupId() {
            return groupId;
        }

        public void setGroupId(long groupId) {
            this.groupId = groupId;
        }
    }

    public static class c8 implements Serializable{
        private byte type;
        private Long groupId;
        private String msg;

        public c8(long groupId, String msg) {
            this.type = 'm';
            this.groupId = groupId;
            this.msg = msg;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class c9 implements Serializable{
        private byte type;
        private long reqId;
        private boolean agree;

        public c9(long reqId, boolean agree) {
            this.type = 'o';
            this.reqId = reqId;
            this.agree = agree;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public long getReqId() {
            return reqId;
        }

        public void setReqId(long reqId) {
            this.reqId = reqId;
        }

        public boolean isAgree() {
            return agree;
        }

        public void setAgree(boolean agree) {
            this.agree = agree;
        }
    }

    public static class cA implements Serializable{
        private byte type;
        private Long groupId;

        public cA(long groupId) {
            this.type = 'b';
            this.groupId = groupId;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }
    }

}
