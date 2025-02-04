package exceptions;

public class OngeldigeGebruikersnaamException extends RuntimeException {

	public OngeldigeGebruikersnaamException() {
		super("Gebruikersnaam voldoet niet aan de eisen: moet uniek zijn, minstens 6 karakters lang en mag niet enkel uit spaties bestaan.");
	}

	public OngeldigeGebruikersnaamException(String message) {
		super(message);
	}
}
