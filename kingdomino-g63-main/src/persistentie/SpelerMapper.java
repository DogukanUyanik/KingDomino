package persistentie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domein.Speler;

public class SpelerMapper {

	private static final String INSERT_SPELER = "INSERT INTO ID372728_G63.speler (gebruikersnaam, geboortejaar, aantalGewonnen, aantalGespeeld)"
			+ "VALUES (?, ?, ?, ?)";
	private static final String UPDATE_SPELER_STATS = "UPDATE ID372728_G63.speler SET aantalGewonnen = ?, aantalGespeeld = ? WHERE gebruikersnaam = ?";

	public void voegToe(Speler speler) {
		try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
				PreparedStatement query = conn.prepareStatement(INSERT_SPELER)) {
			query.setString(1, speler.getGebruikersnaam());
			query.setInt(2, speler.getGeboortejaar());
			query.setInt(3, speler.getAantalGewonnen());
			query.setInt(4, speler.getAantalGespeeld());

			query.executeUpdate();

		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Speler geefSpeler(String gebruikersnaam) {
		Speler speler = null;

		try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
				PreparedStatement query = conn
						.prepareStatement("SELECT * FROM ID372728_G63.speler WHERE gebruikersnaam = ?")) {
			query.setString(1, gebruikersnaam);
			try (ResultSet rs = query.executeQuery()) {
				if (rs.next()) {
					int geboortejaar = rs.getInt("geboortejaar");
					int aantalGewonnen = rs.getInt("aantalGewonnen");
					int aantalGespeeld = rs.getInt("aantalGespeeld");

					speler = new Speler(gebruikersnaam, geboortejaar, aantalGewonnen, aantalGespeeld);
				}
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return speler;
	}

	public List<String> haalAlleGebruikersnamenOp() {
		List<String> gebruikersnamen = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
				PreparedStatement query = conn.prepareStatement("SELECT gebruikersnaam FROM ID372728_G63.speler")) {

			try (ResultSet rs = query.executeQuery()) {
				while (rs.next()) {
					gebruikersnamen.add(rs.getString("gebruikersnaam"));
				}
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return gebruikersnamen;
	}

	

	public void updateSpelerGegevens(Speler speler) {
	    try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
	         PreparedStatement query = conn.prepareStatement(UPDATE_SPELER_STATS)) {
	        query.setInt(1, speler.getAantalGewonnen());
	        query.setInt(2, speler.getAantalGespeeld());
	        query.setString(3, speler.getGebruikersnaam());

	        query.executeUpdate();
	    } catch (SQLException ex) {
	        throw new RuntimeException(ex);
	    }
	}

}
