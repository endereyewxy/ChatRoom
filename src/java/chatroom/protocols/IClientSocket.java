package chatroom.protocols;

import chatroom.protocols.entity.User;

import java.io.IOException;

public interface IClientSocket {
    void bind(IClient client);

    void closeSocket() throws IOException;

    void acquireUserList() throws IOException;

    void acquireChatList() throws IOException;

    void acquireChatMemberList(int chatUuid) throws IOException;

    void requestSignIn(String username, String password) throws IOException;

    void requestCreateChat(String name, User[] users) throws IOException;

    void requestJoinChat(int userUuid, int chatUuid) throws IOException;

    void requestQuitChat(int userUuid, int chatUuid) throws IOException;

    void requestSendMessage(int chatUuid, String text) throws IOException;
}
