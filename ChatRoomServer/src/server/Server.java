package server;

import javax.swing.*;
import server.event.AddUserEvent;
import server.event.DeleteUserEvent;
import server.event.QueryUserEvent;
import server.event.UpdateUserEvent;
import java.util.ResourceBundle;

public class Server extends JFrame{
    private JButton addUser = new JButton("新增用户");
    private JButton deleteUser = new JButton("删除用户");
    private JButton updateUser = new JButton("修改用户");
    private JButton queryUser = new JButton("查询用户");
    private String port = ResourceBundle.getBundle("config").getString("port");
    private JLabel portLabel = new JLabel(String.format("服务监听在%s端口", port));
    Server(){ // 服务端界面
        this.setTitle("服务端");
        this.setResizable(false);
        this.setLayout(null);
        this.setBounds(300,300,300,550);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addUser.setBounds(100,50,100,50);
        this.deleteUser.setBounds(100,150,100,50);
        this.updateUser.setBounds(100,250,100,50);
        this.queryUser.setBounds(100,350,100,50);
        this.portLabel.setBounds(70,450,200,50);
        this.addUser.addMouseListener(new AddUserEvent(this));
        this.deleteUser.addMouseListener(new DeleteUserEvent(this));
        this.updateUser.addMouseListener(new UpdateUserEvent(this));
        this.queryUser.addMouseListener(new QueryUserEvent(this));
        this.add(addUser);
        this.add(deleteUser);
        this.add(updateUser);
        this.add(queryUser);
        this.add(portLabel);
        this.setVisible(true);
        // 开始socket服务端线程
        new Thread(new ServerSocketThread(Integer.valueOf(port))).start();
        JOptionPane.showMessageDialog(this,"请勿关闭服务端窗口，否则将停止服务！");
    }
}
