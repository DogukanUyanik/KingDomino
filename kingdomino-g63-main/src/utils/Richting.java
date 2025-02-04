
package utils;

public enum Richting {
	BOVEN(-1, 0), ONDER(1, 0), LINKS(0, -1), RECHTS(0, 1);

	private final int richtingX;
	private final int richtingY;

	Richting(int richtingX, int richtingY) {
		this.richtingX = richtingX;
		this.richtingY = richtingY;
	}

	public int getRichtingX() {
		return richtingX;
	}

	public int getRichtingY() {
		return richtingY;
	}
}
