package chatroom.protocols;

import chatroom.protocols.entity.Chat;
import chatroom.protocols.entity.User;

import java.io.IOException;

public interface IServerSocket {
    void bind(IServer server);

    void replyUserList(int client, User[] users) throws IOException;

    void replyChatList(int client, Chat[] chats) throws IOException;

    void replyChatMemberList(int client, User[] users) throws IOException;

    void notifySignInSucceeded(int client, int userUuid) throws IOException;

    void notifyChatJoinRequest(int client, int userUuid, int chatUuid) throws IOException;

    void notifyChatMembersChanged(int client, User user, Chat chat) throws IOException;

    void notifyMessageReceived(int client, int userUuid, int chatUuid, String text) throws IOException;
}
