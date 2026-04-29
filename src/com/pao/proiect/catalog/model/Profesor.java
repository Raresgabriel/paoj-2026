package com.pao.proiect.catalog.model;

import java.util.HashSet;
import java.util.Set;

public class Profesor extends Persoana {

    protected GradDidactic gradDidactic;
    protected Set<Materie> materiiPredate;

    public Profesor(String nume, String prenume, String cnp, GradDidactic gradDidactic) {
        super(nume, prenume, cnp);
        this.gradDidactic = gradDidactic;
        this.materiiPredate = new HashSet<>();
    }

    @Override
    public String getRol() {
        return "Profesor";
    }

    public GradDidactic getGradDidactic() {
        return gradDidactic;
    }

    public void setGradDidactic(GradDidactic gradDidactic) {
        this.gradDidactic = gradDidactic;
    }

    public Set<Materie> getMateriiPredate() {
        return materiiPredate;
    }

    public void adaugaMaterie(Materie materie) {
        if (materie != null) {
            this.materiiPredate.add(materie);
        }
    }

    public boolean predaMateria(Materie materie) {
        return materie != null && materiiPredate.contains(materie);
    }

    @Override
    public String toString() {
        return getRol() + "{" + nume + " " + prenume
                + ", cnp=" + cnp
                + ", grad=" + gradDidactic
                + ", materii=" + materiiPredate.size() + "}";
    }
}
