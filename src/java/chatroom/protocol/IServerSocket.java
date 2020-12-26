package chatroom.protocol;

import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;

import java.io.IOException;

public interface IServerSocket {
    void bind(IServer server);

    void init();

    void stop();


    void notifySignInAccepted(int client, int uuid) throws IOException;

    void notifySignInRejected(int client, byte why) throws IOException;


    void updateUserList(int client, User[] users) throws IOException;

    void updateChatList(int client, Chat[] chats) throws IOException;

    void updateChatInfo(int client, User[] users) throws IOException;


    void notifyChatJoined(int client, int chat, int user, byte reason) throws IOException;

    void notifyChatQuited(int client, int chat, int user, byte reason) throws IOException;


    void notifyChatJoinRequest(int client, int chat, int user) throws IOException;


    void notifyMessage(int client, int chat, int from, String msg) throws IOException;

    void notifyFileMsg(int client, int chat, int from, Byte[] data) throws IOException;
}
