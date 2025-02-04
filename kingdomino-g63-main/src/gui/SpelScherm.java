package gui;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import domein.DomeinController;
import exceptions.TegelBezetException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SpelScherm extends Stage {

	private static final double MINIMUM_BREEDTE = 980;
	private static final double MINIMUM_HOOGTE = 680;
	private static final String AFBEELDINGEN_MAP = "/images/";
	private static final String FONT_PATH = "/resources/MedievalSharp-Regular.ttf";

	private List<String> gekozenSpelers;
	private DomeinController dc;

	private String huidigeTegelRichting = "RECHTS";

	private ListView<String> startKolomListView;
	private ListView<String> eindKolomListView;
	private ImageView spelStapelTegelView;
	private Font customFont;

	private Button btnStartKolomKies;
	private Button btnEindKolomKies;
	private Button btnDraaien;
	private Button btnPlaats;
	private Button btnOverslaan;
	private Button btnSkipSpel;

	private int huidigeSpelerIndex;
	private List<String> spelersInBeurtVolgorde;
	private ImageView tegelPreview = new ImageView();

	private GridPane root;
	private StackPane overlayPane;

	private ResourceBundle resourceBundle;

	private String gekozenTegel;
	private String taalKeuze;
	private boolean eersteRonde = true;
	private boolean wachtOpTegelPlaatsing;

	private boolean isLaatsteRonde() {
		return dc.isSpelGedaan();
	}

	public SpelScherm(List<String> gekozenSpelers, DomeinController dc, String taalkeuze) {
		this.gekozenSpelers = gekozenSpelers;
		this.dc = dc;

		if ("nl".equals(taalkeuze)) {
			resourceBundle = ResourceBundle.getBundle("resources.berichten_nl_BE");
			setTaalkeuze("nl");
		} else {
			resourceBundle = ResourceBundle.getBundle("resources.berichten_en_UK");
			setTaalkeuze("en");
		}
		laadLettertype();

		bouwGUI();
		initialiseerSpel();
	}

	private void laadLettertype() {
		try {
			customFont = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 30);
			if (customFont == null) {
				throw new NullPointerException("Lettertype kon niet worden geladen van " + FONT_PATH);
			}
		} catch (Exception e) {
			System.err.println("Fout bij het laden van het lettertype: " + e.getMessage());
			customFont = Font.getDefault();
		}
	}

	private void toepassenLettertype() {
		Scene scene = getScene();
		if (scene != null) {
			scene.getRoot().setStyle("-fx-font-family: '" + customFont.getName() + "'; -fx-font-size: 16px;");
		}
	}

	private void applyBackgroundImage(StackPane pane, String imagePath) {
		try {
			InputStream inputStream = getClass().getResourceAsStream(imagePath);
			if (inputStream == null) {
				throw new RuntimeException("Background image stream is null for path: " + imagePath);
			}
			Image image = new Image(inputStream);
			BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
			pane.setBackground(new Background(backgroundImage));
		} catch (Exception e) {
			System.err.println("Error loading background image: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void toonTegelPreview(String tegelAfbeeldingPath) {
		Image image = new Image(getClass().getResourceAsStream(AFBEELDINGEN_MAP + tegelAfbeeldingPath), 100, 100, true,
				true);
		if (image.isError()) {
			System.out.println("Fout bij het laden van de afbeelding: " + tegelAfbeeldingPath);
		}
		tegelPreview.setImage(image);
		tegelPreview.setVisible(true);
	}

	private void setTaalkeuze(String taalKeuze) {
		this.taalKeuze = taalKeuze;
	}

	private void bouwGUI() {
		setTitle("Kingdomino");
		getIcons().add(new Image(getClass().getResourceAsStream("/images/kasteel.jpg")));

		root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(5));

		StackPane backgroundPane = new StackPane();
		applyBackgroundImage(backgroundPane, AFBEELDINGEN_MAP + "background.png");
		backgroundPane.getChildren().add(root);
		Scene scene = new Scene(backgroundPane, MINIMUM_BREEDTE, MINIMUM_HOOGTE);

		toonAlleKoninkrijken(root);
		voegKolommenToe(root);
		eindKolomListView.setDisable(true);
		eindKolomListView.setOpacity(0.75);
		voegKnoppenToe(root);
		voegSpelStapelTegelToeAanGrid(root);

		setScene(scene);
		setMinWidth(MINIMUM_BREEDTE);
		setMinHeight(MINIMUM_HOOGTE);
		toepassenLettertype();
		Platform.runLater(() -> setOnCloseRequest(event -> {
			event.consume();
			if (bevestigSluiting()) {
				Platform.exit();
			}
		}));
	}

	private void initialiseerSpel() {
		spelersInBeurtVolgorde = dc.genereerWillekeurigeSpelernamenVolgorde();
		updateKolommenMetAfbeeldingen();
		huidigeSpelerIndex = 0;
		updateInstructieScherm();
	}

	private void voegKolommenToe(GridPane root) {
		startKolomListView = new ListView<>();
		eindKolomListView = new ListView<>();

		VBox startKolomBox = maakKolom(resourceBundle.getString("startkolom"), startKolomListView);
		VBox eindKolomBox = maakKolom(resourceBundle.getString("eindkolom"), eindKolomListView);

		root.add(startKolomBox, 1, 0);
		root.add(eindKolomBox, 2, 0);
	}

	private VBox maakKolom(String labelText, ListView<String> listView) {
		Label lblKolom = new Label(labelText);
		lblKolom.setStyle("-fx-text-fill: black;");

		VBox kolomBox = new VBox(10, lblKolom, listView);
		kolomBox.setAlignment(Pos.CENTER);
		kolomBox.setStyle("-fx-background-color: transparent;");

		listView.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");

		listView.setPrefSize(150, 250);

		return kolomBox;
	}

	private void voegKnoppenToe(GridPane root) {
		btnStartKolomKies = new Button(resourceBundle.getString("kiesUitStart"));
		btnEindKolomKies = new Button(resourceBundle.getString("kiesUitEind"));
		btnDraaien = new Button(resourceBundle.getString("draai"));

		btnOverslaan = new Button(resourceBundle.getString("skipBeurt"));
		btnOverslaan.setDisable(true);

		btnPlaats = new Button(resourceBundle.getString("plaats"));
		btnSkipSpel = new Button(resourceBundle.getString("skipSpel"));

		btnStartKolomKies.setOnAction(e -> verwerkKeuzeUitStartkolom());
		btnEindKolomKies.setOnAction(e -> verwerkKeuzeUitEindkolom());
		btnDraaien.setOnAction(e -> draaiTegel());
		btnOverslaan.setOnAction(e -> slaBeurtOver());
		btnPlaats.setOnAction(e -> plaatsTegel());
		btnSkipSpel.setOnAction(e -> skipSpel());

		HBox knopBox1 = new HBox(10, btnStartKolomKies, btnEindKolomKies);

		HBox knopBox2 = new HBox(10, btnDraaien, btnOverslaan, btnPlaats);
		HBox knopBox3 = new HBox(10, btnSkipSpel);

		knopBox1.setAlignment(Pos.CENTER);
		knopBox2.setAlignment(Pos.CENTER);
		knopBox3.setAlignment(Pos.CENTER);

		VBox knopContainer = new VBox(5, knopBox1, knopBox2, knopBox3);
		knopContainer.setAlignment(Pos.CENTER);

		root.add(knopContainer, 1, 1, 3, 1);
	}

	private void skipSpel() {
		Alert bevestigingsAlert = new Alert(Alert.AlertType.CONFIRMATION);
		if ("en".equals(taalKeuze)) {
		    bevestigingsAlert.setTitle("End Game");
		    bevestigingsAlert.setHeaderText("Are you sure you want to end the game?");
		    bevestigingsAlert.setContentText("If you stop now, the game will not be completed.");
		} else {
		    bevestigingsAlert.setTitle("Spel beëindigen");
		    bevestigingsAlert.setHeaderText("Weet je zeker dat je het spel wilt beëindigen?");
		    bevestigingsAlert.setContentText("Als je nu stopt, wordt het spel niet voltooid.");
		}
		bevestigingsAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

		Optional<ButtonType> resultaat = bevestigingsAlert.showAndWait();
		if (resultaat.isPresent() && resultaat.get() == ButtonType.YES) {
		    toonEindScherm();
		}
	}
	
	private void toonEindScherm() {
	    List<String> spelernamen = gekozenSpelers.stream().map(naam -> naam.split(" ")[0]).collect(Collectors.toList());
	    Eindscherm eindScherm = new Eindscherm(taalKeuze, dc, spelernamen);
	    dc.berekenEnUpdateSpelerStatistieken(spelernamen);
	    eindScherm.show();
	    close();
	}



	private String bepaalSpelerKleur(String spelerKleur) {
		spelerKleur = spelerKleur.toLowerCase();
		switch (spelerKleur) {
		case "groen":
		case "green":
			return "green";
		case "blauw":
		case "blue":
			return "blue";
		case "roos":
		case "pink":
			return "pink";
		case "geel":
		case "yellow":
			return "yellow";
		default:
			return "black";
		}
	}

	private void slaBeurtOver() {
	    Alert bevestigingsAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    bevestigingsAlert.setTitle(resourceBundle.getString("skipBeurt"));
	    bevestigingsAlert.setHeaderText(resourceBundle.getString("skipBeurtConfirm"));
	    bevestigingsAlert.setContentText("");
	    bevestigingsAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

	    Optional<ButtonType> resultaat = bevestigingsAlert.showAndWait();
	    if (resultaat.isPresent() && resultaat.get() == ButtonType.YES) {
	        toonMelding(resourceBundle.getString("beurtOvergeslagen"), resourceBundle.getString("beurtOvergeslagenMelding"));

	        huidigeSpelerIndex = (huidigeSpelerIndex + 1) % spelersInBeurtVolgorde.size();

	       
	        if (isLaatsteRonde()) {
	            if (huidigeSpelerIndex == 0) { 
	                bereidVolgendeRondeVoor(); 
	            } else {
	                enableKoninkrijkVoorPlaatsing(spelersInBeurtVolgorde.get(huidigeSpelerIndex));
	                toonVolgendeSpelerInstructie();
	                disableAndereKoninkrijken(spelersInBeurtVolgorde.get(huidigeSpelerIndex));
	            }
	        } else {
	            if (huidigeSpelerIndex == 0) {
	                bereidVolgendeRondeVoor();
	            } else {
	                updateInstructieSchermVoorRonde();
	            }
	        }

	        btnOverslaan.setDisable(!isLaatsteRonde());
	        eindKolomListView.setDisable(false);
	        eindKolomListView.setOpacity(1.0);
	        enableAlleKoninkrijken();
	    } else {
	        bevestigingsAlert.close();
	    }
	}


	private void initialiseerSpelStapelTegel() {
		String bovensteTegelAfbeelding = dc.getBovensteTegelSpelstapelAfbeelding();
		Image image = new Image(getClass().getResourceAsStream(AFBEELDINGEN_MAP + bovensteTegelAfbeelding));
		spelStapelTegelView = new ImageView(image);
		spelStapelTegelView.setFitHeight(100);
		spelStapelTegelView.setFitWidth(100);
		spelStapelTegelView.setPreserveRatio(true);
	}

	private void voegSpelStapelTegelToeAanGrid(GridPane root) {
		initialiseerSpelStapelTegel();
		root.add(spelStapelTegelView, 1, 1, 2, 1);
		GridPane.setHalignment(spelStapelTegelView, HPos.CENTER);
		GridPane.setValignment(spelStapelTegelView, VPos.TOP);
	}

	private void verwerkKeuzeUitStartkolom() {

		String geselecteerdeTegel = startKolomListView.getSelectionModel().getSelectedItem();
		if (geselecteerdeTegel != null) {
			int tegelIndex = startKolomListView.getItems().indexOf(geselecteerdeTegel);
			String huidigeSpelerNaam = spelersInBeurtVolgorde.get(huidigeSpelerIndex);
			setGekozenTegel(geselecteerdeTegel);
			try {
				dc.kiesEnPlaatsKoningInStartkolom(huidigeSpelerNaam, tegelIndex);
				updateStartKolomMetAfbeeldingen();
				huidigeSpelerIndex = (huidigeSpelerIndex + 1) % spelersInBeurtVolgorde.size();
			} catch (TegelBezetException e) {
				toonFoutmelding(resourceBundle.getString("tegelBezetTitel"),
						resourceBundle.getString("tegelBezetBoodschap"));
			}
			if (huidigeSpelerIndex == 0) {
				eersteRonde = false;

				initialiseerNieuweRonde();
			}
			updateInstructieScherm();
		} else {
			toonFoutmelding(resourceBundle.getString("selecteerTegelTitel"),
					resourceBundle.getString("selecteerTegelBoodschap"));
		}
		if (eersteRonde) {
	        eindKolomListView.setDisable(true);
	    }
	}

	private void setGekozenTegel(String gekozenTegel) {
		this.gekozenTegel = gekozenTegel;
	}

	private void verwerkKeuzeUitEindkolom() {
		if (eersteRonde) {
	        toonFoutmelding(resourceBundle.getString("eersteRondeTitel"), resourceBundle.getString("eersteRondeBoodschap"));
	        return;
	    }
		String geselecteerdeTegel = eindKolomListView.getSelectionModel().getSelectedItem();
		if (geselecteerdeTegel != null) {
			int tegelIndex = eindKolomListView.getItems().indexOf(geselecteerdeTegel);
			String huidigeSpelerNaam = spelersInBeurtVolgorde.get(huidigeSpelerIndex);
			try {
				dc.kiesEnPlaatsKoningInEindkolom(huidigeSpelerNaam, tegelIndex);
				updateEindKolomMetAfbeeldingen();
				eindKolomListView.setDisable(true);
				wachtOpTegelPlaatsing = true;
				btnOverslaan.setDisable(false);
				String tegelAfbeeldingPath = dc.getAfbeeldingspadVanSpelerTegel(huidigeSpelerNaam);
				toonTegelPreview(tegelAfbeeldingPath);

				disableAndereKoninkrijken(huidigeSpelerNaam);
				enableKoninkrijkVoorPlaatsing(huidigeSpelerNaam);

				toonMelding(resourceBundle.getString("plaats"), resourceBundle.getString("plaatsBoodschap"));
			} catch (TegelBezetException e) {
				toonFoutmelding(resourceBundle.getString("tegelBezetTitel"),
						resourceBundle.getString("tegelBezetBoodschap"));
			}
		} else {
			toonFoutmelding(resourceBundle.getString("selecteerTegelTitel"),
					resourceBundle.getString("selecteerUitEind"));
		}
	}

	private void startLaatsteRondePlaatsing() {

		disableAlleKoninkrijken();
		btnOverslaan.setDisable(false);
		for (String spelerNaam : spelersInBeurtVolgorde) {
			enableKoninkrijkVoorPlaatsing(spelerNaam);
			wachtOpTegelPlaatsing = true;
		}

		huidigeSpelerIndex = 0;
		toonTegelPreviewVoorHuidigeSpeler();
		toonPlaatsInstructie(spelersInBeurtVolgorde.get(huidigeSpelerIndex));
		disableAndereKoninkrijken(spelersInBeurtVolgorde.get(huidigeSpelerIndex));
	}

	private void disableAlleKoninkrijken() {
		for (String speler : gekozenSpelers) {
			VBox koninkrijkBox = (VBox) root.lookup("#koninkrijk-" + speler.replaceAll(" \\(.*\\)", ""));
			if (koninkrijkBox != null) {
				koninkrijkBox.setDisable(true);
				koninkrijkBox.setOpacity(0.5);
			}
		}
	}

	private void toonPlaatsInstructie(String spelerNaam) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(resourceBundle.getString("laatsteRondeTitel"));
		alert.setHeaderText(null);
		alert.setContentText(resourceBundle.getString("laatsteRondePlaatsTegel") + spelerNaam + ".");
		alert.showAndWait();
	}

	private void toonTegelPreviewVoorHuidigeSpeler() {
		String tegelAfbeeldingPath = dc.getAfbeeldingspadVanSpelerTegel(spelersInBeurtVolgorde.get(huidigeSpelerIndex));
		toonTegelPreview(tegelAfbeeldingPath);
	}

	private void enableKoninkrijkVoorPlaatsing(String huidigeSpelerNaam) {
		VBox kingdomBox = (VBox) root.lookup("#koninkrijk-" + huidigeSpelerNaam);
		if (kingdomBox != null) {
			kingdomBox.setDisable(false);
			kingdomBox.setOpacity(1.0);
		}
	}

	private boolean bevestigSluiting() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, resourceBundle.getString("zekerAfsluiten"),
				ButtonType.YES, ButtonType.NO);
		alert.setTitle(resourceBundle.getString("afsluiten"));
		alert.setHeaderText(null);
		alert.showAndWait();
		return alert.getResult() == ButtonType.YES;
	}

	private void updateKolommenMetAfbeeldingen() {
		updateStartKolomMetAfbeeldingen();
		updateEindKolomMetAfbeeldingen();
	}

	private void updateStartKolomMetAfbeeldingen() {
		List<String> startKolomAfbeeldingen = dc.getStartkolomTegelAfbeeldingen();
		Map<Integer, String> koningKleuren = dc.getKoningKleurenStartkolom();

		startKolomListView.setItems(FXCollections.observableArrayList(startKolomAfbeeldingen));
		startKolomListView.setCellFactory(param -> new ListCell<String>() {
			private ImageView tegelImageView = new ImageView();
			private ImageView kroonImageView = new ImageView();
			private StackPane stackPane = new StackPane();

			{
				tegelImageView.setFitHeight(100);
				tegelImageView.setFitWidth(100);
				tegelImageView.setPreserveRatio(true);
				kroonImageView.setFitHeight(40);
				kroonImageView.setFitWidth(40);
				kroonImageView.setPreserveRatio(true);
				stackPane.getChildren().addAll(tegelImageView, kroonImageView);
				StackPane.setAlignment(kroonImageView, Pos.TOP_CENTER);
			}

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setGraphic(null);
					setText(null);
				} else {

					Image tegelImage = new Image(getClass().getResourceAsStream(AFBEELDINGEN_MAP + item));
					tegelImageView.setImage(tegelImage);

					String[] parts = item.split("\\D+");
					if (parts.length > 1) {
						Integer tegelNummer = Integer.parseInt(parts[1]);
						if (koningKleuren.containsKey(tegelNummer)) {
							Image kroonImage = new Image(getClass().getResourceAsStream(
									AFBEELDINGEN_MAP + "koning_" + koningKleuren.get(tegelNummer) + ".png"));
							kroonImageView.setImage(kroonImage);
						} else {
							kroonImageView.setImage(null);
						}
					} else {
						kroonImageView.setImage(null);
					}
					setGraphic(stackPane);
				}
			}
		});
	}

	private void updateEindKolomMetAfbeeldingen() {
		List<String> eindKolomAfbeeldingen = dc.getEindkolomTegelAfbeeldingen();
		Map<Integer, String> koningKleuren = dc.getKoningKleurenEindkolom();

		eindKolomListView.setItems(FXCollections.observableArrayList(eindKolomAfbeeldingen));
		eindKolomListView.setCellFactory(param -> new ListCell<String>() {
			private ImageView tegelImageView = new ImageView();
			private ImageView kroonImageView = new ImageView();
			private StackPane stackPane = new StackPane();

			{
				tegelImageView.setFitHeight(100);
				tegelImageView.setFitWidth(100);
				tegelImageView.setPreserveRatio(true);
				kroonImageView.setFitHeight(40);
				kroonImageView.setFitWidth(40);
				kroonImageView.setPreserveRatio(true);
				stackPane.getChildren().addAll(tegelImageView, kroonImageView);
				StackPane.setAlignment(kroonImageView, Pos.TOP_CENTER);
			}

			@Override

			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {

					setGraphic(null);
					setText(null);
				} else {

					Image tegelImage = new Image(getClass().getResourceAsStream(AFBEELDINGEN_MAP + item));
					tegelImageView.setImage(tegelImage);

					String[] parts = item.split("\\D+");
					if (parts.length > 1 && parts[1].matches("\\d+")) {
						Integer tegelNummer = Integer.parseInt(parts[1]);
						if (koningKleuren.containsKey(tegelNummer)) {
							Image kroonImage = new Image(getClass().getResourceAsStream(
									AFBEELDINGEN_MAP + "koning_" + koningKleuren.get(tegelNummer) + ".png"));
							kroonImageView.setImage(kroonImage);
						} else {
							kroonImageView.setImage(null);
						}
					} else {

						kroonImageView.setImage(null);
					}
					setGraphic(stackPane);
				}
			}
		});
	}

	private void updateSpelStapelTegel() {
		try {
			String bovensteTegelAfbeelding = dc.getBovensteTegelSpelstapelAfbeelding();
			InputStream inputStream = getClass().getResourceAsStream(AFBEELDINGEN_MAP + bovensteTegelAfbeelding);
			if (inputStream != null) {
				Image image = new Image(inputStream);
				spelStapelTegelView.setImage(image);
			} else {
				setLegeTegelAfbeelding();
			}
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			setLegeTegelAfbeelding();
		}
	}

	private void setLegeTegelAfbeelding() {
		Image image = new Image(getClass().getResourceAsStream(AFBEELDINGEN_MAP + "legeTegel.png"));
		spelStapelTegelView.setImage(image);
	}

	private void updateInstructieScherm() {
		String huidigeSpelerNaam = spelersInBeurtVolgorde.get(huidigeSpelerIndex);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(resourceBundle.getString("aanBeurt"));
		alert.setHeaderText(null);

		if (isLaatsteRonde()) {

			alert.setContentText(resourceBundle.getString("laatsteRondeInstructie") + " " + huidigeSpelerNaam + ".");
			startKolomListView.setDisable(true);
			eindKolomListView.setDisable(true);
		} else {

			String kolomNaam = eersteRonde ? resourceBundle.getString("startkolom")
					: resourceBundle.getString("eindkolom");
			alert.setContentText(resourceBundle.getString("speler") + " " + huidigeSpelerNaam + " "
					+ resourceBundle.getString("kiesUit") + " " + kolomNaam + ".");
			eindKolomListView.setDisable(false);
		}

		alert.showAndWait();
	}

	private void updateInstructieSchermVoorRonde() {
		if (!spelersInBeurtVolgorde.isEmpty() && !isLaatsteRonde() && !eindKolomListView.getItems().isEmpty()) {
			String huidigeSpelerNaam = spelersInBeurtVolgorde.get(huidigeSpelerIndex);
			Alert alert = new Alert(Alert.AlertType.INFORMATION,
					resourceBundle.getString("kiesUitEindFull") + " " + huidigeSpelerNaam);
			alert.setTitle(resourceBundle.getString("kiesUitEind"));
			alert.setHeaderText(null);
			alert.showAndWait();
		}
	}

	private void initialiseerNieuweRonde() {
		dc.updateBeurtVolgorde();
		spelersInBeurtVolgorde = dc.getSpelersInBeurtVolgorde();
		huidigeSpelerIndex = 0;
		eindKolomListView.setDisable(false);
		eindKolomListView.setOpacity(1);
		startKolomListView.getSelectionModel().clearSelection();
		startKolomListView.setDisable(true);
		startKolomListView.setOpacity(1);
	}

	private void bereidVolgendeRondeVoor() {

		dc.promoveerEindkolomTotStartkolom();
		updateKolommenMetAfbeeldingen();
		spelersInBeurtVolgorde = dc.getSpelersInBeurtVolgorde();
		if (isLaatsteRonde()) {
			startLaatsteRondePlaatsing();
		} else {
			updateInstructieSchermVoorRonde();
		}
		updateSpelStapelTegel();
	}

	private void toonMelding(String titel, String bericht) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, bericht, ButtonType.OK);
		alert.setTitle(titel);
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	private String vertaalKleurNaarNederlands(String kleur) {
		switch (kleur.toLowerCase()) {
		case "blue":
			return "blauw";
		case "green":
			return "groen";
		case "pink":
			return "roos";
		case "yellow":
			return "geel";
		default:
			return kleur;
		}
	}

	private void toonFoutmelding(String titel, String bericht) {
		Alert alert = new Alert(Alert.AlertType.ERROR, bericht, ButtonType.OK);
		alert.setTitle(titel);
		alert.setHeaderText(null);
		alert.showAndWait();
	
	}

	private void toonAlleKoninkrijken(GridPane root) {
		List<String> koninkrijken = dc.getAlleKoninkrijkenWeergave();
		int linkerKolomIndex = 0;
		int rechterKolomIndex = 4;

		for (int i = 0; i < gekozenSpelers.size(); i++) {
			String spelerNaamZonderKleur = gekozenSpelers.get(i).replaceAll(" \\(.*\\)", "");
			String spelerKleur = gekozenSpelers.get(i).split(" ")[1].replaceAll("[()]", "");

			VBox koninkrijkBox = new VBox(10);
			koninkrijkBox.setId("koninkrijk-" + spelerNaamZonderKleur);

			String cssColor = bepaalSpelerKleur(spelerKleur);
			Label spelerLabel = new Label(spelerNaamZonderKleur);
			spelerLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: " + cssColor
					+ "; -fx-effect: dropshadow(gaussian, black, 5, 0.5, 0, 0);");

			GridPane koninkrijkGrid = new GridPane();
			koninkrijkGrid.setVgap(0);
			koninkrijkGrid.setHgap(0);
			setupKoninkrijkGrid(koninkrijkGrid);
			vulKoninkrijkGridMetData(koninkrijkGrid, koninkrijken.get(i));

			String vertaaldeKleur = vertaalKleurNaarNederlands(spelerKleur);
			String startTegelPath = AFBEELDINGEN_MAP + "starttegel_" + vertaaldeKleur.toLowerCase() + ".png";
			Image startTegelImage = new Image(getClass().getResourceAsStream(startTegelPath), 50, 50, true, true);
			ImageView startTegelView = new ImageView(startTegelImage);
			GridPane.setConstraints(startTegelView, 2, 2);
			koninkrijkGrid.getChildren().add(startTegelView);

			koninkrijkBox.getChildren().addAll(spelerLabel, koninkrijkGrid);
			int kolomIndex = (i % 2 == 0) ? linkerKolomIndex : rechterKolomIndex;
			int rijIndex = i / 2;
			root.add(koninkrijkBox, kolomIndex, rijIndex);
		}
	}

	private void disableAndereKoninkrijken(String huidigeSpelerNaam) {
		String huidigeSpelerNaamZonderKleur = huidigeSpelerNaam.replaceAll(" \\(.*\\)", "");
		for (String speler : gekozenSpelers) {
			String spelerNaamZonderKleur = speler.replaceAll(" \\(.*\\)", "");
			VBox koninkrijkBox = (VBox) root.lookup("#koninkrijk-" + spelerNaamZonderKleur);
			if (koninkrijkBox != null) {
				boolean isHuidigeSpeler = spelerNaamZonderKleur.equals(huidigeSpelerNaamZonderKleur);
				koninkrijkBox.setDisable(!isHuidigeSpeler);

				if (isHuidigeSpeler) {
					koninkrijkBox.setOpacity(1.0);
				} else {
					koninkrijkBox.setOpacity(0.9);
				}
			}
		}
	}

	private void toonVolgendeSpelerInstructie() {
		if (!spelersInBeurtVolgorde.isEmpty()) {
			String huidigeSpelerNaam = spelersInBeurtVolgorde.get(huidigeSpelerIndex);
			toonTegelPreviewVoorHuidigeSpeler();

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			if (isLaatsteRonde()) {
				alert.setContentText(resourceBundle.getString("laatsteRondePlaatsTegel") + " " + huidigeSpelerNaam);
				alert.setTitle(resourceBundle.getString("laatsteRondeTitel"));
			} else {
				alert.setContentText(resourceBundle.getString("kiesUitEindFull") + " " + huidigeSpelerNaam);
				alert.setTitle(resourceBundle.getString("kiesUitEind"));
			}
			alert.setHeaderText(null);
			alert.showAndWait();
		}
	}

	private void enableAlleKoninkrijken() {
		for (int i = 0; i < gekozenSpelers.size(); i++) {
			VBox koninkrijkBox = (VBox) root.lookup("#koninkrijk-" + i);
			if (koninkrijkBox != null) {
				koninkrijkBox.setDisable(false);
				koninkrijkBox.setOpacity(1.0);
			}
		}
	}

	private void setupKoninkrijkGrid(GridPane koninkrijkGrid) {
		StackPane overlay = new StackPane();
		overlay.setPickOnBounds(false);

		overlay.prefWidthProperty().bind(koninkrijkGrid.widthProperty());
		overlay.prefHeightProperty().bind(koninkrijkGrid.heightProperty());

		koninkrijkGrid.add(overlay, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
		GridPane.setHalignment(overlay, HPos.CENTER);
		GridPane.setValignment(overlay, VPos.CENTER);

		koninkrijkGrid.setUserData(overlay);
	}

	private void vulKoninkrijkGridMetData(GridPane koninkrijkGrid, String koninkrijkWeergave) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Label vakLabel = new Label("");
				vakLabel.setMinSize(50, 50);
				vakLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				vakLabel.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: transparent;");
				vakLabel.setOnMouseClicked(e -> {
					if (wachtOpTegelPlaatsing) {
						int colIndex = GridPane.getColumnIndex(vakLabel);
						int rowIndex = GridPane.getRowIndex(vakLabel);
						updatePreviewLocatie(colIndex, rowIndex, koninkrijkGrid);
					}
				});

				koninkrijkGrid.add(vakLabel, j, i);
				GridPane.setFillWidth(vakLabel, true);
				GridPane.setFillHeight(vakLabel, true);
			}
		}
	}

	private void draaiTegel() {
		List<String> richtingen = Arrays.asList("RECHTS", "ONDER", "LINKS", "BOVEN");
		int huidigeIndex = richtingen.indexOf(huidigeTegelRichting);
		int volgendeIndex = (huidigeIndex + 1) % richtingen.size();
		huidigeTegelRichting = richtingen.get(volgendeIndex);

		updateTegelWeergaveMetRichting();
	}

	private void updateTegelWeergaveMetRichting() {

		double cellWidth = 50;
		double cellHeight = 50;
		switch (huidigeTegelRichting) {
		case "RECHTS":
			tegelPreview.setTranslateX(tegelPreview.getTranslateX() + cellWidth / 2);
			tegelPreview.setTranslateY(tegelPreview.getTranslateY() + cellHeight / 2);
			break;
		case "ONDER":
			tegelPreview.setTranslateX(tegelPreview.getTranslateX() - cellWidth / 2);
			tegelPreview.setTranslateY(tegelPreview.getTranslateY() + cellHeight / 2);
			break;
		case "LINKS":
			tegelPreview.setTranslateX(tegelPreview.getTranslateX() - cellWidth / 2);
			tegelPreview.setTranslateY(tegelPreview.getTranslateY() - cellHeight / 2);
			break;
		case "BOVEN":
			tegelPreview.setTranslateX(tegelPreview.getTranslateX() + cellWidth / 2);
			tegelPreview.setTranslateY(tegelPreview.getTranslateY() - cellHeight / 2);
			break;
		}
		tegelPreview.setRotate(tegelPreview.getRotate() + 90);
	}

	private void plaatsTegel() {
	    if (wachtOpTegelPlaatsing) {
	        String huidigeSpelerNaam = spelersInBeurtVolgorde.get(huidigeSpelerIndex);
	        Point2D locatie = bepaalTegelLocatie();
	        boolean succes = dc.plaatsTegelInKoninkrijkVanSpeler(huidigeSpelerNaam, (int) locatie.getY(),
	                (int) locatie.getX(), huidigeTegelRichting);
	        if (succes) {
	            updateKoninkrijkNaPlaatsing(huidigeSpelerNaam);

	            tegelPreview.setImage(null);
	            tegelPreview.setVisible(false);
	            resetTegelRichting();
	            toonMelding(resourceBundle.getString("tegelGeplaatst"), resourceBundle.getString("tegelGeplaatstMelding"));

	            wachtOpTegelPlaatsing = false;
	            if (!dc.isSpelGedaan()) {
	                btnOverslaan.setDisable(true);
	            }
	            eindKolomListView.setDisable(false);
	            eindKolomListView.setOpacity(1.0);
	            enableAlleKoninkrijken();

	            huidigeSpelerIndex = (huidigeSpelerIndex + 1) % spelersInBeurtVolgorde.size();
	            if (huidigeSpelerIndex == 0) {
	                if (dc.isSpelGedaan()) {
	                    toonEindScherm();
	                } else {
	                    bereidVolgendeRondeVoor();
	                }
	            } else {
	                if (isLaatsteRonde()) {
	                    bereidVolgendeSpelerPlaatsingVoor();
	                } else {
	                    toonVolgendeSpelerInstructie();
	                }
	            }
	        } else {
	            if ("en".equals(taalKeuze)) {
	                toonFoutmelding("Placement error", "Could not place the tile at the selected location. Please try again.");
	            } else {
	                toonFoutmelding("Plaatsfout", "Kon de tegel niet plaatsen op de geselecteerde locatie. Probeer opnieuw.");
	            }
	            tegelPreview.setVisible(true);
	        }
	    }
	}

	private void updateKoninkrijkNaPlaatsing(String spelerNaam) {
		VBox koninkrijkBox = (VBox) root.lookup("#koninkrijk-" + spelerNaam.replaceAll(" \\(.*\\)", ""));
		GridPane koninkrijkGrid = (GridPane) koninkrijkBox.getChildren().get(1);
		StackPane overlay = (StackPane) koninkrijkGrid.getUserData();

		ImageView permanenteTegel = new ImageView(tegelPreview.getImage());
		permanenteTegel.setFitWidth(100);
		permanenteTegel.setFitHeight(50);
		permanenteTegel.setTranslateX(tegelPreview.getTranslateX());
		permanenteTegel.setTranslateY(tegelPreview.getTranslateY());
		permanenteTegel.setRotate(tegelPreview.getRotate());
		overlay.getChildren().add(permanenteTegel);
	}

	private void bereidVolgendeSpelerPlaatsingVoor() {
		if (isLaatsteRonde()) {
			String tegelAfbeeldingPath = dc
					.getAfbeeldingspadVanSpelerTegel(spelersInBeurtVolgorde.get(huidigeSpelerIndex));
			toonTegelPreview(tegelAfbeeldingPath);
			enableKoninkrijkVoorPlaatsing(spelersInBeurtVolgorde.get(huidigeSpelerIndex));
			disableAndereKoninkrijken(spelersInBeurtVolgorde.get(huidigeSpelerIndex));
			wachtOpTegelPlaatsing = true;
		}
		toonVolgendeSpelerInstructie();
	}

	private void resetTegelRichting() {
		huidigeTegelRichting = "RECHTS";
		tegelPreview.setRotate(0);
	}

	private void updatePreviewLocatie(int x, int y, GridPane koninkrijkGrid) {

		StackPane overlay = (StackPane) koninkrijkGrid.getUserData();

		double cellWidth = 50;
		double cellHeight = 50;

		double translateX = x * cellWidth;
		double translateY = y * cellHeight;

		switch (huidigeTegelRichting) {
		case "RECHTS":

			translateX -= 1.5 * cellWidth;
			translateY -= 2 * cellHeight;
			break;
		case "ONDER":

			translateX -= 2 * cellWidth;
			translateY -= 1.5 * cellHeight;
			break;
		case "LINKS":

			translateX -= 2.5 * cellWidth;
			translateY -= 2 * cellHeight;
			break;
		case "BOVEN":

			translateX -= 2 * cellWidth;
			translateY -= 2.5 * cellHeight;
			break;
		}

		tegelPreview.setTranslateX(translateX);
		tegelPreview.setTranslateY(translateY);

		if (huidigeTegelRichting.equals("RECHTS") || huidigeTegelRichting.equals("LINKS")) {
			tegelPreview.setFitWidth(2 * cellWidth);
			tegelPreview.setFitHeight(cellHeight);
		} else {
			tegelPreview.setFitWidth(2 * cellWidth);
			tegelPreview.setFitHeight(cellHeight);
		}
		tegelPreview.setPreserveRatio(true);

		if (!overlay.getChildren().contains(tegelPreview)) {
			overlay.getChildren().add(tegelPreview);
		} else {
			tegelPreview.toFront();
		}

		tegelPreview.setVisible(true);
	}

	private Point2D bepaalTegelLocatie() {
		double cellWidth = 50;
		double cellHeight = 50;

		double translateX = tegelPreview.getTranslateX();
		double translateY = tegelPreview.getTranslateY();

		int gridX = 0, gridY = 0;
		switch (huidigeTegelRichting) {
		case "RECHTS":

			gridX = (int) ((translateX + 1.5 * cellWidth) / cellWidth);
			gridY = (int) ((translateY + 2 * cellHeight) / cellHeight);
			break;
		case "ONDER":

			gridX = (int) ((translateX + 2 * cellWidth) / cellWidth);
			gridY = (int) ((translateY + 1.5 * cellHeight) / cellHeight);
			break;
		case "LINKS":

			gridX = (int) ((translateX + 2.5 * cellWidth) / cellWidth);
			gridY = (int) ((translateY + 2 * cellHeight) / cellHeight);
			break;
		case "BOVEN":

			gridX = (int) ((translateX + 2 * cellWidth) / cellWidth);
			gridY = (int) ((translateY + 2.5 * cellHeight) / cellHeight);
			break;
		}

		return new Point2D(gridX, gridY);
	}

}
