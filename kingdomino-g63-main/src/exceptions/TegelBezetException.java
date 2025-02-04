package exceptions;

public class TegelBezetException extends RuntimeException {

	public TegelBezetException() {
		super("De gekozen tegel is reeds bezet");
	}

	public TegelBezetException(String message) {
		super(message);
	}
}
