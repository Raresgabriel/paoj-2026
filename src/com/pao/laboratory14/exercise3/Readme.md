# Exercițiul 3 (Bonus) — Alocare Automată de Săli pentru Evenimente

> **Pachet:** `com.pao.laboratory14.exercise3`
> **Timp estimat:** ~30 min · **Fără teste automate** — demonstrație în `Main.java`

---

## Scenariu

Platforma de e-ticketing primește o listă de evenimente cu ore de start și final. Fiecare eveniment ocupă o sală fizică pe toată durata sa. Trebuie să determini **câte săli sunt necesare minim** și să atribui fiecare eveniment la o sală concretă — fără suprapuneri.

Aceasta este problema **„Minimum Meeting Rooms"** (LeetCode 253), una dintre cele mai frecvente întrebări la interviuri tehnice (Google, Amazon, Meta). Soluția naivă O(N²) este ușor de înțeles; soluția optimă O(N log N) cu `PriorityQueue` este o demonstrație clasică a structurilor de date.

---

## Noțiuni demonstrate

- `PriorityQueue<T>` — min-heap nativ Java; `offer`, `poll`, `peek` în O(log N)
- Algoritm greedy pe intervale sortate — corectitudine prin invariant
- Complexitate O(N log N) vs O(N²) brut
- `record` Java pentru modelarea entităților simple

---

## Cerințe minime

`Main.java` trebuie să demonstreze:

1. O listă hardcodată de **cel puțin 8 evenimente** cu ore de start și final (`HH:MM`), cu **suprapuneri deliberate**
2. Sortarea evenimentelor după ora de start
3. **Varianta 1 — greedy simplu O(N²):** atribuie fiecare eveniment la prima sală disponibilă; afișează:
   ```
   NumeEveniment     (HH:MM - HH:MM)  →  Sala #K
   ```
4. Afișarea numărului minim de săli utilizate
5. **Varianta 2 — PriorityQueue O(N log N):** confirmă același număr minim

---

## Libertate de implementare

- Nu există format I/O fix — evenimentele sunt hardcodate în `Main.java`
- Ordinea atribuirii sălilor poate varia; important este că **numărul minim** este corect
- Evaluarea este manuală pe baza demo-ului și a comentariilor din cod

---

## Hint-uri

- Convertește `"HH:MM"` în minute întregi pentru comparații: `17*60 + 30 = 1050`
- Sortează evenimentele după `startMin`, nu după `endMin`
- **Varianta 1:** `List<Integer> rooms` — `rooms.get(i)` = endMin al sălii `i+1`; dacă `rooms.get(i) <= startMin` → reutilizează sala; altfel → sală nouă (`rooms.add`)
- **Varianta 2:** `PriorityQueue<Integer> pq = new PriorityQueue<>()` — min-heap de `endMin` ale sălilor ocupate
  - dacă `pq.peek() <= startMin` → `pq.poll()` (eliberezi sala) + `pq.offer(endMin)` (reocupi)
  - altfel → `pq.offer(endMin)` (sală nouă)
  - la final: `pq.size()` = numărul minim de săli

