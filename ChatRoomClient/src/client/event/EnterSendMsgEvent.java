package client.event;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ObjectOutputStream;

public class EnterSendMsgEvent extends KeyAdapter implements SendMsgEventInterface {
    private final ObjectOutputStream clientOS;
    private final JTextArea content;
    private final JTextArea sendMsgContent;
    private final JComboBox<String> contactTo;

    public EnterSendMsgEvent(ObjectOutputStream clientOS, JTextArea content, JTextArea sendMsgContent, JComboBox<String> contactTo) {
        this.clientOS = clientOS;
        this.content = content;
        this.sendMsgContent = sendMsgContent;
        this.contactTo = contactTo;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMsg(clientOS, content, sendMsgContent, contactTo);
        }
    }
}
