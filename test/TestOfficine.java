import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TestOfficine {

    @Test
    void rentrerEtQuantiteAvecSynonymes() {
        Officine officine = new Officine();
        officine.rentrer("3 yeux de grenouille");
        assertEquals(3, officine.quantite("œil de grenouille"));
    }

    @Test
    void preparerRespecteLesStocks() {
        Officine officine = new Officine();
        officine.rentrer("5 larmes de brume funèbre");
        officine.rentrer("2 gouttes de sang de citrouille");

        int produites = officine.preparer("3 fioles de glaires purulentes");

        assertEquals(2, produites);
        assertEquals(2, officine.quantite("fiole de glaires purulentes"));
        assertEquals(1, officine.quantite("larme de brume funèbre"));
        assertEquals(0, officine.quantite("goutte de sang de citrouille"));
    }

    @Test
    void preparationChaineeAvecRecetteComposite() {
        Officine officine = new Officine();
        officine.rentrer("6 radicelles de racine hurlante");
        officine.rentrer("4 larmes de brume funèbre");
        officine.rentrer("2 gouttes de sang de citrouille");

        officine.preparer("2 fioles de glaires purulentes");
        int batons = officine.preparer("2 batons de pâte sépulcrale");

        assertEquals(2, batons);
        assertEquals(0, officine.quantite("fiole de glaires purulentes"));
        assertEquals(0, officine.quantite("radicelle de racine hurlante"));
        assertEquals(2, officine.quantite("baton de pâte sépulcrale"));
    }

    @Test
    void expressionInvalideDeclencheErreur() {
        Officine officine = new Officine();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> officine.rentrer("yeux de grenouille"));
        assertTrue(ex.getMessage().toLowerCase().contains("format"));
    }

    @Test
    void quantiteInconnueRenvoieZeroSansErreur() {
        Officine officine = new Officine();
        assertEquals(0, officine.quantite("inconnu"));
    }

    @Test
    void aliasAvecCasseEtEspacesEstNormalise() {
        Officine officine = new Officine();
        officine.rentrer("4   OEil   de   grenouille");
        assertEquals(4, officine.quantite("Yeux   de   Grenouilles"));
    }

    @Test
    void preparerSansStocksNeConsommeRien() {
        Officine officine = new Officine();

        int produites = officine.preparer("1 fiole de glaires purulentes");

        assertEquals(0, produites);
        assertEquals(0, officine.quantite("fiole de glaires purulentes"));
        // ingrédients toujours à 0
        assertEquals(0, officine.quantite("larme de brume funèbre"));
        assertEquals(0, officine.quantite("goutte de sang de citrouille"));
    }

    @Test
    void preparerCalculeLeMinimumPossible() {
        Officine officine = new Officine();
        officine.rentrer("10 larmes de brume funèbre");
        officine.rentrer("1 goutte de sang de citrouille");

        int produites = officine.preparer("5 fioles de glaires purulentes");

        assertEquals(1, produites);
        assertEquals(1, officine.quantite("fiole de glaires purulentes"));
        assertEquals(8, officine.quantite("larme de brume funèbre"));
        assertEquals(0, officine.quantite("goutte de sang de citrouille"));
    }

    @Test
    void preparerPotionInconnueDeclencheErreur() {
        Officine officine = new Officine();
        officine.rentrer("2 larmes de brume funèbre");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> officine.preparer("1 potion inconnue"));
        assertTrue(ex.getMessage().toLowerCase().contains("inconnu"));
        assertEquals(2, officine.quantite("larmes de brume funèbre"));
    }

    @Test
    void quantiteZeroNeModifiePasLeStock() {
        Officine officine = new Officine();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> officine.rentrer("0 crocs de troll"));
        assertTrue(ex.getMessage().toLowerCase().contains("> 0"));
    }

    @Test
    void quantiteNegativeRefusee() {
        Officine officine = new Officine();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> officine.rentrer("-3 crocs de troll"));
        assertTrue(ex.getMessage().toLowerCase().contains("> 0"));
    }

    @Test
    void formatSansNombreDeclencheErreur() {
        Officine officine = new Officine();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> officine.rentrer("une goutte de sang"));
        assertTrue(ex.getMessage().contains("<nombre>"));
    }

    @Test
    void vueStockImmuable() {
        Officine officine = new Officine();
        officine.rentrer("3 yeux de grenouille");

        Map<String, Integer> snap = officine.getStockSnapshot();
        assertEquals(3, snap.get("œil de grenouille"));

        // immuable
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class,
            () -> snap.put("œil de grenouille", 10));
        assertTrue(ex.getMessage() == null || ex.getMessage().isEmpty());

        // le snapshot ne bouge pas après mutation du stock
        officine.rentrer("1 yeux de grenouille");
        assertEquals(3, snap.get("œil de grenouille"));
        assertEquals(4, officine.quantite("œil de grenouille"));
    }
}
