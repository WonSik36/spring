package springbook.user.service;

import springbook.user.domain.*;
import springbook.user.dao.*;

public class TestLevelUpgradePolicy implements UserLevelUpgradePolicy{
	private UserDao userDao;
	private String exceptionId;
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	
	public void setExceptionId(String id) {
		this.exceptionId = id;
	}
	
	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch(currentLevel) {
			case BASIC: return(user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level: "+ currentLevel);
		}
	}

	public void upgradeLevel(User user) {
		if(user.getId().equals(exceptionId))
			throw new TestUserServiceException();
		user.upgradeLevel();
		userDao.update(user);
	}
}
