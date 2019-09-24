package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.domain.*;
import static springbook.user.service.VacationLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.VacationLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {
	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private MailSender mailSender;
	private List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("bumjin", "박범진","p1",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1,0, "Hellow@wolrd.com"),
				new User("joytouch", "강명성","p2",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER,0,"math@wolrd.com"),
				new User("erwins", "신승한","p3",Level.SILVER, 60,MIN_RECOMMEND_FOR_GOLD-1,"ezra@wolrd.com"),
				new User("madnite1", "이상호","p4",Level.SILVER, 60,MIN_RECOMMEND_FOR_GOLD,"kings@wolrd.com"),
				new User("green", "오민규","p5",Level.GOLD, 100,Integer.MAX_VALUE,"chronicle@wolrd.com")
			);
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() throws Exception{
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
	
	@Test
	public void upgradeAllOrNothing()throws Exception{
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(userDao);
		testUserService.setTransactionManager(transactionManager);
		testUserService.setUserLevelUpgradePolicy(new VacationLevelUpgradePolicy());
		testUserService.setMailSender(mailSender);
		
		userDao.deleteAll();
		for(User user: users) userDao.add(user);
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e){
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
}
