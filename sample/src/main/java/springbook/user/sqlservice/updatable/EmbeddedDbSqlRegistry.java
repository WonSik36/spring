package springbook.user.sqlservice.updatable;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import springbook.user.sqlservice.SqlNotFoundException;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
	SimpleJdbcTemplate jdbc;
	TransactionTemplate transactionTemplate;
	
	public void setDataSource(DataSource dataSource) {
		jdbc = new SimpleJdbcTemplate(dataSource);
		transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
	}
	
	@Override
	public void registerSql(String key, String sql) {
		jdbc.update("INSERT INTO SQLMAP(KEY_,SQL_) VALUES(?,?)",key,sql);
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		try {
			return jdbc.queryForObject("SELECT SQL_ FROM SQLMAP WHERE KEY_ = ?", String.class, key);
		}catch(EmptyResultDataAccessException e){
			throw new SqlNotFoundException("can not found the key: "+key,e);
		}
	}

	@Override
	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		int affected = jdbc.update("UPDATE SQLMAP SET SQL_ = ? WHERE KEY_ = ?", sql, key);
		// affected is affected record number
		if(affected == 0) {
			throw new SqlUpdateFailureException("can not found the key: "+key);
		}
	}

	@Override
	public void updateSql(final Map<String, String> sqlmap) throws SqlUpdateFailureException {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					for(Map.Entry<String,String> entry: sqlmap.entrySet()) {
						updateSql(entry.getKey(), entry.getValue());
					}
				}
		});
	}

}
