package chatroom.protocols;

import java.io.IOException;

public interface IClientSocket {
    void bind(IClient client);

    void acquireUserList() throws IOException;

    void acquireChatList() throws IOException;

    void acquireChatMemberList(int chatUuid) throws IOException;

    void requestSignIn(String username, String password) throws IOException;

    void requestCreateChat(String name) throws IOException;

    void requestJoinChat(int userUuid, int chatUuid) throws IOException;

    void requestQuitChat(int userUuid, int chatUuid) throws IOException;

    void requestSendMessage(int chatUuid, String text) throws IOException;
}
