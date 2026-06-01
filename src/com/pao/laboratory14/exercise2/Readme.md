# Exercițiul 2 — Persistență Evenimente cu SQLite

> **Pachet:** `com.pao.laboratory14.exercise2`
> **Timp estimat:** ~45 min · **Teste automate:** da (`Checker.java`, flat)

---

## Scenariu

Platforma de e-ticketing are nevoie să persisteze evenimentele într-o bază de date relațională. Vei implementa stratul de persistență cu **SQLite** — o bază de date embedded, fără server, perfectă pentru prototipuri.

Structura pe care o construiești (`DatabaseConnection` Singleton, interfață generică `Repository<T, ID>`, repository concret cu `PreparedStatement`, `try-with-resources`) este **identică cu cea cerută la Proiectul Individual Etapa II** — considero un rehearsal.

---

## Import din exercițiul 1

```java
import com.pao.laboratory14.exercise1.TipBilet;
```

---

## Structura fișierelor

```
exercise2/
  Main.java
  model/
    Eveniment.java
  util/
    DatabaseConnection.java       ← citește db.properties, expune Connection
  repository/
    Repository.java               ← interfață generică <T, ID>
    EvenimentRepository.java      ← implementare concretă
  resources/
    db.properties                 ← marchează ca Resources Root în IntelliJ!
```

---

## Configurare SQLite

**`resources/db.properties`:**

```properties
db.url=jdbc:sqlite:output/lab14_ex2.db
db.user=
db.password=
```

> SQLite creează automat `output/lab14_ex2.db` dacă nu există.
> Calea este relativă la directorul de lucru (`paoj-2026/`).

---

## Clasele de implementat

### `Eveniment` (model)
Câmpuri: `id` (int, generat de DB), `nume` (String), `data` (String, `yyyy-MM-dd`), `capacitate` (int), `tip` (TipBilet).

### `DatabaseConnection` (Singleton)
Citește `db.url`, `db.user`, `db.password` din `db.properties` via classpath.
Expune `getConnection() : Connection`.

### `Repository<T, ID>` (interfață generică)
Metode: `save`, `findById`, `findAll`, `update`, `delete` — aceeași semnătură ca în README-ul proiectului.

### `EvenimentRepository` (implementare)
- `initSchema()` — execută `DROP TABLE IF EXISTS` + `CREATE TABLE` la pornire (determinism teste)
- `save(Eveniment)` — INSERT cu `PreparedStatement`; setează `id`-ul generat pe obiect
- `findAll()` — `SELECT * ORDER BY id`
- `deleteImpl(int id)` — returnează numărul de rânduri șterse (0 = nu există)
- `count()` — `SELECT COUNT(*)`

> **Referință JDBC:** pentru `PreparedStatement`, `ResultSet`, `try-with-resources` — consultă **README-ul proiectului, secțiunea Etapa II**.

---

## Tabel SQLite

```sql
CREATE TABLE IF NOT EXISTS evenimente (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    nume       TEXT    NOT NULL,
    data       TEXT    NOT NULL,
    capacitate INTEGER,
    tip        TEXT
)
```

---

## Format input

Comenzi citite din stdin până la EOF:

```
COMANDA [argumente]
...
(EOF)
```

## Format output

| Comandă | Output |
|---------|--------|
| `ADD nume data capacitate tip` | `Adaugat: [id] nume` |
| `LIST` | Câte o linie per eveniment (format mai jos); nimic dacă tabelul e gol |
| `DELETE id` | `Sters: id` sau `Nu exista: id` |
| `COUNT` | `Total: N` |

**Format linie eveniment:**

```
[id] nume | data | cap=capacitate | tip
```

---

## Exemplu

```
Input:                                    Output:
ADD Coldplay 2026-06-15 50000 VIP         Adaugat: [1] Coldplay
ADD Festival 2026-07-10 10000 STANDARD    Adaugat: [2] Festival
LIST                                      [1] Coldplay | 2026-06-15 | cap=50000 | VIP
COUNT                                     [2] Festival | 2026-07-10 | cap=10000 | STANDARD
                                          Total: 2
```

---

## Hint-uri

- `DriverManager.getConnection(url)` — pentru SQLite, `user` și `password` sunt irelevante (șiruri goale)
- `PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)` — INSERT cu recuperare ID generat
- `ps.getGeneratedKeys().getInt(1)` — ID-ul nou creat
- `ps.executeUpdate()` returnează numărul de rânduri afectate — util pentru DELETE
- Stochează `tip` ca `String` în DB (`tip.name()`); la citire: `TipBilet.valueOf(rs.getString("tip"))`
- `try-with-resources` pe `PreparedStatement` și `ResultSet` — **conexiunea rămâne deschisă**

