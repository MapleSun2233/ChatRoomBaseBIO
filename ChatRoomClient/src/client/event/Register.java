package client.event;

import utils.Message;
import utils.MsgType;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

public class Register extends MouseAdapter {
    private JFrame frame;
    private JTextField address;

    public Register(JFrame frame, JTextField address) {
        this.frame = frame;
        this.address = address;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String[] addressInfo = address.getText().split(":");
        if(addressInfo.length != 2){
            JOptionPane.showMessageDialog(frame,"请输入正确地服务器地址和端口，例如：127.0.0.1:2345");
        }else{
            try {
                Socket socket = new Socket(addressInfo[0],Integer.valueOf(addressInfo[1]));
                JDialog dialog = new JDialog(frame);
                dialog.setModal(true);
                dialog.setTitle("注册用户");
                dialog.setBounds(300,300,300,240);
                dialog.setLayout(null);
                JLabel label1 = new JLabel("账号：");
                label1.setBounds(20,20,60,30);
                JLabel label2 = new JLabel("密码：");
                label2.setBounds(20,60,60,30);
                JLabel label3 = new JLabel("邮箱：");
                label3.setBounds(20,100,60,30);
                JTextField user = new JTextField("");
                user.setBounds(90,20,180,30);
                JPasswordField pass = new JPasswordField("");
                pass.setBounds(90,60,180,30);
                JTextField email = new JTextField("");
                email.setBounds(90,100,180,30);
                JButton button = new JButton("注册");
                button.setBounds(100,150,100,30);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String username = user.getText().trim();
                        String password = String.valueOf(pass.getPassword()).trim();
                        String emailAddress = email.getText().trim();
                        if(username.length() == 0 || password.length() == 0 || emailAddress.length() == 0){
                            JOptionPane.showMessageDialog(dialog,"所有字段不能为空！");
                        }else if(!username.matches("[A-Za-z0-9_\\u4e00-\\u9fa5]+")){
                            JOptionPane.showMessageDialog(dialog,"用户名含有非法字符！");
                        }else if(!password.matches("[A-Za-z0-9_]+")){
                            JOptionPane.showMessageDialog(dialog,"密码含有非法字符！");
                        }else if(!emailAddress.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")){
                            JOptionPane.showMessageDialog(dialog,"邮箱地址不合法！");
                        }else{
                            try {
                                ObjectOutputStream clientOS = new ObjectOutputStream(socket.getOutputStream());
                                ObjectInputStream clientIS = new ObjectInputStream(socket.getInputStream());
                                Message message = new Message(MsgType.REGISTER_USER,String.format("%s %s %s",username,password,emailAddress));
                                clientOS.writeObject(message);
                                // 验证注册结果
                                message = (Message) clientIS.readObject();
                                switch (message.getType()){
                                    case REGISTER_USER:
                                        if(message.getContent().indexOf("successfully") != -1)
                                            JOptionPane.showMessageDialog(dialog,"注册成功！");
                                        else
                                            JOptionPane.showMessageDialog(dialog,"注册失败，已存在此用户！");
                                        break;
                                }
                                clientIS.close();
                                clientOS.close();
                                socket.close();
                                dialog.dispose();
                            } catch (IOException | ClassNotFoundException ex) {
                                System.out.println("socket发生错误！");
                            }
                        }
                    }
                });
                dialog.add(label1);
                dialog.add(user);
                dialog.add(label2);
                dialog.add(pass);
                dialog.add(label3);
                dialog.add(email);
                dialog.add(button);
                dialog.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,"请检查服务器是否启动，服务器地址和端口是否错误！");
            }
        }
    }
}
