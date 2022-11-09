package server;

import constant.CommonConstant;
import database.UserDao;
import entity.User;
import entity.Message;
import constant.MsgType;
import utils.CommonUtil;

import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * 客户端socket控制线程
 */
public class ClientControlThread extends Thread {
    /**
     * 客户端集合引用
     */
    private final Map<String, ClientControlThread> clients;
    private final Socket socket;
    private final ObjectInputStream is;
    private final ObjectOutputStream os;
    private final String username;

    public ClientControlThread(Map<String, ClientControlThread> clients, Socket socket, ObjectInputStream is, ObjectOutputStream os, String name) {
        this.clients = clients;
        this.socket = socket;
        this.is = is;
        this.os = os;
        this.username = name;
    }

    /**
     * 群发消息
     *
     * @param msgType 消息类型
     * @param message 消息内容
     */
    public void sendMessage(MsgType msgType, String message) {
        clients.values().forEach(client -> {
            try {
                client.os.writeObject(Message.build(msgType, message));
            } catch (IOException e) {
                System.out.println(client.username + "转发消息失败");
            }
        });
    }

    /**
     * 私信
     *
     * @param target  私信目标
     * @param content 私信内容
     */
    public void sendToOne(String target, String content) {
        try {
            ClientControlThread client = clients.get(target);
            if (CommonUtil.isNull(client)) {
                System.out.println(target + "socket丢失，发送失败");
                clients.remove(target);
            } else {
                client.os.writeObject(Message.build(MsgType.PRIVATE_MSG, String.format("%s发来私信：%s", username, content)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        clients.remove(username);
        sendMessage(MsgType.PUBLIC_MSG, String.format("------%s下线了------", username));
        sendUserList();
        try {
            if (CommonUtil.isNotNull(is)) {
                is.close();
            }
            if (CommonUtil.isNotNull(os)) {
                os.close();
            }
            if (CommonUtil.isNotNull(socket)) {
                socket.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 往每个用户发送在线用户列表
     */
    public void sendUserList() {
        StringBuilder nameList = new StringBuilder();
        // 拼接用户列表
        clients.keySet().stream().map(str -> CommonConstant.AND + str).forEach(nameList::append);
        // 删除多余的&分隔符
        nameList.deleteCharAt(0);
        sendMessage(MsgType.USER_LIST, nameList.toString());
    }

    @Override
    public void run() {
        try {
            //客户端发送过来的内容，服务器转发给所有用户
            Message message;
            // 群发公告
            sendMessage(MsgType.PUBLIC_MSG, String.format("------%s上线了------", username));
            //发送新的用户列表
            sendUserList();
            while (true) {
                message = (Message) is.readObject();
                switch (message.getType()) {
                    case UPDATE_USER:
                        String[] passes = message.getContent().split("&");
                        User user = UserDao.queryUser(this.username);
                        if (CommonUtil.isEquals(user.getPassword(), passes[0])) {
                            boolean flag = UserDao.updateUserPassword(this.username, passes[1]);
                            if (flag) {
                                os.writeObject(Message.build(MsgType.UPDATE_SUCCESS, "更新账户成功"));
                            } else {
                                os.writeObject(Message.build(MsgType.UPDATE_FAIL, "更新账户失败"));
                            }
                        } else {
                            os.writeObject(Message.build(MsgType.UPDATE_FAIL, "旧密码错误"));
                        }
                        break;
                    case DELETE_USER:
                        String name = message.getContent();
                        UserDao.deleteUser(name);
                        os.writeObject(Message.build(MsgType.DELETE_SUCCESS, "注销成功"));
                        break;
                    case PRIVATE_MSG:
                        String[] sendToOneInfo = message.getContent().split("&");
                        sendToOne(sendToOneInfo[0], sendToOneInfo[1]);
                        break;
                    case PUBLIC_MSG:
                        sendMessage(MsgType.PUBLIC_MSG, message.getContent());
                        break;
                    default:
                }
            }
        } catch (IOException ioException) {
            System.out.println(this.username + "下线了！");
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } finally {
            this.close();
        }
    }
}
