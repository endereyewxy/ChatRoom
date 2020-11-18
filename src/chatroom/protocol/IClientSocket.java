package chatroom.protocol;

public interface IClientSocket {
    void bind(IClientApp app);

    void requestLogin(String username, String passwordMd5); // C1

    void requestUserList(); // C2

    void requestGroupList(); // C3

    void requestCreateGroup(String groupName); // C4

    void requestJoinGroup(long groupId); // C5

    void requestInvite(long groupId, long userId); // C6

    void requestLeaveGroup(long groupId); // C7

    void requestSendMessage(long groupId, String msg); // C8

    void replyOtherRequestJoinGroup(long reqId, boolean agree); // C9

    void requestGroupMemberList(long groupId); // CA
}
