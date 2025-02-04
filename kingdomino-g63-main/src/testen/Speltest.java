package testen;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import domein.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import exceptions.TegelBezetException;

public class Speltest {

	private Spel spel;

	@Test
	void kiesEnPlaatsKoningInStartKolom_correcteGegevens_voertUit() {
		Speler speler = new Speler("Dogukan");
		spel.voegSpelerToe("Dogukan", utils.Kleur.BLAUW);
		spel.startSpel();

		assertDoesNotThrow(() -> spel.kiesEnPlaatsKoningInStartKolom(speler, 0));

	}

	@Test
	void kiesEnPlaatsKoningInStartKolom_fouteIndex_exception() {
		Speler speler = new Speler("Milan");
		spel.voegSpelerToe("Milan", utils.Kleur.GEEL);
		spel.startSpel();

		assertThrows(IllegalArgumentException.class, () -> spel.kiesEnPlaatsKoningInStartKolom(speler, -1));
		assertThrows(IllegalArgumentException.class, () -> spel.kiesEnPlaatsKoningInStartKolom(speler, 500));
	}

	@Test
	void kiesEnPlaatsKoningInStartKolom_tegelInGebruik_exception() {
		Speler speler1 = new Speler("Thimo");
		spel.voegSpelerToe("Thimo", utils.Kleur.GEEL);
		spel.startSpel();
		spel.kiesEnPlaatsKoningInStartKolom(speler1, 5);

		Speler speler2 = new Speler("Jens");
		spel.voegSpelerToe("Jens", utils.Kleur.GEEL);

		assertThrows(TegelBezetException.class, () -> spel.kiesEnPlaatsKoningInStartKolom(speler2, 0));

	}

	@Test
	void kiesEnPlaatsKoningInEindKolom_correcteGegevens_voertUit() {
		Speler speler = new Speler("Dogukan");
		spel.voegSpelerToe("Dogukan", utils.Kleur.GROEN);
		spel.startSpel();

		assertDoesNotThrow(() -> spel.kiesEnPlaatsKoningInEindkolom(speler, 0));

	}

	@Test
	void kiesEnPlaatsKoningInEindKolom_fouteIndex_exception() {
		Speler speler = new Speler("Milan");
		spel.voegSpelerToe("Milan", utils.Kleur.GEEL);
		spel.startSpel();

		assertThrows(IllegalArgumentException.class, () -> spel.kiesEnPlaatsKoningInEindkolom(speler, -1));
		assertThrows(IllegalArgumentException.class, () -> spel.kiesEnPlaatsKoningInEindkolom(speler, 500));
	}

	@Test
	void kiesEnPlaatsKoningInEindKolom_tegelInGebruik_exception() {
		Speler speler1 = new Speler("Thimo");
		spel.voegSpelerToe("Thimo", utils.Kleur.BLAUW);
		spel.startSpel();
		spel.kiesEnPlaatsKoningInStartKolom(speler1, 5);

		Speler speler2 = new Speler("Jens");
		spel.voegSpelerToe("Jens", utils.Kleur.GEEL);

		assertThrows(TegelBezetException.class, () -> spel.kiesEnPlaatsKoningInEindkolom(speler2, 0));

	}

	@Test
	void vindTegelVanSpeler_geenTegelGevonden_werptException() {
		Koning k = new Koning(null);
		Dominotegel t = new Dominotegel();

		assertThrows(IllegalStateException.class, () -> t.getKoning());

	}





	@Test
	public void testBerekenScore() {
		Speler speler = new Speler("Bilal");

		spel.voegSpelerToe("Bilal", utils.Kleur.GEEL);
		spel.startSpel();
		spel.kiesEnPlaatsKoningInStartKolom(speler, 5);

		int score1 = spel.berekenScore(speler.getKoninkrijk());
		assertEquals(10, score1, "Expected score for kingdom 1: 10");


		// Test case 2: Create another sample kingdom with a grid
		Speler speler2 = new Speler("Thimo");
		spel.startSpel();
		spel.kiesEnPlaatsKoningInStartKolom(speler, 5);
		int score2 = spel.berekenScore(speler2.getKoninkrijk());
		assertEquals(9, score2, "Expected score for kingdom 2: 9");
	}

	// Helper methods to create sample kingdoms (similar to previous example)

	@Test
	public void testBerekenScoreWithEmptyKingdom() {
		// Test with an empty kingdom (all fields are LEEG)
		Speler speler = new Speler("Bilal");
		spel.voegSpelerToe("Bilal", utils.Kleur.GEEL);

		int score = spel.berekenScore(speler.getKoninkrijk());
		assertEquals(0, score, "Expected score for empty kingdom: 0");
	}

}
