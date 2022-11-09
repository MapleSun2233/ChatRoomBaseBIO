package server;

import server.view.ServerView;
import utils.ConfigGetter;

import javax.swing.*;
import java.awt.*;

public class StartServer {
    public static void main(String[] args) {
        // 设置GUI风格，跟随系统风格
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // 启动服务器
        ServerView.start();
    }
}
