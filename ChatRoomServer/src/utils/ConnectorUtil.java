package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorUtil {
    public static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.setLoginTimeout(1);
            connection = DriverManager.getConnection(ConfigGetter.getUrl(), ConfigGetter.getUsername(), ConfigGetter.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("数据库连接异常，请检查数据库配置和数据库状态！");
        }
    }

    /**
     * 如果连接失效重新获取连接
     *
     * @return 数据库连接
     */
    public static Connection getConnection() {
        try {
            if (CommonUtil.isFalse(isValid())) {
                connection = DriverManager.getConnection(ConfigGetter.getUrl(), ConfigGetter.getUsername(), ConfigGetter.getPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 判断连接有效性
     */
    public static boolean isValid() throws SQLException {
        if (CommonUtil.isNull(connection)) {
            return false;
        }
        return connection.isValid(500);
    }
}
