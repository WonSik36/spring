package springbook.user.service;

import springbook.AppContext;
import springbook.TestAppContext;
import springbook.user.dao.*;
import springbook.user.domain.*;
import static springbook.user.service.VacationLevelUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.VacationLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {AppContext.class, TestAppContext.class})
public class UserServiceTest {
	@Autowired private UserService userService;
	@Resource private UserService testUserService;
	@Autowired private UserDao userDao;
	@Autowired private PlatformTransactionManager transactionManager;
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
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		userServiceImpl.setUserLevelUpgradePolicy(new VacationLevelUpgradePolicy());
		
		userServiceImpl.upgradeLevels();

		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0),"joytouch",Level.SILVER);
		checkUserAndLevel(updated.get(1),"madnite1",Level.GOLD);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEMail()));
		assertThat(request.get(1), is(users.get(3).getEMail()));
	}
	
	@Test
	public void mockUpgradeLevels() throws Exception{
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		userServiceImpl.setUserLevelUpgradePolicy(new VacationLevelUpgradePolicy());
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		checkUserAndLevel(users.get(1),"joytouch",Level.SILVER);
		verify(mockUserDao).update(users.get(3));
		checkUserAndLevel(users.get(3),"madnite1",Level.GOLD);
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEMail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEMail()));
	}
	
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
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
		
		userDao.deleteAll();
		for(User user: users) userDao.add(user);
		try {
			this.testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e){
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	@Test(expected=TransientDataAccessResourceException.class)
	public void readOnlyTransactionAttribute() {
		testUserService.getAll();
	}
	
	public static class TestUserService extends UserServiceImpl {
		private String id = "madnite1";
		
		
		protected void upgradeLevel(User user) {
			if(user.getId().contentEquals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
		
		// for learning test
		public List<User> getAll(){
			for(User user: super.getAll()) {
				super.update(user);
			}
			
			return null;
		}
	}
	
	@Test
	public void advisorAutoProxyCreator() {
		assertThat(testUserService, is(java.lang.reflect.Proxy.class));
	}
	
	@Test
	public void transactionSync() {
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();		
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);
		
		List<User> origin = userService.getAll();
		try {
			userService.deleteAll();
			userService.add(users.get(0));
			userService.add(users.get(1));
			
			List<User> beforeRollback = userService.getAll();
			
			assertThat(users.get(0).getId(), is(beforeRollback.get(0).getId()));
			assertThat(users.get(1).getId(), is(beforeRollback.get(1).getId()));
		}finally {
			transactionManager.rollback(txStatus);
		}
		List<User> afterRollback = userService.getAll();
		
		assertThat(origin.size(), is(afterRollback.size()));
		
		for(int i=0;i<origin.size();i++) {
			assertThat(origin.get(i).getId(), is(afterRollback.get(i).getId()));
		}
	}
	
	@Test
	@Transactional // this annotation rollback after test
	public void transactionalRollbackTest() {
		userService.deleteAll();
		userService.add(users.get(0));
		userService.add(users.get(1));
	}
}
