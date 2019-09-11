/*
*   ref: toby's spring 3.1
*    author: toby.epril.com
*/
package springbook.user.dao;

import springbook.user.domain.User;
import java.sql.*;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDao{
    private DataSource dataSource;
    private JdbcContext jdbcContext;
    private JdbcTemplate jdbcTemplate;
    private static final String _insert_query = "INSERT INTO users(id,name,password) values(?,?,?)";
    private static final String _select_query = "SELECT * FROM users WHERE id = ?";
    private static final String _select_all_query = "SELECT * FROM users ORDER BY id";
    private static final String _delete_all_query = "DELETE FROM users";
    private static final String _delete_query = "DELETE FROM users WHERE id = ?";
    private static final String _count_query = "SELECT COUNT(*) FROM users";

    public void setJdbcContext(JdbcContext jc){
        this.jdbcContext = jc;
    }
    
    public void setDataSource(DataSource dataSource) {
    	this.jdbcTemplate = new JdbcTemplate(dataSource);
    	this.dataSource = dataSource;
    }
    
    public void add(final User user){
    	this.jdbcTemplate.update(_insert_query,user.getId(),user.getName(),user.getPassword());
    }

    public User get(String id){
        return this.jdbcTemplate.queryForObject(_select_query, new Object[] {id}, 
        		new RowMapper<User>() {
        			public User mapRow(ResultSet rs, int rowNum)throws SQLException{
        				User user = new User();
        				user.setId(rs.getString("id"));
        				user.setName(rs.getString("name"));
        				user.setPassword(rs.getString("password"));
        				return user;
        			}
        });
    }
    
    public List<User> getAll(){
    	return this.jdbcTemplate.query(_select_all_query,
    			new RowMapper<User>() {
    				public User mapRow(ResultSet rs, int rowNum)throws SQLException{
    					User user = new User();
    					user.setId(rs.getString("id"));
    					user.setName(rs.getString("name"));
    					user.setPassword(rs.getString("password"));
    					return user;
    		}
    	});
    }

    public void delete(final String id){
    	this.jdbcTemplate.update(_delete_query,id);
    }
    
    public void deleteAll(){
    	this.jdbcTemplate.update(_delete_all_query);
    }
    
    public int getCount(){
    	return this.jdbcTemplate.queryForInt(_count_query);
    }
}