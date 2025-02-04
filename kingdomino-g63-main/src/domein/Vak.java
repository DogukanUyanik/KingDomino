package domein;

import utils.Landschapstype;

/**
 * Klasse voor een vak.
 * Een vak bestaat uit een landschapstype(Landschapstype), het aantal kronen(int) erop en of het reeds is bezocht(boolean).
 */
public class Vak {
	private Landschapstype landschapstype;
	private int kronen;
	private boolean bezocht;

	/**
	 * Constructor:
     * Maakt een nieuw vak met het opgegeven landschapstype en aantal kronen
     * Stelt ook in dat het vak nog niet bezocht is.
     * 
     * @param landschapstype(Landschapstype): het landschapstype van het vak
     * @param kronen(int): het aantal kronen op het vak
     */
	public Vak(Landschapstype landschapstype, int kronen) {
		this.landschapstype = landschapstype;
		this.kronen = kronen;
		this.bezocht = false;
	}
	
	 /**
     * Haalt het landschapstype van het vak op.
     *
     * @return het landschapstype van het vak
     */
	public Landschapstype getLandschapstype() {
		return landschapstype;
	}

	/**
     * Stelt het landschapstype van het vak in.
     *
     * @param landschapstype(Landschapstype): het landschapstype van het vak
     */
	public void setLandschapstype(Landschapstype landschapstype) {
		this.landschapstype = landschapstype;
	}

	/**
     * Haalt het aantal kronen op het vak op.
     *
     * @return het aantal kronen op het vak
     */
	public int getKronen() {
		return kronen;
	}

	/**
     * Stelt het aantal kronen op het vak in.
     *
     * @param kronen(int): het aantal kronen op het vak
     */
	public void setKronen(int kronen) {
		this.kronen = kronen;
	}
	
	/**
     * Controleert of het vak bezocht is.
     *
     * @return true als het vak bezocht is, anders false
     */
    public boolean isBezocht() {
        return bezocht;
    }
    
    /**
     * Stelt in of het vak bezocht is.
     *
     * @param bezocht(boolean): true als het vak bezocht is, anders false
     */
    public void setBezocht(boolean bezocht) {
        this.bezocht = bezocht;
    }

    /**
     * Geeft het vak terug, voorgesteld als string.
     *
     * @return een string representatie van het vak
     */
	@Override
	public String toString() {
		return landschapstype + " " + kronen + " kronen";
	}
}
