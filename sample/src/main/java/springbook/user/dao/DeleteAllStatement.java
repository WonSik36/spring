package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy{
	private static String st = "DELETE FROM users";
	public PreparedStatement makePreparedStatement(Connection c)throws SQLException{
		PreparedStatement ps = c.prepareStatement(st);
		return ps;
	}
}
