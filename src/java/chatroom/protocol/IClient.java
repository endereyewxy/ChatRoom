package chatroom.protocol;

import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;

import java.io.IOException;

public interface IClient {
    void bind(IClientSocket socket);


    void notifySignInAccepted(int uuid) throws IOException;

    void notifySignInRejected(byte why) throws IOException;


    void updateUserList(User[] users) throws IOException;

    void updateChatList(Chat[] chats) throws IOException;

    void updateChatInfo(User[] users) throws IOException;


    void notifyChatJoined(int chat, int user, byte reason) throws IOException;

    void notifyChatQuited(int chat, int user, byte reason) throws IOException;


    void notifyChatJoinRequest(int chat, int user) throws IOException;


    void notifyMessage(int chat, int from, String msg) throws IOException;

    void notifyFileMsg(int chat, int from, byte[] data) throws IOException;
}
