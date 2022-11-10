package client.event;

import constant.CommonConstant;
import constant.MsgType;
import entity.Message;
import utils.CommonUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

public class RetrieveEvent extends MouseAdapter {
    private final JFrame frame;
    private final JTextField address;

    public RetrieveEvent(JFrame frame, JTextField address) {
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
                Socket socket = new Socket(addressInfo[0], Integer.parseInt(addressInfo[1]));
                // 对话框
                JDialog dialog = new JDialog(frame);
                dialog.setModal(true);
                dialog.setTitle("找回密码");
                dialog.setBounds(300, 300, 300, 200);
                dialog.setLayout(null);
                JLabel label1 = new JLabel("账号：");
                label1.setBounds(20, 20, 60, 30);
                JLabel label2 = new JLabel("邮箱：");
                label2.setBounds(20, 60, 60, 30);
                JTextField user = new JTextField("");
                user.setBounds(90, 20, 180, 30);
                JTextField email = new JTextField("");
                email.setBounds(90, 60, 180, 30);
                JButton button = new JButton("找回密码");
                button.setBounds(100, 110, 100, 30);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String username = CommonUtil.deleteAllBlankChar(user.getText());
                        String emailAddress = CommonUtil.deleteAllBlankChar(email.getText());
                        if (CommonUtil.isEmpty(username) || CommonUtil.isEmpty(emailAddress)) {
                            JOptionPane.showMessageDialog(dialog, "所有字段不能为空！");
                        } else if (CommonUtil.isNotMatches(username, CommonConstant.VALID_USERNAME_REGEX)) {
                            JOptionPane.showMessageDialog(dialog, "用户名含有非法字符！");
                        } else if (CommonUtil.isNotMatches(emailAddress, CommonConstant.VALID_EMAIL_REGEX)) {
                            JOptionPane.showMessageDialog(dialog, "邮箱地址不合法！");
                        } else {
                            try {
                                ObjectOutputStream clientOS = new ObjectOutputStream(socket.getOutputStream());
                                ObjectInputStream clientIS = new ObjectInputStream(socket.getInputStream());
                                clientOS.writeObject(Message.build(MsgType.RETRIEVE_USER, CommonUtil.join(username, CommonConstant.SINGLE_SPACE, emailAddress)));
                                // 验证找回结果
                                Message message = (Message) clientIS.readObject();
                                switch (message.getType()) {
                                    case RETRIEVE_SUCCESS:
                                        JOptionPane.showMessageDialog(dialog, CommonUtil.join("密码：", message.getContent()));
                                        break;
                                    case RETRIEVE_FAIL:
                                        JOptionPane.showMessageDialog(dialog, message.getContent());
                                        break;
                                    default:
                                        System.out.println("恢复消息类型返回异常，请管理员检查");
                                        break;
                                }
                                clientIS.close();
                                clientOS.close();
                                socket.close();
                                dialog.dispose();
                            } catch (IOException | ClassNotFoundException ex) {
                                System.out.println("socket发生错误！");
                            }
                        }
                    }
                });
                dialog.add(label1);
                dialog.add(user);
                dialog.add(label2);
                dialog.add(email);
                dialog.add(button);
                dialog.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "请检查服务器是否启动，服务器地址和端口是否错误！");
            }
        }
    }
}
