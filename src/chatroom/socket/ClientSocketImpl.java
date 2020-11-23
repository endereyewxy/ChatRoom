package chatroom.socket;

import chatroom.model.clientMsgModel;
import chatroom.protocol.IClientApp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketImpl implements chatroom.protocol.IClientSocket {
    private IClientApp app;

    public Socket socket;

    public ClientSocketImpl() throws IOException {
        System.out.println("客户端启动");
        try {
            socket = new Socket("127.0.0.1", 8888);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendMsg(Socket socket, Object object) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    @Override
    public void bind(IClientApp app) {
        this.app = app;
    }

    @Override
    public void requestLogin(String username, String passwordMd5) throws IOException {
        clientMsgModel.c1 req1 = new clientMsgModel.c1(username, passwordMd5);
        sendMsg(socket, req1);
    }

    @Override
    public void requestUserList() throws IOException {
        clientMsgModel.c2 req2 = new clientMsgModel.c2();
        sendMsg(socket, req2);
    }

    @Override
    public void requestGroupList() throws IOException {
        clientMsgModel.c3 req3 = new clientMsgModel.c3();
        sendMsg(socket, req3);
    }

    @Override
    public void requestCreateGroup(String groupName) throws IOException {
        clientMsgModel.c4 req4 = new clientMsgModel.c4(groupName);
        sendMsg(socket, req4);
    }

    @Override
    public void requestJoinGroup(long groupId) throws IOException {
        clientMsgModel.c5 req5 = new clientMsgModel.c5(groupId);
        sendMsg(socket, req5);
    }

    @Override
    public void requestInvite(long groupId, long userId) throws IOException {
        clientMsgModel.c6 req6 = new clientMsgModel.c6(groupId,userId);
        sendMsg(socket, req6);
    }

    @Override
    public void requestLeaveGroup(long groupId) throws IOException {
        clientMsgModel.c7 req7 = new clientMsgModel.c7(groupId);
        sendMsg(socket, req7);
    }

    @Override
    public void requestSendMessage(long groupId, String msg) throws IOException {
        clientMsgModel.c8 req8 = new clientMsgModel.c8(groupId,msg);
        sendMsg(socket, req8);
    }

    @Override
    public void replyOtherRequestJoinGroup(long reqId, boolean agree) throws IOException {
        clientMsgModel.c9 req9 = new clientMsgModel.c9(reqId,agree);
        sendMsg(socket, req9);
    }

    @Override
    public void requestGroupMemberList(long groupId) throws IOException {
        clientMsgModel.cA reqA = new clientMsgModel.cA(groupId);
        sendMsg(socket, reqA);
    }
}
