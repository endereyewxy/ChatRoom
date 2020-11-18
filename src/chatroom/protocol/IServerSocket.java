package chatroom.protocol;

public interface IServerSocket {
    void bind(IServerApp app);

    void replyLogin(long userId); // S1

    void replyUserList(long[] userIds, String[] usernames); // S2

    void replyGroupList(long[] groupIds, String[] groupNames, byte[] flags); // S3

    void notifyOtherRequestJoinGroup(long groupId, long userId, long reqId); // S4

    void replyGroupJoined(byte flags, long groupId); // S5

    void notifyGroupLeft(long groupId); // S6

    void notifyMessage(long groupId, long userId, String msg); // S7

    void replyRequestMemberList(long groupId, long[] userIds, String[] usernames, byte[] flags); // S8
}
