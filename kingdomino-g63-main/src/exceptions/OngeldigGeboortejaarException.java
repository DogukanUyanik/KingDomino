package exceptions;

public class OngeldigGeboortejaarException extends RuntimeException {

	public OngeldigGeboortejaarException() {
		super("Geboortejaar is ongeldig: elke gebruiker moet minstens 6 jaar oud zijn.");
	}

	public OngeldigGeboortejaarException(String message) {
		super(message);
	}
}
