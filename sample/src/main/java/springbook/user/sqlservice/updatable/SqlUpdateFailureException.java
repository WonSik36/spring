package springbook.user.sqlservice.updatable;

public class SqlUpdateFailureException extends RuntimeException {
	public SqlUpdateFailureException(String message) {
		super(message);
	}
	
	public SqlUpdateFailureException(Throwable cause) {
		super(cause);
	}
}
