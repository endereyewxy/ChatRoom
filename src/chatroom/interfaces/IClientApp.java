package chatroom.interfaces;

public interface IClientApp {
    void bind(IClientSocket socket);

    void onLoginResultReceived(long userId); // S1

    void onUserListReceived(long[] userIds, String[] usernames); // S2

    void onGroupListReceived(long[] groupIds, String[] groupNames); // S3

    void onOtherRequestJoinGroup(long groupId, long userId, long reqId); // S4

    void onGroupJoined(byte flags, long groupId); // S5

    void onGroupLeft(long groupId); // S6

    void onMessageReceived(long groupId, long userId, String msg); // S7
}
