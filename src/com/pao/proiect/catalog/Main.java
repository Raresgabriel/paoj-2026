package com.pao.proiect.catalog;

import com.pao.proiect.catalog.exception.NotaInvalidaException;
import com.pao.proiect.catalog.model.*;
import com.pao.proiect.catalog.repository.*;
import com.pao.proiect.catalog.service.*;
import com.pao.proiect.catalog.util.DatabaseConnection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("=== 0. INIT SCHEMA ===");
        initSchema();
        System.out.println("  Schema initializata (DROP + CREATE).");

        ElevService     elevService     = ElevService.getInstance();
        ProfesorService profesorService = ProfesorService.getInstance();
        CatalogService  catalogService  = CatalogService.getInstance();

        AnScolar an = new AnScolar(2025);
        catalogService.setAnCurent(an);

        System.out.println("\n=== Actiunea 4: Inregistreaza clasa ===");
        Clasa clasa7A = new Clasa("7A", an);
        catalogService.adaugaClasa(clasa7A);

        System.out.println("\n=== Actiunea 1: Adauga elevi ===");
        Elev e1 = new Elev("Popa",    "Andrei",   "5101010120010", clasa7A);
        Elev e2 = new Elev("Ionescu", "Maria",    "6111111120020", clasa7A);
        Elev e3 = new Elev("Dumitru", "Cristina", "6121212120030", clasa7A);
        Elev e4 = new Elev("Mihai",   "Vlad",     "5131313130040", clasa7A);
        for (Elev e : new Elev[]{e1, e2, e3, e4}) {
            elevService.adauga(e);
            clasa7A.adaugaElev(e);
            System.out.println("  " + e);
        }

        System.out.println("\n=== Actiunea 2: Adauga profesori ===");
        Profesor profMate = new Profesor("Popescu",   "Ioana",     "2700101220001", GradDidactic.GRADUL_I);
        Profesor profRom  = new Profesor("Stanescu",  "Alexandra", "2700102220002", GradDidactic.DEFINITIVAT);
        Profesor profEng  = new Profesor("Ene",       "Maria",     "2700103220003", GradDidactic.GRADUL_II);
        Profesor profIst  = new Profesor("Vasilescu", "Gheorghe",  "1700104220004", GradDidactic.DEBUTANT);
        for (Profesor p : new Profesor[]{profMate, profRom, profEng, profIst}) {
            profesorService.adauga(p);
            System.out.println("  " + p);
        }

        System.out.println("\n=== Actiunea 3: Adauga materii ===");
        Materie matematica = new Materie("Matematica",    "MATE");
        Materie romana     = new Materie("Limba Romana",  "ROM");
        Materie engleza    = new Materie("Engleza",       "ENG");
        Materie istorie    = new Materie("Istorie",       "IST");
        for (Materie m : new Materie[]{matematica, romana, engleza, istorie}) {
            catalogService.adaugaMaterie(m);
        }
        System.out.println("  Materii: " + catalogService.listeazaMaterii());

        Diriginte diriginte = new Diriginte("Ionescu", "Dan", "2700105220005",
                GradDidactic.GRADUL_I, clasa7A);
        diriginte.adaugaMaterie(matematica);
        profesorService.adauga(diriginte);
        clasa7A.setDiriginte(diriginte);
        System.out.println("  Diriginte setat: " + diriginte);

        System.out.println("\n=== Actiunea 5: Asociaza profesori cu materii ===");
        profesorService.asociazaCuMaterie(profMate.getCnp(), matematica);
        profesorService.asociazaCuMaterie(profRom.getCnp(),  romana);
        profesorService.asociazaCuMaterie(profEng.getCnp(),  engleza);
        profesorService.asociazaCuMaterie(profIst.getCnp(),  istorie);
        System.out.println("  " + profMate.getNumeComplet() + " preda " + profMate.getMateriiPredate());

        System.out.println("\n=== Actiunea 6: Acorda note ===");
        Nota n1 = catalogService.acordaNota(e1, matematica, profMate, 9,  TipNota.ORAL,       LocalDate.of(2025, 10, 15));
        Nota n2 = catalogService.acordaNota(e1, matematica, profMate, 8,  TipNota.TEZA,       LocalDate.of(2025, 12, 5));
        Nota n3 = catalogService.acordaNota(e1, romana,     profRom,  10, TipNota.REFERAT,    LocalDate.of(2025, 11, 3));
        Nota n4 = catalogService.acordaNota(e1, engleza,    profEng,  7,  TipNota.ORAL,       LocalDate.of(2025, 10, 22));
        Nota n5 = catalogService.acordaNota(e2, matematica, profMate, 10, TipNota.TEZA,       LocalDate.of(2025, 12, 5));
        Nota n6 = catalogService.acordaNota(e2, romana,     profRom,  9,  TipNota.EXTEMPORAL, LocalDate.of(2025, 11, 12));
        Nota n7 = catalogService.acordaNota(e2, istorie,    profIst,  8,  TipNota.ORAL,       LocalDate.of(2025, 10, 30));
        Nota n8 = catalogService.acordaNota(e3, matematica, profMate, 6,  TipNota.EXTEMPORAL, LocalDate.of(2025, 11, 1));
        Nota n9 = catalogService.acordaNota(e3, engleza,    profEng,  9,  TipNota.TEMA,       LocalDate.of(2025, 11, 25));
        catalogService.acordaNota(e4, romana,     profRom,  7, TipNota.ORAL, LocalDate.of(2025, 10, 18));
        System.out.println("  Note acordate.");
        try {
            catalogService.acordaNota(e1, matematica, profMate, 11, TipNota.ORAL, LocalDate.now());
        } catch (NotaInvalidaException ex) {
            System.out.println("  Eroare prinsa: " + ex.getMessage());
        }

        System.out.println("\n=== Actiunea 7: Cauta elev dupa CNP ===");
        Elev gasit = elevService.cautaDupaCnp("6111111120020");
        System.out.println("  Gasit: " + gasit);

        System.out.println("\n=== Actiunea 8: Listeaza elevi din clasa ===");
        List<Elev> eleviSortati = catalogService.listeazaEleviiDinClasa("7A");
        eleviSortati.forEach(ev -> System.out.println("  " + ev.getNumeComplet()));

        System.out.println("\n=== Actiunea 9: Medie la materie ===");
        System.out.printf("  %s la Matematica: %.2f%n", e1.getNumeComplet(),
                catalogService.mediaLaMaterie(e1, matematica));

        System.out.println("\n=== Actiunea 10: Medie generala ===");
        for (Elev ev : eleviSortati) {
            try {
                System.out.printf("  %s -> %.2f%n", ev.getNumeComplet(),
                        catalogService.mediaGenerala(ev));
            } catch (NotaInvalidaException ex) {
                System.out.println("  " + ev.getNumeComplet() + " -> fara note");
            }
        }

        MaterieRepository  materieRepo  = new MaterieRepository();
        ElevRepository     elevRepo     = new ElevRepository();
        ProfesorRepository profesorRepo = new ProfesorRepository();
        NotaRepository     notaRepo     = new NotaRepository();

        System.out.println("\n=== ETAPA II: Persista in SQLite ===");
        for (Materie m : new Materie[]{matematica, romana, engleza, istorie}) {
            materieRepo.save(m);
        }
        for (Elev ev : new Elev[]{e1, e2, e3, e4}) elevRepo.save(ev);
        for (Profesor p : new Profesor[]{profMate, profRom, profEng, profIst, diriginte}) {
            profesorRepo.save(p);
        }
        System.out.println("  Materii, elevi, profesori salvati.");

        System.out.println("\n=== JDBC Transaction: acordaNota ===");
        Nota notaTranzactie = new Nota(10, LocalDate.of(2025, 12, 20),
                e1, matematica, profMate, TipNota.TEMA);
        notaRepo.saveInTransaction(notaTranzactie);
        System.out.println("  Nota salvata in tranzactie cu commit.");

        for (Nota n : new Nota[]{n1,n2,n3,n4,n5,n6,n7,n8,n9}) {
            notaRepo.save(n);
        }

        System.out.println("\n=== findAll Materii ===");
        materieRepo.findAll().forEach(m -> System.out.println("  " + m));

        System.out.println("\n=== findAll Elevi ===");
        elevRepo.findAll().forEach(ev -> System.out.println("  " + ev));

        System.out.println("\n=== findAll Profesori ===");
        profesorRepo.findAll().forEach(p -> System.out.println("  " + p));

        System.out.println("\n=== findById Elev (CNP 5101010120010) ===");
        elevRepo.findById("5101010120010").ifPresent(ev -> System.out.println("  " + ev));

        System.out.println("\n=== update Materie (ROM -> Romana) ===");
        romana.setNume("Romana");
        materieRepo.update(romana);
        materieRepo.findById("ROM").ifPresent(m -> System.out.println("  Dupa update: " + m));

        System.out.println("\n=== delete Elev (Mihai Vlad) ===");
        elevRepo.delete(e4.getCnp());
        System.out.println("  Elevi dupa delete: " + elevRepo.findAll().size());

        System.out.println("\n=== JOIN #1: Elevi cu nr. de note ===");
        elevRepo.eleviCuNrNote().forEach(s -> System.out.println("  " + s));

        System.out.println("\n=== JOIN #2: Profesori cu materiile predate ===");
        profesorRepo.profesoriCuMaterii().forEach(s -> System.out.println("  " + s));

        System.out.println("\n=== JOIN #3: Note cu elev + materie + profesor ===");
        notaRepo.noteWithJoin().forEach(s -> System.out.println("  " + s));

        System.out.println("\n=== Continut audit.csv ===");
        try (BufferedReader br = new BufferedReader(new FileReader("audit.csv"))) {
            br.lines().forEach(l -> System.out.println("  " + l));
        } catch (IOException e) {
            System.out.println("  [audit.csv nu a fost gasit]");
        }

        DatabaseConnection.getInstance().close();
        System.out.println("\n=== DONE ===");
    }

    private static final String SCHEMA =
        "DROP TABLE IF EXISTS nota;" +
        "DROP TABLE IF EXISTS profesor_materie;" +
        "DROP TABLE IF EXISTS elev;" +
        "DROP TABLE IF EXISTS profesor;" +
        "DROP TABLE IF EXISTS materie;" +
        "CREATE TABLE materie (cod TEXT PRIMARY KEY, nume TEXT NOT NULL);" +
        "CREATE TABLE profesor (cnp TEXT PRIMARY KEY, nume TEXT NOT NULL, prenume TEXT NOT NULL, grad TEXT NOT NULL, este_diriginte INTEGER NOT NULL DEFAULT 0);" +
        "CREATE TABLE elev (cnp TEXT PRIMARY KEY, nume TEXT NOT NULL, prenume TEXT NOT NULL, clasa_id TEXT);" +
        "CREATE TABLE profesor_materie (profesor_cnp TEXT NOT NULL, materie_cod TEXT NOT NULL, PRIMARY KEY (profesor_cnp, materie_cod), FOREIGN KEY (profesor_cnp) REFERENCES profesor(cnp), FOREIGN KEY (materie_cod) REFERENCES materie(cod));" +
        "CREATE TABLE nota (id INTEGER PRIMARY KEY AUTOINCREMENT, valoare INTEGER NOT NULL, data TEXT NOT NULL, tip TEXT NOT NULL, elev_cnp TEXT NOT NULL, materie_cod TEXT NOT NULL, profesor_cnp TEXT NOT NULL, FOREIGN KEY (elev_cnp) REFERENCES elev(cnp), FOREIGN KEY (materie_cod) REFERENCES materie(cod), FOREIGN KEY (profesor_cnp) REFERENCES profesor(cnp));";

    private static void initSchema() throws Exception {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        for (String stmt : SCHEMA.split(";")) {
            String s = stmt.trim();
            if (!s.isEmpty()) {
                try (Statement st = conn.createStatement()) {
                    st.execute(s);
                }
            }
        }
    }
}
