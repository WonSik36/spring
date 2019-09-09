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
    private JdbcContext jdbcContext;
    private static final String _insert_query = "INSERT INTO users(id,name,password) values(?,?,?)";
    private static final String _select_query = "SELECT * FROM users WHERE id = ?";
    private static final String _delete_all_query = "DELETE FROM users";
    private static final String _delete_query = "DELETE FROM users WHERE id = ?";

    public void setDataSource(DataSource ds){
        this.dataSource = ds;
    }

    public void setJdbcContext(JdbcContext jc){
        this.jdbcContext = jc;
    }
    
    public void add(final User user)throws SQLException{
    	jdbcContext.workWithStatementStrategy(
    			new StatementStrategy() {
    				public PreparedStatement makePreparedStatement(Connection c)
    						throws SQLException{
    					PreparedStatement ps = c.prepareStatement(_insert_query);
    					ps.setString(1,user.getId());
    					ps.setString(2,user.getName());
    					ps.setString(3,user.getPassword());
    					
    					return ps;
    				}
    			}
    	);
    }

    public User get(String id)throws SQLException{
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

    public void delete(final String id)throws SQLException{
    	jdbcContext.workWithStatementStrategy(
    			new StatementStrategy() {
    				public PreparedStatement makePreparedStatement(Connection c)
    						throws SQLException{
    					PreparedStatement ps = c.prepareStatement(_delete_query);	
    					ps.setString(1,id);
    					return ps;
    				}
    			}
    	);
    }
    
    public void deleteAll()throws SQLException{
    	jdbcContext.executeSql(_delete_all_query);
    }
    
    public int getCount() throws SQLException{
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
}