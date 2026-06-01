# Exercițiul 1 — Colector Custom pentru Raport Vânzări Bilete

> **Pachet:** `com.pao.laboratory14.exercise1`
> **Timp estimat:** ~40 min · **Teste automate:** da (`Checker.java`, 2 părți)

---

## Scenariu

O platformă de e-ticketing vinde bilete la evenimente în trei categorii: `VIP`, `STANDARD` și `BACKSTAGE`. La sfârșitul zilei, sistemul trebuie să genereze un raport al vânzărilor — iar raportul trebuie produs cu un **colector custom**, nu cu `Collectors.groupingBy` simplu.

Vei implementa `Collector<Bilet, ?, RaportVanzari>` cu cele patru componente ale sale:

- **supplier** — creează structura mutabilă de acumulare
- **accumulator** — adaugă fiecare bilet în structura internă
- **combiner** — combină două structuri (necesar pentru streams paralele)
- **finisher** — transformă structura mutabilă într-un obiect `RaportVanzari` **imutabil**

---

## Clase de creat

- `enum TipBilet` — valorile `BACKSTAGE`, `STANDARD`, `VIP` *(în ordine alfabetică — ordinea declarației contează la iterarea cu `values()`!)*
- `class Bilet` — câmpuri: `id` (int), `eveniment` (String), `tip` (TipBilet), `pret` (double)
- `class RaportVanzari` — **imutabil**; conține:
  - `Map<TipBilet, Long> numarPerTip` — câte bilete per tip
  - `Map<TipBilet, Double> incasariPerTip` — total încasări per tip
  - `double totalGlobal` *(folosit în Partea B)*
  - `double medieGlobala` *(folosit în Partea B)*
  - `TipBilet tipCelMaiPopular` — tipul cu cele mai multe bilete; la egalitate de count, primul în ordine **alfabetică** *(folosit în Partea B)*

---

## Format input

```
N
id eveniment tip(VIP|STANDARD|BACKSTAGE) pret
... (N linii)
COMANDA
```

`COMANDA` este fie `RAPORT_SIMPLU`, fie `RAPORT_COMPLET`.

---

## Partea A — RAPORT_SIMPLU

Afișează pentru fiecare tip **prezent** în bilete, sortat **alfabetic** după numele tipului (`BACKSTAGE < STANDARD < VIP`):

```
TIP: count=N incasari=X.XX RON
```

**Exemplu:**

```
Input:                              Output:
4                                   BACKSTAGE: count=1 incasari=500.00 RON
1 ConcertRock VIP 250.00            STANDARD: count=1 incasari=80.00 RON
2 ConcertRock STANDARD 80.00        VIP: count=2 incasari=500.00 RON
3 ConcertRock VIP 250.00
4 ConcertRock BACKSTAGE 500.00
RAPORT_SIMPLU
```

---

## Partea B — RAPORT_COMPLET

Afișează **același format ca Partea A**, urmat de separator `---` și trei linii suplimentare:

```
---
Total: X.XX RON
Medie: X.XX RON
Cel mai popular: TIP
```

**Exemplu:**

```
Input:                              Output:
5                                   BACKSTAGE: count=1 incasari=600.00 RON
1 Gala VIP 300.00                   STANDARD: count=1 incasari=100.00 RON
2 Gala VIP 300.00                   VIP: count=3 incasari=900.00 RON
3 Gala STANDARD 100.00              ---
4 Gala BACKSTAGE 600.00             Total: 1600.00 RON
5 Gala VIP 300.00                   Medie: 320.00 RON
RAPORT_COMPLET                      Cel mai popular: VIP
```

> **Testul 3 din Partea B** acoperă **egalitate la `tipCelMaiPopular`**: 2 BACKSTAGE + 2 VIP + 1 STANDARD → ambele tipuri au `count=2`; câștigă `BACKSTAGE` (primul alfabetic).

---

## Format output

| Comandă | Output |
|---|---|
| `RAPORT_SIMPLU` | câte o linie `TIP: count=N incasari=X.XX RON` per tip prezent, sortat alfabetic |
| `RAPORT_COMPLET` | același ca RAPORT_SIMPLU + `---` + `Total:`, `Medie:`, `Cel mai popular:` |

---

## Hint-uri

- `Collector.of(supplier, accumulator, combiner, finisher)` — `java.util.stream.Collector.of`; finisher-ul este apelat o singură dată la finalizarea streamului
- Accumulator intern: o structură mutabilă indexată pe `TipBilet`
  (ex: `Map<TipBilet, double[]>` cu `double[0]` = count, `double[1]` = sumă)
- Finisher: construiește `RaportVanzari` din accumulator; folosește `Collections.unmodifiableMap` pentru a garanta imutabilitatea
- Sortare output: valorile enum declarate în ordine **alfabetică** (`BACKSTAGE, STANDARD, VIP`) + `Arrays.stream(TipBilet.values()).filter(t -> numarPerTip.containsKey(t))` asigură ordinea corectă
- Formatare prețuri: `String.format("%.2f", valoare)`
- `tipCelMaiPopular` la egalitate: sortează candidații cu `Comparator.comparing(TipBilet::name)` și ia primul

