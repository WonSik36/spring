package springbook;

import javax.sql.DataSource;
import com.mysql.jdbc.Driver;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserLevelUpgradePolicy;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;
import springbook.user.service.UserServiceTest.TestUserService;
import springbook.user.service.VacationLevelUpgradePolicy;
import springbook.user.sqlservice.OxmSqlService;
import springbook.user.sqlservice.SqlRegistry;
import springbook.user.sqlservice.SqlService;
import springbook.user.sqlservice.updatable.EmbeddedDbSqlRegistry;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
@EnableTransactionManagement
public class TestApplicationContext {
	
	/*
	 * DB Connection and Transaction
	 * 
	 */
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost:3306/spring");
		dataSource.setUsername("root");
		dataSource.setPassword("tmfl3fkdzk4");
		
		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource());
		
		return tm;
	}
	
	/*
	 * Application Logic and Test
	 * 
	 */
	
	@Bean
	public UserDao userDao() {
		UserDaoJdbc dao = new UserDaoJdbc();
		dao.setDataSource(dataSource());
		dao.setSqlService(sqlService());
		return dao;
	}
	
	@Bean
	public UserService userSerivce() {
		UserServiceImpl service = new UserServiceImpl();
		service.setUserDao(userDao());
		service.setMailSender(mailSender());
		service.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
		return service;
	}
	
	@Bean
	public UserService testUserService() {
		TestUserService testService = new TestUserService();
		testService.setUserDao(userDao());
		testService.setMailSender(mailSender());
		testService.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
		return testService;
	}
	
	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
	
	@Bean
	public UserLevelUpgradePolicy userLevelUpgradePolicy() {
		return new VacationLevelUpgradePolicy();
	}
	
	/*
	 * SQL Service
	 * 
	 */
	
	@Bean
	public SqlService sqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
		sqlService.setSqlRegistry(sqlRegistry());
		return sqlService;
	}
	
	@Bean
	public SqlRegistry sqlRegistry() {
		EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
		sqlRegistry.setDataSource(embeddedDatabase());
		return sqlRegistry;
	}
	
	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("springbook.user.sqlservice.jaxb");
		return marshaller;
	}
	
	@Bean
	public DataSource embeddedDatabase() {
		return new EmbeddedDatabaseBuilder()
				.setName("embeddedDatabase")
				.setType(HSQL)
				.addScript("classpath:springbook/user/sqlservice/updatable/sqlRegistrySchema.sql")
				.build();
	}
}
