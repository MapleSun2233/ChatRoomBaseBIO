package client.event;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

public class Retrieve extends MouseAdapter {
    private JFrame frame;
    private JTextField address;

    public Retrieve(JFrame frame, JTextField address) {
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
                dialog.setTitle("找回密码");
                dialog.setBounds(300,300,300,240);
                dialog.setLayout(null);
                JLabel label1 = new JLabel("账号：");
                label1.setBounds(20,20,60,30);
                JLabel label2 = new JLabel("邮箱：");
                label2.setBounds(20,60,60,30);
                JLabel label3 = new JLabel("密码：");
                label3.setBounds(20,100,60,30);
                JTextField user = new JTextField("输入需要重置的目标账户");
                user.setBounds(90,20,180,30);
                JTextField email = new JTextField("输入注册时的邮箱地址");
                email.setBounds(90,60,180,30);
                JPasswordField pass = new JPasswordField("");
                pass.setBounds(90,100,180,30);
                JButton button = new JButton("找回密码");
                button.setBounds(100,150,100,30);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String username = user.getText();
                        String password = String.valueOf(pass.getPassword());
                        String emailAddress = email.getText();
                        if(username.length() == 0 || password.length() == 0 || emailAddress.length() == 0){
                            JOptionPane.showMessageDialog(dialog,"所有字段不能为空！");
                        }else if(!emailAddress.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")){
                            JOptionPane.showMessageDialog(dialog,"邮箱地址不合法！");
                        }else{
                            try {
                                BufferedWriter clientOS = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                BufferedReader clientIS = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                clientOS.write(String.format("rp&&%s %s %s\n",username,password,emailAddress));
                                clientOS.flush();
                                // 验证找回结果
                                String receivedMsg = clientIS.readLine();
                                if(receivedMsg.indexOf("successfully") != -1){
                                    JOptionPane.showMessageDialog(dialog,"找回密码成功！");
                                }else if(receivedMsg.indexOf("email") != -1){
                                    JOptionPane.showMessageDialog(dialog,"找回密码失败！邮箱地址不是注册时的预留地址！");
                                }else if(receivedMsg.indexOf("account") != -1){
                                    JOptionPane.showMessageDialog(dialog,"找回密码失败，不存在此用户！");
                                }else{
                                    JOptionPane.showMessageDialog(dialog,"其他未知错误！");
                                }
                                clientIS.close();
                                clientOS.close();
                                socket.close();
                                dialog.dispose();
                            } catch (IOException ex) {
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
