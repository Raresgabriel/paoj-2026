package com.pao.proiect.catalog.service;

import com.pao.proiect.catalog.exception.ElevInexistentException;
import com.pao.proiect.catalog.model.Elev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ElevService {

    private static ElevService instance;

    private final Map<String, Elev> eleviDupaCnp;

    private ElevService() {
        this.eleviDupaCnp = new HashMap<>();
    }

    public static synchronized ElevService getInstance() {
        if (instance == null) {
            instance = new ElevService();
        }
        return instance;
    }

    public void adauga(Elev elev) {
        if (elev == null) {
            throw new IllegalArgumentException("Elev null");
        }
        eleviDupaCnp.put(elev.getCnp(), elev);
    }

    public void sterge(String cnp) {
        Elev removed = eleviDupaCnp.remove(cnp);
        if (removed == null) {
            throw ElevInexistentException.pentruCnp(cnp);
        }
        if (removed.getClasaScolara() != null) {
            removed.getClasaScolara().stergeElev(removed);
        }
    }

    public Elev cautaDupaCnp(String cnp) {
        Elev e = eleviDupaCnp.get(cnp);
        if (e == null) {
            throw ElevInexistentException.pentruCnp(cnp);
        }
        return e;
    }

    public Optional<Elev> gasesteDupaCnp(String cnp) {
        return Optional.ofNullable(eleviDupaCnp.get(cnp));
    }

    public List<Elev> cautaDupaNume(String nume, String prenume) {
        List<Elev> rezultat = new ArrayList<>();
        for (Elev e : eleviDupaCnp.values()) {
            if (e.getNume().equalsIgnoreCase(nume)
                    && e.getPrenume().equalsIgnoreCase(prenume)) {
                rezultat.add(e);
            }
        }
        return rezultat;
    }

    public List<Elev> listeazaToti() {
        List<Elev> toti = new ArrayList<>(eleviDupaCnp.values());
        Collections.sort(toti);
        return toti;
    }

    public int numarTotal() {
        return eleviDupaCnp.size();
    }
}
