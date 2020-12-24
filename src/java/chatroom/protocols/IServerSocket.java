package chatroom.protocols;

import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;

public interface IServerSocket {
    void bind(IServer server);

    void replyUserList(int client, User[] users);

    void replyChatList(int client, Chat[] chats);

    void replyChatMemberList(int client, User[] users);

    void notifySignInSucceeded(int client, int userUuid);

    void notifyChatJoinRequest(int client, int userUuid, int chatUuid);

    void notifyChatMembersChanged(int client, User user, Chat chat);

    void notifyMessageReceived(int client, int userUuid, int chatUuid, String text);
}
