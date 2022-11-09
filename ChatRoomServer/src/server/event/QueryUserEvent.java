package server.event;

import server.view.UserTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 查询用户事件处理
 */
public class QueryUserEvent extends MouseAdapter {
    private final JFrame frame;

    public QueryUserEvent(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JDialog dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setTitle("查询用户");
        dialog.setBounds(300, 300, 300, 300);
        dialog.setLayout(null);
        JTable table = new JTable(new UserTableModel());
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0, 0, 300, 300);
        dialog.add(sp);
        dialog.setVisible(true);
    }
}
