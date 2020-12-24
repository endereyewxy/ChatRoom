package chatroom.protocol;

import java.io.IOException;

public interface IServerSocket {
    void bind(IServerApp app);

    void replyLogin(long client, long userId) throws IOException; // S1

    void replyUserList(long client, Long[] userIds, String[] usernames) throws IOException; // S2

    void replyGroupList(long client, Long[] groupIds, String[] groupNames, Byte[] flags) throws IOException; // S3

    void notifyOtherRequestJoinGroup(long client, long groupId, long userId, long reqId) throws IOException; // S4

    void replyGroupJoined(long client, byte flags, long groupId) throws IOException; // S5

    void notifyGroupLeft(long client, long groupId) throws IOException; // S6

    void notifyMessage(long client, long groupId, long userId, String msg) throws IOException; // S7

    void replyRequestMemberList(long client, long groupId, Long[] userIds, String[] usernames, Byte[] flags) throws IOException; // S8
}
