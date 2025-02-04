package domein;

import java.util.List;

import exceptions.GebruikersnaamInGebruikException;
import persistentie.SpelerMapper;

/**
 *  Een repository klasse voor spelers.
 *  
 *  Deze klasse is verantwoordelijk voor het beheren van speler gegevens, en 
 *  heeft een mapper object om te kunnen communiceren met de databank.
 */
public class SpelerRepository {

	private final SpelerMapper mapper;

	/**
     * Constructor: maakt een nieuwe SpelerRepository en instantieert het mapper object.
     */
	
	public SpelerRepository() {
		mapper = new SpelerMapper();
	}

	/**
     * Voegt een speler toe aan de repository, om dan met de mapper 
     * aan de databank toegevoegd te kunnen worden.
     *
     * @param speler(Speler): de speler die moet worden toegevoegd
     * @throws GebruikersnaamInGebruikException als de gebruikersnaam van de speler al in gebruik is
     */
	public void voegToe(Speler speler) {
		if (bestaatSpeler(speler.getGebruikersnaam())) {
			throw new GebruikersnaamInGebruikException();
		}
		mapper.voegToe(speler);
	}

	/**
	 * Controleert of de speler wel bestaat, op basis van de gebruikersnaam, en of deze
	 * gelijk is aan null of niet. Op basis daarvan wordt een boolean geretourneerd.
	 * 
	 * @param gebruikersnaam(String): de gebruikersnaam van de speler die wordt gecontroleerd.
	 * @return true als de speler bestaat, false als dat niet zo is.
	 */
	private boolean bestaatSpeler(String gebruikersnaam) {
		return mapper.geefSpeler(gebruikersnaam) != null;
	}

	/**
     * Haalt alle gebruikersnamen op van spelers via de mapper.
     *
     * @return een lijst van alle gebruikersnamen van spelers, in string-vorm.
     */
	public List<String> haalAlleGebruikersnamenOp() {
		return mapper.haalAlleGebruikersnamenOp();
	}

	/**
    * Vindt een speler op basis van de gebruikersnaam.
    *
    * @param gebruikersnaam(String): de gebruikersnaam van de speler die moet worden gevonden.
    * @return de gevonden speler, of null als de speler niet gevonden is.
    */
	public Speler vindSpelerOpGebruikersnaam(String gebruikersnaam) {
		return mapper.geefSpeler(gebruikersnaam);
	}
	
	/**
	    * Update de gegevens van een speler door de updateSpelerGegevens in de mapper aan te roepen
	    *
	    * @param speler(Speler): het spelerobject van de speler die moet worden geüpdatet
	    * 
	    */
	public void updateSpelerGegevens(Speler speler) {
	    mapper.updateSpelerGegevens(speler);
	}
}
