package client.event;

import utils.Message;
import utils.MsgType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class DeleteUserEvent implements ActionListener {
    private JFrame frame;
    private String username;
    private ObjectOutputStream clientOS;
    public DeleteUserEvent(JFrame frame, ObjectOutputStream clientOS, String username) {
        this.frame = frame;
        this.username = username;
        this.clientOS = clientOS;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(JOptionPane.showConfirmDialog(frame, "此账户将被删除！确认要注销此账户吗？")==0){
            // 注销业务
            try {
                clientOS.writeObject(new Message(MsgType.DELETE_USER,this.username));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
