package domein;

/**
 * Klasse voor een koning.
 * Een koning heeft een speler(Speler) bij wie hij hoort en een dominotegel(Dominotegel) waar hij op staat.
 */
public class Koning {
	private Speler speler;
	private Dominotegel dominotegel;

	/**
	 * Constructor: maakt een nieuwe koning aan met de speler bij wie hij hoort.
	 * @param speler(Speler): de speler bij wie de koning behoort.
	 */
	public Koning(Speler speler) {
		this.speler = speler;
	}

	/**
	 * Haalt de speler op bij wie de koning hoort.
	 * 
	 * @return de speler bij wie de koning hoort.
	 */
	public Speler getSpeler() {
		return speler;
	}

	/**
	 * Stelt de speler in bij wie de koning hoort.
	 * 
	 * @param speler(Speler): de speler bij wie de koning hoort.
	 */
	public void setSpeler(Speler speler) {
		this.speler = speler;
	}

	/**
	 * Haalt de dominotegel op waar de koning op staat.
	 * 
	 * @return de dominotegel waar de koning op staat.
	 */
	public Dominotegel getDominotegel() {
		return dominotegel;
	}

	/**
	 * Stelt de dominotegel in waar de koning op staat.
	 * 
	 * @param dominotegel(Dominotegel): de dominotegel waar de koning op staat.
	 */
	public void setDominotegel(Dominotegel dominotegel) {
		this.dominotegel = dominotegel;
	}
}
