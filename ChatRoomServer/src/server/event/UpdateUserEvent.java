package server.event;

import database.api.DatabaseInterface;
import server.UserTableModel;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UpdateUserEvent extends MouseAdapter {
    private JFrame frame;
    public UpdateUserEvent(JFrame frame){
        this.frame = frame;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        JDialog dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setTitle("修改用户");
        dialog.setBounds(300,300,300,300);
        dialog.setLayout(null);
        JTable table = new JTable(new UserTableModel());
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0,0,300,200);
        dialog.add(sp);
        JButton button = new JButton("修改");
        button.addMouseListener(new ClickEvent(dialog,table));
        button.setBounds(100,210,100,30);
        dialog.add(button);
        dialog.setVisible(true);
    }
    class ClickEvent extends MouseAdapter{
        private JDialog dialog;
        private JTable table;

        public ClickEvent(JDialog dialog, JTable table) {
            this.dialog = dialog;
            this.table = table;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int rowIndex = table.getSelectedRow();
            if(rowIndex!=-1){
                String username = (String)table.getValueAt(rowIndex,0);
                JDialog updateDialog = new JDialog(dialog);
                updateDialog.setModal(true);
                updateDialog.setTitle("修改用户");
                updateDialog.setBounds(300,300,300,200);
                updateDialog.setLayout(null);
                JLabel label1 = new JLabel("密码：");
                label1.setBounds(20,20,60,30);
                JPasswordField pass = new JPasswordField("");
                pass.setBounds(90,20,180,30);
                JButton button = new JButton("修改");
                button.setBounds(100,90,100,30);
                button.addMouseListener(new UpdateClick(updateDialog,username,pass));
                updateDialog.add(label1);
                updateDialog.add(pass);
                updateDialog.add(button);
                updateDialog.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(dialog,"选择一个用户再执行此操作！");
            }
        }
        class UpdateClick extends MouseAdapter{
            private JDialog updateDialog;
            private String username;
            private JPasswordField pass;

            public UpdateClick(JDialog updateDialog, String username, JPasswordField pass) {
                this.updateDialog = updateDialog;
                this.username = username;
                this.pass = pass;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                char[] passArr = pass.getPassword();
                if(passArr.length == 0){
                    JOptionPane.showMessageDialog(updateDialog,"请输入新密码再执行此操作!");
                }else{
                    String password = String.valueOf(passArr);
                    if(DatabaseInterface.updateUser(username,password)){
                        JOptionPane.showMessageDialog(updateDialog,"修改成功！");
                        updateDialog.dispose();
                    }else{
                        JOptionPane.showMessageDialog(dialog,"修改失败！该账户不存在！");
                    }
                }
            }
        }
    }
}
