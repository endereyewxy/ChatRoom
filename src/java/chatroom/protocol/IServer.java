package chatroom.protocol;

import java.io.IOException;

public interface IServer {
    void bind(IServerSocket socket);


    void requestSignIn(int client, String name, String pMd5) throws IOException;

    void clientOffline(int client) throws IOException;


    void requestChatInfo(int client, int chat) throws IOException;

    void requestInitChat(int client, String name, Integer[] users) throws IOException;

    void requestJoinChat(int client, int chat, int user) throws IOException;

    void requestQuitChat(int client, int chat, int user) throws IOException;


    void sendMessage(int client, int uuid, String msg) throws IOException;

    void sendFileMsg(int client, int uuid, byte[] data) throws IOException;
}
