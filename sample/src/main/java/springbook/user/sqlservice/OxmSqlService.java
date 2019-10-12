package springbook.user.sqlservice;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.springframework.oxm.Unmarshaller;
import springbook.user.dao.UserDao;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class OxmSqlService implements SqlService {
	private final BaseSqlService baseSqlService = new BaseSqlService();
	private final OxmSqlReader oxmSqlReader = new OxmSqlReader(); // fixed
	private SqlRegistry sqlRegistry = new HashMapSqlRegistry();
	
	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}
	
	// SqlReader part
	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.oxmSqlReader.setUnmarshaller(unmarshaller);
	}
	
	// SqlReader part
	public void setSqlmapFile(String sqlmapFile) {
		this.oxmSqlReader.setSqlmapFile(sqlmapFile);
	}
	
	@PostConstruct
	public void loadSql() {
		// save data by using Sql Registry
		this.baseSqlService.setSqlReader(this.oxmSqlReader);
		this.baseSqlService.setSqlRegistry(this.sqlRegistry);
		this.baseSqlService.loadSql();
	}
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		return this.baseSqlService.getSql(key);
	}

	private class OxmSqlReader implements SqlReader{
		private Unmarshaller unmarshaller;
		private static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
		private String sqlmapFile = DEFAULT_SQLMAP_FILE;
		
		@Override
		public void read(SqlRegistry sqlRegistry) {
			try {
				Source source = new StreamSource(UserDao.class.getResourceAsStream(this.sqlmapFile));
				Sqlmap sqlmap = (Sqlmap)this.unmarshaller.unmarshal(source);
				
				for(SqlType sql: sqlmap.getSql()) {
					sqlRegistry.registerSql(sql.getKey(), sql.getValue());
				}
			}catch(IOException e) {
				throw new IllegalArgumentException("can't load "+this.sqlmapFile,e);
			}
		}
		
		public void setUnmarshaller(Unmarshaller unmarshaller) {
			this.unmarshaller = unmarshaller;
		}
		
		public void setSqlmapFile(String sqlmapFile) {
			this.sqlmapFile = sqlmapFile;
		}
		
	}
}
