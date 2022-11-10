package client;

import client.view.ClientView;

import javax.swing.*;

public class StartClient {
    public static void main(String[] args) {
        // 设置GUI风格，跟随系统风格
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // 启动服务器
        ClientView.start();
    }
}
