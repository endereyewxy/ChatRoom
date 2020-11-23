package chatroom.socket;

import chatroom.protocol.IClientApp;

public class ClientSocketImpl implements chatroom.protocol.IClientSocket {
    @Override
    public void bind(IClientApp app) {

    }

    @Override
    public void requestLogin(String username, String passwordMd5) {

    }

    @Override
    public void requestUserList() {

    }

    @Override
    public void requestGroupList() {

    }

    @Override
    public void requestCreateGroup(String groupName) {

    }

    @Override
    public void requestJoinGroup(long groupId) {

    }

    @Override
    public void requestInvite(long groupId, long userId) {

    }

    @Override
    public void requestLeaveGroup(long groupId) {

    }

    @Override
    public void requestSendMessage(long groupId, String msg) {

    }

    @Override
    public void replyOtherRequestJoinGroup(long reqId, boolean agree) {

    }

    @Override
    public void requestGroupMemberList(long groupId) {

    }
}
