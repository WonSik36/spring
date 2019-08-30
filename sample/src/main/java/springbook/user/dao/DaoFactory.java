package springbook.user.dao;

public class DaoFactory{
    public UserDao userDao(){
        UserDao dao = new UserDao(connectionMaker());
        return dao;
    }

    public ConnectionMaker connectionMaker(){
        return new KConnectionMaker();
    }
}