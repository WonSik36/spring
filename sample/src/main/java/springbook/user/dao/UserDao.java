/*
*   ref: toby's spring 3.1
*    author: toby.epril.com
*/
package springbook.user.dao;

import springbook.user.domain.User;
import java.sql.*;

public abstract class UserDao{
    private SimpleConnectionMaker simpleConnectionMaker;
    private final static String _insert_query = "INSERT INTO users(id,name,password) values(?,?,?)";
    private final static String _select_query = "SELECT * FROM users WHERE id = ?";
    private final static String _delete_all_query = "DELETE FROM users";
    private final static String _delete_query = "DELETE FROM users WHERE id = ?";

    public UserDao(){
        simpleConnectionMaker = new SimpleConnectionMaker();
    }

    public void add(User user)throws ClassNotFoundException, SQLException{
        Connection c = simpleConnectionMaker.makeNewConnection();
        PreparedStatement ps = c.prepareStatement(_insert_query);
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id)throws ClassNotFoundException, SQLException{
        Connection c = simpleConnectionMaker.makeNewConnection();
        PreparedStatement ps = c.prepareStatement(_select_query);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

    public void delete(String id)throws ClassNotFoundException, SQLException{
        Connection c = simpleConnectionMaker.makeNewConnection();
        PreparedStatement ps;
        if(id == null){
            ps = c.prepareStatement(_delete_all_query);

        }else{
            ps = c.prepareStatement(_delete_query);
            ps.setString(1, id);
        }
        
        ps.executeUpdate();
        
        ps.close();
        c.close();
    }

    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}