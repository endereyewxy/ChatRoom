package chatroom.protocols;

public interface IServer {
    void bind(IServerSocket socket);

    void acquireUserList(int client);

    void acquireChatList(int client);

    void acquireChatMemberList(int client, int chatUuid);

    void requestSignIn(int client, String username, String password);

    void requestCreateChat(int client, String name);

    void requestJoinChat(int client, int userUuid, int chatUuid);

    void requestQuitChat(int client, int userUuid, int chatUuid);

    void requestSendMessage(int client, int chatUuid, String text);
}
