package domein;

import java.util.*;
import java.util.stream.Collectors;

import exceptions.GebruikersnaamInGebruikException;
import exceptions.KleurInGebruikException;
import exceptions.OngeldigVakException;
import exceptions.SpelerNietGevondenException;
import exceptions.TegelBezetException;
import utils.Kleur;
import utils.Landschapstype;
import utils.Richting;

/**
 * Klasse die een spel voorstelt. Het spel bevat meerdere lijstjes van
 * dominotegels(List<Dominotegel>), waaronder alle dominotegels, de start- en
 * eindkolom en de spelstapel. Ook heeft een spel een lijst met de
 * spelers(List<Speler>) die spelen, een lijst met de beschikbare
 * kleuren(List<Kleur>) en een lijst met de beschikbare
 * gebruikersnamen(List<String>). Het spel houdt ook de volgorde bij van spelers
 * in lijst(List<Speler>)
 */

public class Spel {

	private SpelerRepository spelerRepository;
	private DominotegelRepository dominotegelRepository;
	// lijst van alle mogelijke tegels
	private List<Dominotegel> dominotegels;
	// lijst van tegels gebruikt voor het spel
	private List<Dominotegel> spelstapel;
	private List<Dominotegel> startkolom;
	private List<Dominotegel> eindkolom;

	private List<String> beschikbareGebruikersnamen;
	private List<Speler> spelers;
	private List<Kleur> beschikbareKleuren;

	private List<Speler> beurtVolgorde = new ArrayList<>();
	private int huidigeSpelerIndex;

	private Koninkrijk k = new Koninkrijk();
	private Vak[][] grid = k.getGrid();

	private int minX = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxY = Integer.MIN_VALUE;

	/**
	 * Controleert of een vak vrij is in het koninkrijk.
	 *
	 * @param x De x-coördinaat van het vak.
	 * @param y De y-coördinaat van het vak.
	 * @return true als het vak vrij is, anders false.
	 */
	public boolean isVakVrij(int x, int y) {
		return x >= 0 && x < 9 && y >= 0 && y < 9 && grid[x][y].getLandschapstype() == Landschapstype.LEEG;
	}

	/**
	 * Update de grenzen van het koninkrijk op basis van de opgegeven coördinaten.
	 *
	 * @param x De x-coördinaat.
	 * @param y De y-coördinaat.
	 */
	public void updateGrenzen(int x, int y) {
		minX = Math.min(minX, x);
		maxX = Math.max(maxX, x);
		minY = Math.min(minY, y);
		maxY = Math.max(maxY, y);
	}

	/**
	 * Controleert of het plaatsen van een tegel de maximale grootte van het
	 * koninkrijk zou overschrijden.
	 *
	 * @param startX De start x-coördinaat.
	 * @param startY De start y-coördinaat.
	 * @param eindX  De eind x-coördinaat.
	 * @param eindY  De eind y-coördinaat.
	 * @return true als de maximale grootte wordt overschreden, anders false.
	 */
	public boolean zouMaxGrootteOverschrijden(int startX, int startY, int eindX, int eindY) {
		int tempMinX = Math.min(minX, Math.min(startX, eindX));
		int tempMaxX = Math.max(maxX, Math.max(startX, eindX));
		int tempMinY = Math.min(minY, Math.min(startY, eindY));
		int tempMaxY = Math.max(maxY, Math.max(startY, eindY));
		return (tempMaxX - tempMinX + 1) > 5 || (tempMaxY - tempMinY + 1) > 5;
	}

	/**
	 * Default constructor waarin de lijstjes worden aangemaakt. De lijst van
	 * beschikbare gebruikersnamen wordt opgehaald, en ook worden de beschikbare
	 * kleuren opgehaald.
	 */
	public Spel() {
		this.spelerRepository = new SpelerRepository();
		this.dominotegelRepository = new DominotegelRepository();
		this.spelers = new ArrayList<>();
		beschikbareGebruikersnamen = spelerRepository.haalAlleGebruikersnamenOp();
		this.beschikbareKleuren = new ArrayList<>(Arrays.asList(Kleur.values()));
		this.dominotegels = new ArrayList<>();
		this.startkolom = new ArrayList<>();
		this.eindkolom = new ArrayList<>();
		this.spelstapel = new ArrayList<>();

	}

	/**
	 * Methode waarin het spel wordt gestart. Bij het starten van het spel worden de
	 * dominotegels aangemaakt, zowel de spelstapel als de start en eindkolom gevuld
	 * en ook een willekeurige spelersvolgorde gegenereerd.
	 */
	public void startSpel() {
		initialiseerDominotegels();
		vulSpelstapel();
		vulStartkolom();
		vulEindkolom();
		List<String> willekeurigeVolgorde = genereerWillekeurigeSpelernamenVolgorde();
		beurtVolgorde.clear();
		for (String naam : willekeurigeVolgorde) {
			Speler speler = vindSpelerOpNaam(naam);
			if (speler != null) {
				beurtVolgorde.add(speler);
			}
		}

	}

	/**
	 * Met deze methode worden de beschikbare gebruikersnamen uit de repository
	 * opgehaald.
	 */
	public void laadBeschikbareGebruikersnamen() {
		beschikbareGebruikersnamen = spelerRepository.haalAlleGebruikersnamenOp();
	}

	/**
	 * Geeft de verschillende opties terug van gebruikersnamen en kleuren waaruit de
	 * spelers kunnen kiezen bij het registreren.
	 *
	 * @return de verschillende opties van gebruikersnamen en kleuren.
	 */
	public String haalBeschikbareOptiesOp() {
		StringBuilder beschikbareOpties = new StringBuilder();
		if (beschikbareGebruikersnamen != null && !beschikbareGebruikersnamen.isEmpty()) {
			beschikbareOpties.append("Beschikbare gebruikersnamen:\n");
			beschikbareGebruikersnamen.forEach(naam -> beschikbareOpties.append(naam).append("\n"));
		} else {
			beschikbareOpties.append("Geen gebruikersnamen beschikbaar.\n");
		}
	
		beschikbareOpties.append("\nBeschikbare kleuren:\n");
		beschikbareKleuren.forEach(kleur -> beschikbareOpties.append(kleur.name()).append("\n"));
	
		return beschikbareOpties.toString();
	}

	/**
	 * Geeft de lijst van spelers terug die het spel spelen.
	 *
	 * @return de lijst van spelers die het spel spelen.
	 */
	public List<Speler> getSpelers() {
		return new ArrayList<>(spelers);
	}

	/**
	 * Methode voor het toevoegen van een nieuwe, ongeregistreerde speler. De
	 * methode kijkt ook eerst of de gebruikersnaam of kleur reeds is gekozen en
	 * gooit de toepasselijke exception moest dit het geval zijn. Als de speler nog
	 * niet bestaat, dan wordt zijn/haar/hun kleur ingesteld, koning aangemaakt en
	 * toegewezen, koninkrijk aangemaakt en toegewezen, wordt de speler toegevoegd
	 * aan de spelerlijst en worden de gekozen gebruikersnaam en kleur uit
	 * desbetreffende lijsten verwijderd zodat deze niet door een andere speler
	 * kunnen worden gekozen.
	 *
	 * @param gebruikersnaam(String): de gebruikersnaam van de speler.
	 * @param kleur(Kleur):           de kleur van de speler.
	 *
	 * @throws GebruikersnaamInGebruikException als de gekozen gebruikersnaam al in
	 *                                          gebruik is.
	 * @throws KleurInGebruikException          als de gekozen kleur al in gebruik
	 *                                          is.
	 * @throws SpelerNietGevondenException      als de gekozen speler niet wordt
	 *                                          gevonden.
	 */
	public void voegSpelerToe(String gebruikersnaam, Kleur kleur) {

		if (!beschikbareGebruikersnamen.contains(gebruikersnaam)) {
			throw new GebruikersnaamInGebruikException();
		}
		if (!beschikbareKleuren.contains(kleur)) {
			throw new KleurInGebruikException();
		}

		Speler speler = spelerRepository.vindSpelerOpGebruikersnaam(gebruikersnaam);
		if (speler == null) {
			throw new SpelerNietGevondenException();
		}
		speler.setKleur(kleur);
		Koning koning = new Koning(speler);
		speler.setKoning(koning);
		Koninkrijk koninkrijk = new Koninkrijk();
		speler.setKoninkrijk(koninkrijk);
		spelers.add(speler);
		beschikbareGebruikersnamen.remove(gebruikersnaam);
		beschikbareKleuren.remove(kleur);
	}

	/**
	 * Methode waarin alle dominotegels worden aangemaakt met hun nummer en hun twee
	 * vakken met elk een aantal kronen en een landschapstype.
	 */
	private void initialiseerDominotegels() {
		 dominotegels = dominotegelRepository.haalAlleDominotegelsOp();

	}

	/**
	 * Methode waarbij de spelstapel wordt gevuld afhankelijk van het aantal
	 * spelers. Zijn er 3 spelers dan komen er 36 dominotegels in de spelstapel,
	 * zijn er 4 dan worden dat er 48. De spelstapel wordt ook geschud.
	 */
	private void vulSpelstapel() {
		Collections.shuffle(dominotegels);
		int aantalTegels = spelers.size() == 3 ? 36 : 48;

		for (int i = 0; i < aantalTegels; i++) {
			spelstapel.add(dominotegels.get(i));
		}
	}

	/**
	 * Methode waarbij de startkolom wordt aangevuld. Per speler die meespeelt wordt
	 * er een dominotegel uit de eindkolom gehaald en in de startkolom gezet. Daarna
	 * worden de dominotegels gesorteerd op nummer.
	 */
	private void vulStartkolom() {
		int aantalSpelers = spelers.size();
		for (int i = 0; i < aantalSpelers; i++) {
			startkolom.add(spelstapel.remove(0));
		}
		startkolom.sort(Comparator.comparing(Dominotegel::getNummer));
	}

	/**
	 * Methode waarbij de eindkolom wordt aangevuld. Voordat er dominotegels in de
	 * eindkolom komen wordt er eerst gekeken of er nog wel tegels in de spelstapel
	 * zitten. Als dit nog het geval is, dan wordt er per speler die meespeelt een
	 * dominotegel uit de spelstapel gehaald en in de eindkolom toegevoegd. Daarna
	 * worden de dominotegels gesorteerd op nummer.
	 *
	 */
	private void vulEindkolom() {
		int aantalSpelers = spelers.size();
		for (int i = 0; i < aantalSpelers; i++) {
			if (!spelstapel.isEmpty()) {
				eindkolom.add(spelstapel.remove(0));
			}
		}
		eindkolom.sort(Comparator.comparing(Dominotegel::getNummer));
	}

	/**
	 * Methode waarbij de dominotegels uit de eindkolom, in de startkolom worden
	 * gestoken.
	 */
	public void promoveerEindkolomTotStartkolom() {
		startkolom.clear();
		startkolom.addAll(eindkolom);
		eindkolom.clear();
		vulEindkolom();
		updateBeurtVolgorde();
	}

	/**
	 * Geeft een willekeurige gebruikersnaam van een speler terug uit de spelers die
	 * het spel aan het spelen zijn.
	 *
	 * @return een willekeurige gebruikersnaam van een speler.
	 */
	public String kiesWillekeurigeSpelernaam() {
		Random random = new Random();
		int index = random.nextInt(spelers.size());
		return spelers.get(index).getGebruikersnaam();
	}

	/**
	 * Geeft een willekeurige volgorde terug van de spelers die het spel zijn aan
	 * het spelen.
	 *
	 * @return een lijstje met de willekeurige spelersvolgorde.
	 */
	public List<String> genereerWillekeurigeSpelernamenVolgorde() {
		List<String> spelernamen = spelers.stream().map(Speler::getGebruikersnaam).collect(Collectors.toList());
		Collections.shuffle(spelernamen);
		return spelernamen;
	}

	/**
	 * Geeft de bovenste dominotegel van de spelstapel terug.
	 *
	 * @return de bovenste dominotegel van de spelstapel.
	 */
	public Dominotegel getBovensteTegelVanSpelstapel() {
		return spelstapel.get(0);

	}

	/**
	 * Geeft de huidige startkolom van het spel terug, in stringvorm.
	 *
	 * @return een stringrepresentatie van de huidige startkolom van het spel.
	 */
	public List<String> getStartkolom() {
		List<String> startkolomWeergave = new ArrayList<>();
		for (int i = 0; i < startkolom.size(); i++) {
			Dominotegel tegel = startkolom.get(i);
			startkolomWeergave.add((i + 1) + ": " + tegel.toString());
		}
		return startkolomWeergave;
	}

	/**
	 * Geeft de huidige eindkolom van het spel terug, in stringvorm.
	 *
	 * @return een stringrepresentatie van de huidige eindkolom van het spel.
	 */
	public List<String> getEindkolom() {
		List<String> eindkolomWeergave = new ArrayList<>();
		for (int i = 0; i < eindkolom.size(); i++) {
			Dominotegel tegel = eindkolom.get(i);
			eindkolomWeergave.add((i + 1) + ": " + tegel.toString());
		}
		return eindkolomWeergave;
	}

	/**
	 * Geeft een lijst van de dominotegels uit de huidige startkolom van het spel
	 * terug.
	 *
	 * @return de dominotegels die in de huidige startkolom liggen.
	 */
	public List<Dominotegel> getStartkolomTegels() {
		return new ArrayList<>(startkolom);
	}

	/**
	 * Geeft een lijst van de dominotegels uit de huidige eindkolom van het spel
	 * terug.
	 *
	 * @return de dominotegels die in de huidige eindkolom liggen.
	 */
	public List<Dominotegel> getEindkolomTegels() {
		return new ArrayList<>(eindkolom);
	}

	/**
	 * Geeft de bestandsnaam van de afbeelding van de voorkant of achterkant van een
	 * dominotegel.
	 *
	 * @param tegel(Dominotegel):  De dominotegel waarvan de afbeelding wordt
	 *                             opgevraagd.
	 * @param isVoorkant(boolean): Geeft aan of de voorkant (true) of de achterkant
	 *                             (false) van de tegel wordt opgevraagd.
	 * @return De bestandsnaam van de afbeelding.
	 */
	public String getAfbeeldingNaam(Dominotegel tegel, boolean isVoorkant) {
		String kant = isVoorkant ? "voorkant" : "achterkant";
		int nummer = tegel.getNummer();
		return String.format("tegel_%02d_%s.png", nummer, kant);
	}

	/**
	 * Deze methode kiest een dominotegel uit de startkolom en plaatst de koning van
	 * de speler erop.
	 *
	 * @param speler(Speler):  De speler die de koning plaatst.
	 * @param tegelIndex(int): De index van de gekozen dominotegel in de startkolom.
	 * @throws IllegalArgumentException Als de opgegeven tegelindex ongeldig is.
	 * @throws TegelBezetException      Als de gekozen tegel al een koning heeft.
	 */
	public void kiesEnPlaatsKoningInStartKolom(Speler speler, int tegelIndex) {
		if (tegelIndex < 0 || tegelIndex >= startkolom.size()) {
			throw new IllegalArgumentException("Kies een juiste tegelindex");
		}
		Dominotegel gekozenTegel = startkolom.get(tegelIndex);

		if (gekozenTegel.heeftKoning()) {
			throw new TegelBezetException();
		}
		gekozenTegel.setKoning(speler.getKoning());
	}

	/**
	 * Deze methode kiest een dominotegel uit de eindkolom en plaatst de koning van
	 * de speler erop.
	 *
	 * @param speler(Speler):  De speler die de koning plaatst.
	 * @param tegelIndex(int): De index van de gekozen dominotegel in de eindkolom.
	 * @throws IllegalArgumentException Als de opgegeven tegelindex ongeldig is.
	 * @throws TegelBezetException      Als de gekozen tegel al een koning heeft.
	 */
	public void kiesEnPlaatsKoningInEindkolom(Speler speler, int tegelIndex) {
		if (tegelIndex < 0 || tegelIndex >= eindkolom.size()) {
			throw new IllegalArgumentException("Kies een juiste tegelindex");
		}
		Dominotegel gekozenTegel = eindkolom.get(tegelIndex);

		if (gekozenTegel.heeftKoning()) {
			throw new TegelBezetException();
		}
		gekozenTegel.setKoning(speler.getKoning());
	}

	/**
	 * Deze methode plaatst een dominotegel in het koninkrijk van de speler op de
	 * opgegeven coördinaten en richting.
	 *
	 * @param speler(Speler):           De speler die de dominotegel plaatst.
	 * @param startX(int):              De x-coördinaat van het startvak van de
	 *                                  dominotegel.
	 * @param startY(int):              De y-coördinaat van het startvak van de
	 *                                  dominotegel.
	 * @param richting(Richting):       De richting waarin de dominotegel geplaatst
	 *                                  wordt.
	 * @param dominotegel(Dominotegel): De dominotegel die geplaatst wordt.
	 * @return True als de dominotegel succesvol geplaatst is, anders false.
	 */
	public boolean plaatsDominotegel(Speler speler, int startX, int startY, Richting richting,
			Dominotegel dominotegel) {
		try {
			Koninkrijk koninkrijk = speler.getKoninkrijk();
			int eindX = startX + richting.getRichtingX();
			int eindY = startY + richting.getRichtingY();

			if (!isVakVrij(koninkrijk, startX, startY) || !isVakVrij(koninkrijk, eindX, eindY)) {
				throw new OngeldigVakException("Dit vak is niet vrij");
			}

			if (zouMaxGrootteOverschrijden(startX, startY, eindX, eindY)) {
				throw new OngeldigVakException("Koninkrijk overschrijdt maximale grootte.");
			}

			if (!checkBeideVakkenGrens(koninkrijk, startX, startY, eindX, eindY, dominotegel)) {
				throw new OngeldigVakException("De tegel moet een juiste grens hebben");
			}

			koninkrijk.getGrid()[startX][startY] = dominotegel.getVak1();
			koninkrijk.getGrid()[eindX][eindY] = dominotegel.getVak2();
			updateGrenzen(startX, startY);
			updateGrenzen(eindX, eindY);
			return true;
		} catch (OngeldigVakException e) {
			return false;
		}
	}

	/**
	 * Deze methode controleert of een vak in het koninkrijk vrij is (geen landschap
	 * heeft).
	 *
	 * @param koninkrijk(Koninkrijk): Het koninkrijk waarin het vak zich bevindt.
	 * @param x(int):                 De x-coördinaat van het vak.
	 * @param y(int):                 De y-coördinaat van het vak.
	 * @return True als het vak vrij is, anders false.
	 */
	private boolean isVakVrij(Koninkrijk koninkrijk, int x, int y) {
		Vak[][] grid = koninkrijk.getGrid();
		return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length
				&& grid[x][y].getLandschapstype() == Landschapstype.LEEG;
	}

	/**
	 * Deze methode controleert of beide vakken van een dominotegel grenzen hebben
	 * die compatibel zijn met aangrenzende vakken.
	 *
	 * @param koninkrijk(Koninkrijk):   Het koninkrijk waarin de vakken zich
	 *                                  bevinden.
	 * @param startX(int):              De x-coördinaat van het eerste vak.
	 * @param startY(int):              De y-coördinaat van het eerste vak.
	 * @param eindX(int):               De x-coördinaat van het tweede vak.
	 * @param eindY(int):               De y-coördinaat van het tweede vak.
	 * @param dominotegel(Dominotegel): De dominotegel waarvan de vakken
	 *                                  gecontroleerd worden.
	 * @return True als beide vakken grenzen hebben die compatibel zijn met
	 *         aangrenzende vakken van hetzelfde of het startlandschapstype, anders
	 *         False.
	 */
	private boolean checkBeideVakkenGrens(Koninkrijk koninkrijk, int startX, int startY, int eindX, int eindY,
			Dominotegel dominotegel) {
		boolean result1 = isAangrenzendVakCompatibel(koninkrijk, startX, startY,
				dominotegel.getVak1().getLandschapstype());
		boolean result2 = isAangrenzendVakCompatibel(koninkrijk, eindX, eindY,
				dominotegel.getVak2().getLandschapstype());
		return result1 || result2;
	}

	/**
	 * Deze methode controleert of een vak compatibel is met de aangrenzende vakken.
	 *
	 * @param koninkrijk(Koninkrijk): Het koninkrijk waarin het vak zich bevindt.
	 * @param x(int):                 De x-coördinaat van het vak.
	 * @param y(int):                 De y-coördinaat van het vak.
	 * @param type(Landschapstype):   Het landschapstype van het vak.
	 * @return True als het vak compatibel is met aangrenzende vakken, anders false.
	 */
	private boolean isAangrenzendVakCompatibel(Koninkrijk koninkrijk, int x, int y, Landschapstype type) {
		Vak[][] grid = koninkrijk.getGrid();
		int GROOTTE = grid.length;

		int[][] richtingen = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		for (int[] richting : richtingen) {
			int checkX = x + richting[0];
			int checkY = y + richting[1];

			if (checkX >= 0 && checkX < GROOTTE && checkY >= 0 && checkY < GROOTTE) {
				Landschapstype naastType = grid[checkX][checkY].getLandschapstype();

				if (naastType == type || naastType == Landschapstype.START) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Deze methode berekent en update de prestige punten van alle spelers op basis
	 * van hun koninkrijken.
	 * @param spelers(List<Speler>): Een lijst met spelerobjecten waarvan de statistieken moeten worden geüpdatet
	 */
	public void berekenEnUpdateSpelerStatistieken(List<Speler> spelers) {
	    Speler winnaar = null;
	    int hoogsteScore = 0;

	   
	    for (Speler speler : spelers) {
	        int score = berekenScore(speler.getKoninkrijk());
	        speler.setPrestigePunten(score);
	        if (score > hoogsteScore) {
	            hoogsteScore = score;
	            winnaar = speler;
	        } else if (score == hoogsteScore) {
	            winnaar = null; 
	        }
	    }


	    for (Speler speler : spelers) {
	        speler.setAantalGespeeld(speler.getAantalGespeeld() + 1);
	    }

	
	    if (winnaar != null) {
	        winnaar.setAantalGewonnen(winnaar.getAantalGewonnen() + 1);
	    }

	   
	    updateSpelerGegevensInDatabase();
	}
	/**
	 * Roept de methode in de spelerRepository aan om de gegevens van de spelers te updaten
	 */
	private void updateSpelerGegevensInDatabase() {
	    
	    for (Speler speler : spelers) {
	        spelerRepository.updateSpelerGegevens(speler);
	    }
	}

	/**
	 * Berekent de score van het koninkrijk.
	 *
	 * @param koninkrijk(Koninkrijk): Het koninkrijk waarvan de score berekend
	 *                                wordt.
	 * @return De totale score van het koninkrijk.
	 */
	public int berekenScore(Koninkrijk koninkrijk) {
		Vak[][] grid = koninkrijk.getGrid();
		// grid[i][i].getLandschapstype()
		int score = 0;

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {

				if (!grid[i][j].isBezocht() && grid[i][j].getLandschapstype() != Landschapstype.LEEG) {
					int domeinScore = verkenDomein(grid, i, j, grid[i][j].getLandschapstype());
					score += domeinScore;
					resetBezocht(grid[i][j]);
				}
			}
		}
		return score;
	}

	/**
	 * Deze meethode verkent het domein van het opgegeven landschapstype in het
	 * grid, beginnend vanaf de positie (x, y).
	 *
	 * @param grid(Vak[][])         Dettweedimensionale array die het speelveld
	 *                              voorstelt.
	 * @param x(int):               De x-coördinaat van het startpunt.
	 * @param y(int):               De y-coördinaat van het startpunt.
	 * @param type(Landschapstype): Het landschapstype dat verkend wordt.
	 * @return De score van het verkende domein.
	 */
	private int verkenDomein(Vak[][] grid, int x, int y, Landschapstype type) {
		if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length || grid[x][y].isBezocht()
				|| grid[x][y].getLandschapstype() != type) {
			return 0;
		}
		grid[x][y].setBezocht(true);
		int domeinScore = grid[x][y].getKronen();

		int[][] richtingen = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		for (int[] richting : richtingen) {
			domeinScore += verkenDomein(grid, x + richting[0], y + richting[1], type);
		}
		grid[x][y].setBezocht(false);
		return domeinScore;
	}

	/**
	 * Deze methode stelt de bezochtheidsstatus van het opgegeven vak in op false.
	 *
	 * @param vak(Vak): Het vak waarvan de bezochtheidsstatus gereset wordt.
	 */
	private void resetBezocht(Vak vak) {
		vak.setBezocht(false);
	}

	/**
	 * Geeft het aantal spelers die het spel spelen terug.
	 *
	 * @return het aantal spelers die het spel spelen.
	 */
	public int getAantalSpelers() {
		return spelers.size();
	}

	/**
	 * Geeft een speler terug op basis van de gebruikersnaam. Geeft oftewel de
	 * speler(Speler) terug, of null.
	 *
	 * @param gebruikersnaam(String): de gebruikersnaam van de speler die we zoeken.
	 * @return de speler, of null als er geen speler is gevonden met de opgegeven
	 *         gebruikersnaam.
	 */
	public Speler vindSpelerOpNaam(String gebruikersnaam) {
		for (Speler speler : spelers) {
			if (speler.getGebruikersnaam().equals(gebruikersnaam)) {
				return speler;
			}
		}
		return null;
	}

	/**
	 * Geeft een overzicht van het spel weer, inclusief de spelers met hun kleur en
	 * prestige punten, en de tegels in de start- en eindkolommen.
	 *
	 * @return Een String met het overzicht van het spel.
	 */
	public String toonOverzicht() {
	//	berekenEnUpdateSpelerStatistieken(); // Eerst de scores berekenen en bijwerken
		StringBuilder sb = new StringBuilder("Spel Overzicht:\n");
		for (Speler speler : spelers) {
			sb.append(String.format("Speler: %s, Kleur: %s, PrestigePunten: %d\n", speler.getGebruikersnaam(),
					speler.getKleur().name(), speler.getPrestigePunten()));
			sb.append("Koninkrijk:\n").append(speler.getKoninkrijk().toString()).append("\n");
		}
		sb.append("Startkolom:\n");
		List<String> startkolomWeergave = getStartkolom();
		for (String tegelWeergave : startkolomWeergave) {
			sb.append(tegelWeergave).append("\n");
		}
		sb.append("Eindkolom:\n");
		List<String> eindkolomWeergave = getEindkolom();
		for (String tegelWeergave : eindkolomWeergave) {
			sb.append(tegelWeergave).append("\n");
		}
		return sb.toString();
	}

	/**
	 * Deze methode geeft de speler terug die aan de beurt is.
	 * 
	 * @return de speler die aan de beurt is.
	 */
	public Speler wieIsAanDeBeurt() {
		if (!beurtVolgorde.isEmpty()) {
			return beurtVolgorde.get(0); //
		}
		return null;
	}

	/**
	 * Deze methode gaat naar de volgende beurt door de index van de huidige speler
	 * te verplaatsen naar de volgende speler in de lijst van spelers. Als de
	 * huidige speler de laatste in de lijst is, gaat de beurt terug naar de eerste
	 * speler in de lijst.
	 */
	public void volgendeBeurt() {
		huidigeSpelerIndex = (huidigeSpelerIndex + 1) % spelers.size();
	}

	/**
	 * Deze methode werkt de beurtvolgorde bij op basis van de tegels in de
	 * startkolom.
	 *
	 * De methode maakt een nieuwe lijst aan voor de bijgewerkte volgorde en voegt
	 * spelers toe op basis van hun gekozen dominotegel in de startkolom. Vervolgens
	 * wordt de huidige beurtvolgorde leeggemaakt en bijgewerkt met de nieuwe
	 * volgorde.
	 */
	public void updateBeurtVolgorde() {
		List<Speler> nieuweVolgorde = new ArrayList<>();

		for (Dominotegel tegel : startkolom) {
			if (tegel.getKoning() != null) {
				nieuweVolgorde.add(tegel.getKoning().getSpeler());

			} else {

			}
		}
		beurtVolgorde.clear();
		beurtVolgorde.addAll(nieuweVolgorde);
	}

	/**
	 * Geeft een lijst terug van gebruikersnamen van spelers in de huidige
	 * beurtvolgorde.
	 *
	 * @return Een lijst van gebruikersnamen van spelers in de beurtvolgorde.
	 */
	public List<String> getSpelersInBeurtVolgorde() {

		return beurtVolgorde.stream().map(Speler::getGebruikersnaam).collect(Collectors.toList());
	}

	/**
	 * Methode die de dominotegel zoekt en retourneert uit de startkolom van de
	 * opgegeven speler.
	 *
	 * @param speler(Speler): De speler waarvan de dominotegel gezocht wordt.
	 * @return De dominotegel van de opgegeven speler.
	 * @throws IllegalStateException Als er geen tegel gevonden wordt voor de
	 *                               opgegeven speler.
	 */
	public Dominotegel vindTegelVanSpeler(Speler speler) {
		for (Dominotegel tegel : startkolom) {
			if (tegel.getKoning() != null && tegel.getKoning().getSpeler().equals(speler)) {
				return tegel;
			}
		}
		throw new IllegalStateException("Geen tegel gevonden voor de huidige speler.");
	}

	/**
	 * Methode die de beurtvolgorde initialiseert op basis van de startkolom. Het
	 * leegt de huidige beurtvolgorde en voegt vervolgens spelers toe op basis van
	 * hun gekozen dominotegel.
	 */
	public void initialiseerBeurtVolgorde() {
		beurtVolgorde.clear();

		for (Dominotegel tegel : startkolom) {
			if (tegel.getKoning() != null) {
				beurtVolgorde.add(tegel.getKoning().getSpeler());
			}
		}
	}

	/**
	 * Geeft aan of het spel al dan niet gedaan is, op basis van of de spelstapel
	 * volledig leeg is, en of alle dominotegels uit de eindkolom zijn.
	 *
	 * @return true als aan de voorwaarden wordt voordaan, false als dat niet het
	 *         geval is.
	 */
	public boolean isSpelGedaan() {
		return spelstapel.isEmpty() && eindkolom.isEmpty();
	}

	/**
	 * Deze methode berekent de score van het opgegeven koninkrijk per landschapstype, en geeft deze verschillende scores terug
	 * in een lijst(List<String).
	 * @param koninkrijk(Koninkrijk): Het koninkrijk waarvan de punten moeten worden berekend.
	 * @return een lijst van Strings met de scores per landschapstypes.
	 */
	public List<String> berekenScorePerLandschapstype(Koninkrijk koninkrijk) {
		Vak[][] grid = koninkrijk.getGrid();
		int[] scores = new int[Landschapstype.values().length];

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				Vak vak = grid[i][j];
				if (!vak.isBezocht() && vak.getLandschapstype() != Landschapstype.LEEG
						&& vak.getLandschapstype() != Landschapstype.START) {

					int domeinScore = verkenDomein(grid, i, j, vak.getLandschapstype());
					scores[vak.getLandschapstype().ordinal()] += domeinScore;
					resetBezocht(grid[i][j]);
				}
			}
		}

		List<String> scoreStrings = new ArrayList<>();
		for (Landschapstype type : Landschapstype.values()) {
			if (type != Landschapstype.LEEG && type != Landschapstype.START) {
				scoreStrings.add(type.toString() + ": " + scores[type.ordinal()] + " punten");
			}
		}

		return scoreStrings;
	}

}