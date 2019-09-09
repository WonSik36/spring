package springbook.user.dao;

import springbook.user.domain.User; 
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
        u1 = new User("harry", "harry poter", "magician");
    	u2 = new User("henry", "3rd", "magaret");
    	u3 = new User("sam", "smith", "mouse");
    }
	
	@Test
    public void addAndGet() throws ClassNotFoundException, SQLException{
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
        
        User u1 = new User("harry", "harry poter", "magician");
    	User u2 = new User("henry", "3rd", "magaret");

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
    public void count() throws SQLException, ClassNotFoundException{
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
    public void getUserFailure() throws SQLException, ClassNotFoundException{
    	dao.deleteAll();
    	assertThat(dao.getCount(), is(0));
    	
    	dao.get("unknown_id");
    }
}