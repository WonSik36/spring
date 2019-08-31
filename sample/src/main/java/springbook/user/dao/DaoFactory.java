package springbook.user.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory{
    private static final String _url = "jdbc:mysql://localhost:3306/spring";
    private static final String _user = "root";
    private static final String _pw = "tmfl3fkdzk4";
    @Bean
    public UserDao userDao(){
        UserDao dao = new UserDao();
        dao.setDataSource(dataSource());
        return dao;
    }
    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource ds = new SimpleDriverDataSource();

        ds.setDriverClass(com.mysql.jdbc.Driver.class);
        ds.setUrl(_url);
        ds.setUsername(_user);
        ds.setPassword(_pw);

        return ds;
    }
}