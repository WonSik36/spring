package springbook;

import javax.sql.DataSource;
import com.mysql.jdbc.Driver;
import springbook.user.dao.UserDao;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserLevelUpgradePolicy;
import springbook.user.service.UserService;
import springbook.user.service.VacationLevelUpgradePolicy;
import springbook.user.service.UserServiceTest.TestUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="springbook.user") // for Autowired annotation of UserDao, UserService
@Import(SqlServiceContext.class) // import SQL Service context
@PropertySource("/springbook/database.properties")
public class AppContext {
	@Value("${db.driverClass}") Class<? extends Driver> driverClass;
	@Value("${db.url}") String url;
	@Value("${db.username}") String username;
	@Value("${db.password}") String password;
	
	/*
	 * DB Connection and Transaction
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(this.driverClass);			
		dataSource.setUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		
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
	
	@Autowired UserDao userDao;
	@Autowired UserService userService;
	
	@Bean
	public UserLevelUpgradePolicy userLevelUpgradePolicy() {
		return new VacationLevelUpgradePolicy();
	}
	
	/*
	 * Test Application Context
	 * 
	 */
	
	@Configuration
	@Profile("test")
	public static class TestAppContext {
		
		@Bean
		public UserService testUserService() {
			return new TestUserService();
		}
		
		@Bean
		public MailSender mailSender() {
			return new DummyMailSender();
		}
	}
	
	/*
	 * Production Application Context
	 * 
	 */
	
	@Configuration
	@Profile("production")
	public static class ProductionAppContext {
		@Bean
		public MailSender mailSender() {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost("localhost");
			return mailSender;
		}
	}
	
	/*
	 *  to Support @Value annotation
	 *  get PropertySourcePlaceholderConfigurer
	 */
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	/*
	 *  sql map configuration
	 *  this is for Sql Service
	 */
	@Bean
	public SqlMapConfig sqlMapConfig() {
		return new UserSqlMapConfig();
	}
}
