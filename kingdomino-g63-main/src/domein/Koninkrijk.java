package domein;

import utils.Landschapstype;

/**
 * Klasse voor een koninkrijk van een speler.
 * Het koninkrijk bestaat uit een grid van vakken.
 */
public class Koninkrijk {
    private Vak[][] grid ;
	
	 /**
     * Constructor die een nieuw koninkrijk maakt met een leeg grid van vakken.
     * Het startvak wordt op de middelste positie geplaatst.
     */
	public Koninkrijk() {
	    this.grid = new Vak[5][5];
	    for (int i = 0; i < grid.length; i++) {
	        for (int j = 0; j < grid[i].length; j++) {
	            grid[i][j] = new Vak(Landschapstype.LEEG, 0);
	        }
	    }
	    grid[2][2] = new Vak(Landschapstype.START, 0);
	}

	/**
     * Geeft het grid van vakken van het koninkrijk terug.
     *
     * @return het grid van vakken van het koninkrijk
     */
    public Vak[][] getGrid() {
        return grid;
    }

    /**
     * Geeft een koninkrijk terug als string, waarbij elk vak wordt weergegeven 
     * als "[ ]" als het leeg is en 
     * als "[<eerste letter landschapstype><aantal kronen>]" als het gevuld is.
     *
     * @return een string representatie van het koninkrijk
     */
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        for (Vak[] element : grid) {
//            for (int j = 0; j < element.length; j++) {
//                if (element[j].getLandschapstype() == Landschapstype.LEEG) {
//                    builder.append("[ ]"); 
//                } else {
//                    builder.append("[")
//                           .append(element[j].getLandschapstype().name().substring(0, 1))
//                           .append(element[j].getKronen())
//                           .append("]");
//                }
//                if (j < element.length - 1) {
//                    builder.append(" ");
//                }
//            }
//            builder.append("\n");
//        }
//        return builder.toString();
//    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                builder.append("   ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}