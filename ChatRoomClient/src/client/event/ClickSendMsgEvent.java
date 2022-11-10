package client.event;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectOutputStream;

public class ClickSendMsgEvent extends MouseAdapter implements SendMsgEventInterface {
    private final ObjectOutputStream clientOS;
    private final JTextArea content;
    private final JTextArea sendMsgContent;
    private final JComboBox<String> contactTo;

    public ClickSendMsgEvent(ObjectOutputStream clientOS, JTextArea content, JTextArea sendMsgContent, JComboBox<String> contactTo) {
        this.clientOS = clientOS;
        this.content = content;
        this.sendMsgContent = sendMsgContent;
        this.contactTo = contactTo;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        sendMsg(clientOS, content, sendMsgContent, contactTo);
    }
}
