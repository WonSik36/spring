package springbook.user.dao;

import java.sql.*;

public interface SimpleConnectionMaker{
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException;
}