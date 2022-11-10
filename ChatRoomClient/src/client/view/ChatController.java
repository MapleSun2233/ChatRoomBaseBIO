package client.view;

import client.event.ClickSendMsgEvent;
import client.event.DeleteUserEvent;
import client.event.EnterSendMsgEvent;
import client.event.UpdateUserEvent;
import constant.CommonConstant;
import entity.Message;
import utils.CommonUtil;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.SocketException;
import java.util.Arrays;

/**
 * 聊天室界面控制线程
 */
public class ChatController extends JFrame implements Runnable {
    private final ObjectInputStream clientIS;
    private final ObjectOutputStream clientOS;
    private final JTextArea list = new JTextArea();
    private final JTextArea content = new JTextArea();
    private final JScrollPane contentScrollPane;
    private final JScrollPane userListScrollPane;
    private final JComboBox<String> contactTo = new JComboBox<>();
    private final String username;

    public ChatController(ObjectInputStream clientIS, ObjectOutputStream clientOS, String username) {
        // 引用连接
        this.clientIS = clientIS;
        this.clientOS = clientOS;
        this.username = username;
        // 设置聊天框
        this.setResizable(false);
        this.setTitle("聊天室");
        this.setBounds(100, 100, 650, 570);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("账户管理");
        JMenuItem updatePass = new JMenuItem("修改密码");
        JMenuItem deleteUser = new JMenuItem("注销账号");
        menu.setFont(new Font(null, Font.BOLD, 14));
        updatePass.addActionListener(new UpdateUserEvent(this, clientIS, clientOS, this.username));
        deleteUser.addActionListener(new DeleteUserEvent(this, clientOS, this.username));
        menu.add(updatePass);
        menu.add(deleteUser);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        JLabel listTitle = new JLabel("在线用户列表");
        listTitle.setBounds(10, 20, 200, 20);
        this.userListScrollPane = new JScrollPane(list);
        this.userListScrollPane.setBounds(10, 50, 170, 400);
        this.userListScrollPane.setVisible(true);
        this.list.setEditable(false);

        JLabel contentTitle = new JLabel("聊天记录");
        contentTitle.setBounds(210, 20, 200, 20);
        this.contentScrollPane = new JScrollPane(content);
        this.contentScrollPane.setVisible(true);
        this.contentScrollPane.setBounds(220, 50, 400, 400);
        this.content.setLineWrap(true);
        this.content.setEditable(false);

        JTextArea sendMsgContent = new JTextArea();
        sendMsgContent.setBounds(120, 460, 420, 30);
        sendMsgContent.addKeyListener(new EnterSendMsgEvent(this.clientOS, content, sendMsgContent, contactTo));

        JButton sendButton = new JButton("发送");
        sendButton.addMouseListener(new ClickSendMsgEvent(this.clientOS, content, sendMsgContent, contactTo));
        this.contactTo.setBounds(10, 460, 90, 30);
        this.contactTo.addItem(CommonConstant.ALL);
        sendButton.setBounds(555, 460, 65, 30);

        this.add(listTitle);
        this.add(userListScrollPane);
        this.add(contentTitle);
        this.add(contentScrollPane);
        this.add(sendMsgContent);
        this.add(contactTo);
        this.add(sendButton);
        this.setVisible(true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                //读取服务器的信息
                Message message = (Message) clientIS.readObject();
                switch (message.getType()) {
                    case USER_LIST:
                        String[] userList = message.getContent().split(CommonConstant.AND);
                        // 重置在线联系人下拉列表
                        this.contactTo.removeAllItems();
                        this.contactTo.addItem(CommonConstant.ALL);
                        // 制作在线名单， 排除自己加入到联系人下拉列表中
                        StringBuilder sb = new StringBuilder();
                        Arrays.stream(userList)
                                .peek(name -> {
                                    if (CommonUtil.isEquals(name, username)) {
                                        sb.append(name).append("(本账户)");
                                    } else {
                                        sb.append(name);
                                    }
                                    sb.append(CommonConstant.NEW_LINE);
                                })
                                .filter(name -> CommonUtil.isNotEquals(name, username))
                                .forEach(this.contactTo::addItem);
                        list.setText(sb.toString());
                        userListScrollPane.getVerticalScrollBar().setValue(userListScrollPane.getVerticalScrollBar().getMaximum());
                        break;
                    case UPDATE_SUCCESS:
                    case UPDATE_FAIL:
                        JOptionPane.showMessageDialog(this, message.getContent());
                        break;
                    case DELETE_SUCCESS:
                        JOptionPane.showMessageDialog(this, message.getContent());
                        this.dispose();
                        System.exit(0);
                        break;
                    case PRIVATE_MSG:
                    case PUBLIC_MSG:
                        // 处理聊天信息
                        content.setText(CommonUtil.join(content.getText(), message.getContent(), CommonConstant.NEW_LINE));
                        contentScrollPane.getVerticalScrollBar().setValue(contentScrollPane.getVerticalScrollBar().getMaximum());
                        break;
                    default:
                }
            }
        } catch (SocketException e) {
            System.out.println("socket异常");
        } catch (EOFException e) {
            System.out.println("终止异常");
        } catch (IOException e) {
            System.out.println("IO异常");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
