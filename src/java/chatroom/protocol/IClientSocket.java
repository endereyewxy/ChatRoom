package chatroom.protocol;

import java.io.IOException;

public interface IClientSocket {
    void bind(IClient client);

    void init();

    void stop();


    void requestSignIn(String name, String pMd5) throws IOException;


    void requestChatInfo(int chat) throws IOException;

    void requestInitChat(String name, Integer[] users) throws IOException;

    void requestJoinChat(int chat, int user) throws IOException;

    void requestQuitChat(int chat, int user) throws IOException;


    void sendMessage(int uuid, String msg) throws IOException;

    void sendFileMsg(int uuid, Byte[] data) throws IOException;
}
