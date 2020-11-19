package chatroom.protocol;

public interface IServerApp {
    void bind(IServerSocket socket);

    void onRequestLogin(long client, String username, String passwordMd5); // C1

    void onRequestUserList(long client); // C2

    void onRequestGroupList(long client); // C3

    void onRequestCreateGroup(long client, String groupName); // C4

    void onRequestJoinGroup(long client, long groupId); // C5

    void onRequestInvite(long client, long groupId, long userId); // C6

    void onRequestLeaveGroup(long client, long groupId); // C7

    void onRequestSendMessage(long client, long groupId, String msg); // C8

    void onOtherRequestJoinGroupReplied(long client, long reqId, boolean agree); // C9

    void onRequestGroupMemberList(long client, long groupId); // CA

    void onUserLogout(long client); // automatically triggered when connection lost
}
