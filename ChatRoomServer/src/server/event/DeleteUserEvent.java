package server.event;

import database.api.DatabaseInterface;
import server.UserTableModel;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeleteUserEvent extends MouseAdapter {
    private JFrame frame;
    public DeleteUserEvent(JFrame frame){
        this.frame = frame;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        JDialog dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setTitle("删除用户");
        dialog.setBounds(300,300,300,300);
        dialog.setLayout(null);
        JTable table = new JTable(new UserTableModel());
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0,0,300,200);
        dialog.add(sp);
        JButton button = new JButton("删除");
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
                if(DatabaseInterface.deleteUser((String)table.getValueAt(rowIndex,0))){
                    JOptionPane.showMessageDialog(dialog,"删除成功！");
                    dialog.dispose();
                }else{
                    JOptionPane.showMessageDialog(dialog,"删除失败！该账户不存在！");
                }
            }else{
                JOptionPane.showMessageDialog(dialog,"选择一个用户再执行此操作！");
            }
        }
    }
}
