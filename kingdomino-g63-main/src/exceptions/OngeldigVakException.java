package exceptions;

public class OngeldigVakException extends RuntimeException {
	public OngeldigVakException() {
		super("Vak ongeldig");
	}

	public OngeldigVakException(String message) {
		super(message);
	}
}
