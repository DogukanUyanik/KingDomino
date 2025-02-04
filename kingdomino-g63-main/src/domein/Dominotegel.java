package domein;

import utils.Landschapstype;

/**
 * Klasse voor een dominotegel. Een dominotegel heeft een nummer(int), een koning(Koning) door wie hij wordt gekozen
 * en bestaat uit 2 vakken(Vak[]);
 */
public class Dominotegel {
	private int nummer;
	private Vak[] vakken = new Vak[2];
	private Koning koning;

	/**
	 * Constructor om een dominotegel aan te maken met een nummer(int) en 2 vakken(Vak[]), vak1 en vak2
	 * 
	 * @param nummer: het nummer van de dominotegel
	 * @param vak1: Het eerste vak van de dominotegel
	 * @param vak2: Het tweede vak van de dominotegel
	 */
	public Dominotegel(int nummer, Vak vak1, Vak vak2) {
		this.nummer = nummer;
		this.vakken[0] = vak1;
		this.vakken[1] = vak2;
	}

	/**
	 * Default constructor om een dominotegel aan te maken
	 */
	public Dominotegel() {
		this(0, null, null);
	}

	/**
	 * Haalt het nummer van de dominotegel op
	 * 
	 * @return het nummer van de dominotegel.
	 */
	public int getNummer() {
		return nummer;
	}

	/**
	 * Haalt beide vakken op van de dominotegel.
	 * 
	 * @return allebei de vakken van de dominotegel.
	 */
	public Vak[] getVakken() {
		return vakken;
	}

	/**
	 * Haalt enkel het eerste vak van de dominotegel op.
	 * 
	 * @return het eerste vak van de dominotegel.
	 */
	public Vak getVak1() {
		return vakken[0];
	}

	/**
	 * Haalt enkel het tweede vak van de dominotegel op.
	 * 
	 * @return het tweede vak van de dominotegel.
	 */
	public Vak getVak2() {
		return vakken[1];
	}
	
	/**
	 * Geeft de dominotegel terug, als string.
	 * 
	 * @return een stringrepresentatie van de dominotegel, met eventuele kleur bij als de tegel reeds is gekozen.
	 */
	@Override
	public String toString() {
		String basis = String.format("Tegel %d - [%s] | [%s]", nummer, vakken[0], vakken[1]);
		if (heeftKoning()) {
			Koning koning = getKoning();
			String koningInfo = "Koning: " + koning.getSpeler().getKleur().name();
			return basis + ", " + koningInfo;
		} else {
			return basis + ", Geen koning";
		}
	}
	
	/**
	 * Haalt de koning op die op de dominotegel staat.
	 * 
	 * @return de koning die op de dominotegel staat.
	 */
	public Koning getKoning() {
		return this.koning;
	}

	/**
	 * Stelt de koning die op de dominotegel staat in.
	 * 
	 * @param koning(Koning): de koning die op de dominotegel staat.
	 */
	public void setKoning(Koning koning) {
		this.koning = koning;
	}

	/**
	 * Geeft een boolean terug, gebaseerd op het feit of een dominotegel al een koning heeft of niet.
	 * 
	 * @return true, als er reeds een koning de dominotegel heeft gekozen, false als dit nog niet het geval is.
	 */
	public boolean heeftKoning() {
		return this.koning != null;
	}

	/**
	 * Geeft het landschapstype terug van het linker/eerste vak van de dominotegel.
	 * 
	 * @return het landschapstype van het linker/eerste vak van de dominotegel.
	 */
	public Landschapstype getLandschapstypeLinks() {
        return vakken[0].getLandschapstype();
	}

	/**
	 * Geeft het landschapstype terug van het rechter/tweede vak van de dominotegel.
	 * 
	 * @return het landschapstype van het rechter/tweede vak van de dominotegel.
	 */
    public Landschapstype getLandschapstypeRechts() {
        return vakken[1].getLandschapstype();
    }
}