-- Catalog scolar - schema SQLite
-- Drop in ordine inversa FK-urilor

DROP TABLE IF EXISTS nota;
DROP TABLE IF EXISTS profesor_materie;
DROP TABLE IF EXISTS elev;
DROP TABLE IF EXISTS profesor;
DROP TABLE IF EXISTS materie;

CREATE TABLE materie (
    cod  TEXT PRIMARY KEY,
    nume TEXT NOT NULL
);

CREATE TABLE profesor (
    cnp             TEXT PRIMARY KEY,
    nume            TEXT NOT NULL,
    prenume         TEXT NOT NULL,
    grad            TEXT NOT NULL,
    este_diriginte  INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE elev (
    cnp      TEXT PRIMARY KEY,
    nume     TEXT NOT NULL,
    prenume  TEXT NOT NULL,
    clasa_id TEXT
);

-- relatia M:N profesor <-> materie  (FK #1 + FK #2)
CREATE TABLE profesor_materie (
    profesor_cnp TEXT NOT NULL,
    materie_cod  TEXT NOT NULL,
    PRIMARY KEY (profesor_cnp, materie_cod),
    FOREIGN KEY (profesor_cnp) REFERENCES profesor(cnp),
    FOREIGN KEY (materie_cod)  REFERENCES materie(cod)
);

-- nota are 3 FK-uri (FK #3 + FK #4 + FK #5)
CREATE TABLE nota (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    valoare      INTEGER NOT NULL,
    data         TEXT    NOT NULL,
    tip          TEXT    NOT NULL,
    elev_cnp     TEXT    NOT NULL,
    materie_cod  TEXT    NOT NULL,
    profesor_cnp TEXT    NOT NULL,
    FOREIGN KEY (elev_cnp)     REFERENCES elev(cnp),
    FOREIGN KEY (materie_cod)  REFERENCES materie(cod),
    FOREIGN KEY (profesor_cnp) REFERENCES profesor(cnp)
);
