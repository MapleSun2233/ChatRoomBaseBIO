package server;

import database.api.DAO;
import database.model.User;
import utils.Message;
import utils.MsgType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSocketThread implements Runnable{
    private List<ClientControlThread> clients; // 客户端集合
    private ServerSocket socket;
    ServerSocketThread(int port){
        clients = new ArrayList<>();
        try{
            socket = new ServerSocket(port);
        }catch (IOException e){
            System.out.println("Socket服务启动失败，请重启服务！");
        }
    }
    @Override
    public void run() {
        while(true){
            Socket clientSocket = null;
            ObjectInputStream clientIS = null;
            ObjectOutputStream clientOS = null;
            try {
                clientSocket = socket.accept(); // 接受一个连接
                clientOS = new ObjectOutputStream(clientSocket.getOutputStream());
                clientIS = new ObjectInputStream(clientSocket.getInputStream());
                Message message = (Message) clientIS.readObject();
                switch (message.getType()){
                    case REGISTER_USER:
                        String[] registerInfo = message.getContent().split(" ");
                        if(DAO.addUser(registerInfo[0],registerInfo[1],registerInfo[2])){
                            clientOS.writeObject(new Message(MsgType.REGISTER_USER,"register successfully"));
                        }else{
                            clientOS.writeObject(new Message(MsgType.REGISTER_USER,"account had existed"));
                        }
                        clientOS.flush();
                        clientIS.close();
                        clientOS.close();
                        clientSocket.close();
                        break;
                    case LOGIN_USER:
                        String[] loginInfo = message.getContent().split(" ");
                        User existUser = DAO.queryAUser(loginInfo[0]);
                        if(existUser == null){
                            loginFail(clientSocket,clientIS,clientOS,"account is not exist");
                        }else if(isLogined(existUser.getUsername())){
                            loginFail(clientSocket,clientIS,clientOS,"account had login");
                        }else if(!existUser.getPassword().equals(loginInfo[1])){
                            loginFail(clientSocket,clientIS,clientOS,"password error");
                        }else{
                            System.out.println(loginInfo[0]+"上线了！");
                            ClientControlThread ccThread = new ClientControlThread(clients,clientSocket,clientIS,clientOS,loginInfo[0]);
                            clients.add(ccThread);
                            ccThread.start();
                            clientOS.writeObject(new Message(MsgType.LOGIN_USER,"login successfully"));
                        }
                        break;
                    case RETRIEVE_USER:
                        String[] retrieveInfo = message.getContent().split(" ");
                        User user = DAO.queryAUser(retrieveInfo[0]);
                        if(user == null){
                            clientOS.writeObject(new Message(MsgType.RETRIEVE_USER,"account is not exist"));
                        }else if(!user.getEmail().equals(retrieveInfo[1])){
                            clientOS.writeObject(new Message(MsgType.RETRIEVE_USER,"email is wrong"));
                        }else{
                            clientOS.writeObject(new Message(MsgType.RETRIEVE_USER,String.format("p&%s",user.getPassword())));
                        }
                        clientIS.close();
                        clientOS.close();
                        clientSocket.close();
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                try {
                    loginFail(clientSocket,clientIS,clientOS,"socket连接异常");
                } catch (IOException ex) {
                    System.out.println("失败消息推送失败！");
                }
            }
        }
    }
    public boolean isLogined(String username){
        for(ClientControlThread clientControlThread : clients)
            if(clientControlThread.getUsername().equals(username))
                return true;
        return false;
    }
    public void loginFail(Socket clientSocket,ObjectInputStream clientIS,ObjectOutputStream clientOS,String msg) throws IOException {
        if(clientOS!=null){
            clientOS.writeObject(new Message(MsgType.LOGIN_USER,msg));
            clientOS.close();
        }
        if(clientIS!=null)clientIS.close();
        if(clientSocket!=null)clientSocket.close();
    }
}
