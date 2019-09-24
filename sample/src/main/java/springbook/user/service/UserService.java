package springbook.user.service;

import springbook.user.domain.*;
import springbook.user.dao.*;
import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class UserService {
	private UserDao userDao;
	private PlatformTransactionManager transactionManager;
	private UserLevelUpgradePolicy userLevelUpgradePolicy;
	private MailSender mailSender;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
		this.userLevelUpgradePolicy = userLevelUpgradePolicy;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void upgradeLevels()throws Exception{
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
	
		try {
			List<User> users = userDao.getAll();
			for(User user:users) {
				if(userLevelUpgradePolicy.canUpgradeLevel(user))
					upgradeLevel(user);
			}
			transactionManager.commit(status);
		}catch(Exception e) {
			transactionManager.rollback(status);
			throw e;
		}
	}

	public void add(User user) {
		if(user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}
	
	private void sendUpgradeEMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setTo(user.getEMail());
		mailMessage.setFrom("useradmin@ksug.org");
		mailMessage.setSubject("About upgrade of your Level");
		mailMessage.setText("Your level is "+user.getLevel().name());
		
		this.mailSender.send(mailMessage);
	}
}
