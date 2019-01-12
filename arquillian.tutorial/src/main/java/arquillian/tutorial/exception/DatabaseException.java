package arquillian.tutorial.exception;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 1L;

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Exception e) {
		super(e);
	}
	
	public DatabaseException() {
	}
	
}