package client.event;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.IOException;

public class EnterSendMsg extends KeyAdapter {
    private BufferedWriter clientOS;
    private JTextArea content;
    private JTextArea sendMsgContent;
    private JComboBox<String> contactTo;

    public EnterSendMsg(BufferedWriter clientOS, JTextArea content, JTextArea sendMsgContent, JComboBox<String> contactTo) {
        this.clientOS = clientOS;
        this.content = content;
        this.sendMsgContent = sendMsgContent;
        this.contactTo = contactTo;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            String str = sendMsgContent.getText().trim();
            if(str.length() > 0)
                try {
                    String contactToWho = (String)contactTo.getSelectedItem();
                    if(contactToWho.equals("全部"))
                        clientOS.write(str);
                    else{
                        clientOS.write(String.format("@%s&%s",contactToWho,str));
                        content.setText(content.getText()+String.format("本账户发送给%s的私信：%s",contactToWho,str)+'\n');//更新聊天内容
                    }
                    clientOS.newLine();
                    clientOS.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            sendMsgContent.setText("");
        }
    }
}
