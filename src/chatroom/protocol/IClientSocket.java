package chatroom.protocol;

import java.io.IOException;

public interface IClientSocket {
    void bind(IClientApp app);

    void requestLogin(String username, String passwordMd5) throws IOException; // C1

    void requestUserList() throws IOException; // C2

    void requestGroupList() throws IOException; // C3

    void requestCreateGroup(String groupName) throws IOException; // C4

    void requestJoinGroup(long groupId) throws IOException; // C5

    void requestInvite(long groupId, long userId) throws IOException; // C6

    void requestLeaveGroup(long groupId) throws IOException; // C7

    void requestSendMessage(long groupId, String msg) throws IOException; // C8

    void replyOtherRequestJoinGroup(long reqId, boolean agree) throws IOException; // C9

    void requestGroupMemberList(long groupId) throws IOException; // CA
}
