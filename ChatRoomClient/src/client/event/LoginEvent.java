package client.event;

import client.view.ChatController;
import constant.CommonConstant;
import constant.MsgType;
import entity.Message;
import utils.CommonUtil;
import utils.ThreadPoolUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

public class LoginEvent extends MouseAdapter {
    private final JFrame frame;
    private final JTextField address;

    public LoginEvent(JFrame frame, JTextField address) {
        this.frame = frame;
        this.address = address;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String[] addressInfo = CommonUtil.verifyAndGetAddressInfo(address.getText());
        if (CommonUtil.isNull(addressInfo)) {
            JOptionPane.showMessageDialog(frame, "请输入正确地服务器地址和端口，例如：127.0.0.1:2345");
        } else {
            try {
                // socket连接
                Socket socket = new Socket(addressInfo[0], Integer.parseInt(addressInfo[1]));
                ObjectOutputStream clientOS = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream clientIS = new ObjectInputStream(socket.getInputStream());
                // 对话框
                JDialog dialog = new JDialog(frame);
                dialog.setModal(true);
                dialog.setTitle("登录聊天室");
                dialog.setBounds(300, 300, 300, 200);
                dialog.setLayout(null);
                JLabel label1 = new JLabel("账号：");
                label1.setBounds(20, 20, 60, 30);
                JLabel label2 = new JLabel("密码：");
                label2.setBounds(20, 60, 60, 30);
                JTextField user = new JTextField("");
                user.setBounds(90, 20, 180, 30);
                JPasswordField pass = new JPasswordField("");
                pass.setBounds(90, 60, 180, 30);
                JButton button = new JButton("登录");
                button.setBounds(100, 110, 100, 30);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String username = CommonUtil.deleteAllBlankChar(user.getText());
                        String password = CommonUtil.deleteAllBlankChar(String.valueOf(pass.getPassword()));
                        if (CommonUtil.isEmpty(username) || CommonUtil.isEmpty(password)) {
                            JOptionPane.showMessageDialog(dialog, "账户和密码不能为空！");
                        } else if (CommonUtil.isNotMatches(username, CommonConstant.VALID_USERNAME_REGEX)) {
                            JOptionPane.showMessageDialog(dialog, "用户名含有非法字符！");
                        } else if (CommonUtil.isNotMatches(password, CommonConstant.VALID_PASSWORD_REGEX)) {
                            JOptionPane.showMessageDialog(dialog, "密码含有非法字符！");
                        } else {
                            try {
                                clientOS.writeObject(Message.build(MsgType.LOGIN_USER, CommonUtil.join(username, CommonConstant.SINGLE_SPACE, password)));
                                Message message = (Message) clientIS.readObject();
                                switch (message.getType()) {
                                    case LOGIN_SUCCESS:
                                        JOptionPane.showMessageDialog(dialog, message.getContent());
                                        // 加入到聊天线程中
                                        ThreadPoolUtil.execute(new ChatController(clientIS, clientOS, username));
                                        // 对话框退出
                                        dialog.dispose();
                                        // 客户端面板退出
                                        frame.dispose();
                                        break;
                                    case LOGIN_FAIL:
                                        JOptionPane.showMessageDialog(dialog, message.getContent());
                                        clientIS.close();
                                        clientOS.close();
                                        socket.close();
                                        dialog.dispose();
                                        break;
                                    default:
                                        System.out.println("登录消息类型返回异常，请管理员检查");
                                        break;
                                }
                            } catch (IOException | ClassNotFoundException ex) {
                                System.out.println("socket发生错误！");
                            }
                        }
                    }
                });
                dialog.add(label1);
                dialog.add(user);
                dialog.add(label2);
                dialog.add(pass);
                dialog.add(button);
                dialog.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "请检查服务器是否启动，服务器地址和端口是否错误！");
            }
        }
    }
}
