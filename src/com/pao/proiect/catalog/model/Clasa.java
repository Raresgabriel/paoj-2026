package com.pao.proiect.catalog.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Clasa implements Identificabil {

    private final String id;
    private Diriginte diriginte;
    private AnScolar anScolar;
    private final List<Elev> elevi;

    public Clasa(String id, AnScolar anScolar) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id clasa invalid");
        }
        this.id = id;
        this.anScolar = anScolar;
        this.elevi = new ArrayList<>();
    }

    public Clasa(String id, AnScolar anScolar, Diriginte diriginte) {
        this(id, anScolar);
        this.diriginte = diriginte;
        if (diriginte != null) {
            diriginte.setClasaDirigentie(this);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public Diriginte getDiriginte() {
        return diriginte;
    }

    public void setDiriginte(Diriginte diriginte) {
        this.diriginte = diriginte;
        if (diriginte != null) {
            diriginte.setClasaDirigentie(this);
        }
    }

    public AnScolar getAnScolar() {
        return anScolar;
    }

    public void setAnScolar(AnScolar anScolar) {
        this.anScolar = anScolar;
    }

    public List<Elev> getElevi() {
        return Collections.unmodifiableList(elevi);
    }

    public void adaugaElev(Elev elev) {
        if (elev == null) return;
        if (!elevi.contains(elev)) {
            elevi.add(elev);
            elev.setClasaScolara(this);
        }
    }

    public boolean stergeElev(Elev elev) {
        boolean removed = elevi.remove(elev);
        if (removed && elev != null && this.equals(elev.getClasaScolara())) {
            elev.setClasaScolara(null);
        }
        return removed;
    }

    public int numarElevi() {
        return elevi.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clasa)) return false;
        Clasa clasa = (Clasa) o;
        return Objects.equals(id, clasa.id) && Objects.equals(anScolar, clasa.anScolar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anScolar);
    }

    @Override
    public String toString() {
        String numeDir = diriginte != null
                ? diriginte.getNumeComplet()
                : "fara diriginte";
        return "Clasa{" + id
                + ", an=" + (anScolar != null ? anScolar.getDenumire() : "-")
                + ", elevi=" + elevi.size()
                + ", diriginte=" + numeDir + "}";
    }
}
