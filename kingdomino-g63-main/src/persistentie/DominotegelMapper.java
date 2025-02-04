package persistentie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import domein.Dominotegel;
import domein.Vak;
import utils.Landschapstype;

public class DominotegelMapper {

    private static final String SELECT_DOMINOTEGEL = "SELECT * FROM ID372728_G63.dominotegel";

    public List<Dominotegel> haalAlleDominotegelsOp() {
        List<Dominotegel> dominotegels = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
             PreparedStatement query = conn.prepareStatement(SELECT_DOMINOTEGEL);
             ResultSet rs = query.executeQuery()) {

            while (rs.next()) {
                int nummer = rs.getInt("nummer");
                String landschapstypeLinks = rs.getString("landschapstypeLinks");
                String landschapstypeRechts = rs.getString("landschapstypeRechts");
                int aantalKronenLinks = rs.getInt("aantalKronenLinks");
                int aantalKronenRechts = rs.getInt("aantalKronenRechts");

                Vak vak1 = new Vak(Landschapstype.valueOf(landschapstypeLinks), aantalKronenLinks);
                Vak vak2 = new Vak(Landschapstype.valueOf(landschapstypeRechts), aantalKronenRechts);

                Dominotegel dominotegel = new Dominotegel(nummer, vak1, vak2);
                dominotegels.add(dominotegel);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return dominotegels;
    }   

}


    
