package springbook;

import javax.sql.DataSource;
import com.mysql.jdbc.Driver;
import springbook.user.dao.UserDao;
import springbook.user.service.UserLevelUpgradePolicy;
import springbook.user.service.UserService;
import springbook.user.service.VacationLevelUpgradePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="springbook.user") // for Autowired annotation of UserDao, UserService
@Import({SqlServiceContext.class, TestAppContext.class, ProductionAppContext.class}) // import SQL Service context
public class AppContext {
	
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
	
	@Autowired UserDao userDao;
	@Autowired UserService userService;
	
	@Bean
	public UserLevelUpgradePolicy userLevelUpgradePolicy() {
		return new VacationLevelUpgradePolicy();
	}
}
