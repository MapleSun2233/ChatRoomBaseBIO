package utils;

import java.util.ResourceBundle;

public class ConfigGetter {
    private static final ResourceBundle CONFIG;

    static {
        CONFIG = ResourceBundle.getBundle("config");
    }

    public static int getPort() {
        return Integer.parseInt(CONFIG.getString("port"));
    }

    public static String getUrl() {
        return CONFIG.getString("url");
    }

    public static String getUsername() {
        return CONFIG.getString("username");
    }

    public static String getPassword() {
        return CONFIG.getString("password");
    }

    public static int getRecommendScreenWidth() {
        return Integer.parseInt(CONFIG.getString("recommendScreenWidth"));
    }

    public static int getRecommendScreenHeight() {
        return Integer.parseInt(CONFIG.getString("recommendScreenHeight"));
    }
}
