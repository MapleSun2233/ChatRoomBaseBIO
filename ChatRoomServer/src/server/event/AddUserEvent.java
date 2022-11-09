package server.event;

import constant.CommonConstant;
import database.UserDao;
import utils.CommonUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 新增用户事件处理
 */
public class AddUserEvent extends MouseAdapter {
    private final JFrame frame;

    public AddUserEvent(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // 会话框
        JDialog dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setTitle("增加用户");
        dialog.setBounds(300, 300, 300, 240);
        dialog.setLayout(null);
        JLabel label1 = new JLabel("账号：");
        label1.setBounds(20, 20, 60, 30);
        JLabel label2 = new JLabel("密码：");
        label2.setBounds(20, 60, 60, 30);
        JLabel label3 = new JLabel("邮箱：");
        label3.setBounds(20, 100, 60, 30);
        JTextField user = new JTextField("");
        user.setBounds(90, 20, 180, 30);
        JPasswordField pass = new JPasswordField("");
        pass.setBounds(90, 60, 180, 30);
        JTextField email = new JTextField("");
        email.setBounds(90, 100, 180, 30);
        JButton button = new JButton("注册");
        button.setBounds(100, 150, 100, 30);
        button.addMouseListener(new ClickEvent(dialog, user, pass, email));
        dialog.add(label1);
        dialog.add(user);
        dialog.add(label2);
        dialog.add(pass);
        dialog.add(label3);
        dialog.add(email);
        dialog.add(button);
        dialog.setVisible(true);
    }

    static class ClickEvent extends MouseAdapter {
        private final JDialog dialog;
        private final JTextField user;
        private final JPasswordField pass;
        private final JTextField email;

        public ClickEvent(JDialog dialog, JTextField user, JPasswordField pass, JTextField email) {
            this.dialog = dialog;
            this.user = user;
            this.pass = pass;
            this.email = email;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            String username = CommonUtil.deleteAllBlankChar(user.getText());
            String password = CommonUtil.deleteAllBlankChar(String.valueOf(pass.getPassword()));
            String emailAddress = CommonUtil.deleteAllBlankChar(email.getText());
            if (CommonUtil.isEmpty(username) || CommonUtil.isEmpty(password) || CommonUtil.isEmpty(emailAddress)) {
                JOptionPane.showMessageDialog(dialog, "所有字段不能为空！");
            } else if (!username.matches(CommonConstant.VALID_USERNAME_REGEX)) {
                JOptionPane.showMessageDialog(dialog, "用户名含有非法字符！");
            } else if (!password.matches(CommonConstant.VALID_PASSWORD_REGEX)) {
                JOptionPane.showMessageDialog(dialog, "密码含有非法字符！");
            } else if (!emailAddress.matches(CommonConstant.VALID_EMAIL_REGEX)) {
                JOptionPane.showMessageDialog(dialog, "邮箱格式不合法！");
            } else {
                if (UserDao.addUser(username, password, emailAddress)) {
                    JOptionPane.showMessageDialog(dialog, "添加成功！");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "添加失败！该账户已存在！");
                }
            }
        }
    }
}
