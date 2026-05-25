# Exercise 1 - Server de Mesagerie Text

## Scenariu

Implementezi motorul de protocol pentru un server de mesagerie text.
Motorul gestioneaza o sesiune cu stari explicite si raspunde la fiecare comanda
cu o linie de output determinista.
Testele automate valideaza parserul, tranzitiile de stare si comenzile compuse.

---

## Stari de sesiune

```
INIT  →  AUTH  →  OPEN  →  CLOSED
              ↑    |
              └────┘  (re-AUTH din OPEN)
```

| Stare | Descriere                                                                       |
|---|---------------------------------------------------------------------------------|
| `INIT` | Starea initiala la pornire. Nu s-a autentificat nimeni.                         |
| `AUTH` | Utilizatorul s-a autentificat. Sesiunea nu e inca deschisa.                     |
| `OPEN` | Sesiunea e activa. Se pot trimite mesaje.                                       |
| `CLOSED` | Sesiunea a fost inchisa. Nu mai accepta nicio comanda (deci e stare terminala). |

---

## Referinta comenzi

| Comanda | Sintaxa | Stare necesara | Output succes | Erori posibile |
|---|---|---|---|---|
| `AUTH` | `AUTH <user>` | orice, **exceptand** `CLOSED` | `OK AUTH user=<user>` | lipseste `<user>` → `ERR E_PARSE AUTH`; stare `CLOSED` → `ERR E_STATE CLOSED` |
| `OPEN` | `OPEN` | `AUTH` | `OK OPEN` | argumente extra → `ERR E_PARSE OPEN`; stare `OPEN` → `ERR E_STATE ALREADY_OPEN`; stare `CLOSED` → `ERR E_STATE CLOSED`; stare `INIT` → `ERR E_STATE NOT_OPEN` |
| `SEND` | `SEND <payload>` | `OPEN` | `OK OPEN sent` | lipseste `<payload>` → `ERR E_PARSE SEND`; stare `CLOSED` → `ERR E_STATE CLOSED`; orice alta stare → `ERR E_STATE NOT_OPEN` |
| `BROADCAST` | `BROADCAST <payload>` | `OPEN` | `OK OPEN broadcast` | lipseste `<payload>` → `ERR E_PARSE BROADCAST`; stare `CLOSED` → `ERR E_STATE CLOSED`; orice alta stare → `ERR E_STATE NOT_OPEN` |
| `HISTORY` | `HISTORY` | `OPEN` | `OK OPEN history=<N>` | argumente extra → `ERR E_PARSE HISTORY`; stare `CLOSED` → `ERR E_STATE CLOSED`; orice alta stare → `ERR E_STATE NOT_OPEN` |
| `CLOSE` | `CLOSE` | `OPEN` | `OK CLOSED` | argumente extra → `ERR E_PARSE CLOSE`; stare `CLOSED` → `ERR E_STATE CLOSED`; stare `INIT` sau `AUTH` → `ERR E_STATE NOT_OPEN` |

> **Comanda necunoscuta**: orice alt token → `ERR E_PARSE UNKNOWN_COMMAND`

---

## Coduri de eroare

| Cod | Semnificatie |
|---|---|
| `E_PARSE AUTH` | Comanda `AUTH` fara argument (ex: `AUTH` singur) |
| `E_PARSE OPEN` | Comanda `OPEN` cu argumente extra (ex: `OPEN now`) |
| `E_PARSE SEND` | Comanda `SEND` fara payload |
| `E_PARSE BROADCAST` | Comanda `BROADCAST` fara payload |
| `E_PARSE HISTORY` | Comanda `HISTORY` cu argumente extra |
| `E_PARSE CLOSE` | Comanda `CLOSE` cu argumente extra |
| `E_PARSE UNKNOWN_COMMAND` | Token necunoscut |
| `E_STATE CLOSED` | Sesiunea e deja `CLOSED`; nicio comanda nu mai e acceptata |
| `E_STATE NOT_OPEN` | Comanda necesita starea `OPEN`, dar sesiunea nu e inca deschisa (sau e inchisa) |
| `E_STATE ALREADY_OPEN` | `OPEN` emis iar desi sesiunea e deja in starea `OPEN` |

---

## Nota: historyCount

`HISTORY` returneaza numarul de mesaje trimise in sesiunea curenta.
- Se incrementeaza la fiecare `SEND` sau `BROADCAST` reusit (in starea `OPEN`).
- Se reseteaza la `0` la fiecare `AUTH` (inclusiv re-AUTH din `OPEN`).

### Re-AUTH din OPEN
`AUTH` poate fi apelat si din starea `OPEN` — sesiunea se intoarce in starea `AUTH`
si historyCount se reseteaza. Aceasta permite re-autentificarea fara a reporni motorul.

---

## Exemple per comanda

### AUTH — succes
```
Input:  AUTH alice
Output: OK AUTH user=alice
```

### AUTH — lipsa argument
```
Input:  AUTH
Output: ERR E_PARSE AUTH
```

### AUTH — din starea CLOSED
```
Input:  (stare CLOSED dupa CLOSE anterior) AUTH bob
Output: ERR E_STATE CLOSED
```

### OPEN — succes (din AUTH)
```
Input:  OPEN
Output: OK OPEN
```

### OPEN — din INIT (fara AUTH anterior)
```
Input:  OPEN
Output: ERR E_STATE NOT_OPEN
```

### OPEN — deja deschis
```
Input:  (stare OPEN) OPEN
Output: ERR E_STATE ALREADY_OPEN
```

### SEND — succes
```
Input:  SEND hello world
Output: OK OPEN sent
```

### SEND — din AUTH (sesiune nedeschisa)
```
Input:  (stare AUTH) SEND hi
Output: ERR E_STATE NOT_OPEN
```

### BROADCAST — succes
```
Input:  BROADCAST ping
Output: OK OPEN broadcast
```

### HISTORY — succes (dupa 2 mesaje: 1 SEND + 1 BROADCAST)
```
Input:  HISTORY
Output: OK OPEN history=2
```

### CLOSE — succes
```
Input:  CLOSE
Output: OK CLOSED
```

### CLOSE — din AUTH
```
Input:  (stare AUTH) CLOSE
Output: ERR E_STATE NOT_OPEN
```

### Comanda necunoscuta
```
Input:  OPENX
Output: ERR E_PARSE UNKNOWN_COMMAND
```

---

## Format input

```
Q
comanda_1
comanda_2
...
comanda_Q
```

- Prima linie: numarul `Q` de comenzi.
- Urmatoarele `Q` linii: cate o comanda per linie.
- Liniile goale sunt ignorate la parsare.
- Token-urile sunt separate prin spatii (unul sau mai multe).

---

## Format output

Pentru fiecare comanda se tipareste exact o linie:
- Succes: `OK <state_curent> <mesaj>` sau `OK <state_nou>` (pentru CLOSE: `OK CLOSED`)
- Eroare: `ERR <cod_eroare> <detaliu>`

---

## Parti

### Partea A — Parsare si sintaxa

Testeaza detectia erorilor de sintaxa (`E_PARSE`) independent de starea sesiunii.

Scenarii acoperite:
- Flux complet valid (AUTH → OPEN → SEND → BROADCAST → CLOSE).
- Comanda fara argument obligatoriu (`AUTH` fara user).
- Token necunoscut (`OPENX`).
- AUTH valid dupa erori de parsare (motorul continua sa functioneze).

### Partea B — Tranzitii de stare

Testeaza erorile de stare (`E_STATE`) — comenzi emise in momentul gresit.

Scenarii acoperite:
- `SEND` inainte de AUTH si OPEN (`NOT_OPEN`).
- `OPEN` emis de doua ori (`ALREADY_OPEN`).
- Comanda dupa `CLOSE` (`CLOSED` este terminal).
- Secventa completa cu re-validare stare.
- **Re-AUTH din `OPEN`**: `AUTH` tranzitioneaza starea inapoi la `AUTH`; comenzile ulterioare (ex: `SEND`) returneaza `ERR E_STATE NOT_OPEN` pana se apeleaza `OPEN` din nou.

### Partea C — Comenzi compuse

Introduce `HISTORY` si valideaza scenarii multi-pas.

Scenarii acoperite:
- Flux complet cu SEND + BROADCAST + HISTORY: `history=2`.
- Sesiune inchisa urmata de comenzi suplimentare (all `ERR E_STATE CLOSED`).
- **Re-AUTH cu reset historyCount**: dupa re-AUTH si re-OPEN, `HISTORY` returneaza `history=0`.

---

## Hint-uri de implementare

- Separa parserul (detectia tokenilor, aritatea comenzii) de executia comenzii (logica de stare).
- Pastreaza starea sesiunii si historyCount ca variabile de instanta ale unui obiect `Session` sau `ProtocolEngine`.
- Nu trata cazurile speciale inline — o metoda per comanda face codul testabil.
- Ordinea verificarilor conteaza: verifica mai intai aritatea (E_PARSE), apoi starea (E_STATE).
