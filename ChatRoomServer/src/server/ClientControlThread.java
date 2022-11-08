package server;

import database.api.DAO;
import database.model.User;
import utils.Message;
import utils.MsgType;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * 管理客户端socket
 */
public class ClientControlThread extends Thread{
    private List<ClientControlThread> clients;
    private Socket socket;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private String username;
    private boolean beConnected;

    public ClientControlThread(List<ClientControlThread> clients, Socket socket, ObjectInputStream is, ObjectOutputStream os, String name) {
        this.clients = clients;
        this.socket = socket;
        this.is = is;
        this.os = os;
        this.username = name;
        this.beConnected = true;
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(MsgType msgType,String message){
        try {
            for (ClientControlThread client : clients){
                client.os.writeObject(new Message(msgType,message));
            }
        } catch (NullPointerException e) {
            System.out.println("发送信息失败");
        } catch (IOException e) {
            System.out.println("发送失败！");
        }
    }
    public void sendToOne(String[] info){
        try {
            for (ClientControlThread client : clients){
                if(client.getUsername().equals(info[0])){
                    client.os.writeObject(new Message(MsgType.PRIVATE_MSG,String.format("%s发来的私信：%s",this.getUsername(),info[1])));
                }
            }
        } catch (NullPointerException e) {
            System.out.println("发送信息失败");
        } catch (IOException e) {
            System.out.println("发送失败！");
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        clients.remove(this);
        this.sendMessage(MsgType.PUBLIC_MSG,String.format("------%s下线了------",this.getUsername()));
        this.sendUserList();
        try {
            if (is != null) is.close();
            if (os != null) os.close();
            if (socket != null) socket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 往每个用户发送在线用户列表
     */
    public void sendUserList() {
        StringBuilder nameList = new StringBuilder();//用于存放在线用户列表
        int length = clients.size();
        for (int i = 0 ; i < length; i++) {
            nameList.append(clients.get(i).getUsername());
            if(i<length-1)
                nameList.append('&');
        }
        sendMessage(MsgType.USER_LIST,nameList.toString());
    }

    public void run() {
        try{
            Message message = null;//客户端发送过来的内容，服务器转发给所有用户
            sendMessage(MsgType.PUBLIC_MSG,String.format("------%s上线了------",this.getUsername()));
            sendUserList();//发送新的用户列表
                while (beConnected) {
                    message = (Message) is.readObject();
                    switch (message.getType()) {
                        case UPDATE_USER:
                            String[] passes = message.getContent().split("&");
                            User user = DAO.queryAUser(this.username);
                            if (user.getPassword().equals(passes[0])) {
                                boolean flag = DAO.updateUserPassword(this.username, passes[1]);
                                if (flag) os.writeObject(new Message(MsgType.UPDATE_USER, "true"));
                                else os.writeObject(new Message(MsgType.UPDATE_USER, "false"));
                            } else {
                                os.writeObject(new Message(MsgType.UPDATE_USER, "oldPasswordError"));
                            }
                            break;
                        case DELETE_USER:
                            String name = message.getContent();
                            if (DAO.deleteUser(name))
                                os.writeObject(new Message(MsgType.DELETE_USER, "true"));
                            else
                                os.writeObject(new Message(MsgType.DELETE_USER, "false"));
                            break;
                        case PRIVATE_MSG:
                            String[] sendToOneInfo = message.getContent().split("&");
                            sendToOne(sendToOneInfo);
                            break;
                        case PUBLIC_MSG:
                            sendMessage(MsgType.PUBLIC_MSG, String.format("%s:%s", this.getUsername(), message.getContent()));
                            break;
                    }
                }
            } catch (IOException ioException) {
                System.out.println(this.username+"下线了！");
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } finally{
                this.close();
            }
    }
}
