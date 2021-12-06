package client.event;

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
                dialog.setBounds(300,300,300,200);
                dialog.setLayout(null);
                JLabel label1 = new JLabel("账号：");
                label1.setBounds(20,20,60,30);
                JLabel label2 = new JLabel("密码：");
                label2.setBounds(20,60,60,30);
                JTextField user = new JTextField("");
                user.setBounds(90,20,180,30);
                JPasswordField pass = new JPasswordField("");
                pass.setBounds(90,60,180,30);
                JButton button = new JButton("注册");
                button.setBounds(100,110,100,30);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String username = user.getText();
                        String password = String.valueOf(pass.getPassword());
                        if(username.length() == 0 || password.length() == 0){
                            JOptionPane.showMessageDialog(dialog,"账户和密码不能为空！");
                        }else{
                            try {
                                BufferedWriter clientOS = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                BufferedReader clientIS = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                clientOS.write(String.format("r&&%s %s\n",username,password));
                                clientOS.flush();
                                // 验证注册结果
                                if(clientIS.readLine().indexOf("successfully") != -1){
                                    JOptionPane.showMessageDialog(dialog,"注册成功！");
                                }else{
                                    JOptionPane.showMessageDialog(dialog,"注册失败，已存在此用户！");
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
                dialog.add(button);
                dialog.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,"请检查服务器是否启动，服务器地址和端口是否错误！");
            }
        }
    }
}
