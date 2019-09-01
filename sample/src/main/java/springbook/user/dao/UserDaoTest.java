package springbook.user.dao;

import springbook.user.domain.User; 
import java.sql.SQLException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
// import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest{
    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException{
        // ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        ApplicationContext context = new GenericXmlApplicationContext("springbook/applicationContext.xml");
        UserDao dao = context.getBean("userDao",UserDao.class);

        dao.delete(null);
        assertThat(dao.getCount(), is(0));
        
        User user = new User();
        user.setId("gyumee");
        user.setName("Cheung");
        user.setPassword("468789");

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user.getId());
        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));
    }
}