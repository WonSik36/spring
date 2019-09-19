package springbook.user.service;

import springbook.user.domain.*;
import springbook.user.dao.*;

public class UserService {
	UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
