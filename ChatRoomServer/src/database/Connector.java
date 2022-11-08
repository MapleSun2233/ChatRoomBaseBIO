package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Connector {
    private static String URL = null;
    private static String IP = null;
    private static String USERNAME = null;
    private static String PASSWORD = null;
    private static Connection connection = null;
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
        IP = resourceBundle.getString("ip");
        URL = String.format("jdbc:mysql://%s:3306/chat?useSSL=true&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai",IP);
        USERNAME = resourceBundle.getString("username");
        PASSWORD = resourceBundle.getString("password");
    }
    public static Connection getConnection(){
        if(connection==null){
            try {
                connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    public static void closeConnection(){
        if(connection!=null)
            try{
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        connection = null;
    }
}
