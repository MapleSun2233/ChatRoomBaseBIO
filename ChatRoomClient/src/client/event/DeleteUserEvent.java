package client.event;

import constant.MsgType;
import entity.Message;
import utils.CommonUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class DeleteUserEvent implements ActionListener {
    private final JFrame frame;
    private final String username;
    private final ObjectOutputStream clientOS;

    public DeleteUserEvent(JFrame frame, ObjectOutputStream clientOS, String username) {
        this.frame = frame;
        this.username = username;
        this.clientOS = clientOS;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(frame, "此账户将被删除！确认要注销此账户吗？");
        if (CommonUtil.isZero(confirm)) {
            // 注销业务
            try {
                clientOS.writeObject(Message.build(MsgType.DELETE_USER, this.username));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
