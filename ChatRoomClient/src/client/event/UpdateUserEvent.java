package client.event;

import utils.Message;
import utils.MsgType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class UpdateUserEvent implements ActionListener {
    private JFrame frame;
    private String username;
    private ObjectInputStream clientIS;
    private ObjectOutputStream clientOS;
    public UpdateUserEvent(JFrame frame, ObjectInputStream clientIS, ObjectOutputStream clientOS,String username){
        this.frame = frame;
        this.username = username;
        this.clientIS = clientIS;
        this.clientOS = clientOS;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JDialog dialog = new JDialog(frame);
        dialog.setTitle("修改密码");
        dialog.setBounds(300,300,300,240);
        dialog.setLayout(null);
        JLabel label1 = new JLabel("账号：");
        label1.setBounds(20,20,60,30);
        JLabel label2 = new JLabel("旧密码：");
        label2.setBounds(20,60,60,30);
        JLabel label3 = new JLabel("新密码：");
        label3.setBounds(20,100,60,30);
        JTextField user = new JTextField(this.username);
        user.setEditable(false);
        user.setBounds(90,20,180,30);
        JPasswordField oldPass = new JPasswordField("");
        oldPass.setBounds(90,60,180,30);
        JPasswordField newPass = new JPasswordField("");
        newPass.setBounds(90,100,180,30);
        JButton button = new JButton("修改");
        button.setBounds(100,150,100,30);
        button.addMouseListener(new ClickEvent(dialog,oldPass,newPass));
        dialog.add(label1);
        dialog.add(label2);
        dialog.add(label3);
        dialog.add(user);
        dialog.add(oldPass);
        dialog.add(newPass);
        dialog.add(button);
        dialog.setVisible(true);
    }
    class ClickEvent extends MouseAdapter{
        private JDialog dialog;
        private JPasswordField oldPass;
        private JPasswordField newPass;

        public ClickEvent(JDialog dialog,JPasswordField oldPass, JPasswordField newPass) {
            this.dialog = dialog;
            this.oldPass = oldPass;
            this.newPass = newPass;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            String oldPassword = String.valueOf(oldPass.getPassword()).trim();
            String newPassword = String.valueOf(newPass.getPassword()).trim();
            if(oldPassword.length() == 0 && newPassword.length() == 0){
                JOptionPane.showMessageDialog(dialog,"所有字段必须填写！");
            }else if(!newPassword.matches("[A-Za-z0-9_]+") || !oldPassword.matches("[A-Za-z0-9_]+")){
                JOptionPane.showMessageDialog(dialog,"密码含有非法字符！");
            }else{
                try {
                        clientOS.writeObject(new Message(MsgType.UPDATE_USER,String.format("%s&%s",oldPassword,newPassword)));
                        dialog.dispose();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                }
            }
        }
    }
}
