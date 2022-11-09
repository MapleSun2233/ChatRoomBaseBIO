package constant;

public class SQLCollection {
    public static final String ADD_USER_SQL = "insert into user values (?,?,?)";
    public static final String DELETE_USER_SQL = "delete from user where username= ?";
    public static final String UPDATE_USER_SQL = "update user set password = ?,email = ? where username= ?";
    public static final String UPDATE_PASSWORD_SQL = "update user set password = ? where username= ?";
    public static final String LIST_USER_SQL = "select username from user";
    public static final String QUERY_USER_SQL = "select username,password,email from user where username = ?";
}
