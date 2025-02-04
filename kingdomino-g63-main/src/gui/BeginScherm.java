package gui;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import domein.DomeinController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import utils.Kleur;

public class BeginScherm extends GridPane {

	private DomeinController dc;
	private ListView<String> lstViewBeschikbareSpelers;
	private Button btnStart;
	private ListView<String> lstViewGekozenSpelers;
	private ResourceBundle resourceBundle;
	private String taalkeuze;
	private static final String FONT_PATH = "/resources/MedievalSharp-Regular.ttf";
	private Font customFont;
	
	public BeginScherm(String taalkeuze, DomeinController dc) {
		this.dc = dc;
		this.setAlignment(Pos.CENTER);
		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(25));
		
		laadLettertype();
		
		Image achtergrondFoto = new Image(getClass().getResourceAsStream("/images/background.png"));
		BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
		BackgroundImage achtergrond = new BackgroundImage(achtergrondFoto, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
		setBackground(new Background(achtergrond));

		if ("nl".equals(taalkeuze)) {
			resourceBundle = ResourceBundle.getBundle("resources.berichten_nl_BE");
			setTaalkeuze("nl");
		} else {
			resourceBundle = ResourceBundle.getBundle("resources.berichten_en_UK");
			setTaalkeuze("en");
		}

		VBox beschikbareSpelersBox = new VBox();
		DropShadow dropShadow3 = new DropShadow();
		dropShadow3.setColor(Color.WHITE);
		dropShadow3.setRadius(10);
		dropShadow3.setSpread(5);
	
		Label lblBeschikbareSpelers = new Label(resourceBundle.getString("beschikbareSpelers"));
		lblBeschikbareSpelers.setFont(customFont);
		lblBeschikbareSpelers.setEffect(dropShadow3);
		
		lstViewBeschikbareSpelers = new ListView<>();
		beschikbareSpelersBox.getChildren().addAll(lblBeschikbareSpelers, lstViewBeschikbareSpelers);

		VBox gekozenSpelersBox = new VBox();
		Label lblGekozenSpelers = new Label(resourceBundle.getString("gekozenSpelers"));
		lblGekozenSpelers.setFont(customFont);
		lblGekozenSpelers.setEffect(dropShadow3);
		
		lstViewGekozenSpelers = new ListView<>();
		Tooltip ttGekozenSpelers = new Tooltip();
		ttGekozenSpelers.setText(resourceBundle.getString("ttGekozenSpelers"));
		lstViewGekozenSpelers.setTooltip(ttGekozenSpelers);
		gekozenSpelersBox.getChildren().addAll(lblGekozenSpelers, lstViewGekozenSpelers);
		
		lstViewBeschikbareSpelers.setStyle("-fx-font: " + customFont.getSize() + " \"" + customFont.getFamily() + "\";");
		lstViewGekozenSpelers.setStyle("-fx-font: " + customFont.getSize() + " \"" + customFont.getFamily() + "\";");

		if (customFont != null) {
		    lstViewBeschikbareSpelers.setStyle("-fx-font: " + customFont.getSize() + " \"" + customFont.getFamily() + "\";");
		    lstViewGekozenSpelers.setStyle("-fx-font: " + customFont.getSize() + " \"" + customFont.getFamily() + "\";");
		}
		
		DropShadow dropShadow2 = new DropShadow();
		dropShadow2.setRadius(15); 
		dropShadow2.setColor(Color.BLACK); 
		
		ComboBox<String> kleurKeuzeBox = new ComboBox<>();
		Kleur[] kleuren = Kleur.values();
		String[] kleurStrings = new String[kleuren.length + 1];

		if (taalkeuze == "nl") {
			kleurStrings[0] = "Kies een kleur";
		} else {
			kleurStrings[0] = "Pick a colour";
		}

		for (int i = 0; i < kleuren.length; i++) {
			kleurStrings[i + 1] = resourceBundle.getString("kleur" + kleuren[i].toString());
		}
		kleurKeuzeBox.getItems().addAll(kleurStrings);
		kleurKeuzeBox.setValue(kleurStrings[0]);
		
		try {
		    InputStream fontStream = getClass().getResourceAsStream(FONT_PATH);
		    customFont = Font.loadFont(fontStream, 15); 
		} catch (Exception e) {
		    e.printStackTrace();
		    
		    customFont = Font.getDefault(); 
		}
		if (customFont != null) {
		    kleurKeuzeBox.setStyle("-fx-font: " + customFont.getSize() + " \"" + customFont.getFamily() + "\";");
		    kleurKeuzeBox.setEffect(dropShadow2);
		    
		} else {
		    kleurKeuzeBox.setStyle("-fx-font:  Arial;"); 
		}

		VBox knoppenBox = new VBox();
		Button btnNaarRechts = new Button("->");
		DropShadow dropShadow = new DropShadow();
		btnNaarRechts.setFont(customFont);
		btnNaarRechts.setEffect(dropShadow);
		Button btnNaarLinks = new Button("<-");
		btnNaarLinks.setEffect(dropShadow);
		btnNaarLinks.setFont(customFont);

		btnNaarRechts.setOnAction(event -> {
			String gekozenSpeler = lstViewBeschikbareSpelers.getSelectionModel().getSelectedItem();
			String gekozenKleur = kleurKeuzeBox.getValue();
			if (gekozenSpeler != null && gekozenKleur != null && !gekozenKleur.equals("Kies een kleur")
					&& !gekozenKleur.equals("Pick a colour") && lstViewGekozenSpelers.getItems().size() < 4) {
				lstViewGekozenSpelers.getItems().add(gekozenSpeler + " (" + gekozenKleur + ")");
				lstViewBeschikbareSpelers.getItems().remove(gekozenSpeler);
				updateStaatStartKnop();

				kleurKeuzeBox.getItems().remove(gekozenKleur);

				if ("nl".equals(taalkeuze)) {
					kleurKeuzeBox.setValue("Kies een kleur");
				} else {
					kleurKeuzeBox.setValue("Pick a colour");
				}
			}

			if (lstViewGekozenSpelers.getItems().size() >= 4) {
				btnNaarRechts.setDisable(true);
			}
		});

		btnNaarLinks.setOnAction(event -> {
			String gekozenSpeler = lstViewGekozenSpelers.getSelectionModel().getSelectedItem();
			if (gekozenSpeler != null) {
				String[] spelerInfo = gekozenSpeler.split("\\s+");
				String spelerNaam = spelerInfo[0];
				String kleur = spelerInfo[1].substring(1, spelerInfo[1].length() - 1);

				lstViewBeschikbareSpelers.getItems().add(spelerNaam);

				kleurKeuzeBox.getItems().add(kleur);

				lstViewGekozenSpelers.getItems().remove(gekozenSpeler);
				ObservableList<String> ongekozenSpelers = lstViewBeschikbareSpelers.getItems();
				ongekozenSpelers.sort(String::compareToIgnoreCase);
				updateStaatStartKnop();
				btnNaarRechts.setDisable(false);
			}
		});

		knoppenBox.getChildren().addAll(btnNaarRechts, btnNaarLinks);
		knoppenBox.setAlignment(Pos.CENTER);
		knoppenBox.setSpacing(10);

		VBox knoppenVBox = new VBox();
		knoppenVBox.getChildren().addAll(new Label(), knoppenBox);
		knoppenVBox.setAlignment(Pos.CENTER);

		Button btnRegistreer = new Button(resourceBundle.getString("registreer"));
		btnRegistreer.setFont(customFont);
		btnRegistreer.setEffect(dropShadow);
		btnRegistreer.setOnAction(event -> openRegistratieScherm());

		btnStart = new Button("Start");
		btnStart.setFont(customFont);
		btnStart.setEffect(dropShadow);
		btnStart.setDisable(true);
		btnStart.setOnAction(event -> {
			Stage stage = (Stage) getScene().getWindow();
			stage.close();
			try {
				maakSpelersEnStartSpel();
				openSpelScherm();
			} catch (Exception e) {
				toonFoutmelding("Fout bij het starten van het spel", e.getMessage());
			}
		});

		HBox startKnopBox = new HBox(btnStart);
		startKnopBox.setAlignment(Pos.BASELINE_RIGHT);

		this.add(beschikbareSpelersBox, 0, 0);
		this.add(kleurKeuzeBox, 1, 1);
		this.add(knoppenVBox, 1, 0);
		this.add(gekozenSpelersBox, 2, 0);
		this.add(btnRegistreer, 0, 1);
		this.add(startKnopBox, 2, 1);

		updateBeschikbareSpelers();

		Platform.runLater(() -> {
			Stage stage = (Stage) getScene().getWindow();

			stage.setOnCloseRequest(event -> {
				event.consume();

				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle(resourceBundle.getString("afsluiten"));
				alert.setHeaderText(null);
				alert.setContentText(resourceBundle.getString("zekerAfsluiten"));

				ButtonType btnJa = new ButtonType(resourceBundle.getString("ja"));
				ButtonType btnNee = new ButtonType(resourceBundle.getString("nee"));
				alert.getButtonTypes().setAll(btnJa, btnNee);

				alert.showAndWait().ifPresent(buttonType -> {
					if (buttonType == btnJa) {
						Platform.exit();
					}
				});
			});
		});
	}

	private void setTaalkeuze(String taalkeuze) {
		this.taalkeuze = taalkeuze;
	}

	public void updateBeschikbareSpelers() {
		List<String> beschikbareSpelersNamen = dc.haalAlleGebruikersnamenOp();
		lstViewBeschikbareSpelers.setItems(FXCollections.observableArrayList(beschikbareSpelersNamen));
	}

	private void updateStaatStartKnop() {
		btnStart.setDisable(lstViewGekozenSpelers.getItems().size() < 3);
	}

	private void openRegistratieScherm() {
		RegistratieScherm registratieScherm = new RegistratieScherm(dc, this, resourceBundle);
		registratieScherm.show();
	}

	private void openSpelScherm() {
		String[] gekozenSpelersArray = lstViewGekozenSpelers.getItems().toArray(new String[0]);
		List<String> gekozenSpelersList = Arrays.asList(gekozenSpelersArray);
		SpelScherm spelScherm = new SpelScherm(gekozenSpelersList, dc, taalkeuze);
		spelScherm.setResizable(false);
		spelScherm.show();
	}

	public void show() {
		Stage primaryStage = new Stage();
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/kasteel.jpg")));
		primaryStage.setScene(new Scene(this, 800, 600));
		primaryStage.setTitle("Kingdomino");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public String[] getGekozenSpelers() {
		ObservableList<String> gekozenSpelersList = lstViewGekozenSpelers.getItems();
		if (gekozenSpelersList != null && !gekozenSpelersList.isEmpty()) {
			return gekozenSpelersList.toArray(new String[0]);
		} else {
			return null;
		}
	}

	private void maakSpelersEnStartSpel() {
		List<String> geselecteerdeSpelerItems = lstViewGekozenSpelers.getItems();
		for (String item : geselecteerdeSpelerItems) {
			String[] delen = item.split("\\(");
			String gebruikersnaam = delen[0].trim();
			String kleurString = delen[1].replace(")", "").trim();

			String vertaaldeKleur = vertaalKleur(kleurString);
			dc.voegSpelerToe(gebruikersnaam, vertaaldeKleur.toUpperCase());
		}
		dc.startSpel();
	}

	private String vertaalKleur(String kleurString) {
		switch (kleurString.toLowerCase()) {
		case "pink":
			return "roos";
		case "blue":
			return "blauw";
		case "green":
			return "groen";
		case "yellow":
			return "geel";
		default:
			return kleurString;
		}
	}

	private void toonFoutmelding(String titel, String bericht) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(titel);
		alert.setHeaderText(null);
		alert.setContentText(bericht);
		alert.showAndWait();
	}
	
	private void laadLettertype() {
		try {
			customFont = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 20);
			if (customFont == null) {
				throw new NullPointerException("Lettertype kon niet worden geladen van " + FONT_PATH);
			}
		} catch (Exception e) {
			System.err.println("Fout bij het laden van het lettertype: " + e.getMessage());
			customFont = Font.getDefault();
		}
	}
}