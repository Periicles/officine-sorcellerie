# Officine de Sorcellerie (Halloween)

Petit kata Java/Maven pour gérer une officine qui stocke des ingrédients et prépare des potions.

## Architecture

- `pom.xml` : config Maven (Java 17, JUnit 5), sources dans `src`, tests dans `test`.
- `src/Main.java` : point d'entrée console minimal.
- `src/Officine.java` : gestion des stocks et préparation.
- `src/Catalogue.java` : alias, normalisation, recettes, parsing des quantités.
- `test/TestOfficine.java` : tests JUnit 5 (cas usuels, erreurs, quantités invalides, snapshot immuable).

## Fonctionnalités clés

- Aliases et normalisation (casse/espaces) pour ingrédients et potions.
- Validation des expressions `<nombre> <nom>` avec quantité strictement > 0 et messages distincts (format vs nom inconnu).
- Préparation respecte les stocks et met à jour les quantités ; recettes composites supportées.
- `getStockSnapshot()` expose une vue immuable du stock courant.

## Exécution

```bash
mvn test
mvn exec:java -Dexec.mainClass=Main
```
