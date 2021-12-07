package client.event;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class DeleteUserEvent implements ActionListener {
    private JFrame frame;
    private String username;
    private BufferedWriter clientOS;
    public DeleteUserEvent(JFrame frame, BufferedWriter clientOS, String username) {
        this.frame = frame;
        this.username = username;
        this.clientOS = clientOS;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(JOptionPane.showConfirmDialog(frame, "此账户将被删除！确认要注销此账户吗？")==0){
            // 注销业务
            try {
                clientOS.write(String.format("d$%s",this.username));
                clientOS.newLine();
                clientOS.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
