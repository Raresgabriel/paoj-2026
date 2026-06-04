package com.pao.proiect.catalog.service;

import com.pao.proiect.catalog.model.Materie;
import com.pao.proiect.catalog.model.Profesor;
import com.pao.proiect.catalog.service.AuditService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ProfesorService {

    private static ProfesorService instance;

    private final Map<String, Profesor> profesoriDupaCnp;

    private ProfesorService() {
        this.profesoriDupaCnp = new HashMap<>();
    }

    public static synchronized ProfesorService getInstance() {
        if (instance == null) {
            instance = new ProfesorService();
        }
        return instance;
    }

    public void adauga(Profesor profesor) {
        if (profesor == null) {
            throw new IllegalArgumentException("Profesor null");
        }
        profesoriDupaCnp.put(profesor.getCnp(), profesor);
        AuditService.getInstance().log("adauga_profesor");
    }

    public void sterge(String cnp) {
        if (profesoriDupaCnp.remove(cnp) == null) {
            throw new NoSuchElementException("Nu exista profesor cu CNP-ul: " + cnp);
        }
    }

    public Profesor cautaDupaCnp(String cnp) {
        Profesor p = profesoriDupaCnp.get(cnp);
        if (p == null) {
            throw new NoSuchElementException("Nu exista profesor cu CNP-ul: " + cnp);
        }
        return p;
    }

    public Optional<Profesor> gasesteDupaCnp(String cnp) {
        return Optional.ofNullable(profesoriDupaCnp.get(cnp));
    }

    public void asociazaCuMaterie(String cnpProfesor, Materie materie) {
        Profesor p = cautaDupaCnp(cnpProfesor);
        p.adaugaMaterie(materie);
        AuditService.getInstance().log("asociaza_profesor_materie");
    }

    public List<Profesor> listeazaToti() {
        List<Profesor> toti = new ArrayList<>(profesoriDupaCnp.values());
        toti.sort(Comparator
                .comparing(Profesor::getNume, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Profesor::getPrenume, String.CASE_INSENSITIVE_ORDER));
        return toti;
    }

    public List<Profesor> profesoriCarePredau(Materie materie) {
        List<Profesor> rezultat = new ArrayList<>();
        for (Profesor p : profesoriDupaCnp.values()) {
            if (p.predaMateria(materie)) {
                rezultat.add(p);
            }
        }
        return rezultat;
    }

    public int numarTotal() {
        return profesoriDupaCnp.size();
    }
}
