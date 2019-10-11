package springbook.user.sqlservice;

public class DefaultSqlService extends BaseSqlService {
	public DefaultSqlService() {
		setSqlReader(new JaxbSqlReader());
		setSqlRegistry(new HashMapSqlRegistry());
	}
}
