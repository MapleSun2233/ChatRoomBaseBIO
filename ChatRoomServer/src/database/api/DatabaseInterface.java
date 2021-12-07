package database.api;

import database.Connector;
import database.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseInterface {
    public static boolean addUser(String username,String password,String email){
        String sql = "insert into user values (?,?,?)";
        boolean flag = false;
        try(PreparedStatement statement = Connector.getConnection().prepareStatement(sql)){
            statement.setString(1,username);
            statement.setString(2,password);
            statement.setString(3,email);
            flag = statement.executeUpdate() == 1;
        }catch (SQLException e){
            System.out.println("数据库异常或账户已经存在！");
        }finally {
            Connector.closeConnection();
        }
        return flag;
    }
    public static boolean deleteUser(String username){
        String sql = "delete from user where username= ?";
        boolean flag = false;
        try(PreparedStatement statement = Connector.getConnection().prepareStatement(sql)){
            statement.setString(1,username);
            flag = statement.executeUpdate() == 1;
        }catch (SQLException e){
            System.out.println("数据库异常或账户不存在！");
        }finally {
            Connector.closeConnection();
        }
        return flag;
    }
    public static boolean updateUser(String username,String password,String email){
        String sql = "update user set password = ?,email = ? where username= ?";
        boolean flag = false;
        try(PreparedStatement statement = Connector.getConnection().prepareStatement(sql)){
            statement.setString(1,password);
            statement.setString(2,email);
            statement.setString(3,username);
            flag = statement.executeUpdate() == 1;
        }catch (SQLException e){
            System.out.println("数据库异常或账户不存在！");
        }finally {
            Connector.closeConnection();
        }
        return flag;
    }
    public static boolean updateUserPassword(String username,String password){
        String sql = "update user set password = ? where username= ?";
        boolean flag = false;
        try(PreparedStatement statement = Connector.getConnection().prepareStatement(sql)){
            statement.setString(1,password);
            statement.setString(2,username);
            flag = statement.executeUpdate() == 1;
        }catch (SQLException e){
            System.out.println("数据库异常或账户不存在！");
        }finally {
            Connector.closeConnection();
        }
        return flag;
    }
    public static String[] queryUsers(){
        ArrayList<String> list = new ArrayList<>();
        try{
            ResultSet set = Connector.getConnection().createStatement().executeQuery("select username from user");
            while(set.next()) list.add(set.getString(1));
            return list.toArray(new String[list.size()]);
        }catch (SQLException e){
            System.out.println("数据库异常");
        }finally {
            Connector.closeConnection();
        }
        return null;
    }
    public static User queryAUser(String username){
        String sql = "select username,password,email from user where username = ?";
        try(PreparedStatement statement = Connector.getConnection().prepareStatement(sql)){
            statement.setString(1,username);
            ResultSet set = statement.executeQuery();
            while(set.next()) return new User(set.getString(1),set.getString(2),set.getString(3));
        }catch (SQLException e){
            System.out.println("数据库异常");
        }finally {
            Connector.closeConnection();
        }
        return null;
    }
}
