import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Officine {
    private final Map<String, Integer> stock = new HashMap<>();
    private final Catalogue catalogue = new Catalogue();

    public Officine() {}

    public void rentrer(String expression) {
        Catalogue.Quantified q = catalogue.parseQuantified(expression);
        stock.merge(q.name(), q.quantity(), Integer::sum);
    }

    public int quantite(String nom) {
        String canonical = catalogue.canonicalName(nom);
        if (canonical == null) {
            return 0;
        }
        return stock.getOrDefault(canonical, 0);
    }

    public int preparer(String expression) {
        Catalogue.Quantified demande = catalogue.parseQuantified(expression);
        Map<String, Integer> recette = catalogue.recetteFor(demande.name());
        if (recette == null) {
            return 0;
        }

        int possible = demande.quantity();
        for (Map.Entry<String, Integer> entry : recette.entrySet()) {
            int disponible = stock.getOrDefault(entry.getKey(), 0);
            possible = Math.min(possible, disponible / entry.getValue());
        }

        if (possible <= 0) {
            return 0;
        }

        for (Map.Entry<String, Integer> entry : recette.entrySet()) {
            int nouveauStock = stock.getOrDefault(entry.getKey(), 0) - entry.getValue() * possible;
            stock.put(entry.getKey(), nouveauStock);
        }

        stock.merge(demande.name(), possible, Integer::sum);
        return possible;
    }

    public Map<String, Integer> getStockSnapshot() {
        return Collections.unmodifiableMap(new HashMap<>(stock));
    }
}
