package chatroom.protocols;

import java.io.IOException;

public interface IServer {
    void bind(IServerSocket socket);

    void acquireUserList(int client) throws IOException;

    void acquireChatList(int client) throws IOException;

    void acquireChatMemberList(int client, int chatUuid) throws IOException;

    void requestSignIn(int client, String username, String password) throws IOException;

    void requestCreateChat(int client, String name) throws IOException;

    void requestJoinChat(int client, int userUuid, int chatUuid) throws IOException;

    void requestQuitChat(int client, int userUuid, int chatUuid) throws IOException;

    void requestSendMessage(int client, int chatUuid, String text) throws IOException;
}
