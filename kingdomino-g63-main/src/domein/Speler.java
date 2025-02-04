package domein;

import java.time.LocalDate;
import exceptions.OngeldigGeboortejaarException;
import exceptions.OngeldigeGebruikersnaamException;
import utils.Kleur;

/**
 * Klasse voor een speler.
 * Een speler heeft een gebruikersnaam(String), een geboortejaar(int), het aantal keren dat de speler al is gewonnen(int),
 * het aantal keren dat de speler al heeft gespeeld(int), een aantal prestigepunten(int), een kleur(Kleur), een koning(Koning) 
 * en een koninkrijk(Koninkrijk)
 */
public class Speler {
	private String gebruikersnaam;
	private int geboortejaar;
	private int aantalGewonnen, aantalGespeeld;
	private int prestigePunten;
	private Kleur kleur;
	private Koning koning;
	private Koninkrijk koninkrijk;

	/**
	 * Constructor: maakt een nieuwe speler aan met opgegeven gebruikersnaam en geboortejaar. Bij deze constructor wordt het
	 * aantal keren dat de speler gewonnen en gespeeld heeft automatisch op 0 ingesteld.
	 * 
	 * @param gebruikersnaam(String): de gebruikersnaam van de speler.
	 * @param geboortejaar(int): het geboortejaar van de speler.
	 */
	public Speler(String gebruikersnaam, int geboortejaar) {
		this(gebruikersnaam, geboortejaar, 0, 0);
	}
	
	
	/**
	 * Aangemaakt om spel te kunnen testen
	 * 
	 * @param gebruikersnaam(String): de gebruikersnaam van de speler.
	 */
	public Speler(String gebruikersnaam) {
		this(gebruikersnaam, 0, 0, 0);
	}

	/**
	 * Constructor: maakt een nieuwe speler aan met opgegeven gebruikersnaam, geboortejaar, het aantal keren dat de speler gewonnen is
	 * en het aantal keren dat de speler al gespeeld heeft.
	 * 
	 * @param gebruikersnaam(String): de gebruikersnaam van de speler.
	 * @param geboortejaar(int): het geboortejaar van de speler.
	 * @param aantalGewonnen(int): het aantal keren dat de speler al gewonnen is.
	 * @param aantalGespeeld(int): het aantal keren dat de speler al een spel heeft gespeeld.
	 */
	public Speler(String gebruikersnaam, int geboortejaar, int aantalGewonnen, int aantalGespeeld) {
		setGebruikersnaam(gebruikersnaam);
		setGeboortejaar(geboortejaar);
		setAantalGewonnen(aantalGewonnen);
		setAantalGespeeld(aantalGespeeld);
	}

	/**
	 * Default constructor: Aangemaakt om van de methode getKleur gebruik te kunnen maken in de klasse Koninkrijk.
	 */
	public Speler() {
		this(null, 0, 0, 0);
	}

	/**
	 * Haalt de gebruikersnaam van de speler op.
	 * 
	 * @return de gebruikersnaam van de speler.
	 */
	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	/**
     * Stelt de gebruikersnaam van de speler in. Vooraleer dit gebeurt worden er eerst een aantal controles uitgevoerd. 
     * In het geval dat de gebruikersnaam gelijk is aan null, de gebruikersnaam voorloop of achterloop spaties heeft, 
     * leeg is of dat de gebruikersnaam korter is dan 6 karakters wordt er een OngeldigeGebruikersnaamException gegooid. 
     *
     * @param gebruikersnaam(String): de gebruikersnaam van de speler.
     * @throws OngeldigeGebruikersnaamException als de gebruikersnaam van de speler ongeldig is.
     */
	private void setGebruikersnaam(String gebruikersnaam) {
		if (gebruikersnaam == null || gebruikersnaam.trim().isEmpty() || gebruikersnaam.length() < 6) {
			throw new OngeldigeGebruikersnaamException();
		}
		this.gebruikersnaam = gebruikersnaam;
	}

	/**
	 * Haalt het geboortejaar van de speler op.
	 * 
	 * @return het geboortejaar van de speler.
	 */
	public int getGeboortejaar() {
		return geboortejaar;
	}

	/**
	 * Stelt het geboortejaar van de speler in. Vooraleer dit gebeurt worden er eerst een aantal controles uitgevoerd. 
	 * In het geval dat het geboortejaar erop duidt dat de speler jonger is dan 6 jaar, dan wordt er een
	 * OngeldigGeboortejaarException gegooid.
	 * 
	 * @param geboortejaar(int): het geboortejaar van de speler.
	 * @throws OngeldigGeboortejaarException als het geboortejaar van de speler ongeldig is.
	 */
	public void setGeboortejaar(int geboortejaar) {
		if (geboortejaar <= 0 || geboortejaar > LocalDate.now().getYear() - 6) {
			throw new OngeldigGeboortejaarException();
		}
		this.geboortejaar = geboortejaar;
	}

	/**
	 * Haalt het aantal keren op dat de speler al gewonnen is.
	 * 
	 * @return het aantal keren dat de speler al is gewonnen.
	 */
	public int getAantalGewonnen() {
		return aantalGewonnen;
	}

	/**
	 * Stelt het aantal keren in dat de speler al is gewonnen.
	 * 
	 * @param aantalGewonnen(int): het aantal keren dat de speler al is gewonnen.
	 */
	public void setAantalGewonnen(int aantalGewonnen) {
		this.aantalGewonnen = aantalGewonnen;
	}

	/**
	 * Haalt het aantal keren op dat de speler al heeft gespeeld.
	 * 
	 * @return het aantal keren dat de speler al heeft gespeeld.
	 */
	public int getAantalGespeeld() {
		return aantalGespeeld;
	}

	/**
	 * Stelt het aantal keren in dat de speler al heeft gespeeld.
	 * 
	 * @param aantalGespeeld(int): het aantal keren dat de speler al heeft gespeeld.
	 */
	public void setAantalGespeeld(int aantalGespeeld) {
		this.aantalGespeeld = aantalGespeeld;
	}
	
	/**
	 * Haalt de kleur van de speler op.
	 * 
	 * @return de kleur van de speler.
	 */
	public Kleur getKleur() {
		return kleur;
	}

	/**
	 * Stelt de kleur van de speler in.
	 * 
	 * @param kleur(Kleur): de kleur van de speler.
	 */
	public void setKleur(Kleur kleur) {
		this.kleur = kleur;

	}

	/**
	 * Haalt de koning van de speler op.
	 * 
	 * @return de koning van de speler.
	 */
	public Koning getKoning() {
		return koning;
	}
	
	/**
	 * Stelt de koning van de speler in.
	 * 
	 * @param koning(Koning): de koning van de speler.
	 */
	public void setKoning(Koning koning) {
		this.koning = koning;
	}
	
	/**
	 * Haalt het koninkrijk van de speler op.
	 * 
	 * @return het koninkrijk van de speler.
	 */
	public Koninkrijk getKoninkrijk() {
		return koninkrijk;
	}

	/**
	 * Stelt het koninkrijk van de speler in.
	 * 
	 * @param koninkrijk(Koninkrijk): het koninkrijk van de speler.
	 */
	public void setKoninkrijk(Koninkrijk koninkrijk) {
		this.koninkrijk = koninkrijk;
	}
	
	/**
	 * Haalt de prestigepunten van de speler op.
	 * 
	 * @return de prestigepunten van de speler.
	 */
	public int getPrestigePunten() {
		return prestigePunten;
	}

	/**
	 * Stelt de prestigepunten van de speler in.
	 * 
	 * @param prestigePunten(int): de prestigepunten van de speler.
	 */
	public void setPrestigePunten(int prestigePunten) {
		this.prestigePunten = prestigePunten;

	}

}
