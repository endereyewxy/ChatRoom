package chatroom.implement.client;

import chatroom.implement.client.ui.UIClient;
import chatroom.protocols.IClient;
import chatroom.protocols.IClientSocket;
import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;

public class Client implements IClient {
    private IClientSocket socket;

    @Override
    public void bind(IClientSocket socket) {
        this.socket = socket;
        UIClient.launch(this, socket);
    }

    @Override
    public void replyUserList(User[] users) {
    }

    @Override
    public void replyChatList(Chat[] chats) {
    }

    @Override
    public void replyChatMemberList(User[] users) {
    }

    @Override
    public void notifySignInSucceeded(int userUuid) {
    }

    @Override
    public void notifyChatJoinRequest(int userUuid, int chatUuid) {
    }

    @Override
    public void notifyChatMembersChanged(User user, Chat chat) {
    }

    @Override
    public void notifyMessageReceived(int userUuid, int chatUuid, String text) {
    }
}
