package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorUtil {
    public static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(ConfigGetter.getUrl(), ConfigGetter.getUsername(), ConfigGetter.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果连接失效重新获取连接
     *
     * @return 数据库连接
     */
    public static Connection getConnection() {
        try {
            boolean valid = connection.isValid(500);
            if (CommonUtil.isFalse(valid)) {
                connection = DriverManager.getConnection(ConfigGetter.getUrl(), ConfigGetter.getUsername(), ConfigGetter.getPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection() {
        if (CommonUtil.isNotNull(connection)) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
