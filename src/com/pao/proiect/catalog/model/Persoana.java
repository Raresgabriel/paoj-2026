package com.pao.proiect.catalog.model;

import java.util.Objects;

public abstract class Persoana implements Identificabil {

    protected String nume;
    protected String prenume;
    protected final String cnp;

    protected Persoana(String nume, String prenume, String cnp) {
        if (cnp == null || cnp.isBlank()) {
            throw new IllegalArgumentException("CNP invalid");
        }
        if (nume == null || nume.isBlank()) {
            throw new IllegalArgumentException("Nume invalid");
        }
        if (prenume == null || prenume.isBlank()) {
            throw new IllegalArgumentException("Prenume invalid");
        }
        this.nume = nume;
        this.prenume = prenume;
        this.cnp = cnp;
    }

    public abstract String getRol();

    @Override
    public String getId() {
        return cnp;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getCnp() {
        return cnp;
    }

    public String getNumeComplet() {
        return nume + " " + prenume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persoana persoana = (Persoana) o;
        return Objects.equals(cnp, persoana.cnp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), cnp);
    }

    @Override
    public String toString() {
        return getRol() + "{" + nume + " " + prenume + ", cnp=" + cnp + "}";
    }
}
