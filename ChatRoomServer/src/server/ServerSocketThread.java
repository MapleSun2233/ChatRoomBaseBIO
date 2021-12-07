package server;

import database.api.DatabaseInterface;
import database.model.User;

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
            BufferedReader clientIS = null;
            BufferedWriter clientOS = null;
            try {
                clientSocket = socket.accept(); // 接受一个连接
                clientIS = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientOS = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String receivedAccountInfo = clientIS.readLine();
                if(receivedAccountInfo.startsWith("r&&")){ // 注册任务
                    receivedAccountInfo = receivedAccountInfo.substring(3);
                    String[] loginInfo = receivedAccountInfo.split(" ");
                    if(DatabaseInterface.addUser(loginInfo[0],loginInfo[1],loginInfo[2])){
                        clientOS.write("register successfully");
                    }else{
                        clientOS.write("account had existed");
                    }
                    clientOS.newLine();
                    clientOS.flush();
                    clientIS.close();
                    clientOS.close();
                    clientSocket.close();
                }else if(receivedAccountInfo.startsWith("rp&&")){ // 找回密码任务
                    receivedAccountInfo = receivedAccountInfo.substring(4);
                    String[] loginInfo = receivedAccountInfo.split(" ");
                    User user = DatabaseInterface.queryAUser(loginInfo[0]);
                    if(user == null){
                        clientOS.write("account is not exist");
                    }else if(!user.getEmail().equals(loginInfo[1])){
                        clientOS.write("email is wrong");
                    }else{
                        clientOS.write(String.format("p&%s",user.getPassword()));
                    }
                    clientOS.newLine();
                    clientOS.flush();
                    clientIS.close();
                    clientOS.close();
                    clientSocket.close();
                }else{ // 登录任务
                    String[] loginInfo = receivedAccountInfo.split(" ");
                    User user = DatabaseInterface.queryAUser(loginInfo[0]);
                    if(user == null){
                        loginFail(clientSocket,clientIS,clientOS,"account is not exist");
                    }else if(isLogined(user.getUsername())){
                        loginFail(clientSocket,clientIS,clientOS,"account had login");
                    }else if(!user.getPassword().equals(loginInfo[1])){
                        loginFail(clientSocket,clientIS,clientOS,"password error");
                    }else{
                        ClientControlThread ccThread = new ClientControlThread(clients,clientSocket,clientIS,clientOS,loginInfo[0]);
                        clients.add(ccThread);
                        ccThread.start();
                        clientOS.write("login successfully\n");
                        clientOS.flush();
                    }
                }
            } catch (IOException e) {
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
    public void loginFail(Socket clientSocket,BufferedReader clientIS,BufferedWriter clientOS,String msg) throws IOException {
        if(clientOS!=null){
            clientOS.write(msg);
            clientOS.newLine();
            clientOS.flush();
            clientOS.close();
        }
        if(clientIS!=null)clientIS.close();
        if(clientSocket!=null)clientSocket.close();
    }
}
