package domein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import exceptions.SpelerNietGevondenException;
import utils.Kleur;
import utils.Landschapstype;
import utils.Richting;

/**
 * Klasse die de domeincontroller voorstelt. De domeincontroller heeft zowel een
 * spel object, als een spelerrepository object, om methodes in de spel en
 * spelerrepository klassen te kunnen aanroepen op andere plaatsen.
 */
public class DomeinController {
	private Spel spel;
	private final SpelerRepository spelerRepository;

	/**
	 * Default constructor: maakt een nieuw spelerrepository en spel object aan.
	 */
	public DomeinController() {
		spelerRepository = new SpelerRepository();
		this.spel = new Spel();

	}

	/**
	 * Deze methode roept de voegToe methode aan uit de spelerrepository met de
	 * opgegeven speler zijn/haar/hun gebruikersnaam en geboortejaar
	 *
	 * @param gebruikersnaam(String): de gebruikersnaam van de opgegeven speler
	 * @param geboortejaar(int):      het geboortejaar van de opgegeven speler
	 */
	public void registreerSpeler(String gebruikersnaam, int geboortejaar) {
		Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);
		spelerRepository.voegToe(nieuweSpeler);
		spel.laadBeschikbareGebruikersnamen();
	}

	/**
	 * Deze methode roept de haalAlleGebruikersnamenOp methode aan uit de
	 * spelerrepository klasse.
	 *
	 * @return de output(List<String>) van de haalAlleGebruikersnamenOp methode uit
	 *         de spelerrepository klasse.
	 */
	public List<String> haalAlleGebruikersnamenOp() {
		return spelerRepository.haalAlleGebruikersnamenOp();
	}

	/**
	 * Deze methode roept de haalBeschikbareOptiesOp methode aan uit de spel klasse.
	 *
	 * @return de output (String) van de haalBeschikbareOptiesOp methode uit de spel
	 *         klasse.
	 */
	public String haalBeschikbareOptiesOp() {
		return spel.haalBeschikbareOptiesOp();
	}

	/**
	 * Deze methode roept voegSpelerToe aan uit de spel klasse om een nieuwe speler
	 * toe te voegen met de opgegeven gebruikersnaam en kleur.
	 *
	 * @param gebruikersnaam(String): De gebruikersnaam van de nieuwe speler.
	 * @param kleurKeuze(String):     De kleurkeuze van de nieuwe speler.
	 * @throws IllegalArgumentException Als de opgegeven kleurkeuze ongeldig is.
	 */
	public void voegSpelerToe(String gebruikersnaam, String kleurKeuze) {
		Kleur kleur;
		try {
			kleur = Kleur.valueOf(kleurKeuze);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Ongeldige kleur ingevoerd.");
		}
		spel.voegSpelerToe(gebruikersnaam, kleur);
	}

	/**
	 * Deze methode roept de getStartkolom methode aan uit de spel klasse.
	 *
	 * @return de startkolom(List<String>) uit de spel klasse.
	 */
	public List<String> getStartkolom() {
		return spel.getStartkolom();
	}

	/**
	 * Deze methode roept de getEindkolom methode aan uit de spel klasse.
	 *
	 * @return de eindkolom(List<String>) uit de spel klasse.
	 */
	public List<String> getEindkolom() {
		return spel.getEindkolom();
	}

	/**
	 * Deze methode roept de startSpel methode aan uit de spel klasse.
	 */
	public void startSpel() {
		spel.startSpel();
	}

	/**
	 * Deze methode roept de getAantalSpelers methode aan uit de spel klasse.
	 *
	 * @return de output(int) van de getAantalSpelers methode uit de spel klasse.
	 */
	public int getAantalSpelers() {
		return spel.getAantalSpelers();
	}

	/**
	 * Deze methode roept de kiesWillekeurigeSpelernaam methode aan uit de spel
	 * klasse.
	 *
	 * @return de output(String) van de kiesWillekeurigeSpelernaam methode uit de
	 *         spel klasse.
	 */
	public String kiesWillekeurigeSpelernaam() {
		return spel.kiesWillekeurigeSpelernaam();
	}

	/**
	 * Deze methode roept de vindSpelerOpNaam methode aan uit de spel klasse.
	 *
	 * @return de output(Speler) van de vindSpelerOpNaam methode uit de spel klasse.
	 * @param gebruikersnaam(String): de gebruikersnaam van de speler die we willen
	 *                                zoeken.
	 */
	public Speler vindSpelerOpNaam(String gebruikersnaam) {
		return spel.vindSpelerOpNaam(gebruikersnaam);
	}

	/**
	 * Deze methode roept de kiesEnPlaatsKoningInStartKolom methode op uit de spel
	 * klasse en deze laat de speler met de opgegeven gebruikersnaam een koning
	 * kiezen en plaatsen in de startkolom op de opgegeven tegelindex.
	 *
	 * @param gebruikersNaam(String): De gebruikersnaam van de speler die een koning
	 *                                wil plaatsen.
	 * @param tegelIndex(int):        De index van de tegel in de startkolom waar de
	 *                                koning moet worden geplaatst.
	 * @throws SpelerNietGevondenException Als de speler met de opgegeven
	 *                                     gebruikersnaam niet bestaat.
	 */
	public void kiesEnPlaatsKoningInStartkolom(String gebruikersNaam, int tegelIndex) {
		Speler speler = spel.vindSpelerOpNaam(gebruikersNaam);
		if (speler == null) {
			throw new SpelerNietGevondenException();
		}
		spel.kiesEnPlaatsKoningInStartKolom(speler, tegelIndex);
	}

	/**
	 * Deze methode roept de kiesEnPlaatsKoningInEindkolom methode op uit de spel
	 * klasse en deze laat de speler met de opgegeven gebruikersnaam een koning
	 * kiezen en plaatsen in de eindkolom op de opgegeven tegelindex. Na het kiezen
	 * en plaatsen van een koning wordt ook de volgendeBeurt methode uit spel
	 * aangeroepen om de beurt aan een volgende speler te geven.
	 *
	 * @param gebruikersNaam(String): De gebruikersnaam van de speler die een koning
	 *                                wil plaatsen.
	 * @param tegelIndex(int):        De index van de tegel in de eindkolom waar de
	 *                                koning moet worden geplaatst.
	 * @throws SpelerNietGevondenException Als de speler met de opgegeven
	 *                                     gebruikersnaam niet bestaat.
	 */
	public void kiesEnPlaatsKoningInEindkolom(String gebruikersNaam, int tegelIndex) {
		Speler speler = spel.vindSpelerOpNaam(gebruikersNaam);
		if (speler == null) {
			throw new SpelerNietGevondenException();
		}
		spel.kiesEnPlaatsKoningInEindkolom(speler, tegelIndex);
		spel.volgendeBeurt();
	}

	/**
	 * Deze methode roept de genereerWillekeurigeSpelernamenVolgorde methode aan uit
	 * de spel klasse.
	 *
	 * @return de output(List<String>) van de
	 *         genereerWillekeurigeSpelernamenVolgorde methode uit de spel klasse.
	 */
	public List<String> genereerWillekeurigeSpelernamenVolgorde() {
		return spel.genereerWillekeurigeSpelernamenVolgorde();
	}

	/**
	 * Deze methode roept de toonOverzicht methode aan uit de spel klasse.
	 *
	 * @return de output(String) van de toonOverzicht methode uit de spel klasse.
	 */
	public String toonOverzicht() {
		return spel.toonOverzicht();
	}

	/**
	 * Deze methode roept de plaatsTegelInKoninkrijkVanSpelerAanDeBeurt methode op
	 * uit de spel klasse en die plaatst de tegel van de speler aan de beurt in het
	 * koninkrijk van die speler.
	 *
	 * @param startX(int):        De x-coördinaat van de tegel in het koninkrijk.
	 * @param startY(int):        De y-coördinaat van de tegel in het koninkrijk.
	 * @param richting(Richting): De richting waarin de tegel moet worden geplaatst
	 *                            de output(boolean) van de
	 *                            plaatsTegelInKoninkrijkVanSpelerAanDeBeurt methode
	 *                            uit de spel klasse.
	 */
	public boolean plaatsTegelInKoninkrijkVanSpeler(String gebruikersNaam, int startX, int startY,
													String richtingString) {
		Speler speler = vindSpelerOpNaam(gebruikersNaam);
		if (speler == null) {
			throw new SpelerNietGevondenException("Speler niet gevonden.");
		}
		Richting richting;
		try {
			richting = Richting.valueOf(richtingString.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Ongeldige richting: " + richtingString);
		}
		Dominotegel tegel = spel.vindTegelVanSpeler(speler);
		return spel.plaatsDominotegel(speler, startX, startY, richting, tegel);
	}

	/**
	 * Deze methode geeft de gebruikersnaam van de speler die aan de beurt is.
	 *
	 * @return De gebruikersnaam van de speler die aan de beurt is.
	 * @throws IllegalStateException Als er geen speler aan de beurt is.
	 */
	public String geefNaamSpelerAanDeBeurt() {
		Speler spelerAanDeBeurt = spel.wieIsAanDeBeurt();
		if (spelerAanDeBeurt != null) {
			return spelerAanDeBeurt.getGebruikersnaam();
		}
		throw new IllegalStateException("Er is geen speler aan de beurt.");
	}

	/**
	 * Deze methode geeft de string-representatie van het koninkrijk van de speler
	 * die aan de beurt is.
	 *
	 * @return De string-representatie van het koninkrijk van de speler die aan de
	 *         beurt is.
	 * @throws IllegalStateException Als er geen speler aan de beurt is of de speler
	 *                               aan de beurt geen koninkrijk heeft.
	 */
	public String toonKoninkrijkVanSpelerAanDeBeurt() {
		Speler spelerAanDeBeurt = spel.wieIsAanDeBeurt();
		if (spelerAanDeBeurt != null) {
			return spelerAanDeBeurt.getKoninkrijk().toString();
		}
		throw new IllegalStateException("Er is geen speler aan de beurt of speler heeft geen koninkrijk.");
	}

	/**
	 * Deze methode roept de initialiseerBeurtVolgorde methode aan uit de spel
	 * klasse.
	 */
	public void initialiseerBeurtVolgorde() {
		spel.initialiseerBeurtVolgorde();
	}

	/**
	 * Deze methode roept de volgendeBeurt methode aan uit de spel klasse.
	 */
	public void volgendeBeurt() {
		spel.volgendeBeurt();
	}

	/**
	 * Deze methode roept de updateBeurtVolgorde methode aan uit de spel klasse.
	 */
	public void updateBeurtVolgorde() {
		spel.updateBeurtVolgorde();
	}

	/**
	 * Deze methode roept de promoveerEindkolomTotStartkolom methode aan uit de spel
	 * klasse.
	 */
	public void promoveerEindkolomTotStartkolom() {
		spel.promoveerEindkolomTotStartkolom();
	}

	/**
	 * Deze methode roept de isSpelGedaan methode aan uit de spel klasse.
	 *
	 * @return de output(boolean) van de isSpelGedaan methode uit de spel klasse.
	 */
	public boolean isSpelGedaan() {
		return spel.isSpelGedaan();
	}

	/**
	 * Deze methode geeft een lijst met de string-representaties van alle
	 * koninkrijken in het spel.
	 *
	 * @return Een lijst met strings, waarbij elke string de string-representatie is
	 *         van een koninkrijk.
	 */
	public List<String> getAlleKoninkrijkenWeergave() {
		return spel.getSpelers().stream().map(speler -> speler.getKoninkrijk().toString()).collect(Collectors.toList());
	}

	/**
	 * Deze methode roept de getStartkolomTegels methode aan uit de spel klasse.
	 *
	 * @return de output(List<Dominotegel>) van de getStartkolomTegels methode uit
	 *         de spel klasse.
	 */
	public List<Dominotegel> getStartkolomTegels() {
		return spel.getStartkolomTegels();
	}

	/**
	 * Deze methode roept de getEindkolomTegels methode aan uit de spel klasse.
	 *
	 * @return de output(List<Dominotegel>) van de getEindkolomTegels methode uit de
	 *         spel klasse.
	 */
	public List<Dominotegel> getEindkolomTegels() {
		return spel.getEindkolomTegels();
	}

	/**
	 * Deze methode geeft de bestandsnaam van de afbeelding van een tegel.
	 *
	 * @param tegel      De tegel waarvoor de afbeelding moet worden opgehaald.
	 * @param isVoorkant True als de afbeelding van de voorkant van de tegel moet
	 *                   worden opgehaald, false als de afbeelding van de achterkant
	 *                   moet worden opgehaald.
	 * @return De bestandsnaam van de afbeelding.
	 */
	public String getAfbeeldingNaam(Dominotegel tegel, boolean isVoorkant) {
		return spel.getAfbeeldingNaam(tegel, isVoorkant);
	}

	/**
	 * Deze methode geeft een lijst met de afbeeldingen van de tegels in de
	 * startkolom.
	 *
	 * @return Een lijst met bestandsnamen van de afbeeldingen van de tegels in de
	 *         startkolom.
	 */
	public List<String> getStartkolomTegelAfbeeldingen() {
		List<String> afbeeldingen = new ArrayList<>();
		for (Dominotegel tegel : spel.getStartkolomTegels()) {
			afbeeldingen.add("tegel_" + tegel.getNummer() + "_voorkant.png");
		}
		return afbeeldingen;
	}

	/**
	 * Deze methode geeft een lijst met de afbeeldingen van de tegels in de
	 * eindkolom.
	 *
	 * @return Een lijst met bestandsnamen van de afbeeldingen van de tegels in de
	 *         eindkolom.
	 */
	public List<String> getEindkolomTegelAfbeeldingen() {
		List<String> afbeeldingen = new ArrayList<>();
		for (Dominotegel tegel : spel.getEindkolomTegels()) {
			afbeeldingen.add("tegel_" + tegel.getNummer() + "_voorkant.png");
		}
		return afbeeldingen;
	}

	/**
	 * Deze methode geeft de afbeelding van de bovenste tegel op de spelstapel.
	 *
	 * @return De bestandsnaam van de afbeelding van de bovenste tegel, of null als
	 *         de spelstapel leeg is.
	 */
	public String getBovensteTegelSpelstapelAfbeelding() {
		Dominotegel bovensteTegel = spel.getBovensteTegelVanSpelstapel();
		if (bovensteTegel != null) {
			return String.format("tegel_%d_achterkant.png", bovensteTegel.getNummer());
		}
		return null;
	}


	/**
	 * Deze methode geeft de afbeelding van de tegel van een speler uit de startkolom.
	 *
	 * @param spelerNaam(String): De gebruikersnaam van de speler
	 * @return De bestandsnaam van de afbeelding van tegel van de speler, of null als
	 *         de speler geen tegel heeft.
	 */
	public String getAfbeeldingspadVanSpelerTegel(String spelerNaam) {
		Speler speler = vindSpelerOpNaam(spelerNaam);
		if (speler != null) {
			Dominotegel tegel = spel.vindTegelVanSpeler(speler);
			if (tegel != null) {
				return "tegel_" + tegel.getNummer() + "_voorkant.png";
			}
		}
		return null;
	}

	/**
	 * Deze methode roept de getSpelersInBeurtVolgorde methode aan uit de spel
	 * klasse.
	 *
	 * @return de output(List<String>) van de getSpelersInBeurtVolgorde methode uit
	 *         de spel klasse.
	 */
	public List<String> getSpelersInBeurtVolgorde() {
		return spel.getSpelersInBeurtVolgorde();
	}

	/**
	 * Deze methode geeft een map met de tegelnummers in de startkolom als keys en
	 * de kleuren van de koningen op die tegels als values.
	 *
	 * @return Een map met tegelnummers(int) als keys en kleuren van
	 *         koningen(String) als values.
	 */
	public Map<Integer, String> getKoningKleurenStartkolom() {
		Map<Integer, String> koningKleuren = new HashMap<>();
		for (Dominotegel tegel : spel.getStartkolomTegels()) {
			if (tegel.heeftKoning()) {
				koningKleuren.put(tegel.getNummer(), tegel.getKoning().getSpeler().getKleur().name().toLowerCase());
			}
		}
		return koningKleuren;
	}

	/**
	 * Deze methode geeft een map met de tegelnummers in de eindkolom als keys en de
	 * kleuren van de koningen op die tegels als values.
	 *
	 * @return Een map met tegelnummers(int) als keys en kleuren van
	 *         koningen(String) als values.
	 */
	public Map<Integer, String> getKoningKleurenEindkolom() {
		Map<Integer, String> koningKleuren = new HashMap<>();
		for (Dominotegel tegel : spel.getEindkolomTegels()) {
			if (tegel.heeftKoning()) {
				koningKleuren.put(tegel.getNummer(), tegel.getKoning().getSpeler().getKleur().name().toLowerCase());
			}
		}
		return koningKleuren;
	}

	/**
	 * Deze methode haalt de scores op van de opgegeven spelers, per landschapstype.
	 * @param spelernamen(List<String>): de namen van de spelers die meedoen aan het spel,
	 * en van wie de scores moeten worden berekend.
	 * @return Een map die elke speler(key) koppelt aan een lijst met scores per landschapstype(value)
	 */
	public Map<String, List<String>> haalLandschapScoresOpPerSpeler(List<String> spelernamen) {
	    Map<String, List<String>> spelerScores = new HashMap<>();
	    for (String naam : spelernamen) {
	        Speler speler = vindSpelerOpNaam(naam);
	        if (speler != null) {
	            Koninkrijk koninkrijk = speler.getKoninkrijk();
	            List<String> scores = spel.berekenScorePerLandschapstype(koninkrijk);
	            spelerScores.put(speler.getGebruikersnaam(), scores);
	        } else {
	            spelerScores.put(naam, Arrays.asList("Speler niet gevonden"));
	        }
	    }
	    return spelerScores;
	}
	/**
	 * Deze methode roept de methode aan om spelerstatistieken te updaten.
	 * @param spelernamen(List<String>): de namen van de spelers die meedoen aan het spel,
	 */
	public void berekenEnUpdateSpelerStatistieken(List<String> gebruikersnamen) {
	    List<Speler> spelers = new ArrayList<>();
	    for (String naam : gebruikersnamen) {
	        Speler speler = vindSpelerOpNaam(naam);
	        if (speler != null) {
	            spelers.add(speler);
	        } else {
	            System.out.println("Geen speler gevonden met de gebruikersnaam: " + naam);
	        }
	    }
	    spel.berekenEnUpdateSpelerStatistieken(spelers);
	}
	
	



}