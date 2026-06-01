# Laboratorul 14 — Platformă E-Ticketing: Colector Custom, SQLite și Algoritmi de Planificare

> **Pachet:** `com.pao.laboratory14`
> **Tip:** laborator complet (2 exerciții obligatorii + 1 bonus)
> **Data limită:** joi, 4 iunie 2026, ora 23:59

---

## Noțiuni noi față de laboratoarele anterioare

### Colector custom (`Collector.of`)

Lab10 bonus a introdus `Collectors.groupingBy`. Acum implementezi propriul `Collector<T, A, R>` cu accumulator propriu și **finisher** care produce un obiect de domeniu imutabil — imposibil de obținut cu colectorii standard.

### JDBC + SQLite

Prima interacțiune directă cu o bază de date relațională. SQLite nu necesită server — fișierul este baza de date. Structura de cod (`DatabaseConnection` Singleton, interfață generică `Repository<T, ID>`, `PreparedStatement`, `try-with-resources`) este **identică cu cea cerută la Proiectul Individual Etapa II** — considero un rehearsal.

### `PriorityQueue` și planificare pe intervale

Structură de date nefolosită anterior în laboratoare. Rezolvă clasica problemă **„Minimum Meeting Rooms"** (frecventă la interviuri Google, Amazon, Meta) în O(N log N) față de O(N²) brut.

---

## Cerință prealabilă — JAR SQLite în classpath

1. Adaugă `lib/sqlite-jdbc-3.47.1.0.jar` în IntelliJ:
   `File → Project Structure → Libraries → + → Java → selectează lib/sqlite-jdbc-3.47.1.0.jar`

2. Marchează `exercise2/resources/` ca **Resources Root**:
   clic dreapta pe director → `Mark Directory as → Resources Root`

> `lib/mysql-connector-j-9.2.0.jar` este disponibil pentru **Proiect Etapa II** (MySQL).

---

## Exerciții

| # | Pachet | Concept principal | Timp estimat | Teste automate |
|---|--------|-------------------|--------------|----------------|
| 1 | [`exercise1/`](exercise1/Readme.md) | Colector custom `RaportVanzari` — `Collector.of`, finisher imutabil | ~40 min | ✓ (2 părți) |
| 2 | [`exercise2/`](exercise2/Readme.md) | Persistență SQLite — `DatabaseConnection` Singleton, `Repository<T,ID>`, `PreparedStatement` | ~45 min | ✓ (flat) |
| 3 *(bonus)* | [`exercise3/`](exercise3/Readme.md) | Alocare săli cu `PriorityQueue` — algoritm clasic de interviu | ~30 min | manual |

> **Total estimat:** ~1h25 min (fără bonus) · ~1h55 min (cu bonus)

---

## Cum rulezi testele automate

Deschide `Checker.java` din exercițiul dorit și apasă **Run** în IntelliJ.

**Director de lucru:** rădăcina proiectului (`paoj-2026/`):
`Run → Edit Configurations → Working directory → $PROJECT_DIR$`

- **exercise1** — teste pe 2 părți (`partA`, `partB`); Checker apelează `IOTest.runParts`.
- **exercise2** — teste plate (`1.in`/`1.out`, ...); Checker apelează `IOTest.runFlat`.
  > Fiecare rulare a `Main.main()` resetează baza de date (`DROP TABLE IF EXISTS` la start) — testele sunt deterministe.

---

## Diferența dintre runParts și runFlat

- `exercise1` folosește `tests/partA/`, `tests/partB/` → `IOTest.runParts(...)`
- `exercise2` folosește fișiere plate `tests/N.in` + `tests/N.out` → `IOTest.runFlat(...)`

---

## Fișiere din acest laborator

| Fișier | Rol |
|--------|-----|
| `exercise1/Readme.md` | Cerințe exercițiu 1 |
| `exercise1/Main.java` | Implementează cerința (completează TODO-urile) |
| `exercise1/Checker.java` | Rulează testele automate pentru exercițiul 1 |
| `exercise2/Readme.md` | Cerințe exercițiu 2 |
| `exercise2/Main.java` | Implementează cerința (completează TODO-urile) |
| `exercise2/Checker.java` | Rulează testele automate pentru exercițiul 2 |
| `exercise2/resources/db.properties` | Configurare conexiune SQLite |
| `exercise2/util/DatabaseConnection.java` | Singleton conexiune DB |
| `exercise2/repository/Repository.java` | Interfață generică repository |
| `exercise2/repository/EvenimentRepository.java` | Implementare concretă (completează TODO-urile) |
| `exercise3/Readme.md` | Cerințe exercițiu bonus |
| `exercise3/Main.java` | Demonstrație completă (alocare săli) |

