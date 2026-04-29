package com.pao.proiect.catalog;

import com.pao.proiect.catalog.exception.NotaInvalidaException;
import com.pao.proiect.catalog.model.AnScolar;
import com.pao.proiect.catalog.model.Clasa;
import com.pao.proiect.catalog.model.Diriginte;
import com.pao.proiect.catalog.model.Elev;
import com.pao.proiect.catalog.model.GradDidactic;
import com.pao.proiect.catalog.model.Materie;
import com.pao.proiect.catalog.model.Profesor;
import com.pao.proiect.catalog.model.TipNota;
import com.pao.proiect.catalog.service.CatalogService;
import com.pao.proiect.catalog.service.ElevService;
import com.pao.proiect.catalog.service.ProfesorService;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ElevService elevService = ElevService.getInstance();
        ProfesorService profesorService = ProfesorService.getInstance();
        CatalogService catalogService = CatalogService.getInstance();

        AnScolar an = new AnScolar(2025);
        catalogService.setAnCurent(an);

        Clasa clasa7A = new Clasa("7A", an);
        catalogService.adaugaClasa(clasa7A);

        System.out.println("Adauga elevi noi");
        Elev elev1 = new Elev("Popa", "Andrei", "5101010120010", clasa7A);
        Elev elev2 = new Elev("Ionescu", "Maria", "6111111120020", clasa7A);
        Elev elev3 = new Elev("Dumitru", "Cristina", "6121212120030", clasa7A);
        Elev elev4 = new Elev("Mihai", "Vlad", "5131313130040", clasa7A);
        for (Elev e : new Elev[]{elev1, elev2, elev3, elev4}) {
            elevService.adauga(e);
            clasa7A.adaugaElev(e);
            System.out.println("  " + e);
        }

        System.out.println("\nAdauga profesori noi");
        Profesor profMate = new Profesor("Popescu", "Ioana", "2700101220001", GradDidactic.GRADUL_I);
        Profesor profRom  = new Profesor("Stanescu", "Alexandra", "2700102220002", GradDidactic.DEFINITIVAT);
        Profesor profEng  = new Profesor("Ene", "Maria", "2700103220003", GradDidactic.GRADUL_II);
        Profesor profIst  = new Profesor("Vasilescu", "Gheorghe", "1700104220004", GradDidactic.DEBUTANT);
        for (Profesor p : new Profesor[]{profMate, profRom, profEng, profIst}) {
            profesorService.adauga(p);
            System.out.println("  " + p);
        }

        System.out.println("\nAdauga materii");
        Materie matematica = new Materie("Matematica", "MATE");
        Materie romana     = new Materie("Limba Romana", "ROM");
        Materie engleza    = new Materie("Engleza", "ENG");
        Materie istorie    = new Materie("Istorie", "IST");
        for (Materie m : new Materie[]{matematica, romana, engleza, istorie}) {
            catalogService.adaugaMaterie(m);
        }
        System.out.println("  Materii: " + catalogService.listeazaMaterii());

        System.out.println("\nInregistreaza clasa cu diriginte");
        Diriginte diriginte7A = new Diriginte(
                "Popescu", "Ioana", "2700101220099", GradDidactic.GRADUL_I, clasa7A);
        diriginte7A.adaugaMaterie(matematica);
        profesorService.adauga(diriginte7A);
        clasa7A.setDiriginte(diriginte7A);
        System.out.println("  " + clasa7A.getId() + " -> " + diriginte7A);

        System.out.println("\nAsociaza profesori cu materii");
        profesorService.asociazaCuMaterie(profMate.getCnp(), matematica);
        profesorService.asociazaCuMaterie(profRom.getCnp(),  romana);
        profesorService.asociazaCuMaterie(profEng.getCnp(),  engleza);
        profesorService.asociazaCuMaterie(profIst.getCnp(),  istorie);
        System.out.println("  " + profMate.getNumeComplet() + " preda " + profMate.getMateriiPredate());
        System.out.println("  " + profRom.getNumeComplet()  + " preda " + profRom.getMateriiPredate());
        System.out.println("  " + profEng.getNumeComplet()  + " preda " + profEng.getMateriiPredate());
        System.out.println("  " + profIst.getNumeComplet()  + " preda " + profIst.getMateriiPredate());

        System.out.println("\nAcorda note elevilor");
        catalogService.acordaNota(elev1, matematica, profMate, 9,  TipNota.ORAL,    LocalDate.of(2025, 10, 15));
        catalogService.acordaNota(elev1, matematica, profMate, 8,  TipNota.TEZA,    LocalDate.of(2025, 12, 5));
        catalogService.acordaNota(elev1, romana,     profRom,  10, TipNota.REFERAT, LocalDate.of(2025, 11, 3));
        catalogService.acordaNota(elev1, engleza,    profEng,  7,  TipNota.ORAL,    LocalDate.of(2025, 10, 22));
        catalogService.acordaNota(elev2, matematica, profMate, 10, TipNota.TEZA,        LocalDate.of(2025, 12, 5));
        catalogService.acordaNota(elev2, romana,     profRom,  9,  TipNota.EXTEMPORAL,  LocalDate.of(2025, 11, 12));
        catalogService.acordaNota(elev2, istorie,    profIst,  8,  TipNota.ORAL,        LocalDate.of(2025, 10, 30));
        catalogService.acordaNota(elev3, matematica, profMate, 6,  TipNota.EXTEMPORAL, LocalDate.of(2025, 11, 1));
        catalogService.acordaNota(elev3, engleza,    profEng,  9,  TipNota.TEMA,        LocalDate.of(2025, 11, 25));
        catalogService.acordaNota(elev4, romana,     profRom,  7,  TipNota.ORAL,        LocalDate.of(2025, 10, 18));
        System.out.println("  Note acordate.");

        try {
            catalogService.acordaNota(elev1, engleza, profMate, 10, TipNota.ORAL, LocalDate.now());
        } catch (NotaInvalidaException ex) {
            System.out.println("  Eroare prinsa: " + ex.getMessage());
        }
        try {
            catalogService.acordaNota(elev1, matematica, profMate, 11, TipNota.ORAL, LocalDate.now());
        } catch (NotaInvalidaException ex) {
            System.out.println("  Eroare prinsa: " + ex.getMessage());
        }

        System.out.println("\nCauta elev dupa CNP");
        Elev gasit = elevService.cautaDupaCnp("6111111120020");
        System.out.println("  6111111120020 -> " + gasit);

        System.out.println("\nListeaza elevii dintr-o clasa");
        List<Elev> eleviSortati = catalogService.listeazaEleviiDinClasa("7A");
        for (Elev e : eleviSortati) {
            System.out.println("  " + e.getNumeComplet() + " (CNP " + e.getCnp() + ")");
        }

        System.out.println("\nMedia unui elev la o materie");
        System.out.printf("  %s la %s: %.2f%n",
                elev1.getNumeComplet(), matematica.getNume(),
                catalogService.mediaLaMaterie(elev1, matematica));
        System.out.printf("  %s la %s: %.2f%n",
                elev1.getNumeComplet(), romana.getNume(),
                catalogService.mediaLaMaterie(elev1, romana));
        try {
            catalogService.mediaLaMaterie(elev4, matematica);
        } catch (NotaInvalidaException ex) {
            System.out.println("  Eroare prinsa: " + ex.getMessage());
        }

        System.out.println("\nMedia generala a unui elev");
        for (Elev e : eleviSortati) {
            try {
                double mg = catalogService.mediaGenerala(e);
                System.out.printf("  %s -> %.2f%n", e.getNumeComplet(), mg);
                System.out.println("    note: " + catalogService.listeazaNoteElev(e));
            } catch (NotaInvalidaException ex) {
                System.out.println("  " + e.getNumeComplet() + " -> fara note");
            }
        }

        System.out.println("\nStergere elev");
        System.out.println("  Total elevi inainte: " + elevService.numarTotal());
        elevService.sterge(elev4.getCnp());
        System.out.println("  Total elevi dupa: " + elevService.numarTotal());
        System.out.println("  Clasa 7A are " + clasa7A.numarElevi() + " elevi.");
    }
}
