package exceptions;

public class InvalidBirthYearException extends RuntimeException {

	public InvalidBirthYearException() {
		super("Birth year is invalid: every player has to be atleast 6 years old.");
	}

	public InvalidBirthYearException(String message) {
		super(message);
	}
}
