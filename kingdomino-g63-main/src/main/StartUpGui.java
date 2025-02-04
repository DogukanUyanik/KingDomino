package main;

import gui.TaalKeuzeScherm;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartUpGui extends Application {

	@Override
	public void start(Stage primaryStage) {
		Image icoontje = new Image(getClass().getResourceAsStream("/images/kasteel.jpg"));
		primaryStage.getIcons().add(icoontje);
		Font.loadFont(getClass().getResourceAsStream("resources/fonts/MedievalSharp.ttf"), 14);
		TaalKeuzeScherm taalKeuzeScherm = new TaalKeuzeScherm();

		VBox root = new VBox();
		root.getChildren().add(taalKeuzeScherm);

		Scene scene = new Scene(root, 600, 300);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Kingdomino");
		primaryStage.setResizable(false);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
