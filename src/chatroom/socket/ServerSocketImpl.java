package chatroom.socket;

import chatroom.model.serverModel;
import chatroom.protocol.IServerApp;
import chatroom.server.ServerThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerSocketImpl extends ServerSocket implements chatroom.protocol.IServerSocket{
    private IServerApp app;
    private Socket socket;
    private HashMap<Long, ServerThread> threads = new HashMap<>();

    public ServerSocketImpl() throws IOException {
        System.out.println("服务端启动");
        while (true) {
            try {
                socket = this.accept();
                final ServerThread task = new ServerThread(socket);//todo 将所有连接放入线程管理
                threads.put(task.getClient(), task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Socket getSocket(long client) {
        //遍历活动线程查找到对应的线程
        final ServerThread thread = threads.get(client);
        return thread.getSocket();
    }

    public void sendMsg(Socket socket, Object object) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    @Override
    public void bind(IServerApp app) {
        this.app = app;
    }

    @Override
    public void replyLogin(long client, long userId) throws IOException {
        byte type = 'i';
        serverModel.s1 msg1 = new serverModel.s1(type, userId);
        Socket socket = getSocket(client);
        sendMsg(socket, msg1);
    }

    @Override
    public void replyUserList(long client, Long[] userIds, String[] usernames) throws IOException {
        byte type = 'u';
        serverModel.s2 msg2 = new serverModel.s2(type, userIds, usernames);
        Socket socket = getSocket(client);
        sendMsg(socket, msg2);
    }

    @Override
    public void replyGroupList(long client, Long[] groupIds, String[] groupNames, Byte[] flags) throws IOException {
        byte type = 'g';
        serverModel.s3 msg3 = new serverModel.s3(type, groupIds, groupNames, flags);
        Socket socket = getSocket(client);
        sendMsg(socket, msg3);
    }

    @Override
    public void notifyOtherRequestJoinGroup(long client, long groupId, long userId, long reqId) throws IOException {
        byte type = 'o';
        serverModel.s4 msg4 = new serverModel.s4(type, groupId, userId, reqId);
        Socket socket = getSocket(client);
        sendMsg(socket, msg4);
    }

    @Override
    public void replyGroupJoined(long client, byte flags, long groupId) throws IOException {
        byte type = 'j';
        serverModel.s5 msg5 = new serverModel.s5(type, flags, groupId);
        Socket socket = getSocket(client);
        sendMsg(socket, msg5);
    }

    @Override
    public void notifyGroupLeft(long client, long groupId) throws IOException {
        byte type = 'e';
        serverModel.s6 msg6 = new serverModel.s6(type, groupId);
        Socket socket = getSocket(client);
        sendMsg(socket, msg6);
    }

    @Override
    public void notifyMessage(long client, long groupId, long userId, String msg) throws IOException {
        byte type = 'm';
        serverModel.s7 msg7 = new serverModel.s7(type, groupId, userId, msg);
        Socket socket = getSocket(client);
        sendMsg(socket, msg7);
    }

    @Override
    public void replyRequestMemberList(long client, long groupId, Long[] userIds, String[] usernames, Byte[] flags) throws IOException {
        byte type = 'b';
        serverModel.s8 msg8 = new serverModel.s8(type, groupId, userIds, usernames, flags);
        Socket socket = getSocket(client);
        sendMsg(socket, msg8);
    }

}
