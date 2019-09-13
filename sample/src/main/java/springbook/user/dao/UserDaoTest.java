package springbook.user.dao;

import springbook.user.domain.User;

import java.util.List;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
// import org.springframework.context.annotation.AnnotationConfigApplicationContext;
// import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
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
//	private ApplicationContext context;
    private UserDao dao;
    private User u1;
    private User u2;
    private User u3;
	
	@Before
    public void setUp() {
		// ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//        this.dao = context.getBean("userDao",UserDao.class);
		u1 = new User("sam", "smith", "mouse");
        u2 = new User("harry", "harry poter", "magician");
        u3 = new User("henry", "3rd", "magaret");
    }
	
	@Test
    public void addAndGet(){
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
        
        dao.add(u1);
        dao.add(u2);
        assertThat(dao.getCount(), is(2));

        User u3 = dao.get(u1.getId());
        assertThat(u3.getName(), is(u1.getName()));
        assertThat(u3.getPassword(), is(u1.getPassword()));
        
        u3 = dao.get(u2.getId());
        assertThat(u3.getName(), is(u2.getName()));
        assertThat(u3.getPassword(), is(u2.getPassword()));
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
    
    private void checkSameUser(User u1, User u2) {
    	assertThat(u1.getId(), is(u2.getId()));
    	assertThat(u1.getName(), is(u2.getName()));
    	assertThat(u1.getPassword(), is(u2.getPassword()));
    }
}