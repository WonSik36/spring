/*
*   ref: toby's spring 3.1
*    author: toby.epril.com
*/
package springbook.user.dao;

import springbook.user.domain.User;
import java.sql.*;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

public class UserDao{
    private DataSource dataSource;
    private static final String _insert_query = "INSERT INTO users(id,name,password) values(?,?,?)";
    private static final String _select_query = "SELECT * FROM users WHERE id = ?";
    private static final String _delete_all_query = "DELETE FROM users";
    private static final String _delete_query = "DELETE FROM users WHERE id = ?";

    public void setDataSource(DataSource ds){
        this.dataSource = ds;
    }

    public void add(User user)throws ClassNotFoundException, SQLException{
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(_insert_query);
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id)throws ClassNotFoundException, SQLException{
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(_select_query);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        User user = null;
        if(rs.next()) {
        	user = new User();
        	user.setId(rs.getString("id"));
        	user.setName(rs.getString("name"));
        	user.setPassword(rs.getString("password"));
        }
        rs.close();
        ps.close();
        c.close();
        if(user == null)
        	throw new EmptyResultDataAccessException(1);
        return user;
    }

    public void delete(String id)throws ClassNotFoundException, SQLException{
    	StatementStrategy st = new DeleteAllStatement(); 
    	jdbcContextWithStatementStrategy(st);
    }
    
    public int getCount() throws ClassNotFoundException, SQLException{
    	Connection c = dataSource.getConnection();
    	PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM USERS");
    	ResultSet rs = ps.executeQuery();
    	rs.next();
    	int count = rs.getInt(1);
    	
    	rs.close();
    	ps.close();
    	c.close();
    	
    	return count;
    }
    
    public void jdbcContextWithStatementStrategy(StatementStrategy stmt)throws SQLException{
    	Connection c = null;
        PreparedStatement ps = null;
        try {
        	c = dataSource.getConnection();
        	ps = stmt.makePreparedStatement(c);

        	ps.executeUpdate();
        }catch(SQLException e) {
        	throw e;
        }finally {
        	if(ps!=null) {try {ps.close();}catch(SQLException e) {}}
        	if(c!=null) {try {c.close();}catch(SQLException e) {}}
        }
    }
}