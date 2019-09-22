package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.domain.*;
import static springbook.user.service.VacationLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.VacationLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {
	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;
	private List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("bumjin", "�ڹ���","p1",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1,0),
				new User("joytouch", "����","p2",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER,0),
				new User("erwins", "�Ž���","p3",Level.SILVER, 60,MIN_RECOMMEND_FOR_GOLD-1),
				new User("madnite1", "�̻�ȣ","p4",Level.SILVER, 60,MIN_RECOMMEND_FOR_GOLD),
				new User("green", "���α�","p5",Level.GOLD, 100,Integer.MAX_VALUE)
			);
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() {
		userDao.deleteAll();
		for(User user: users) userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevelUpgraded(users.get(0),false);
		checkLevelUpgraded(users.get(1),true);
		checkLevelUpgraded(users.get(2),false);
		checkLevelUpgraded(users.get(3),true);
		checkLevelUpgraded(users.get(4),false);
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}
	
	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
}
