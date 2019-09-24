/*
*   ref: toby's spring 3.1
*    author: toby.epril.com
*/
package springbook.user.dao;

import springbook.user.domain.*;
import java.sql.*;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.mysql.cj.exceptions.MysqlErrorNumbers;

public class UserDaoJdbc implements UserDao{
    private JdbcTemplate jdbcTemplate;
    private static final String _insert_query = "INSERT INTO users(id,name,password,level,login,recommend,email) "
    		+ "values(?,?,?,?,?,?,?)";
    private static final String _update_query = "UPDATE users SET name=?,password=?,level=?,login=?,"
    		+ "recommend=?,email=? WHERE id=?";
    private static final String _select_query = "SELECT * FROM users WHERE id = ?";
    private static final String _select_all_query = "SELECT * FROM users ORDER BY id";
    private static final String _delete_all_query = "DELETE FROM users";
    private static final String _delete_query = "DELETE FROM users WHERE id = ?";
    private static final String _count_query = "SELECT COUNT(*) FROM users";
    private RowMapper<User> userMapper = new RowMapper<User>() {
    	public User mapRow(ResultSet rs, int rowNum)throws SQLException{
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			user.setEMail(rs.getString("email"));
			return user;
		}
    };
    
    public void setDataSource(DataSource dataSource) {
    	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public void add(final User user){
    	this.jdbcTemplate.update(_insert_query,user.getId(),user.getName(),user.getPassword(),
    			user.getLevel().intValue(),user.getLogin(),user.getRecommend(),user.getEMail());    		
    }
    
    public void update(User user) {
    	this.jdbcTemplate.update(_update_query,user.getName(),user.getPassword(),
    			user.getLevel().intValue(),user.getLogin(),user.getRecommend(),user.getEMail(),user.getId());
    }

    public User get(String id){
    	return this.jdbcTemplate.queryForObject(_select_query, new Object[] {id}, userMapper);
    }
    
    public List<User> getAll(){
    	return this.jdbcTemplate.query(_select_all_query,userMapper);
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