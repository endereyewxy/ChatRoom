package chatroom.protocols;

import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;

public interface IClient {
    void bind(IClientSocket socket);

    void replyUserList(User[] users);

    void replyChatList(Chat[] chats);

    void replyChatMemberList(User[] users);

    void notifySignInSucceeded(int userUuid);

    void notifyChatJoinRequest(int userUuid, int chatUuid);

    void notifyChatMembersChanged(User user, Chat chat);

    void notifyMessageReceived(int userUuid, int chatUuid, String text);
}
