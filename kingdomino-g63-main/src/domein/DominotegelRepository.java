package domein;

import java.util.List;
import persistentie.DominotegelMapper;

/**
 *  Een repository klasse voor dominotegels.
 *  
 *  Deze klasse is verantwoordelijk voor het doorgeven van de dominotegels en 
 *  heeft een mapper object om te kunnen communiceren met de databank.
 */
public class DominotegelRepository {

    private final DominotegelMapper dominotegelMapper;

	/**
     * Constructor: maakt een nieuwe DominotegelRepository aan en instantieert het mapper object.
     */
    public DominotegelRepository() {
        this.dominotegelMapper = new DominotegelMapper();
    }

    /**
     * Deze methode geeft alle dominotegels terug in een lijst. 
     * Deze lijst wordt aangemaakt in de DominotegelMapper, deze methode geeft het enkel door.
     * @return de lijst van Dominotegels uit de databank.
     */
    public List<Dominotegel> haalAlleDominotegelsOp() {
        return dominotegelMapper.haalAlleDominotegelsOp();
    }


}
    

