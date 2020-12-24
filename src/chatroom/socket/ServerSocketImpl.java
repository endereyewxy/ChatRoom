package chatroom.socket;

import chatroom.model.ClientMsgModel;
import chatroom.model.ServerMsgModel;
import chatroom.protocol.IServerApp;
import chatroom.server.ServerImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerSocketImpl extends ServerSocket implements chatroom.protocol.IServerSocket {
    private       IServerApp                  app     = new ServerImpl();
    static        long                        count   = 1L;
    private final HashMap<Long, ServerThread> threads = new HashMap<>();

    public ServerSocketImpl() throws IOException {
        try {
            System.out.println("服务端启动");
            this.setReuseAddress(true);
            this.bind(new InetSocketAddress(8888));
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = this.accept();
                this.app.bind(this);
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
        private final Socket socket;
        private final long   client;

        public ServerThread(Socket socket) {
            this.socket = socket;
            this.client = count++;
            System.out.println("连接的客户端序号为：" + client);
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
            boolean           runFlag = true;
            ObjectInputStream in;

            public void run() {
                while (runFlag) {
                    if (socket.isClosed()) {
                        System.out.println("线程关闭");
                        return;
                    }
                    try {
                        if (socket.getInputStream().available() == 0)
                            continue;
                        in = new ObjectInputStream(socket.getInputStream());
                        Object obj = in.readObject();
                        if (obj instanceof ClientMsgModel.c1) {
                            ClientMsgModel.c1 msg = ((ClientMsgModel.c1) obj);
                            System.out.println("Server receive msg(type c1)");
                            app.onRequestLogin(client, msg.getUsername(), msg.getPasswordMd5());
                        } else if (obj instanceof ClientMsgModel.c2) {
                            System.out.println("Server receive msg(type c2)");
                            app.onRequestUserList(client);
                        } else if (obj instanceof ClientMsgModel.c3) {
                            System.out.println("Server receive msg(type c3)");
                            app.onRequestGroupList(client);
                        } else if (obj instanceof ClientMsgModel.c4) {
                            ClientMsgModel.c4 msg = ((ClientMsgModel.c4) obj);
                            System.out.println("Server receive msg(type c4)");
                            app.onRequestCreateGroup(client, msg.getGroupName());
                        } else if (obj instanceof ClientMsgModel.c5) {
                            ClientMsgModel.c5 msg = ((ClientMsgModel.c5) obj);
                            System.out.println("Server receive msg(type c5)");
                            app.onRequestJoinGroup(client, msg.getGroupId());
                        } else if (obj instanceof ClientMsgModel.c6) {
                            ClientMsgModel.c6 msg = ((ClientMsgModel.c6) obj);
                            System.out.println("Server receive msg(type c6)");
                            app.onRequestInvite(client, msg.getGroupId(), msg.getUserId());
                        } else if (obj instanceof ClientMsgModel.c7) {
                            ClientMsgModel.c7 msg = ((ClientMsgModel.c7) obj);
                            System.out.println("Server receive msg(type c7)");
                            app.onRequestLeaveGroup(client, msg.getGroupId());
                        } else if (obj instanceof ClientMsgModel.c8) {
                            ClientMsgModel.c8 msg = ((ClientMsgModel.c8) obj);
                            System.out.println("Server receive msg(type c8)");
                            app.onRequestSendMessage(client, msg.getGroupId(), msg.getMsg());
                        } else if (obj instanceof ClientMsgModel.c9) {
                            ClientMsgModel.c9 msg = ((ClientMsgModel.c9) obj);
                            System.out.println("Server receive msg(type c9)");
                            app.onOtherRequestJoinGroupReplied(client, msg.getReqId(), msg.isAgree());
                        } else {
                            ClientMsgModel.cA msg = ((ClientMsgModel.cA) obj);
                            System.out.println("Server receive msg(type cA)");
                            app.onRequestGroupMemberList(client, msg.getGroupId());
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        //关闭线程
                        try {
                            socket.close();
                            app.onUserLogout(client);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        }

    }

    public Socket getSocket(long client) {
        //遍历活动线程查找到对应的线程
        final ServerThread thread = threads.get(client);
        return thread.getSocket();
    }

    public void sendMsg(Socket socket, Object object) throws IOException {
        OutputStream       os                 = socket.getOutputStream();
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
        ServerMsgModel.s1 msg1   = new ServerMsgModel.s1(userId);
        Socket            socket = getSocket(client);
        sendMsg(socket, msg1);
    }

    @Override
    public void replyUserList(long client, Long[] userIds, String[] usernames) throws IOException {
        ServerMsgModel.s2 msg2   = new ServerMsgModel.s2(userIds, usernames);
        Socket            socket = getSocket(client);
        sendMsg(socket, msg2);
    }

    @Override
    public void replyGroupList(long client, Long[] groupIds, String[] groupNames, Byte[] flags) throws IOException {
        ServerMsgModel.s3 msg3   = new ServerMsgModel.s3(groupIds, groupNames, flags);
        Socket            socket = getSocket(client);
        sendMsg(socket, msg3);
    }

    @Override
    public void notifyOtherRequestJoinGroup(long client, long groupId, long userId, long reqId) throws IOException {
        ServerMsgModel.s4 msg4   = new ServerMsgModel.s4(groupId, userId, reqId);
        Socket            socket = getSocket(client);
        sendMsg(socket, msg4);
    }

    @Override
    public void replyGroupJoined(long client, byte flags, long groupId) throws IOException {
        ServerMsgModel.s5 msg5   = new ServerMsgModel.s5(flags, groupId);
        Socket            socket = getSocket(client);
        sendMsg(socket, msg5);
    }

    @Override
    public void notifyGroupLeft(long client, long groupId) throws IOException {
        ServerMsgModel.s6 msg6   = new ServerMsgModel.s6(groupId);
        Socket            socket = getSocket(client);
        sendMsg(socket, msg6);
    }

    @Override
    public void notifyMessage(long client, long groupId, long userId, String msg) throws IOException {
        ServerMsgModel.s7 msg7   = new ServerMsgModel.s7(groupId, userId, msg);
        Socket            socket = getSocket(client);
        sendMsg(socket, msg7);
    }

    @Override
    public void replyRequestMemberList(long client, long groupId, Long[] userIds, String[] usernames, Byte[] flags) throws IOException {
        ServerMsgModel.s8 msg8   = new ServerMsgModel.s8(groupId, userIds, usernames, flags);
        Socket            socket = getSocket(client);
        sendMsg(socket, msg8);
    }
}
