package main;

import cui.KingDominoApp;
import domein.DomeinController;

public class StartUp {

	public static void main(String[] args) {
		DomeinController dc = new DomeinController();
        KingDominoApp app = new KingDominoApp(dc);
        app.start();
	}

}
