package exceptions;

public class KleurInGebruikException extends RuntimeException {
	public KleurInGebruikException() {
		super("Kleur reeds in gebruik.");
	}

	public KleurInGebruikException(String message) {
		super(message);

	}
}
