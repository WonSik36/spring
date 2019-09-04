package springbook.user.dao;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public interface StatementStrategy {
	PreparedStatement makePreparedStatement(Connection c)throws SQLException;
}
