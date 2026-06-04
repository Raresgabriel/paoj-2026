package com.pao.proiect.catalog.service;

import com.pao.proiect.catalog.exception.ElevInexistentException;
import com.pao.proiect.catalog.service.AuditService;
import com.pao.proiect.catalog.exception.MaterieInexistentaException;
import com.pao.proiect.catalog.exception.NotaInvalidaException;
import com.pao.proiect.catalog.model.AnScolar;
import com.pao.proiect.catalog.model.Clasa;
import com.pao.proiect.catalog.model.Elev;
import com.pao.proiect.catalog.model.Materie;
import com.pao.proiect.catalog.model.Nota;
import com.pao.proiect.catalog.model.Profesor;
import com.pao.proiect.catalog.model.TipNota;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class CatalogService {

    private static CatalogService instance;

    public static final Comparator<Nota> COMPARATOR_NOTE_DUPA_DATA =
            Comparator.comparing(Nota::getData)
                    .thenComparing(Nota::getValoare);

    private AnScolar anCurent;
    private final Map<String, Materie> materiiDupaCod;
    private final TreeSet<Materie> materiiSortate;
    private final Map<String, Clasa> claseDupaId;
    private final Map<Elev, List<Nota>> noteElevi;

    private CatalogService() {
        this.materiiDupaCod = new HashMap<>();
        this.materiiSortate = new TreeSet<>();
        this.claseDupaId = new HashMap<>();
        this.noteElevi = new HashMap<>();
    }

    public static synchronized CatalogService getInstance() {
        if (instance == null) {
            instance = new CatalogService();
        }
        return instance;
    }

    public void setAnCurent(AnScolar anCurent) {
        this.anCurent = anCurent;
    }

    public AnScolar getAnCurent() {
        return anCurent;
    }

    public void adaugaMaterie(Materie materie) {
        if (materie == null) {
            throw new IllegalArgumentException("Materie null");
        }
        materiiDupaCod.put(materie.getCod(), materie);
        materiiSortate.add(materie);
        AuditService.getInstance().log("adauga_materie");
    }

    public void stergeMaterie(String cod) {
        Materie m = materiiDupaCod.remove(cod);
        if (m == null) {
            throw MaterieInexistentaException.pentruCod(cod);
        }
        materiiSortate.remove(m);
    }

    public Materie cautaMaterie(String cod) {
        Materie m = materiiDupaCod.get(cod);
        if (m == null) {
            throw MaterieInexistentaException.pentruCod(cod);
        }
        return m;
    }

    public List<Materie> listeazaMaterii() {
        return new ArrayList<>(materiiSortate);
    }

    public void adaugaClasa(Clasa clasa) {
        if (clasa == null) {
            throw new IllegalArgumentException("Clasa null");
        }
        claseDupaId.put(clasa.getId(), clasa);
        AuditService.getInstance().log("inregistreaza_clasa");
    }

    public void stergeClasa(String id) {
        if (claseDupaId.remove(id) == null) {
            throw new NoSuchElementException("Nu exista clasa cu id-ul: " + id);
        }
    }

    public Clasa cautaClasa(String id) {
        Clasa c = claseDupaId.get(id);
        if (c == null) {
            throw new NoSuchElementException("Nu exista clasa cu id-ul: " + id);
        }
        return c;
    }

    public List<Clasa> listeazaClase() {
        List<Clasa> toate = new ArrayList<>(claseDupaId.values());
        toate.sort(Comparator.comparing(Clasa::getId));
        return toate;
    }

    public List<Elev> listeazaEleviiDinClasa(String idClasa) {
        Clasa c = cautaClasa(idClasa);
        List<Elev> elevi = new ArrayList<>(c.getElevi());
        Collections.sort(elevi);
        AuditService.getInstance().log("listeaza_elevi_clasa");
        return elevi;
    }

    public Nota acordaNota(Elev elev, Materie materie, Profesor profesor,
                           int valoare, TipNota tip, LocalDate data) {
        if (elev == null) {
            throw new ElevInexistentException("Elev null");
        }
        if (materie == null || !materiiDupaCod.containsKey(materie.getCod())) {
            throw MaterieInexistentaException.pentruCod(
                    materie != null ? materie.getCod() : "null");
        }
        if (!profesor.predaMateria(materie)) {
            throw new NotaInvalidaException(
                    "Profesorul " + profesor.getNumeComplet()
                            + " nu preda materia " + materie.getNume());
        }
        Nota nota = new Nota(valoare, data, elev, materie, profesor, tip);
        noteElevi.computeIfAbsent(elev, k -> new ArrayList<>()).add(nota);
        AuditService.getInstance().log("acorda_nota");
        return nota;
    }

    public List<Nota> listeazaNoteElev(Elev elev) {
        List<Nota> note = new ArrayList<>(noteElevi.getOrDefault(elev, List.of()));
        note.sort(COMPARATOR_NOTE_DUPA_DATA);
        return note;
    }

    public List<Nota> notePeMaterie(Elev elev, Materie materie) {
        List<Nota> rezultat = new ArrayList<>();
        for (Nota n : noteElevi.getOrDefault(elev, List.of())) {
            if (n.getMaterie().equals(materie)) {
                rezultat.add(n);
            }
        }
        return rezultat;
    }

    public double mediaLaMaterie(Elev elev, Materie materie) {
        List<Nota> note = notePeMaterie(elev, materie);
        if (note.isEmpty()) {
            throw new NotaInvalidaException(
                    "Elevul " + elev.getNumeComplet()
                            + " nu are note la materia " + materie.getNume());
        }
        double suma = 0;
        for (Nota n : note) {
            suma += n.getValoare();
        }
        AuditService.getInstance().log("calcul_medie_materie");
        return suma / note.size();
    }

    public double mediaGenerala(Elev elev) {
        List<Nota> toate = noteElevi.getOrDefault(elev, List.of());
        if (toate.isEmpty()) {
            throw new NotaInvalidaException(
                    "Elevul " + elev.getNumeComplet() + " nu are nici-o nota");
        }
        Map<Materie, List<Nota>> peMaterie = new HashMap<>();
        for (Nota n : toate) {
            peMaterie.computeIfAbsent(n.getMaterie(), k -> new ArrayList<>()).add(n);
        }
        double sumaMedii = 0;
        for (Map.Entry<Materie, List<Nota>> e : peMaterie.entrySet()) {
            double sumaNote = 0;
            for (Nota n : e.getValue()) {
                sumaNote += n.getValoare();
            }
            sumaMedii += sumaNote / e.getValue().size();
        }
        AuditService.getInstance().log("calcul_medie_generala");
        return sumaMedii / peMaterie.size();
    }
}
