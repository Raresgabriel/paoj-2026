package com.pao.proiect.catalog.model;

import java.util.Comparator;

public class Elev extends Persoana implements Comparable<Elev> {

    private Clasa clasaScolara;

    public Elev(String nume, String prenume, String cnp, Clasa clasaScolara) {
        super(nume, prenume, cnp);
        this.clasaScolara = clasaScolara;
    }

    @Override
    public String getRol() {
        return "Elev";
    }

    public Clasa getClasaScolara() {
        return clasaScolara;
    }

    public void setClasaScolara(Clasa clasaScolara) {
        this.clasaScolara = clasaScolara;
    }

    @Override
    public int compareTo(Elev altul) {
        return Comparator
                .comparing(Elev::getNume, Comparator.nullsLast(String::compareToIgnoreCase))
                .thenComparing(Elev::getPrenume, Comparator.nullsLast(String::compareToIgnoreCase))
                .compare(this, altul);
    }

    @Override
    public String toString() {
        String numeClasa = clasaScolara != null ? clasaScolara.getId() : "-";
        return "Elev{" + nume + " " + prenume + ", cnp=" + cnp + ", clasa=" + numeClasa + "}";
    }
}
