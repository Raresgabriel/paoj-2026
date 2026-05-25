# Exercise 2 (Bonus) - Demo socket multi-client

## Scenariu

Extinde motorul de protocol din exercitiul 1 printr-un demo practic:
un server accepta mai multi clienti simultan, fiecare rulandu-si propria sesiune de protocol.

Scopul este sa intelegi cum se leaga `ServerSocket`, `Socket` si gestionarea sesiunilor
concurente — nu sa produci un server de productie.

---

## Ce trebuie sa demonstreze demo-ul

1. Serverul porneste pe un port configurabil si asteapta conexiuni.
2. Cel putin 2 clienti se conecteaza simultan si trimit comenzi protocol independente.
3. Fiecare client primeste raspunsurile corecte conform motorului din ex1 (sesiuni izolate).
4. Log clar in consola: conectare, fiecare comanda+raspuns, deconectare.
5. Serverul se opreste controlat dupa ce toti clientii s-au deconectat.

---

## Clase Java relevante — referinta rapida

### Partea de server

**`ServerSocket`**
- Deschide un port TCP si accepta conexiuni.
- Metoda cheie: `serverSocket.accept()` — blocheaza pana vine un client, returneaza un `Socket`.
- Se inchide cu `serverSocket.close()` sau in `try-with-resources`.

**`ExecutorService`** (din `java.util.concurrent`)
- Gestioneaza un pool de thread-uri pentru sesiunile clientilor.
- `Executors.newCachedThreadPool()` creeaza thread-uri la cerere.
- Alternativa simpla: `new Thread(sessionHandler).start()` pentru fiecare client.

### Partea de client

**`Socket`**
- Conecteaza la server cu `new Socket(host, port)`.
- Expune stream-uri: `socket.getInputStream()` si `socket.getOutputStream()`.

### Comunicare

**`BufferedReader`** + **`InputStreamReader`**
- Citeste linii text de pe stream-ul socket-ului: `reader.readLine()`.

**`PrintWriter`**
- Trimite linii text pe stream-ul de output al socket-ului.
- Construieste cu `autoFlush=true` pentru a nu bloca.

**`socket.setSoTimeout(ms)`**
- Seteaza un timeout pe `readLine()` — util pentru a evita blocarea la deconectare.

---

## Libertate de implementare

- Nu exista format I/O fix — datele de test pot fi hardcodate in clienti.
- Nu este nevoie de `Checker.java`; evaluarea este manuala.
- Poti rula serverul si clientii in acelasi `Main.java` (thread server + thread-uri client).
- Mesajele de log sunt la alegere — important sa fie lizibile.

---

## Exemplu de log orientativ (forma libera)

```
[SERVER] Listening on port 9000
[CLIENT-1] Connected
[CLIENT-2] Connected
[CLIENT-1] >> AUTH alice  =>  OK AUTH user=alice
[CLIENT-2] >> AUTH bob    =>  OK AUTH user=bob
[CLIENT-1] >> OPEN        =>  OK OPEN
[CLIENT-2] >> OPEN        =>  OK OPEN
[CLIENT-1] >> SEND hi     =>  OK OPEN sent
[CLIENT-2] >> BROADCAST x =>  OK OPEN broadcast
[CLIENT-1] Disconnected
[CLIENT-2] Disconnected
[SERVER] All clients done. Shutting down.
```

---

## Hint-uri

- Fiecare `Runnable` de sesiune client iti instantiaza propriul motor de protocol din ex1.
- Inchide intotdeauna `Socket`-ul in `finally` sau `try-with-resources`.
- Pentru shutdown controlat, poti folosi un `CountDownLatch` sau `ExecutorService.shutdown()` + `awaitTermination`.
