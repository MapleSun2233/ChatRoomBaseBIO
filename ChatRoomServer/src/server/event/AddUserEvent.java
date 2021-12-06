package server.event;

import database.api.DatabaseInterface;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddUserEvent extends MouseAdapter {
    private JFrame frame;
    public AddUserEvent(JFrame frame){this.frame = frame;}
    @Override
    public void mouseClicked(MouseEvent e) {
        JDialog dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setTitle("增加用户");
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
        button.addMouseListener(new ClickEvent(dialog,user,pass));
        dialog.add(label1);
        dialog.add(user);
        dialog.add(label2);
        dialog.add(pass);
        dialog.add(button);
        dialog.setVisible(true);
    }
    class ClickEvent extends MouseAdapter{
        private JDialog dialog;
        private JTextField user;
        private JPasswordField pass;

        public ClickEvent(JDialog dialog, JTextField user, JPasswordField pass) {
            this.dialog = dialog;
            this.user = user;
            this.pass = pass;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            String username = user.getText();
            String password = String.valueOf(pass.getPassword());
            if(username.length() == 0 || password.length() == 0){
                JOptionPane.showMessageDialog(dialog,"账户和密码不能为空！");
            }else{
                if(DatabaseInterface.addUser(username,password)){
                    JOptionPane.showMessageDialog(dialog,"添加成功！");
                    dialog.dispose();
                }else{
                    JOptionPane.showMessageDialog(dialog,"添加失败！该账户已存在！");
                }
            }
        }
    }
}
