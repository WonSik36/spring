package springbook.user.sqlservice.updatable;

import static org.junit.Assert.fail;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
	@Autowired
	private UpdatableSqlRegistry updatableSqlRegistry;
	private EmbeddedDatabase db;
	
	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		db = new EmbeddedDatabaseBuilder()
				.setType(HSQL)
				.addScript("classpath:springbook/user/sqlservice/updatable/sqlRegistrySchema.sql")
				.build();
		
		EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
		embeddedDbSqlRegistry.setDataSource(db);
		
		return embeddedDbSqlRegistry;
	}
	
	@After
	public void tearDown() {
		db.shutdown();
	}
	
	@Test
	public void transactionalUpdate() {
		checkFindResult("SQL1","SQL2","SQL3");
		
		Map<String,String> sqlmap = new HashMap<String,String>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY9999", "Modified9999");
		
		try {
			updatableSqlRegistry.updateSql(sqlmap);
			fail();
		}catch(SqlUpdateFailureException e) {}
		
		checkFindResult("SQL1","SQL2","SQL3");
	}
	

}
