package springbook.user.dao;

import java.sql.*;

public interface ConnectionMaker{
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException;
}