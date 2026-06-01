# PAOJ 2026

Materiale și resurse pentru cursul **Programare Avansată pe Obiecte în Java** — 2026.

---

## Proiect

📁 **[Cerințe proiect individual](src/com/pao/project/README.md)** — Etapa I (24 apr) · Etapa II (5 iun)


## Laboratoare

| Laborator                                          | Subiect                                                          |
|----------------------------------------------------|------------------------------------------------------------------|
| [laboratory01](src/com/pao/laboratory01/Readme.md) | Primul program, array-uri, Scanner                               |
| [laboratory02](src/com/pao/laboratory02/Readme.md) | Clase, încapsulare, Singleton, Comparator                        |
| [laboratory03](src/com/pao/laboratory03/Readme.md) | Moștenire, clase abstracte, interfețe, equals/hashCode, colecții |
| [laboratory04](src/com/pao/laboratory04/Readme.md) | Map, enum-uri, excepții custom                                   |
| [laboratory05](src/com/pao/laboratory05/Readme.md) | Records, Comparable aprofundat, Comparator multiplu              |
| [laboratory06](src/com/pao/laboratory06/Readme.md) | Interfețe și clase — studiu detaliat (Comparable, Comparator, callback, extindere) |
| [laboratory07](src/com/pao/laboratory07/Readme.md) | Sealed classes și enum-uri — concepte avansate                    |
| [laboratory08](src/com/pao/laboratory08/Readme.md) | Interfețe marker, clonare superficială/profundă și introducere fluxuri I/O |
| [laboratory09](src/com/pao/laboratory09/Readme.md) | Fluxuri I/O avansate: DataInputStream/DataOutputStream, RandomAccessFile, ByteBuffer, try-with-resources, Serializable |
| [laboratory10](src/com/pao/laboratory10/Readme.md) | Predicate, compoziție de reguli, Lambda avansat, Comparator complex, Stream API aplicat |
| [laboratory11](src/com/pao/laboratory11/Readme.md) | Collector custom, snapshot imutabil, motor de reguli antifraudă (exerciții) |
| [laboratory12](src/com/pao/laboratory12/Readme.md) | JDBC, persistență, tranzacții și audit |
| [laboratory13](src/com/pao/laboratory13/Readme.md) | Motor de protocol și demo socket |
| [laboratory14](src/com/pao/laboratory14/Readme.md) | Collector custom, SQLite și algoritmi de planificare |

- `laboratory07` a introdus construcțiile mai noi din limbaj: `sealed` classes și un studiu mai aprofundat al `enum`-urilor.
- `laboratory08` acoperă interfețele marker (`Cloneable`, `Serializable`), clonarea superficială vs. profundă și introducerea în fluxuri de I/O (`FileReader`, `BufferedReader`, `BufferedWriter`).
- `laboratory09` va aprofunda fluxurile de I/O: `DataInputStream`/`DataOutputStream`, `RandomAccessFile`, `ByteBuffer`, `try-with-resources` și `Serializable` ca exercițiu obligatoriu.
- `laboratory10` abordează expresii lambda avansate, `Predicate` și compoziția de reguli (exerciții cu motor de reguli), plus utilizarea avansată a `Stream` API-ului și `Comparator`-ilor compuși.
- `laboratory11` se concentrează pe colecții avansate și agregare: implementarea unui `Collector` custom, snapshot-uri imutabile și integrarea unui mic motor de reguli antifraudă (exerciții 1–3).
- `laboratory12` trece la JDBC și persistență: conexiune la bază de date, CRUD, tranzacții și audit.
- `laboratory13` introduce un motor de protocol text și un demo socket multi-client.
- `laboratory14` închide seria cu un alt `Collector` custom, persistare SQLite și o problemă clasică de planificare cu `PriorityQueue`.

Începând cu **laboratory04**, soluțiile se trimit pe GitHub la un fork personal al acestui repo.
**Data limită:** vineri, ora 23:59, în fiecare săptămână.

---

### TODO NOUTATE laboratory06-laboratory14: verificarea automata a calculelor din exercițiile obligatorii

You have a jar in the root folder, under ./utils:

[java-diff-utils-4.15.jar](src/com/pao/test/utils/java-diff-utils-4.15.jar)

A .jar file si like an archive of an library, in this case com.github.difflib.

Steps needed to run the tests:


1. In IntelliJ

   > Right-click the jar
   >
   > → "Add as Library"
   >
   > → Select your project Module
   >
   > → Ok (button).

2. In VS Code: Update your .classpath or launch config if needed.
    - Example .classpath entry:
   ```xml
   <classpathentry kind="lib" path="src/com/pao/test/utils/java-diff-utils-4.15.jar"/>
   ```
    - Example launch config:
    ```json
    {
        "type": "java",
        "name": "Launch Main",
        "request": "launch",
        "mainClass": "com.pao.laboratory04.Main",
        "classpath": [
            "src/com/pao/test/utils/java-diff-utils-4.15.jar"
        ]
    }
    ```
   >  TODO !!! inca lucrez pentru a gasi o solutie clara pentru VS Code.


3. How to run in terminal?

#### Windows PowerShell
```powershell
# Compile all files in any folder (replace PATH with src or src\com\pao\laboratory06, etc.)
javac -d output @((Get-ChildItem -Recurse -Filter "*.java" -Path PATH).FullName)

# With JAR (for tests)
javac -d output -cp "lib\java-diff-utils-4.15.jar" @((Get-ChildItem -Recurse -Filter "*.java" -Path PATH).FullName)

# Run
java -cp output com.pao.laboratory06.exercise1.Main
java -cp "output;lib\java-diff-utils-4.15.jar" com.pao.laboratory06.exercise1.Test
```

#### macOS / Linux / WSL
```bash
# Compile all files in any folder (replace PATH with src or src/com/pao/laboratory06, etc.)
javac -d output $(find PATH -name "*.java" -type f)

# With JAR (for tests)
javac -d output -cp "lib/java-diff-utils-4.15.jar" $(find PATH -name "*.java" -type f)

# Run
java -cp output com.pao.laboratory06.exercise1.Main
java -cp "output:lib/java-diff-utils-4.15.jar" com.pao.laboratory06.exercise1.Test
```

⚠️ **Key:** Replace `PATH` with your desired folder — includes all subdirectories automatically.
- Full project: `src`
- Single lab: `src/com/pao/laboratory06`
- Single exercise: `src/com/pao/laboratory06/exercise1`

---
## Revenind la trimiterea solutiilor


Mai jos găsești:

0. Ce sa aveti deja instalat (TODO adauga link)
1. [Cum trimiți soluțiile](#1-cum-trimiți-soluțiile) — fork, configurare remotes, commit săptămânal
2. [Formularul de înregistrare](#2-completați-url-ul-fork-ului) — link fork personal
3. [Punctarea laboratoarelor](#3-punctarea-laboratoarelor) — prezență, obligatoriu, bonus



### 0. Ce sa aveti deja instalat pe calculatoare

- ✅ Cont pe [github.com](https://github.com) (gratuit)
- ✅ Git instalat — verifică cu `git --version` ([descarcă de aici](https://git-scm.com/downloads) dacă nu ai)
- ✅ Autentificare configurată — [GitHub CLI](https://cli.github.com/) (`gh auth login`) sau SSH key

Dacă vrei să te conectezi ușor la GitHub din terminal, recomand să instalezi și
configurezi [GitHub CLI](https://cli.github.com/):

După aceea, scrieti:

```bash
gh auth login
```

Apasati Enter, Enter, Y, Enter, Enter, și te autentifici în browser.
După ce te întorci în terminal, ar trebui să vezi mesajul "Logged in to github.com as USERNAME".

### 1. Cum trimiți soluțiile

> 🎬 **Video tutorial:**
>
> Partea 1 - fork și setarea a două "remote-uri"
> (o singură dată la începutul semestrului)
>
> https://youtu.be/ICJUYkHkWr4
>
> Partea 2 - flux săptămânal pentru fiecare laborator
>
> https://youtu.be/a27-0an-bTo

---

#### Partea 1 - Configurare inițială (o singură dată)

**1. Salvează-ți munca curentă (dacă ai folosit Git)**

```bash
git add .
git commit -m "Salvare progres înainte de reconfigurare"
```

> Dacă nu ai folosit Git până acum, poți sări peste acest pas.

**2. Creează fork-ul pe GitHub:**

- Deschide [https://github.com/stefaneduard-deaconu/paoj-2026](https://github.com/stefaneduard-deaconu/paoj-2026)
- Click **Fork** (dreapta sus) → **Create fork**
- debifează opțiunea de a include doar `main` (dacă e bifată)
- Acum ai `https://github.com/USERNAME-TĂU/paoj-2026` pe contul tău

**3. Dacă nu ai folosit încă Git, clonează fork-ul tău.

Adică în contul tău de github găsești repo-ul paoj-2026, iei URL-ul
(va arăta așa https://github.com/USERNAME-TĂU/paoj-2026.git)

```bash
git clone https://github.com/USERNAME-TĂU/paoj-2026.git
# si apoi din IntelliJ sau Code deschizi folderul paoj-2026
```

**4. Configurezi două remote-uri (repo-ul laboratorului, repo-ul tău)**

remote = URL către un repo Git

După acest pas, vei avea două remote-uri:

- `upstream` — repo-ul original al cursului (https://github.com/stefaneduard-deaconu/paoj-2026.git)
- `origin` — fork-ul tău (https://github.com/USERNAME-TĂU/paoj-2026.git)

```bash
git remote add upstream https://github.com/stefaneduard-deaconu/paoj-2026.git
git remote set-url upstream https://github.com/stefaneduard-deaconu/paoj-2026.git
git remote add origin https://github.com/USERNAME-TĂU/paoj-2026.git
git remote set-url origin https://github.com/USERNAME-TĂU/paoj-2026.git
```

> De ce toate 4?
> - `add` adaugă un nou remote, dar nu face nimic dacă există deja
> - `set-url` setează URL-ul remote-ului, necesar dacă în origin ai deja URL-ul cursului în loc de fork-ul tău

**5. Verifică:**

```bash
git remote -v
# origin    https://github.com/USERNAME-TĂU/paoj-2026.git             (fork-ul tău)
# upstream  https://github.com/stefaneduard-deaconu/paoj-2026.git     (repo-ul cursului)
```

✅ **Gata!** Ai acum un repo local conectat la două remote-uri: `origin` (fork-ul tău) și `upstream` (repo-ul cursului).

#### Partea 2 - Flux săptămânal

**1. Preiei branch-ul nou de pe `upstream`:**

> Vei folosi `lab5` în loc de `labX` pentru laboratory04, `lab6` pentru laboratory05 etc.

```bash
git fetch upstream lab5   # înlocuiește X cu numărul lab (ex: lab04)
git checkout -b lab5 --track upstream/lab5
git push -u origin lab5 
```

> Comenzile de sus fac următoarele:
> - `fetch` aduce branch-ul nou de la upstream
> - `checkout -b` creează un nou branch local numit `labX` care urmărește `upstream/labX`

**2. Lucrează** la exerciții — creează clase, completează TODO-uri.

**3. Salvează și trimiți soluția:**

```bash
git add .
git commit -m "LabX: exercitiile 1-4 completate"
git push origin labX
```

### 2. Completați URL-ul fork-ului

Trimite link-ul fork-ului pe formularul următor, ca să știm cui oferim punctajul:

[PAOJ 2026 - Alegerea proiectului si Incarcarea activitatii](https://forms.gle/zKPvTiP3oTJrxhR19)

### 3. Punctarea laboratoarelor

#### Structura notei finale

| Componentă              | Pondere |
|-------------------------|---------|
| Proiect individual      | 50%     |
| Laboratoare (12 din 14 fara bonus, 10/11 cu 10/5 bonusuri) | 25%     |
| Activitate și prezență  | 25%     |

#### Prezență

- **12 prezențe obligatorii** din 14 laboratoare (sau 11+5bonusuri, sau 10+10 bonusuri)
- Laburile 1–3 sunt punctate pentru prezență + soluție completă
- La Lab 04, exercițiul bonus era opțional — absența lui nu scade punctajul

#### Laboratoarele 4–14

Fiecare laborator valorează **2.08(3)%** din nota finală:

| Ce rezolvi                       | Punctaj                   |
|----------------------------------|---------------------------|
| Prezență + exerciții obligatorii | 2.08(3)% (practic 25%/12) |
| Exercițiul bonus                 | 0.4%                      |

#### Prezenta

-> se deduce din submit-ul saptamanal, plus prezentarea o data la 2 saptamani a ce ati lucrat (online/fizic)

#### Punctajul (TODO sa revin cu rezumat la fiecare saptamana in parte)

-> in laboratoarele 4-14 aveti si exercitii bonus, care valoreaza 2.5% din punctajul total al prezentei+activitate.

<br />
<br />
---
Urmatoare sectiune contine doar intrebari adresate de voi ca studenti, si raspunsurile pe care le-ati gasit sau le-am gasit impreuna.
---
<br />
<br />

## Întrebări frecvente (FAQ)

### 1. Cum pot să obțin un job pe un proiect Java?

Cel mai important lucru în prezent este **Spring Boot** — frameworkul dominant pentru aplicații enterprise Java, cerut
în marea majorității anunțurilor de angajare.

Pe lângă asta, **ingineria cloud** e esențială. Certificările **AWS** sunt foarte apreciate și cresc șansele de
angajare — un domeniu în care investesc și eu.

**Pe scurt:**

- **Spring Boot** — aplicații backend Java solide
- **Certificare AWS** — competențe cloud

---

### 2. Pot îmbina mai mulți comparatori în `Arrays.sort()` pentru a sorta după multiple criterii?

Da, folosind **`thenComparing()`** (Java 8+). Dacă primul comparator consideră elemente egale, se trece la următorul
criteriu.

**Metode principale:**

- `Comparator.comparing()` — primul criteriu
- `.thenComparing()` — criteriu secundar (la egalitate)
- `.reversed()` — inversează ordinea

**Exemplu:**

```java
listaAngajati.sort(
        Comparator.comparing(Angajat::getNume)
              .

thenComparing(Angajat::getVarsta)
);
```

**Variante utile:**

- Inversare: `Comparator.comparing(Angajat::getNume, Comparator.reverseOrder())`
- Valori null: `nullsFirst()` / `nullsLast()`
- Performanță: `thenComparingInt()` / `thenComparingLong()` evită autoboxing

### 3. Cum rulez Java din terminal?

**Am Java instalat?**

```bash
java -version
javac -version
```

Dacă primești un număr de versiune (ex: `21.0.x`), ești pregătit.
Dacă nu, descarcă JDK de la [adoptium.net](https://adoptium.net/).

**Compilare și rulare:**

```bash
javac NumeleFisierului.java   # generează NumeleFisierului.class
java NumeleFisierului         # fără extensia .class
```

**Clasa are `package`? Lucrează din `src/`:**

```bash
cd src
javac com/pao/laboratory01/Main.java
java com.pao.laboratory01.Main
```

> Compilarea folosește `/` (sau `\` pe Windows), rularea folosește `.` (puncte).

**Rezumat rapid:**

| Acțiune                           | Comandă                         |
|-----------------------------------|---------------------------------|
| Verificare Java                   | `java -version`                 |
| Compilare (fără pachet)           | `javac Main.java`               |
| Rulare (fără pachet)              | `java Main`                     |
| Compilare (cu pachet, din `src/`) | `javac com/pao/lab01/Main.java` |
| Rulare (cu pachet, din `src/`)    | `java com.pao.lab01.Main`       |

