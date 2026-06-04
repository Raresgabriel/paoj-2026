package com.pao.proiect.catalog.model;

import java.util.Objects;

public class Materie implements Identificabil, Comparable<Materie> {

    private String nume;
    private final String cod;

    public Materie(String nume, String cod) {
        if (nume == null || nume.isBlank()) {
            throw new IllegalArgumentException("Nume materie invalid");
        }
        if (cod == null || cod.isBlank()) {
            throw new IllegalArgumentException("Cod materie invalid");
        }
        this.nume = nume;
        this.cod = cod;
    }

    @Override
    public String getId() {
        return cod;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getCod() {
        return cod;
    }

    @Override
    public int compareTo(Materie altul) {
        return this.nume.compareToIgnoreCase(altul.nume);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Materie)) return false;
        Materie materie = (Materie) o;
        return Objects.equals(cod, materie.cod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod);
    }

    @Override
    public String toString() {
        return "Materie{" + nume + " (" + cod + ")}";
    }
}
