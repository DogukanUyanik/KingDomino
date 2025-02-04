package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domein.Koning;
import domein.Speler;
import domein.Dominotegel;
import domein.Vak;
import utils.Landschapstype;

class KoningTest {

    private Koning koning;
    private Speler speler;
    private Dominotegel dominotegel;

    @BeforeEach
    public void setUp() {
        speler = new Speler("TestSpeler", 2000);
        koning = new Koning(speler);
        Vak vak1 = new Vak(Landschapstype.BOS, 1);
        Vak vak2 = new Vak(Landschapstype.AARDE, 2);
        dominotegel = new Dominotegel(1, vak1, vak2);
    }

    @Test
    void maakKoning_spelerCorrect_maaktObject() {
        assertEquals(speler, koning.getSpeler());
    }

    @Test
    void setSpeler_spelerCorrect_pasSpelerAan() {
        Speler nieuweSpeler = new Speler("NieuweSpeler", 2000);
        koning.setSpeler(nieuweSpeler);
        assertEquals(nieuweSpeler, koning.getSpeler());
    }

   

    @Test
    void setDominotegel_vervangDominotegel_pasDominotegelAan() {
        Vak vak3 = new Vak(Landschapstype.GRAS, 3);
        Vak vak4 = new Vak(Landschapstype.ZAND, 4);
        Dominotegel nieuweDominotegel = new Dominotegel(3, vak3, vak4);
        koning.setDominotegel(nieuweDominotegel);
        assertEquals(nieuweDominotegel, koning.getDominotegel());
    }
}
