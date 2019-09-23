package springbook.user.service;

import springbook.user.domain.*;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.dao.*;

public class UserService {
	private UserDao userDao;
	private DataSource dataSource;
	private UserLevelUpgradePolicy userLevelUpgradePolicy;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
	}
	
	public void upgradeLevels()throws Exception{
		TransactionSynchronizationManager.initSynchronization();
		Connection c = DataSourceUtils.getConnection(dataSource);
		c.setAutoCommit(false);
		
		try {
			List<User> users = userDao.getAll();
			
			for(User user:users) {
				if(userLevelUpgradePolicy.canUpgradeLevel(user))
					userLevelUpgradePolicy.upgradeLevel(user);
			}
			c.commit();
		}catch(Exception e) {
			c.rollback();
			throw e;
		}finally {
			DataSourceUtils.releaseConnection(c, dataSource);
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}
	}

	public void add(User user) {
		if(user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	public UserLevelUpgradePolicy getUserLevelUpgradePolicy() {
		return this.userLevelUpgradePolicy;
	}
}
