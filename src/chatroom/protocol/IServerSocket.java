package chatroom.protocol;

public interface IServerSocket {
    void bind(IServerApp app);

    void replyLogin(long client, long userId); // S1

    void replyUserList(long client, Long[] userIds, String[] usernames); // S2

    void replyGroupList(long client, Long[] groupIds, String[] groupNames, Byte[] flags); // S3

    void notifyOtherRequestJoinGroup(long client, long groupId, long userId, long reqId); // S4

    void replyGroupJoined(long client, byte flags, long groupId); // S5

    void notifyGroupLeft(long client, long groupId); // S6

    void notifyMessage(long client, long groupId, long userId, String msg); // S7

    void replyRequestMemberList(long client, long groupId, Long[] userIds, String[] usernames, Byte[] flags); // S8
}
