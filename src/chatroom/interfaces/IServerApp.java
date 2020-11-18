package chatroom.interfaces;

public interface IServerApp {
    void bind(IServerSocket socket);

    void onRequestLogin(long clientUserId, String username, String passwordMd5); // C1

    void onRequestUserList(long clientUserId); // C2

    void onRequestGroupList(long clientUserId); // C3

    void onRequestCreateGroup(long clientUserId, String groupName); // C4

    void onRequestJoinGroup(long clientUserId, long groupId); // C5

    void onRequestInvite(long clientUserId, long groupId, long userId); // C6

    void onRequestLeaveGroup(long clientUserId, long groupId); // C7

    void onRequestSendMessage(long clientUserId, long groupId, String msg); // C8

    void onOtherRequestJoinGroupReplied(long clientUserId, long reqId, boolean agree); // C9

    void onUserLogout(long clientUserId); // automatically triggered when connection lost
}
