# Laboratory 13 - Protocol Engine and Socket Demo

> Pachet: `com.pao.laboratory13`
> Tip: laborator scurt (1 exercitiu obligatoriu + 1 bonus)
> 
> **Deadline submit**: vineri, 29 mai, 23:59  
> **Deadline Prezentare**: joi, 4 iunie (ora de laborator)

---

## Obiective

1. Modelare protocol text si stare de sesiune.
2. Validare comenzi si coduri de eroare deterministe.
3. Bonus: demo socket multi-client.

---

## Exercitii

| # | Pachet | Concept principal | Timp estimat | Teste automate |
|---|---|---|---|---|
| 1 | [exercise1/](exercise1/Readme.md) | Motor de protocol (parser + state machine) | ~60 min | yes (runParts) |
| 2 (bonus) | [exercise2/](exercise2/Readme.md) | Demo socket multi-client (`ServerSocket`, `Socket`) | ~30 min | manual |

---

## Cum rulezi testele automate

Pentru exercitiul obligatoriu:
- `exercise1/Checker.java` ruleaza `IOTest.runParts(...)`.

Working directory: radacina proiectului (`paoj-2026`).

---

## Comenzi implementate

| Comanda | Sintaxa |
|---|---|
| `AUTH` | `AUTH <user>` |
| `OPEN` | `OPEN` |
| `SEND` | `SEND <payload>` |
| `BROADCAST` | `BROADCAST <payload>` |
| `HISTORY` | `HISTORY` |
| `CLOSE` | `CLOSE` |

---

## Diferenta intre obligatoriu si bonus

- `exercise1` este auto-testat pe parti (`tests/partA`, `partB`, `partC`).
- `exercise2` este bonus manual: fara `Checker.java` si fara `tests/`.
