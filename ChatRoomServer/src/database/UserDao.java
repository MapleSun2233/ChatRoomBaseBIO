package database;

import constant.SQLCollection;
import entity.User;
import utils.ConnectorUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {

    public static boolean addUser(String username, String password, String email) {
        boolean flag = false;
        try (PreparedStatement statement = ConnectorUtil.getConnection().prepareStatement(SQLCollection.ADD_USER_SQL)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            flag = statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("数据库异常或账户已经存在！");
        }
        return flag;
    }

    public static boolean deleteUser(String username) {
        boolean flag = false;
        try (PreparedStatement statement = ConnectorUtil.getConnection().prepareStatement(SQLCollection.DELETE_USER_SQL)) {
            statement.setString(1, username);
            flag = statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("数据库异常或账户不存在！");
        }
        return flag;
    }

    public static boolean updateUser(String username, String password, String email) {
        boolean flag = false;
        try (PreparedStatement statement = ConnectorUtil.getConnection().prepareStatement(SQLCollection.UPDATE_USER_SQL)) {
            statement.setString(1, password);
            statement.setString(2, email);
            statement.setString(3, username);
            flag = statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("数据库异常或账户不存在！");
        }
        return flag;
    }

    public static boolean updateUserPassword(String username, String password) {
        boolean flag = false;
        try (PreparedStatement statement = ConnectorUtil.getConnection().prepareStatement(SQLCollection.UPDATE_PASSWORD_SQL)) {
            statement.setString(1, password);
            statement.setString(2, username);
            flag = statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("数据库异常或账户不存在！");
        }
        return flag;
    }

    public static String[] listUsernames() {
        ArrayList<String> list = new ArrayList<>();
        try {
            ResultSet set = ConnectorUtil.getConnection().createStatement().executeQuery(SQLCollection.LIST_USER_SQL);
            while (set.next()) {
                list.add(set.getString(1));
            }
            return list.toArray(new String[0]);
        } catch (SQLException e) {
            System.out.println("数据库异常");
        }
        return null;
    }

    public static User queryUser(String username) {
        try (PreparedStatement statement = ConnectorUtil.getConnection().prepareStatement(SQLCollection.QUERY_USER_SQL)) {
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return new User(set.getString(1), set.getString(2), set.getString(3));
            }
        } catch (SQLException e) {
            System.out.println("数据库异常");
        }
        return null;
    }
}
