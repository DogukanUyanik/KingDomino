package gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import domein.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Eindscherm extends BorderPane {
	private static final String AFBEELDINGEN_MAP = "/images/";
	private String taalkeuze;
	private Button btnBegin;
	private Button btnAfsluiten;
	private DomeinController dc;
	private ResourceBundle resourceBundle;
	private Font customFont;
	private static final String FONT_PATH = "/resources/MedievalSharp-Regular.ttf";

	public Eindscherm(String taalkeuze, DomeinController dc, List<String> spelernamen) {
		this.taalkeuze = taalkeuze;
		this.dc = dc;

		if ("nl".equals(taalkeuze)) {
			resourceBundle = ResourceBundle.getBundle("resources.berichten_nl_BE");
		} else {
			resourceBundle = ResourceBundle.getBundle("resources.berichten_en_UK");
		}

		StackPane backgroundPane = new StackPane();
		applyBackgroundImage(backgroundPane, AFBEELDINGEN_MAP + "background.png");
		laadLettertype();
		toepassenLettertype();

		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(40));
		gridPane.setAlignment(Pos.CENTER);

		backgroundPane.getChildren().add(gridPane);

		toonSpelerScores(spelernamen, backgroundPane);

		this.setCenter(backgroundPane);
		this.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.SOLID, null, BorderWidths.DEFAULT)));
	}

	private void applyBackgroundImage(StackPane pane, String imagePath) {
		Image image = new Image(getClass().getResourceAsStream(imagePath));
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		pane.setBackground(new Background(backgroundImage));
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

	private void toepassenLettertype() {
		Scene scene = getScene();
		if (scene != null) {
			scene.getRoot().setStyle("-fx-font-family: '" + customFont.getName() + "'; -fx-font-size: 16px;");
		}
	}

	public void toonSpelerScores(List<String> spelernamen, StackPane backgroundPane) {
		Map<String, List<String>> spelerScores = dc.haalLandschapScoresOpPerSpeler(spelernamen);
		Map<String, Integer[]> landschapScores = new HashMap<>();
		Integer[] totaalScores = new Integer[spelernamen.size()];
		java.util.Arrays.fill(totaalScores, 0);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(15);
		gridPane.setVgap(15);
		gridPane.setPadding(new Insets(30));
		gridPane.setAlignment(Pos.CENTER);

		// Voeg de papierachtergrond toe
		ImageView paperBackground = new ImageView(new Image(getClass().getResourceAsStream("/images/papier.png")));
		paperBackground.setFitWidth(1000);
		paperBackground.setFitHeight(700);
		paperBackground.setPreserveRatio(true);
		StackPane.setAlignment(paperBackground, Pos.CENTER);
		backgroundPane.getChildren().add(paperBackground);

		backgroundPane.getChildren().add(gridPane);

		int row = 1;

		int column = 1;
		for (String spelerNaam : spelernamen) {
			Label spelerLabel = new Label(spelerNaam);
			spelerLabel.setFont(customFont);
			gridPane.add(spelerLabel, column, 0);
			column++;
		}

		for (String speler : spelerScores.keySet()) {
			int spelerIndex = spelernamen.indexOf(speler);
			for (String score : spelerScores.get(speler)) {
				String[] parts = score.split(": ");
				String type = parts[0];
				int punten = Integer.parseInt(parts[1].split(" ")[0]);

				if (!landschapScores.containsKey(type)) {
					landschapScores.put(type, new Integer[spelernamen.size()]);
				}
				landschapScores.get(type)[spelerIndex] = punten;
				totaalScores[spelerIndex] += punten;
			}
		}

		for (Map.Entry<String, Integer[]> entry : landschapScores.entrySet()) {
			String landschapstype = entry.getKey();
			Image landschapImage = new Image(
					getClass().getResourceAsStream(AFBEELDINGEN_MAP + landschapstype + ".png"));
			ImageView landschapImageView = new ImageView(landschapImage);
			landschapImageView.setFitWidth(100);
			landschapImageView.setFitHeight(50);
			gridPane.add(landschapImageView, 0, row);

			Integer[] scores = entry.getValue();
			column = 1;
			for (Integer score : scores) {
				Label scoreLabel = new Label(score == null ? "-" : score.toString());
				scoreLabel.setFont(customFont);
				gridPane.add(scoreLabel, column, row);
				column++;
			}
			row++;
		}

		Label totaalLabel = new Label();
		if ("en".equals(taalkeuze)) {
		    totaalLabel.setText("Total");
		} else {
		    totaalLabel.setText("Totaal");
		}
		totaalLabel.setFont(customFont);
		gridPane.add(totaalLabel, 0, row);
		column = 1;
		for (Integer score : totaalScores) {
		    Label scoreLabel = new Label(score.toString());
		    scoreLabel.setFont(customFont);
		    gridPane.add(scoreLabel, column, row);
		    column++;
		}

	}

	private void maakKnoppen(GridPane gridPane) {
		HBox knoppenBox = new HBox(10);
		knoppenBox.setPadding(new Insets(10));
		knoppenBox.setAlignment(Pos.CENTER);

		btnAfsluiten = new Button(resourceBundle.getString("afsluiten"));
		btnAfsluiten.setStyle("-fx-font-weight: bold");
		btnAfsluiten.setOnAction(e -> {
			Stage stage = (Stage) btnAfsluiten.getScene().getWindow();
			stage.close();
		});

		btnBegin = new Button(resourceBundle.getString("terugBegin"));
		btnBegin.setStyle("-fx-font-weight: bold");
		btnBegin.setOnAction(e -> {
			DomeinController dc2 = new DomeinController();
			BeginScherm beginScherm = new BeginScherm(taalkeuze, dc2);
			beginScherm.show();
			Stage stage = (Stage) btnBegin.getScene().getWindow();
			stage.close();
		});

		knoppenBox.getChildren().addAll(btnAfsluiten, btnBegin);
		gridPane.add(knoppenBox, 0, 11, gridPane.getColumnCount(), 1);
	}

	public void show() {
		Stage primaryStage = new Stage();
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/kasteel.jpg")));

		primaryStage.setScene(new Scene(this, 1200, 700));
		primaryStage.setTitle(resourceBundle.getString("eindscherm"));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}