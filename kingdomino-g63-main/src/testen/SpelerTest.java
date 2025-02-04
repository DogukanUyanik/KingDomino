package testen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import domein.Speler;
import exceptions.OngeldigGeboortejaarException;
import exceptions.OngeldigeGebruikersnaamException;

class SpelerTest {

	@Test
	void maakSpeler_metAlleGegevens_correctObjectAangemaakt() {
		Speler speler = new Speler("avatar", 2003, 4, 25);
		Assertions.assertAll(() -> Assertions.assertEquals("avatar", speler.getGebruikersnaam()),
				() -> Assertions.assertEquals(2003, speler.getGeboortejaar()),
				() -> Assertions.assertEquals(4, speler.getAantalGewonnen()),
				() -> Assertions.assertEquals(25, speler.getAantalGespeeld()));
	}

	@Test
	void maakSpeler_metCorrecteGebruikersnaamEnGeboortejaar_correctObjectAangemaakt() {
		Speler speler = new Speler("avatar", 2003);
		Assertions.assertAll(() -> Assertions.assertEquals("avatar", speler.getGebruikersnaam()),
				() -> Assertions.assertEquals(2003, speler.getGeboortejaar()),
				() -> Assertions.assertEquals(0, speler.getAantalGewonnen()),
				() -> Assertions.assertEquals(0, speler.getAantalGespeeld()));
	}

	@Test
	void maakSpeler_metTeKorteGebruikersnaam_gooitException() {
		Assertions.assertThrows(OngeldigeGebruikersnaamException.class, () -> new Speler("test", 2003));
	}
	
	@Test
	void maakSpeler_metGebruikersnaamEnkelSpaties_gooitException() {
		Assertions.assertThrows(OngeldigeGebruikersnaamException.class, () -> new Speler("      ", 2003));
	}
	
	@Test
	void maakSpeler_metTeJongGeboortejaar_gooitException() {
		Assertions.assertThrows(OngeldigGeboortejaarException.class, () -> new Speler("avatar", 2020));
	}

	

	@Test
	void maakSpeler_metNegatiefGeboortejaar_gooitException() {
		Assertions.assertThrows(OngeldigGeboortejaarException.class, () -> new Speler("avatar", -2003));
	}

	@Test
	void maakSpeler_metGrootGeboortejaar_gooitException() {
		Assertions.assertThrows(OngeldigGeboortejaarException.class, () -> new Speler("avatar", 3000));
	}

	@Test
	void maakSpeler_metSpecialeTekensInGebruikersnaam_correctObjectAangemaakt() {
		Assertions.assertDoesNotThrow(() -> new Speler("av@tar", 2003));
	}

}
