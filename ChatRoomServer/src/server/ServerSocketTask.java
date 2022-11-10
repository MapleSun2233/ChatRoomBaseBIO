package server;

import constant.CommonConstant;
import database.UserDao;
import entity.User;
import entity.Message;
import constant.MsgType;
import utils.CommonUtil;
import utils.ThreadPoolUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSocketTask implements Runnable {
    /**
     * 客户端映射集合
     */
    private final Map<String, ClientControlThread> clients = new ConcurrentHashMap<>();
    private ServerSocket serverSocket;

    public ServerSocketTask(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Socket服务启动失败，请重启服务！");
        }
    }

    @Override
    public void run() {
        while (true) {
            // socket客户端连接
            Socket clientSocket;
            // 客户端输入流
            ObjectInputStream clientIS;
            // 客户端输出流
            ObjectOutputStream clientOS;
            try {
                // 阻塞接受一个连接
                clientSocket = serverSocket.accept();
                clientOS = new ObjectOutputStream(clientSocket.getOutputStream());
                clientIS = new ObjectInputStream(clientSocket.getInputStream());
                Message message = (Message) clientIS.readObject();
                switch (message.getType()) {
                    case REGISTER_USER:
                        registerUser(clientOS, message);
                        closeResource(clientSocket, clientIS, clientOS);
                        break;
                    case LOGIN_USER:
                        loginUser(clientSocket, clientIS, clientOS, message);
                        break;
                    case RETRIEVE_USER:
                        retrieveUser(clientOS, message);
                        closeResource(clientSocket, clientIS, clientOS);
                        break;
                    default:
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("客户端socket连接异常！");
            }
        }
    }

    /**
     * 关闭资管
     *
     * @param clientSocket 客户端连接
     * @param clientIS     输入流
     * @param clientOS     输出流
     * @throws IOException IO异常
     */
    public void closeResource(Socket clientSocket, ObjectInputStream clientIS, ObjectOutputStream clientOS) throws IOException {
        if (CommonUtil.isNotNull(clientOS)) {
            clientOS.close();
        }
        if (CommonUtil.isNotNull(clientIS)) {
            clientIS.close();
        }
        if (CommonUtil.isNotNull(clientSocket)) {
            clientSocket.close();
        }
    }

    public void registerUser(ObjectOutputStream clientOS, Message message) throws IOException {
        String[] registerInfo = message.getContent().split(CommonConstant.SINGLE_SPACE);
        if (UserDao.addUser(registerInfo[0], registerInfo[1], registerInfo[2])) {
            clientOS.writeObject(Message.build(MsgType.REGISTER_SUCCESS, "注册成功"));
        } else {
            clientOS.writeObject(Message.build(MsgType.REGISTER_FAIL, "账户已存在"));
        }
        clientOS.flush();
    }

    public void loginUser(Socket clientSocket, ObjectInputStream clientIS, ObjectOutputStream clientOS, Message message) throws IOException {
        String[] loginInfo = message.getContent().split(CommonConstant.SINGLE_SPACE);
        // 根据用户名查询
        User existUser = UserDao.queryUser(loginInfo[0]);
        // 判断异常情况
        String responseMessage = CommonConstant.EMPTY_STRING;
        if (CommonUtil.isNull(existUser)) {
            responseMessage = "账户不存在";
        } else if (clients.containsKey(existUser.getUsername())) {
            responseMessage = "账户已经登录";
        } else if (CommonUtil.isNotEquals(existUser.getPassword(), loginInfo[1])) {
            responseMessage = "密码错误";
        }
        // 判断是否有错误消息，没有就是成功
        if (CommonUtil.isEmpty(responseMessage)) {
            ClientControlThread ccThread = new ClientControlThread(clients, clientSocket, clientIS, clientOS, loginInfo[0]);
            clients.put(loginInfo[0], ccThread);
            ThreadPoolUtil.execute(ccThread);
            clientOS.writeObject(Message.build(MsgType.LOGIN_SUCCESS, "登录成功"));
            System.out.println(CommonUtil.join(loginInfo[0], "上线了！"));
        } else {
            clientOS.writeObject(Message.build(MsgType.LOGIN_FAIL, responseMessage));
            closeResource(clientSocket, clientIS, clientOS);
        }
    }

    public static void retrieveUser(ObjectOutputStream clientOS, Message message) throws IOException {
        String[] retrieveInfo = message.getContent().split(CommonConstant.SINGLE_SPACE);
        User user = UserDao.queryUser(retrieveInfo[0]);
        if (CommonUtil.isNull(user)) {
            clientOS.writeObject(Message.build(MsgType.RETRIEVE_FAIL, "账户不存在"));
        } else if (CommonUtil.isNotEquals(user.getEmail(), retrieveInfo[1])) {
            clientOS.writeObject(Message.build(MsgType.RETRIEVE_FAIL, "邮箱错误"));
        } else {
            clientOS.writeObject(Message.build(MsgType.RETRIEVE_SUCCESS, user.getPassword()));
        }
    }
}
