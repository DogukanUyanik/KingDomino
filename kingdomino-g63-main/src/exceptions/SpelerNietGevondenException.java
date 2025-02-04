package exceptions;

public class SpelerNietGevondenException extends RuntimeException {

	public SpelerNietGevondenException() {
		super("De opgegeven speler werd niet gevonden");
	}

	public SpelerNietGevondenException(String message) {
		super(message);
	}
}
