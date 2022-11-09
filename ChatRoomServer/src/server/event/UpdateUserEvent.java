package server.event;

import constant.CommonConstant;
import database.UserDao;
import server.view.UserTableModel;
import utils.CommonUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 更新用户事件处理
 */
public class UpdateUserEvent extends MouseAdapter {
    private final JFrame frame;

    public UpdateUserEvent(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // 会话框
        JDialog dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setTitle("修改用户");
        dialog.setBounds(300, 300, 300, 300);
        dialog.setLayout(null);
        JTable table = new JTable(new UserTableModel());
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0, 0, 300, 200);
        dialog.add(sp);
        JButton button = new JButton("修改");
        button.addMouseListener(new ClickEvent(dialog, table));
        button.setBounds(100, 210, 100, 30);
        dialog.add(button);
        dialog.setVisible(true);
    }

    static class ClickEvent extends MouseAdapter {
        private final JDialog dialog;
        private final JTable table;

        public ClickEvent(JDialog dialog, JTable table) {
            this.dialog = dialog;
            this.table = table;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int rowIndex = table.getSelectedRow();
            if (rowIndex != -1) {
                String username = (String) table.getValueAt(rowIndex, 0);
                JDialog updateDialog = new JDialog(dialog);
                updateDialog.setModal(true);
                updateDialog.setTitle("修改用户");
                updateDialog.setBounds(300, 300, 300, 240);
                updateDialog.setLayout(null);
                JLabel label1 = new JLabel("密码：");
                label1.setBounds(20, 20, 60, 30);
                JPasswordField pass = new JPasswordField("");
                pass.setBounds(90, 20, 180, 30);
                JLabel label2 = new JLabel("邮箱：");
                label2.setBounds(20, 60, 60, 30);
                JTextField email = new JTextField("");
                email.setBounds(90, 60, 180, 30);
                JButton button = new JButton("修改");
                button.setBounds(100, 130, 100, 30);
                button.addMouseListener(new UpdateClick(updateDialog, username, pass, email));
                updateDialog.add(label1);
                updateDialog.add(pass);
                updateDialog.add(label2);
                updateDialog.add(email);
                updateDialog.add(button);
                updateDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(dialog, "选择一个用户再执行此操作！");
            }
        }

        class UpdateClick extends MouseAdapter {
            private final JDialog updateDialog;
            private final String username;
            private final JPasswordField pass;
            private final JTextField email;

            public UpdateClick(JDialog updateDialog, String username, JPasswordField pass, JTextField email) {
                this.updateDialog = updateDialog;
                this.username = username;
                this.pass = pass;
                this.email = email;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                String password = CommonUtil.deleteAllBlankChar(String.valueOf(pass.getPassword()));
                String emailAddress = CommonUtil.deleteAllBlankChar(email.getText());
                if (CommonUtil.isEmpty(password) || CommonUtil.isEmpty(emailAddress)) {
                    JOptionPane.showMessageDialog(updateDialog, "所有字段必须填写!");
                } else if (!password.matches(CommonConstant.VALID_PASSWORD_REGEX)) {
                    JOptionPane.showMessageDialog(dialog, "密码含有非法字符！");
                } else if (!emailAddress.matches(CommonConstant.VALID_EMAIL_REGEX)) {
                    JOptionPane.showMessageDialog(updateDialog, "邮箱地址不合法!");
                } else {
                    if (UserDao.updateUser(username, password, emailAddress)) {
                        JOptionPane.showMessageDialog(updateDialog, "修改成功！");
                        updateDialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "修改失败！该账户不存在！");
                    }
                }
            }
        }
    }
}
