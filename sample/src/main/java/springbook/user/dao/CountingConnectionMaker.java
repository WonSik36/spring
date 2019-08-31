package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker{
    private int count = 0;
    private ConnectionMaker cm;

    public void setConnectionMaker(ConnectionMaker cm){
        this.cm = cm;
    }

    public Connection makeNewConnection() throws ClassNotFoundException, SQLException{
        this.count++;
        return cm.makeNewConnection();
    }

    public int getCount(){
        return this.count;
    }
}