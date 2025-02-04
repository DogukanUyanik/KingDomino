package testen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import domein.Vak;
import utils.Landschapstype;

class VakTest {

    @Test
    void maakVak_metCorrecteGegevens_maaktObject() {
        Vak vak = new Vak(Landschapstype.BOS, 3);
        Assertions.assertEquals(Landschapstype.BOS, vak.getLandschapstype());
        Assertions.assertEquals(3, vak.getKronen());
        Assertions.assertFalse(vak.isBezocht());
    }

    @Test
    void setLandschapstype_wijzigtLandschapstype() {
        Vak vak = new Vak(Landschapstype.AARDE, 2);
        vak.setLandschapstype(Landschapstype.ZAND);
        Assertions.assertEquals(Landschapstype.ZAND, vak.getLandschapstype());
    }

    @Test
    void setKronen_wijzigtKronen() {
        Vak vak = new Vak(Landschapstype.GRAS, 4);
        vak.setKronen(5);
        Assertions.assertEquals(5, vak.getKronen());
    }

    @Test
    void setBezocht_wijzigtBezochtStatus() {
        Vak vak = new Vak(Landschapstype.WATER, 1);
        vak.setBezocht(true);
        Assertions.assertTrue(vak.isBezocht());
    }
}
