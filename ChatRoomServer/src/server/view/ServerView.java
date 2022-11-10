package server.view;

import javax.swing.*;

import server.ServerSocketTask;
import server.event.AddUserEvent;
import server.event.DeleteUserEvent;
import server.event.QueryUserEvent;
import server.event.UpdateUserEvent;
import utils.CommonUtil;
import utils.ConfigGetter;
import utils.ConnectorUtil;
import utils.ThreadPoolUtil;

import java.awt.*;
import java.sql.SQLException;

public class ServerView extends JFrame {
    public static final ServerView SERVER_VIEW = new ServerView();

    public static void start() {
        JButton addUser = new JButton("新增用户");
        JButton deleteUser = new JButton("删除用户");
        JButton updateUser = new JButton("修改用户");
        JButton queryUser = new JButton("查询用户");
        JLabel portLabel = new JLabel(String.format("服务监听在%d端口", ConfigGetter.getPort()));
        // 设置界面
        SERVER_VIEW.setTitle("服务端");
        SERVER_VIEW.setResizable(false);
        SERVER_VIEW.setLayout(null);
        SERVER_VIEW.setBounds(300, 300, 300, 550);
        SERVER_VIEW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置组件位置
        addUser.setBounds(100, 50, 100, 50);
        deleteUser.setBounds(100, 150, 100, 50);
        updateUser.setBounds(100, 250, 100, 50);
        queryUser.setBounds(100, 350, 100, 50);
        portLabel.setBounds(70, 450, 200, 50);
        // 设置组件监听事件
        addUser.addMouseListener(new AddUserEvent(SERVER_VIEW));
        deleteUser.addMouseListener(new DeleteUserEvent(SERVER_VIEW));
        updateUser.addMouseListener(new UpdateUserEvent(SERVER_VIEW));
        queryUser.addMouseListener(new QueryUserEvent(SERVER_VIEW));
        // 组件加入Server Frame
        SERVER_VIEW.add(addUser);
        SERVER_VIEW.add(deleteUser);
        SERVER_VIEW.add(updateUser);
        SERVER_VIEW.add(queryUser);
        SERVER_VIEW.add(portLabel);
        SERVER_VIEW.setVisible(true);
        // 检查数据库连接状态
        try {
            if (CommonUtil.isFalse(ConnectorUtil.isValid())) {
                JOptionPane.showMessageDialog(SERVER_VIEW, "数据库状态异常，请检查数据库连接！");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(SERVER_VIEW, "数据库状态异常，请检查数据库连接！");
        }
        // 开启socket服务线程
        ThreadPoolUtil.execute(new ServerSocketTask(ConfigGetter.getPort()));
        // 检查分辨率并提示调整，避免分辨率过高导致组件位置异常
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (device.getDisplayMode().getWidth() > ConfigGetter.getRecommendScreenWidth() || device.getDisplayMode().getHeight() > ConfigGetter.getRecommendScreenHeight()) {
            JOptionPane.showMessageDialog(SERVER_VIEW, String.format("检查到分辨率过高，视图可能显示异常，建议设置%d×%d！", ConfigGetter.getRecommendScreenWidth(), ConfigGetter.getRecommendScreenHeight()));
        }
        // 提示框
        JOptionPane.showMessageDialog(SERVER_VIEW, "请勿关闭服务端窗口，否则服务将随之停止！");
    }
}
