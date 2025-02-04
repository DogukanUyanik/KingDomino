package cui;

import java.util.List;
import java.util.Scanner;

import domein.DomeinController;

public class KingDominoApp {

	private DomeinController dc;
	private Scanner scanner;

	public KingDominoApp(DomeinController dc) {
		this.dc = dc;
		this.scanner = new Scanner(System.in);
	}

	public void start() {
		boolean running = true;
		while (running) {
			toonHoofdmenu();
			String input = scanner.nextLine();
			if (input.matches("[1-3]")) {
				int keuze = Integer.parseInt(input);
				switch (keuze) {
				case 1:
					registreerSpeler();
					break;
				case 2:
					startNieuwSpel();

					kiesTegelInStartkolom();
					// toonOverzicht();
					System.out.println(dc.toonOverzicht());
					dc.initialiseerBeurtVolgorde();
					System.out.println(dc.getEindkolomTegelAfbeeldingen());
					speelSpel();
					// break;
				case 3:
					System.out.println("Afsluiten...");
					running = false;
					break;
				}
			} else {
				System.out.println("Ongeldige invoer, probeer opnieuw.");
			}
		}
	}

	private void toonHoofdmenu() {
		System.out.println("\nWelkom bij KingDomino");
		System.out.println("1. Registreer nieuwe speler");
		System.out.println("2. Start nieuw spel");
		System.out.println("3. Afsluiten");
		System.out.print("Maak een keuze: ");
	}

	private void registreerSpeler() {
		System.out.print("Voer gebruikersnaam in: ");
		String gebruikersnaam = scanner.nextLine();

		System.out.print("Voer geboortejaar in: ");
		int geboortejaar = Integer.parseInt(scanner.nextLine());

		try {
			dc.registreerSpeler(gebruikersnaam, geboortejaar);
			System.out.println("Speler succesvol geregistreerd!");
		} catch (Exception e) {
			System.out.println("Fout bij het registreren van de speler: " + e.getMessage());
		}
	}

	private void selecteerSpelerEnKleur() {
		while (true) {
			System.out.println("Selecteer een speler en kleur.");
			System.out.println(dc.haalBeschikbareOptiesOp());

			System.out.print("Voer de gebruikersnaam in van de speler die je wilt toevoegen: ");
			String gebruikersnaam = scanner.nextLine();

			System.out.print("Kies een kleur:");
			String kleurKeuze = scanner.nextLine().toUpperCase();

			try {
				dc.voegSpelerToe(gebruikersnaam, kleurKeuze);
				System.out.println("Speler succesvol toegevoegd aan het spel.");
				break;
			} catch (Exception e) {
				System.out.println("Er is een fout opgetreden: " + e.getMessage());
			}
		}
	}



	private void startNieuwSpel() {
		int aantalSpelers = 0;
		while (aantalSpelers < 3 || aantalSpelers > 4) {
			System.out.println("Hoeveel spelers zullen deelnemen? Kies 3 of 4.");
			String input = scanner.nextLine();
			try {
				aantalSpelers = Integer.parseInt(input);
				if (aantalSpelers < 3 || aantalSpelers > 4) {
					System.out.println("Ongeldig aantal spelers. Kies opnieuw.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Ongeldige invoer, voer een getal in.");
			}
		}

		for (int i = 0; i < aantalSpelers; i++) {
			selecteerSpelerEnKleur();
		}
		dc.startSpel();
	}

	private void kiesTegelInStartkolom() {
		List<String> willekeurigeSpelernamen = dc.genereerWillekeurigeSpelernamenVolgorde();
		for (String gebruikersnaam : willekeurigeSpelernamen) {
			List<String> startkolomWeergave = dc.getStartkolom();
			System.out.println("Startkolom:");
			startkolomWeergave.forEach(System.out::println);

			int gekozenTegelIndex = -1;
			while (gekozenTegelIndex == -1) {
				System.out.print(
						"\nSpeler " + gebruikersnaam + " is aan de beurt.\nKies een tegelindex uit de startkolom: ");
				String input = scanner.nextLine();
				try {
					gekozenTegelIndex = Integer.parseInt(input) - 1;
					dc.kiesEnPlaatsKoningInStartkolom(gebruikersnaam, gekozenTegelIndex);
					System.out.println(
							"Speler " + gebruikersnaam + " heeft tegel " + (gekozenTegelIndex + 1) + " gekozen.");
				} catch (NumberFormatException e) {
					System.out.println("Ongeldige invoer, voer een getal in.");
					gekozenTegelIndex = -1;
				} catch (IllegalArgumentException | IllegalStateException e) {
					System.out.println(e.getMessage());
					gekozenTegelIndex = -1;
				}
			}
		}
	}

	private void kiesTegelUitEindkolom() {
		List<String> eindkolomWeergave = dc.getEindkolom();
		System.out.println("Eindkolom:");
		eindkolomWeergave.forEach(System.out::println);

		String gebruikersnaam = dc.geefNaamSpelerAanDeBeurt();
		int gekozenTegelIndex = -1;
		while (gekozenTegelIndex == -1) {
			System.out
					.print("\nSpeler " + gebruikersnaam + " is aan de beurt.\nKies een tegelindex uit de eindkolom: ");
			String input = scanner.nextLine();
			try {
				gekozenTegelIndex = Integer.parseInt(input) - 1;
				dc.kiesEnPlaatsKoningInEindkolom(gebruikersnaam, gekozenTegelIndex);
				System.out
						.println("Speler " + gebruikersnaam + " heeft tegel " + (gekozenTegelIndex + 1) + " gekozen.");
			} catch (NumberFormatException e) {
				System.out.println("Ongeldige invoer, voer een getal in.");
				gekozenTegelIndex = -1;
			} catch (IllegalArgumentException | IllegalStateException e) {
				System.out.println(e.getMessage());
				gekozenTegelIndex = -1;
			}
		}
	}

	private void speelRondeVanSpelers() {
		for (int i = 0; i < dc.getAantalSpelers(); i++) {
			System.out.println("Het is de beurt van speler: " + dc.geefNaamSpelerAanDeBeurt());
			kiesTegelUitEindkolom();
			boolean tegelGeplaatst = false;
			while (!tegelGeplaatst) {
				System.out.print("Kies x coördinaat voor vak1: ");
				int startX = Integer.parseInt(scanner.nextLine());

				System.out.print("Kies y coördinaat voor vak1: ");
				int startY = Integer.parseInt(scanner.nextLine());

				System.out.print("Kies de richting van de tegel (BOVEN, ONDER, LINKS, RECHTS): ");
				String richting = scanner.nextLine();

				tegelGeplaatst = dc.plaatsTegelInKoninkrijkVanSpeler(startX, startY, richting);
				if (tegelGeplaatst) {
					System.out.println("Tegel succesvol geplaatst.");
				} else {
					System.out
							.println("Er is een fout opgetreden: De tegel kan niet geplaatst worden. Probeer opnieuw.");
				}
			}
		}
		dc.promoveerEindkolomTotStartkolom();
		System.out.println(dc.toonOverzicht());
		dc.updateBeurtVolgorde();
	}

	private void speelSpel() {
		while (!dc.isSpelGedaan()) {
			speelRondeVanSpelers();

		}
	}
}
