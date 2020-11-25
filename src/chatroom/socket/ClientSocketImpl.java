package chatroom.socket;

import chatroom.model.clientMsgModel;
import chatroom.model.serverMsgModel;
import chatroom.protocol.IClientApp;

import java.io.*;
import java.net.Socket;

public class ClientSocketImpl implements chatroom.protocol.IClientSocket {
    private IClientApp app;    //todo
    public Socket socket;

    public ClientSocketImpl() {
        System.out.println("客户端启动");
        try {
            socket = new Socket("127.0.0.1", 8888);
            ReadThread readThread = new ReadThread();
            readThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class ReadThread extends Thread {
        boolean runFlag = true;
        ObjectInputStream in;
        public void run() {
            while (runFlag) {
                if (socket.isClosed()) {
                    return;
                }
                try {
                    if(socket.getInputStream().available()==0)
                        continue;
                    in = new ObjectInputStream(socket.getInputStream());
                    Object obj = in.readObject();
                    if (obj instanceof serverMsgModel.s1) {
                        serverMsgModel.s1 msg = ((serverMsgModel.s1) obj);
                        System.out.println("Client receive msg(type s1)");
                        app.onLoginResultReceived(msg.getUserId());
                    } else if (obj instanceof serverMsgModel.s2) {
                        serverMsgModel.s2 msg = ((serverMsgModel.s2) obj);
                        System.out.println("Client receive msg(type s2)");
                        app.onUserListReceived(msg.getUserId(), msg.getUsernames());
                    } else if (obj instanceof serverMsgModel.s3) {
                        serverMsgModel.s3 msg = ((serverMsgModel.s3) obj);
                        System.out.println("Client receive msg(type s3)");
                        app.onGroupListReceived(msg.getGroupIds(), msg.getGroupNames(), msg.getFlags());
                    } else if (obj instanceof serverMsgModel.s4) {
                        serverMsgModel.s4 msg = ((serverMsgModel.s4) obj);
                        System.out.println("Client receive msg(type s4)");
                        app.onOtherRequestJoinGroup(msg.getGroupId(), msg.getUserId(), msg.getReqId());
                    } else if (obj instanceof serverMsgModel.s5) {
                        serverMsgModel.s5 msg = ((serverMsgModel.s5) obj);
                        System.out.println("Client receive msg(type s5)");
                        app.onGroupJoined(msg.getFlags(), msg.getUserId());
                    } else if (obj instanceof serverMsgModel.s6) {
                        serverMsgModel.s6 msg = ((serverMsgModel.s6) obj);
                        System.out.println("Client receive msg(type s6)");
                        app.onGroupLeft(msg.getGroupId());
                    } else if (obj instanceof serverMsgModel.s7) {
                        serverMsgModel.s7 msg = ((serverMsgModel.s7) obj);
                        System.out.println("Client receive msg(type s7)");
                        app.onMessageReceived(msg.getGroupId(), msg.getUserId(), msg.getMsg());
                    } else {
                        serverMsgModel.s8 msg = ((serverMsgModel.s8) obj);
                        System.out.println("Client receive msg(type s8)");
                        app.onGroupMemberListReceived(msg.getGroupId(), msg.getUserIds(), msg.getUsernames(), msg.getFlags());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ClientSocketImpl client = new ClientSocketImpl();
        client.requestLogin("user1","123456");
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
        clientMsgModel.c6 req6 = new clientMsgModel.c6(groupId, userId);
        sendMsg(socket, req6);
    }

    @Override
    public void requestLeaveGroup(long groupId) throws IOException {
        clientMsgModel.c7 req7 = new clientMsgModel.c7(groupId);
        sendMsg(socket, req7);
    }

    @Override
    public void requestSendMessage(long groupId, String msg) throws IOException {
        clientMsgModel.c8 req8 = new clientMsgModel.c8(groupId, msg);
        sendMsg(socket, req8);
    }

    @Override
    public void replyOtherRequestJoinGroup(long reqId, boolean agree) throws IOException {
        clientMsgModel.c9 req9 = new clientMsgModel.c9(reqId, agree);
        sendMsg(socket, req9);
    }

    @Override
    public void requestGroupMemberList(long groupId) throws IOException {
        clientMsgModel.cA reqA = new clientMsgModel.cA(groupId);
        sendMsg(socket, reqA);
    }
}
