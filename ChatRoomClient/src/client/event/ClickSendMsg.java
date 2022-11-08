package client.event;

import utils.Message;
import utils.MsgType;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClickSendMsg extends MouseAdapter {
    private ObjectOutputStream clientOS;
    private JTextArea content;
    private JTextArea sendMsgContent;
    private JComboBox<String> contactTo;

    public ClickSendMsg(ObjectOutputStream clientOS, JTextArea content, JTextArea sendMsgContent, JComboBox<String> contactTo) {
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
                    clientOS.writeObject(new Message(MsgType.PUBLIC_MSG,str));
                else{
                    clientOS.writeObject(new Message(MsgType.PRIVATE_MSG,String.format("%s&%s",contactToWho,str)));
                    content.setText(content.getText()+String.format("本账户发送给%s的私信：%s",contactToWho,str)+'\n');//更新聊天内容
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        sendMsgContent.setText("");
    }
}
