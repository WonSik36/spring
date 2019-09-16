package springbook.user.dao;

import java.util.List;
import springbook.user.domain.User;

public interface UserDao {
	void add(User user);
	User get(String id);
	List<User> getAll();
	void delete(String id);
	void deleteAll();
	int getCount();
}
