package gui;

import java.util.ResourceBundle;

import domein.DomeinController;
import exceptions.GebruikersnaamInGebruikException;
import exceptions.OngeldigGeboortejaarException;
import exceptions.OngeldigeGebruikersnaamException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegistratieScherm extends Stage {

	private DomeinController dc;
	private BeginScherm beginScherm;
	private TextField txfGebruikersnaam;
	private TextField txfGeboortejaar;
	private static final String AFBEELDINGEN_MAP = "/images/";
	private static final String FONT_PATH = "/resources/MedievalSharp-Regular.ttf";
	private Font customFont;

	public RegistratieScherm(DomeinController dc, BeginScherm beginScherm, ResourceBundle resourceBundle) {
		this.dc = dc;
		this.beginScherm = beginScherm;
		this.setTitle(resourceBundle.getString("registreerSpeler"));
		laadLettertype();
		bouwGUI(resourceBundle);
	}

	private void applyBackgroundImage(StackPane pane, String imagePath) {
		Image image = new Image(getClass().getResourceAsStream(imagePath));
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		pane.setBackground(new Background(backgroundImage));
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

	private void bouwGUI(ResourceBundle resourceBundle) {
		Image icoontje = new Image(getClass().getResourceAsStream("/images/kasteel.jpg"));
		this.getIcons().add(icoontje);

		StackPane backgroundPane = new StackPane();
		applyBackgroundImage(backgroundPane, AFBEELDINGEN_MAP + "background.png");

		VBox registratieBox = new VBox(10);
		registratieBox.setAlignment(Pos.CENTER);
		registratieBox.setPadding(new Insets(20));

		Image logoImage = new Image(getClass().getResourceAsStream("/images/logo.png"));
		ImageView logoView = new ImageView(logoImage);
		logoView.setFitWidth(500);
		logoView.setFitHeight(250);
		logoView.setPreserveRatio(true);

		registratieBox.getChildren().addAll(logoView, maakInvoerGrid(resourceBundle), maakKnoppenBox(resourceBundle));

		backgroundPane.getChildren().add(registratieBox);

		Scene registratieScene = new Scene(backgroundPane, 600, 450);
		this.setScene(registratieScene);

		this.initModality(Modality.APPLICATION_MODAL);
	}

	private GridPane maakInvoerGrid(ResourceBundle resourceBundle) {
		GridPane invoerGrid = new GridPane();
		invoerGrid.setAlignment(Pos.CENTER);
		invoerGrid.setHgap(10);
		invoerGrid.setVgap(10);

		Label lblGebruikersnaam = new Label(resourceBundle.getString("voerGebruikersnaam"));
		lblGebruikersnaam.setFont(customFont);
		txfGebruikersnaam = new TextField();
		txfGebruikersnaam.setFont(customFont);

		Label lblGeboorteJaar = new Label(resourceBundle.getString("voerGeboortejaar"));
		lblGeboorteJaar.setFont(customFont);
		txfGeboortejaar = new TextField();
		txfGeboortejaar.setFont(customFont);

		invoerGrid.addRow(0, lblGebruikersnaam, txfGebruikersnaam);
		invoerGrid.addRow(1, lblGeboorteJaar, txfGeboortejaar);

		return invoerGrid;
	}

	private HBox maakKnoppenBox(ResourceBundle resourceBundle) {
		Button btnRegistreer = new Button(resourceBundle.getString("registreer"));
		btnRegistreer.setFont(customFont);
		btnRegistreer.setOnAction(event -> registreerKnopHandler(resourceBundle));

		Button btnAnnuleren = new Button(resourceBundle.getString("annuleren"));
		btnAnnuleren.setFont(customFont);
		btnAnnuleren.setOnAction(event -> close());

		HBox knoppenBox = new HBox(10);
		knoppenBox.setAlignment(Pos.CENTER);
		knoppenBox.setSpacing(125);
		knoppenBox.getChildren().addAll(btnRegistreer, btnAnnuleren);

		return knoppenBox;
	}

	private void registreerKnopHandler(ResourceBundle resourceBundle) {
		String gebruikersnaam = txfGebruikersnaam.getText().trim();
		String geboortejaar = txfGeboortejaar.getText().trim();

		try {
			dc.registreerSpeler(gebruikersnaam, Integer.parseInt(geboortejaar));
			beginScherm.updateBeschikbareSpelers();
			close();
		} catch (GebruikersnaamInGebruikException e) {
			toonAlert(resourceBundle.getString("foutieveInvoer"), null, resourceBundle.getString("gebruikersnaamInGeb"),
					Alert.AlertType.WARNING);
		} catch (OngeldigeGebruikersnaamException e) {
			toonAlert(resourceBundle.getString("foutieveInvoer"), null,
					resourceBundle.getString("ongeldigeGebruikersnaam"), Alert.AlertType.WARNING);
		} catch (OngeldigGeboortejaarException e) {
			toonAlert(resourceBundle.getString("foutieveInvoer"), null, resourceBundle.getString("ongGeboortejaar"),
					Alert.AlertType.WARNING);
		} catch (NumberFormatException e) {
			toonAlert(resourceBundle.getString("foutieveInvoer"), null, resourceBundle.getString("ongGeboortejaar"),
					Alert.AlertType.WARNING);
		}
	}

	private void toonAlert(String titel, String header, String inhoud, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(titel);
		alert.setHeaderText(header);
		alert.setContentText(inhoud);
		alert.showAndWait();
	}

}
