package client;

import client.event.Login;
import client.event.Register;
import javax.swing.*;

public class Client extends JFrame{
    private JLabel label = new JLabel("服务器地址");
    private JTextField address = new JTextField("127.0.0.1:2345");
    private JButton registerUser = new JButton("注册");
    private JButton loginUser = new JButton("登录");
    Client(){
        this.setTitle("客户端");
        this.setResizable(false);
        this.setLayout(null);
        this.setBounds(300,300,300,350);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label.setBounds(10,30,80,30);
        address.setBounds(100,30,180,30);
        registerUser.setBounds(100,100,100,50);
        loginUser.setBounds(100,200,100,50);
        registerUser.addMouseListener(new Register(this,address));
        loginUser.addMouseListener(new Login(this,address));
        this.add(label);
        this.add(address);
        this.add(registerUser);
        this.add(loginUser);
        this.setVisible(true);
    }
}
