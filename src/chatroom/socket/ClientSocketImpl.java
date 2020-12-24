package chatroom.socket;

import chatroom.model.ClientMsgModel;
import chatroom.model.ServerMsgModel;
import chatroom.protocol.IClientApp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketImpl implements chatroom.protocol.IClientSocket {
    private IClientApp app;
    public  Socket     socket;

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
        boolean           runFlag = true;
        ObjectInputStream in;

        public void run() {
            while (runFlag) {
                if (socket.isClosed()) {
                    return;
                }
                try {
                    if (socket.getInputStream().available() == 0)
                        continue;
                    in = new ObjectInputStream(socket.getInputStream());
                    Object obj = in.readObject();
                    if (obj instanceof ServerMsgModel.s1) {
                        ServerMsgModel.s1 msg = ((ServerMsgModel.s1) obj);
                        System.out.println("Client receive msg(type s1)");
                        app.onLoginResultReceived(msg.getUserId());
                    } else if (obj instanceof ServerMsgModel.s2) {
                        ServerMsgModel.s2 msg = ((ServerMsgModel.s2) obj);
                        System.out.println("Client receive msg(type s2)");
                        app.onUserListReceived(msg.getUserId(), msg.getUsernames());
                    } else if (obj instanceof ServerMsgModel.s3) {
                        ServerMsgModel.s3 msg = ((ServerMsgModel.s3) obj);
                        System.out.println("Client receive msg(type s3)");
                        app.onGroupListReceived(msg.getGroupIds(), msg.getGroupNames(), msg.getFlags());
                    } else if (obj instanceof ServerMsgModel.s4) {
                        ServerMsgModel.s4 msg = ((ServerMsgModel.s4) obj);
                        System.out.println("Client receive msg(type s4)");
                        app.onOtherRequestJoinGroup(msg.getGroupId(), msg.getUserId(), msg.getReqId());
                    } else if (obj instanceof ServerMsgModel.s5) {
                        ServerMsgModel.s5 msg = ((ServerMsgModel.s5) obj);
                        System.out.println("Client receive msg(type s5)");
                        app.onGroupJoined(msg.getFlags(), msg.getUserId());
                    } else if (obj instanceof ServerMsgModel.s6) {
                        ServerMsgModel.s6 msg = ((ServerMsgModel.s6) obj);
                        System.out.println("Client receive msg(type s6)");
                        app.onGroupLeft(msg.getGroupId());
                    } else if (obj instanceof ServerMsgModel.s7) {
                        ServerMsgModel.s7 msg = ((ServerMsgModel.s7) obj);
                        System.out.println("Client receive msg(type s7)");
                        app.onMessageReceived(msg.getGroupId(), msg.getUserId(), msg.getMsg());
                    } else {
                        ServerMsgModel.s8 msg = ((ServerMsgModel.s8) obj);
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
        client.requestLogin("user1", "123456");
    }

    public void sendMsg(Socket socket, Object object) throws IOException {
        OutputStream       os                 = socket.getOutputStream();
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
        ClientMsgModel.c1 req1 = new ClientMsgModel.c1(username, passwordMd5);
        sendMsg(socket, req1);
    }

    @Override
    public void requestUserList() throws IOException {
        ClientMsgModel.c2 req2 = new ClientMsgModel.c2();
        sendMsg(socket, req2);
    }

    @Override
    public void requestGroupList() throws IOException {
        ClientMsgModel.c3 req3 = new ClientMsgModel.c3();
        sendMsg(socket, req3);
    }

    @Override
    public void requestCreateGroup(String groupName) throws IOException {
        ClientMsgModel.c4 req4 = new ClientMsgModel.c4(groupName);
        sendMsg(socket, req4);
    }

    @Override
    public void requestJoinGroup(long groupId) throws IOException {
        ClientMsgModel.c5 req5 = new ClientMsgModel.c5(groupId);
        sendMsg(socket, req5);
    }

    @Override
    public void requestInvite(long groupId, long userId) throws IOException {
        ClientMsgModel.c6 req6 = new ClientMsgModel.c6(groupId, userId);
        sendMsg(socket, req6);
    }

    @Override
    public void requestLeaveGroup(long groupId) throws IOException {
        ClientMsgModel.c7 req7 = new ClientMsgModel.c7(groupId);
        sendMsg(socket, req7);
    }

    @Override
    public void requestSendMessage(long groupId, String msg) throws IOException {
        ClientMsgModel.c8 req8 = new ClientMsgModel.c8(groupId, msg);
        sendMsg(socket, req8);
    }

    @Override
    public void replyOtherRequestJoinGroup(long reqId, boolean agree) throws IOException {
        ClientMsgModel.c9 req9 = new ClientMsgModel.c9(reqId, agree);
        sendMsg(socket, req9);
    }

    @Override
    public void requestGroupMemberList(long groupId) throws IOException {
        ClientMsgModel.cA reqA = new ClientMsgModel.cA(groupId);
        sendMsg(socket, reqA);
    }
}
