package client.event;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;

public class ClickSendMsg extends MouseAdapter {
    private BufferedWriter clientOS;
    private JTextArea content;
    private JTextArea sendMsgContent;
    private JComboBox<String> contactTo;

    public ClickSendMsg(BufferedWriter clientOS, JTextArea content, JTextArea sendMsgContent, JComboBox<String> contactTo) {
        this.clientOS = clientOS;
        this.content = content;
        this.sendMsgContent = sendMsgContent;
        this.contactTo = contactTo;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
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
