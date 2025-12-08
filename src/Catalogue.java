import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Catalogue {
    private final Map<String, String> aliasToCanonical = new HashMap<>();
    private final Map<String, Map<String, Integer>> recettes = new HashMap<>();

    Catalogue() {
        registerAliases();
        registerRecettes();
    }

    Quantified parseQuantified(String expression) {
        String cleaned = expression == null ? "" : expression.trim();
        Matcher matcher = Pattern.compile("^([+-]?\\d+)\\s+(.+)$").matcher(cleaned);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Format invalide, attendu '<nombre> <nom>' : " + expression);
        }

        int quantity = Integer.parseInt(matcher.group(1));
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantité doit être > 0 : " + quantity);
        }

        String canonical = canonicalName(matcher.group(2));
        if (canonical == null) {
            throw new IllegalArgumentException("Nom inconnu : " + matcher.group(2));
        }
        return new Quantified(quantity, canonical);
    }

    String canonicalName(String raw) {
        return aliasToCanonical.get(normalizeKey(raw));
    }

    Map<String, Integer> recetteFor(String potionCanonical) {
        return recettes.get(potionCanonical);
    }

    private String normalizeKey(String raw) {
        String lowered = raw.toLowerCase(Locale.ROOT).trim();
        return lowered.replaceAll("\\s+", " ");
    }

    private void registerAliases() {
        addAliases("œil de grenouille", "œil de grenouille", "oeil de grenouille", "yeux de grenouille", "yeux de grenouilles");
        addAliases("larme de brume funèbre", "larme de brume funèbre", "larmes de brume funèbre");
        addAliases("radicelle de racine hurlante", "radicelle de racine hurlante", "radicelles de racine hurlante");
        addAliases("pincée de poudre de lune", "pincée de poudre de lune", "pincées de poudre de lune");
        addAliases("croc de troll", "croc de troll", "crocs de troll");
        addAliases("fragment d'écaille de dragonnet", "fragment d'écaille de dragonnet", "fragments d'écaille de dragonnet", "fragments d'écailles de dragonnet");
        addAliases("goutte de sang de citrouille", "goutte de sang de citrouille", "gouttes de sang de citrouille");

        addAliases("fiole de glaires purulentes", "fiole de glaires purulentes", "fioles de glaires purulentes");
        addAliases("bille d'âme évanescente", "bille d'âme évanescente", "billes d'âme évanescente");
        addAliases("soupçon de sels suffocants", "soupçon de sels suffocants", "soupçons de sels suffocants");
        addAliases("baton de pâte sépulcrale", "baton de pâte sépulcrale", "batons de pâte sépulcrale");
        addAliases("bouffée d'essence de cauchemar", "bouffée d'essence de cauchemar", "bouffées d'essence de cauchemar");
    }

    private void addAliases(String canonical, String... aliases) {
        String canonKey = normalizeKey(canonical);
        for (String alias : aliases) {
            aliasToCanonical.put(normalizeKey(alias), canonKey);
        }
    }

    private void registerRecettes() {
        addRecette("fiole de glaires purulentes",
                "2 larmes de brume funèbre",
                "1 goutte de sang de citrouille");
        addRecette("bille d'âme évanescente",
                "3 pincées de poudre de lune",
                "1 œil de grenouille");
        addRecette("soupçon de sels suffocants",
                "2 crocs de troll",
                "1 fragment d'écaille de dragonnet",
                "1 radicelle de racine hurlante");
        addRecette("baton de pâte sépulcrale",
                "3 radicelles de racine hurlante",
                "1 fiole de glaires purulentes");
        addRecette("bouffée d'essence de cauchemar",
                "2 pincées de poudre de lune",
                "2 larmes de brume funèbre");
    }

    private void addRecette(String potion, String... ingredients) {
        Map<String, Integer> requirements = new HashMap<>();
        for (String ingredient : ingredients) {
            Quantified q = parseQuantified(ingredient);
            requirements.merge(q.name(), q.quantity(), Integer::sum);
        }
        recettes.put(normalizeKey(potion), requirements);
    }

    record Quantified(int quantity, String name) {}
}
