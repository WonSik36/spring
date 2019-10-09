package springbook.user.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import springbook.user.dao.UserDao;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

public class XmlSqlService implements SqlService,SqlRegistry,SqlReader {
	private SqlReader sqlReader;	//sqlService
	private SqlRegistry sqlRegistry; //sqlService
	private Map<String, String> sqlMap = new HashMap<String, String>(); // sqlRegistry
	private String sqlmapFile;	// sqlReader
	
	
	
	/******************************************* sqlService implements ****************************************/

	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}
	
	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}
	
	@PostConstruct
	public void loadSql() {
		this.sqlReader.read(this.sqlRegistry);
	}
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			return this.sqlRegistry.findSql(key);
		}catch(SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e);
		}
	}

	
	/******************************************* sqlRegistry implements ****************************************/

	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key,sql);
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if(sql == null)
			throw new SqlRetrievalFailureException("can not found the key: "+key);
		else
			return sql;
	}

	
	/******************************************* sqlReader implements ****************************************/

	@Override
	public void read(SqlRegistry sqlRegistry) {
		String contextPath = Sqlmap.class.getPackage().getName();
		try {			
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
			Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
			for(SqlType sql: sqlmap.getSql()) {
				sqlRegistry.registerSql(sql.getKey(), sql.getValue());
			}
			
		}catch(JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}

}
