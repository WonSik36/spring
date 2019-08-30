package springbook.user.dao;

import springbook.user.domain.User;
import java.sql.SQLException;

public class UserDaoTest{
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        ConnectionMaker connectionMaker = new KConnectionMaker();

        UserDao dao = new UserDao(connectionMaker);
        dao.delete(null);
        System.out.println("Delete Success");

        User user = new User();
        user.setId("whiteship");
        user.setName("wonsik");
        user.setPassword("123456");

        dao.add(user);

        System.out.println(user.getId()+" Enrollment Success");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId()+" Inquire Success");
    }
}