package chatroom.socket;

import chatroom.model.clientMsgModel;
import chatroom.model.serverMsgModel;
import chatroom.protocol.IServerApp;
import chatroom.server.ServerImpl;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerSocketImpl extends ServerSocket implements chatroom.protocol.IServerSocket {
    private IServerApp app= new ServerImpl();
    private Socket socket;
    static long count = 1L;
    private final HashMap<Long, ServerThread> threads = new HashMap<>();

    public ServerSocketImpl() throws IOException {
        try {
            System.out.println("服务端启动");
            this.setReuseAddress(true);
            this.bind(new InetSocketAddress(8888));
            while (true) {
                socket = this.accept();
                System.out.println("客户端进入");
                final ServerThread task = new ServerThread(socket);
                threads.put(task.getClient(), task);
                task.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ServerThread extends Thread {
        private Socket socket;
        private long client;
        boolean runFlag = true;

        public ServerThread(Socket socket) {
            if (socket == null) {
                runFlag = false;
                app.onUserLogout(client);
                return;
            }
            this.socket = socket;
            this.client = count++;
            ReadThread readThread = new ReadThread();
            readThread.start();
        }

        public Socket getSocket() {
            return this.socket;
        }

        public long getClient() {
            return this.client;
        }

        class ReadThread extends Thread {
            boolean runFlag = true;
            ObjectInputStream in;

            public void run() {
                try {
                    in = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (runFlag) {
                    if (socket.isClosed()) {
                        return;
                    }
                    try {
                        Object obj = in.readObject();
                        if (obj instanceof clientMsgModel.c1) {
                            clientMsgModel.c1 msg = ((clientMsgModel.c1) obj);
                            System.out.println("Client receive msg(type c1)");
                            //todo deal with app == null's probelm
                            app.onRequestLogin(client,msg.getUsername(), msg.getPasswordMd5());
                        } else if (obj instanceof clientMsgModel.c2) {
                            clientMsgModel.c2 msg = ((clientMsgModel.c2) obj);
                            System.out.println("Client receive msg(type c2)");
                            app.onRequestUserList(client);
                        } else if (obj instanceof clientMsgModel.c3) {
                            clientMsgModel.c3 msg = ((clientMsgModel.c3) obj);
                            System.out.println("Client receive msg(type c3)");
                            app.onRequestGroupList(client);
                        } else if (obj instanceof clientMsgModel.c4) {
                            clientMsgModel.c4 msg = ((clientMsgModel.c4) obj);
                            System.out.println("Client receive msg(type c4)");
                            app.onRequestCreateGroup(client, msg.getGroupName());
                        } else if (obj instanceof clientMsgModel.c5) {
                            clientMsgModel.c5 msg = ((clientMsgModel.c5) obj);
                            System.out.println("Client receive msg(type c5)");
                            app.onRequestJoinGroup(client, msg.getGroupId());
                        } else if (obj instanceof clientMsgModel.c6) {
                            clientMsgModel.c6 msg = ((clientMsgModel.c6) obj);
                            System.out.println("Client receive msg(type c6)");
                            app.onRequestInvite(client, msg.getGroupId(), msg.getUserId());
                        } else if (obj instanceof clientMsgModel.c7) {
                            clientMsgModel.c7 msg = ((clientMsgModel.c7) obj);
                            System.out.println("Client receive msg(type c7)");
                            app.onRequestLeaveGroup(client, msg.getGroupId());
                        } else if (obj instanceof clientMsgModel.c8) {
                            clientMsgModel.c8 msg = ((clientMsgModel.c8) obj);
                            System.out.println("Client receive msg(type c8)");
                            app.onRequestSendMessage(client, msg.getGroupId(), msg.getMsg());
                        } else if (obj instanceof clientMsgModel.c9) {
                            clientMsgModel.c9 msg = ((clientMsgModel.c9) obj);
                            System.out.println("Client receive msg(type c9)");
                            app.onOtherRequestJoinGroupReplied(client, msg.getReqId(), msg.isAgree());
                        } else {
                            clientMsgModel.cA msg = ((clientMsgModel.cA) obj);
                            System.out.println("Client receive msg(type cA)");
                            app.onRequestGroupMemberList(client, msg.getGroupId());
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

    }


    public static void main(String[] args) throws IOException {
        ServerSocketImpl client = new ServerSocketImpl();
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
        serverMsgModel.s1 msg1 = new serverMsgModel.s1(userId);
        Socket socket = getSocket(client);
        sendMsg(socket, msg1);
    }

    @Override
    public void replyUserList(long client, Long[] userIds, String[] usernames) throws IOException {
        serverMsgModel.s2 msg2 = new serverMsgModel.s2(userIds, usernames);
        Socket socket = getSocket(client);
        sendMsg(socket, msg2);
    }

    @Override
    public void replyGroupList(long client, Long[] groupIds, String[] groupNames, Byte[] flags) throws IOException {
        serverMsgModel.s3 msg3 = new serverMsgModel.s3(groupIds, groupNames, flags);
        Socket socket = getSocket(client);
        sendMsg(socket, msg3);
    }

    @Override
    public void notifyOtherRequestJoinGroup(long client, long groupId, long userId, long reqId) throws IOException {
        serverMsgModel.s4 msg4 = new serverMsgModel.s4(groupId, userId, reqId);
        Socket socket = getSocket(client);
        sendMsg(socket, msg4);
    }

    @Override
    public void replyGroupJoined(long client, byte flags, long groupId) throws IOException {
        serverMsgModel.s5 msg5 = new serverMsgModel.s5(flags, groupId);
        Socket socket = getSocket(client);
        sendMsg(socket, msg5);
    }

    @Override
    public void notifyGroupLeft(long client, long groupId) throws IOException {
        serverMsgModel.s6 msg6 = new serverMsgModel.s6(groupId);
        Socket socket = getSocket(client);
        sendMsg(socket, msg6);
    }

    @Override
    public void notifyMessage(long client, long groupId, long userId, String msg) throws IOException {
        serverMsgModel.s7 msg7 = new serverMsgModel.s7(groupId, userId, msg);
        Socket socket = getSocket(client);
        sendMsg(socket, msg7);
    }

    @Override
    public void replyRequestMemberList(long client, long groupId, Long[] userIds, String[] usernames, Byte[] flags) throws IOException {
        serverMsgModel.s8 msg8 = new serverMsgModel.s8(groupId, userIds, usernames, flags);
        Socket socket = getSocket(client);
        sendMsg(socket, msg8);
    }

}
