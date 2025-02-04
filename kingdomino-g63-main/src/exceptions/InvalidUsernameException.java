package exceptions;

public class InvalidUsernameException extends RuntimeException {

	public InvalidUsernameException() {
		super("Username does not meet the requirements: must be unique, at least 6 characters long and cannot consist only of spaces.");
	}

	public InvalidUsernameException(String message) {
		super(message);
	}
}
