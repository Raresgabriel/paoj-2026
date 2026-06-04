package com.pao.proiect.catalog.model;

public class Diriginte extends Profesor {

    private Clasa clasaDirigentie;

    public Diriginte(String nume, String prenume, String cnp, GradDidactic gradDidactic) {
        super(nume, prenume, cnp, gradDidactic);
    }

    public Diriginte(String nume, String prenume, String cnp,
                     GradDidactic gradDidactic, Clasa clasaDirigentie) {
        super(nume, prenume, cnp, gradDidactic);
        this.clasaDirigentie = clasaDirigentie;
    }

    @Override
    public String getRol() {
        return "Diriginte";
    }

    public Clasa getClasaDirigentie() {
        return clasaDirigentie;
    }

    public void setClasaDirigentie(Clasa clasaDirigentie) {
        this.clasaDirigentie = clasaDirigentie;
    }

    @Override
    public String toString() {
        String numeClasa = clasaDirigentie != null ? clasaDirigentie.getId() : "-";
        return "Diriginte{" + nume + " " + prenume
                + ", cnp=" + cnp
                + ", grad=" + gradDidactic
                + ", clasa=" + numeClasa + "}";
    }
}
