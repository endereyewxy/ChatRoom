package chatroom.implement.client;

import chatroom.implement.client.ui.UIClient;
import chatroom.protocols.IClient;
import chatroom.protocols.IClientSocket;
import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;

public class Client implements IClient {
    @Override
    public void bind(IClientSocket socket) {
        UIClient.launch(socket);
    }

    @Override
    public void replyUserList(User[] users) {
        UIClient.replyUserList(users);
    }

    @Override
    public void replyChatList(Chat[] chats) {
        UIClient.replyChatList(chats);
    }

    @Override
    public void replyChatMemberList(User[] users) {
        UIClient.replyChatMemberList(users);
    }

    @Override
    public void notifySignInSucceeded(int userUuid) {
        UIClient.notifySignInSucceeded(userUuid);
    }

    @Override
    public void notifyChatJoinRequest(int userUuid, int chatUuid) {
        UIClient.notifyChatJoinRequest(userUuid, chatUuid);
    }

    @Override
    public void notifyChatMembersChanged(User user, Chat chat) {
        UIClient.notifyChatMembersChanged(user, chat);
    }

    @Override
    public void notifyMessageReceived(int userUuid, int chatUuid, String text) {
        UIClient.notifyMessageReceived(userUuid, chatUuid, text);
    }
}
