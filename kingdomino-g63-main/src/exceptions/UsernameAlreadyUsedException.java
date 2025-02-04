package exceptions;

public class UsernameAlreadyUsedException extends RuntimeException {

	public UsernameAlreadyUsedException() {
		super("Username already in use.");
	}

	public UsernameAlreadyUsedException(String message) {
		super(message);

	}

}
