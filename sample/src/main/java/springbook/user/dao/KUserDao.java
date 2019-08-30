/*
*   ref: toby's spring 3.1
*    author: toby.epril.com
*/
package springbook.user.dao;

import springbook.user.domain.User;
import java.sql.*;

public class KUserDao extends UserDao{
    private final static String _url = "jdbc:mysql://localhost:3306/spring";
    private final static String _user = "root";
    private final static String _pw = "tmfl3fkdzk4";
    public static void main(String[] args)throws ClassNotFoundException, SQLException{
        UserDao dao = new KUserDao();
        dao.delete(null);
        System.out.println("Delete Success");

        User user = new User();
        user.setId("whiteship");
        user.setName("wonsik");
        user.setPassword("123456");

        dao.add(user);

        System.out.println(user.getId()+" Enrollment Success");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId()+" Inquire Success");
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(_url,_user,_pw);
        return c;
    }
}