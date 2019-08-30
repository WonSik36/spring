package springbook.user.dao;

import java.sql.*;

public class KConnectionMaker implements ConnectionMaker{
    private final static String _url = "jdbc:mysql://localhost:3306/spring";
    private final static String _user = "root";
    private final static String _pw = "tmfl3fkdzk4";
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(_url,_user,_pw);
        return c;
    }
}