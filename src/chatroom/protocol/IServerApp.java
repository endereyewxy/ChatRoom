package chatroom.protocol;

import java.io.IOException;

public interface IServerApp {
    void bind(IServerSocket socket);

    void onRequestLogin(long client, String username, String passwordMd5) throws IOException; // C1

    void onRequestUserList(long client) throws IOException; // C2

    void onRequestGroupList(long client) throws IOException; // C3

    void onRequestCreateGroup(long client, String groupName) throws IOException; // C4

    void onRequestJoinGroup(long client, long groupId) throws IOException; // C5

    void onRequestInvite(long client, long groupId, long userId) throws IOException; // C6

    void onRequestLeaveGroup(long client, long groupId) throws IOException; // C7

    void onRequestSendMessage(long client, long groupId, String msg); // C8

    void onOtherRequestJoinGroupReplied(long client, long reqId, boolean agree) throws IOException; // C9

    void onRequestGroupMemberList(long client, long groupId) throws IOException; // CA

    void onUserLogout(long client); // automatically triggered when connection lost
}
