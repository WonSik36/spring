/*
*   ref: toby's spring 3.1
*    author: toby.epril.com
*/
package springbook.user.dao;

import springbook.user.domain.*;
import springbook.user.sqlservice.*;
import java.sql.*;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoJdbc implements UserDao{
    private JdbcTemplate jdbcTemplate;
    @Autowired private SqlService sqlService;
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
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
    	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public void setSqlService(SqlService sqlService) {
    	this.sqlService = sqlService;
    }
    
    public void add(final User user){
    	this.jdbcTemplate.update(this.sqlService.getSql("userAdd"),user.getId(),user.getName(),user.getPassword(),
    			user.getLevel().intValue(),user.getLogin(),user.getRecommend(),user.getEMail());    		
    }
    
    public void update(User user) {
    	this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"),user.getName(),user.getPassword(),
    			user.getLevel().intValue(),user.getLogin(),user.getRecommend(),user.getEMail(),user.getId());
    }

    public User get(String id){
    	return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"), new Object[] {id}, userMapper);
    }
    
    public List<User> getAll(){
    	return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"),userMapper);
    }

    public void delete(final String id){
    	this.jdbcTemplate.update(this.sqlService.getSql("userDelete"),id);
    }
    
    public void deleteAll(){
    	this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
    }
    
    public int getCount(){
    	return this.jdbcTemplate.queryForInt(this.sqlService.getSql("userGetCount"));
    }
}