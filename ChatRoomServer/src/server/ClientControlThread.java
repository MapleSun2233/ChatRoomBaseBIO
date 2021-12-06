package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

/**
 * 管理客户端socket
 */
public class ClientControlThread extends Thread{
    private List<ClientControlThread> clients;
    private Socket socket;
    private BufferedReader is;
    private BufferedWriter os;
    private String username;
    private boolean beConnected;

    public ClientControlThread(List<ClientControlThread> clients, Socket socket, BufferedReader is, BufferedWriter os, String name) {
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

    public void sendMessage(String message){
        try {
            for (ClientControlThread client : clients){
                client.os.write(message);
                client.os.newLine();
                client.os.flush();
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
                    client.os.write(String.format("%s发来的私信：%s",this.getUsername(),info[1]));
                    client.os.newLine();
                    client.os.flush();
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
        this.sendMessage(String.format("------%s下线了------",this.getUsername()));
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
        nameList.append("ul&");
        int length = clients.size();
        for (int i = 0 ; i < length; i++) {
            nameList.append(clients.get(i).getUsername());
            if(i<length-1)
                nameList.append('&');
        }
        sendMessage(nameList.toString());
    }

    public void run() {
        try{
            String message = null;//客户端发送过来的内容，服务器转发给所有用户
            sendMessage(String.format("------%s上线了------",this.getUsername()));
            sendUserList();//发送新的用户列表
            while (beConnected){
                message=is.readLine();
                if(message!=null)
                    if(message.startsWith("@")){
                        String[] sendToOneInfo = message.substring(1).split("&");
                        sendToOne(sendToOneInfo);
                    }else
                        sendMessage(String.format("%s:%s",this.getUsername(),message));
            }
        }catch(SocketException e){
            System.out.println("a client quit!");
        }catch(EOFException e){
            System.out.println("Client closed!");
        }catch (IOException e) {
            System.out.println("发生未知IO错误！");
        }finally{
            this.close();
        }
    }
}
