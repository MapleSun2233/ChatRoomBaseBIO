package client.view;

import client.event.LoginEvent;
import client.event.RegisterEvent;
import client.event.RetrieveEvent;

import javax.swing.*;

public class ClientView extends JFrame {

    public static final ClientView CLIENT_VIEW = new ClientView();

    public static void start() {
        JLabel label = new JLabel("服务器地址");
        JTextField address = new JTextField("127.0.0.1:2345");
        JButton registerUser = new JButton("注册");
        JButton loginUser = new JButton("登录");
        JButton retrievePassword = new JButton("找回密码");
        // 设置界面
        CLIENT_VIEW.setTitle("客户端");
        CLIENT_VIEW.setResizable(false);
        CLIENT_VIEW.setLayout(null);
        CLIENT_VIEW.setBounds(300, 300, 300, 380);
        CLIENT_VIEW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置组件位置
        label.setBounds(10, 30, 80, 30);
        address.setBounds(100, 30, 180, 30);
        registerUser.setBounds(100, 100, 100, 50);
        loginUser.setBounds(100, 180, 100, 50);
        retrievePassword.setBounds(100, 260, 100, 50);
        // 设置组件监听事件
        registerUser.addMouseListener(new RegisterEvent(CLIENT_VIEW, address));
        loginUser.addMouseListener(new LoginEvent(CLIENT_VIEW, address));
        retrievePassword.addMouseListener(new RetrieveEvent(CLIENT_VIEW, address));
        // 组件加入Client Frame
        CLIENT_VIEW.add(label);
        CLIENT_VIEW.add(address);
        CLIENT_VIEW.add(registerUser);
        CLIENT_VIEW.add(loginUser);
        CLIENT_VIEW.add(retrievePassword);
        CLIENT_VIEW.setVisible(true);
    }
}
