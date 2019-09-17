package springbook.user.dao;

import springbook.user.domain.*;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

// each test, object is made all time
// so it is okay to make instance variable
// but applicationContext is made all time so it should be static
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTest{
	@Autowired
    private UserDao dao;	// @Autowired search appropriate bean in application context
	@Autowired
	private DataSource dataSource;
    private User u1;
    private User u2;
    private User u3;
	
	@Before
    public void setUp() {
		// ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//        this.dao = context.getBean("userDao",UserDao.class);
		u1 = new User("sam", "smith", "mouse",Level.BASIC, 1, 0);
        u2 = new User("harry", "harry poter", "magician",Level.SILVER, 55, 10);
        u3 = new User("henry", "3rd", "magaret",Level.GOLD, 100, 40);
    }
	
	@Test
    public void addAndGet(){
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
        
        dao.add(u1);
        dao.add(u2);
        assertThat(dao.getCount(), is(2));

        User u3 = dao.get(u1.getId());
        checkSameUser(u3,u1);
        
        u3 = dao.get(u2.getId());
        checkSameUser(u3,u2);
    }
    @Test
    public void count(){
    	dao.deleteAll();
    	assertThat(dao.getCount(), is(0));
    	
    	dao.add(u1);
    	assertThat(dao.getCount(), is(1));
    	
    	dao.add(u2);
    	assertThat(dao.getCount(), is(2));
    	
    	dao.add(u3);
    	assertThat(dao.getCount(), is(3));
    }
    
    @Test(expected=EmptyResultDataAccessException.class)
    public void getUserFailure(){
    	dao.deleteAll();
    	assertThat(dao.getCount(), is(0));
    	
    	dao.get("unknown_id");
    }
    
    @Test
    public void getAll() {
    	dao.deleteAll();
    	
    	// negative test
    	List<User> user0 = dao.getAll();
    	assertThat(user0.size(), is(0));
    	
    	dao.add(u1);
    	List<User> user1 = dao.getAll();
    	assertThat(user1.size(), is(1));
    	checkSameUser(u1,user1.get(0));
    	
    	dao.add(u2);
    	List<User> user2 = dao.getAll();
    	assertThat(user2.size(), is(2));
    	checkSameUser(u2,user2.get(0));
    	checkSameUser(u1,user2.get(1));
    	
    	dao.add(u3);
    	List<User> user3 = dao.getAll();
    	assertThat(user3.size(), is(3));
    	checkSameUser(u2,user3.get(0));
    	checkSameUser(u3,user3.get(1));
    	checkSameUser(u1,user3.get(2));
    }
    
    @Test(expected=DuplicateKeyException.class)
    public void duplicateKey() {
    	dao.deleteAll();
    	
    	dao.add(u1);
    	dao.add(u1);
    }
    
    @Test
    public void sqlExceptionTranslate() {
    	dao.deleteAll();
    	try {
    		dao.add(u1);
    		dao.add(u1);
    	}catch(DuplicateKeyException e) {
    		SQLException sqlEx = (SQLException)e.getRootCause();
    		SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
    		assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
    	}
    }
    
    private void checkSameUser(User u1, User u2) {
    	assertThat(u1.getId(), is(u2.getId()));
    	assertThat(u1.getName(), is(u2.getName()));
    	assertThat(u1.getPassword(), is(u2.getPassword()));
    	assertThat(u1.getLevel(), is(u2.getLevel()));
    	assertThat(u1.getLogin(), is(u2.getLogin()));
    	assertThat(u1.getRecommend(), is(u2.getRecommend()));
    }
}