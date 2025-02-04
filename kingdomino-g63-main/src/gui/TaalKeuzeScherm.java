package gui;

import domein.DomeinController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TaalKeuzeScherm extends VBox {
	private static final double MAX_BREEDTE = 100;
	private DomeinController dc;
	private static final String FONT_PATH = "/resources/MedievalSharp-Regular.ttf";
	private Font customFont;

	public TaalKeuzeScherm() {
		this.dc = new DomeinController();
		setAlignment(Pos.CENTER);
		setSpacing(20);
		setPadding(new Insets(20));

		laadLettertype();

		Image achtergrondFoto = new Image(getClass().getResourceAsStream("/images/background.png"));
		BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
		BackgroundImage achtergrond = new BackgroundImage(achtergrondFoto, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
		setBackground(new Background(achtergrond));

		ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
		logoView.setFitWidth(500);
		logoView.setPreserveRatio(true);

		Label lblKiesTaal = new Label("Kies een taal | Choose a language");
		lblKiesTaal.setFont(customFont);
		lblKiesTaal.setStyle(
				"-fx-text-fill: white; -fx-font-size: 18px; -fx-stroke: black; -fx-stroke-width: 2; -fx-effect: dropshadow(gaussian, black, 5, 0.0, 2, 2);");

		Button btnNL = new Button("Nederlands");
		Button btnEN = new Button("English");

		btnNL.setMinWidth(150);
		btnEN.setMinWidth(150);

		btnNL.setFont(customFont);
		btnEN.setFont(customFont);

		btnNL.setOnAction(event -> {
			Stage stage = (Stage) getScene().getWindow();
			stage.close();

			String taalkeuze = "nl";
			openBeginScherm(taalkeuze);
		});

		btnEN.setOnAction(event -> {
			Stage stage = (Stage) getScene().getWindow();
			stage.close();

			String taalkeuze = "en";
			openBeginScherm(taalkeuze);
		});

		VBox taalKeuzeBox = new VBox(10, logoView, lblKiesTaal, btnNL, btnEN);
		taalKeuzeBox.setAlignment(Pos.CENTER);
		taalKeuzeBox.setPrefHeight(300);
		taalKeuzeBox.setPrefWidth(300);

		getChildren().add(taalKeuzeBox);
	}

	private void laadLettertype() {
		try {
			customFont = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 16);
			if (customFont == null) {
				throw new NullPointerException("Lettertype kon niet worden geladen van " + FONT_PATH);
			}
		} catch (Exception e) {
			System.err.println("Fout bij het laden van het lettertype: " + e.getMessage());
			customFont = Font.getDefault();
		}
	}

	private void openBeginScherm(String taalkeuze) {
		BeginScherm beginScherm = new BeginScherm(taalkeuze, dc);
		beginScherm.show();
	}
}
