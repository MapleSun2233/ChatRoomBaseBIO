package client;

import client.event.ClickSendMsg;
import client.event.EnterSendMsg;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * 管理聊天室
 */
public class ChatRoomThread extends JFrame implements Runnable{
    private Socket socket;
    private BufferedReader clientIS;
    private BufferedWriter clientOS;
    private JLabel listTitle = new JLabel("在线用户列表");
    private JTextArea list = new JTextArea();
    private JLabel contentTitle = new JLabel("聊天记录");
    private JTextArea content = new JTextArea();
    private JTextArea sendMsgContent = new JTextArea();
    private JButton sendButton = new JButton("发送");
    private JScrollPane contentScrollPane = null;
    private JScrollPane userListScrollPane = null;
    private JComboBox<String> contactTo = new JComboBox<>();
    private String username;
    public ChatRoomThread(Socket socket, BufferedReader clientIS, BufferedWriter clientOS, String username){
        this.socket = socket;
        this.clientIS = clientIS;
        this.clientOS = clientOS;
        this.username = username;
        this.setResizable(false);
        this.setTitle("聊天室");
        this.setBounds(100,100,650,570);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.listTitle.setBounds(10,20,200,20);
        this.userListScrollPane = new JScrollPane(list);
        this.userListScrollPane.setBounds(10, 50, 170, 400);
        this.userListScrollPane.setVisible(true);
        this.list.setEditable(false);
        this.contentTitle.setBounds(210, 20, 200, 20);
        this.contentScrollPane = new JScrollPane(content);
        this.contentScrollPane.setVisible(true);
        this.contentScrollPane.setBounds(220, 50, 400, 400);
        this.content.setLineWrap(true);
        this.content.setEditable(false);
        this.sendMsgContent.setBounds(120, 460, 420, 30);
        this.sendMsgContent.addKeyListener(new EnterSendMsg(this.clientOS,content,sendMsgContent,contactTo));
        this.sendButton.addMouseListener(new ClickSendMsg(this.clientOS,content,sendMsgContent,contactTo));
        this.contactTo.setBounds(10,460,90,30);
        this.contactTo.addItem("全部");
        this.sendButton.setBounds(555,460,65,30);
        this.add(listTitle);
        this.add(userListScrollPane);
        this.add(contentTitle);
        this.add(contentScrollPane);
        this.add(sendMsgContent);
        this.add(contactTo);
        this.add(sendButton);
        this.setVisible(true);
    }
    @Override
    public void run() { // 开始消息处理线程
        try{
            while (true){
                String str=clientIS.readLine().trim();//读取服务器的信息
                if (str!=null) {
                    if(str.startsWith("ul&")){ // 处理用户列表信息
                        String[] userList =  str.substring(3).split("&");
                        this.contactTo.removeAllItems(); // 重置下拉列表
                        this.contactTo.addItem("全部");
                        for(String user : userList) if(!user.equals(username))this.contactTo.addItem(user);
                        StringBuilder sb = new StringBuilder();
                        int length = userList.length;
                        for(int i = 0 ; i < length; i++)
                            if(userList[i].equals(username))
                                sb.append(String.format("%s(本账户)\n",userList[i]));
                            else
                                sb.append(String.format("%s\n",userList[i]));
                        list.setText(sb.toString());
                        userListScrollPane.getVerticalScrollBar().setValue(userListScrollPane.getVerticalScrollBar().getMaximum());
                    }else{ // 处理聊天信息
                        content.setText(content.getText()+str+'\n');
                        contentScrollPane.getVerticalScrollBar().setValue(contentScrollPane.getVerticalScrollBar().getMaximum());
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("socket异常");
        } catch (EOFException e) {
            System.out.println("终止异常");
        } catch(IOException e){
            System.out.println("IO异常");
        }
    }
}
