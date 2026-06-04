package com.pao.proiect.catalog.model;

import com.pao.proiect.catalog.exception.NotaInvalidaException;

import java.time.LocalDate;
import java.util.Objects;

public final class Nota {

    private final int valoare;
    private final LocalDate data;
    private final Elev elev;
    private final Materie materie;
    private final Profesor profesor;
    private final TipNota tip;

    public Nota(int valoare, LocalDate data, Elev elev, Materie materie,
                Profesor profesor, TipNota tip) {
        if (valoare < 1 || valoare > 10) {
            throw new NotaInvalidaException(
                    "Valoarea notei trebuie sa fie intre 1 si 10. Primit: " + valoare);
        }
        if (data == null) {
            throw new NotaInvalidaException("Data notei nu poate fi null");
        }
        if (elev == null) {
            throw new NotaInvalidaException("Elev null");
        }
        if (materie == null) {
            throw new NotaInvalidaException("Materie null");
        }
        if (profesor == null) {
            throw new NotaInvalidaException("Profesor null");
        }
        if (tip == null) {
            throw new NotaInvalidaException("Tip nota null");
        }
        this.valoare = valoare;
        this.data = data;
        this.elev = elev;
        this.materie = materie;
        this.profesor = profesor;
        this.tip = tip;
    }

    public int getValoare() {
        return valoare;
    }

    public LocalDate getData() {
        return data;
    }

    public Elev getElev() {
        return elev;
    }

    public Materie getMaterie() {
        return materie;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public TipNota getTip() {
        return tip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nota)) return false;
        Nota nota = (Nota) o;
        return valoare == nota.valoare
                && Objects.equals(data, nota.data)
                && Objects.equals(elev, nota.elev)
                && Objects.equals(materie, nota.materie)
                && Objects.equals(profesor, nota.profesor)
                && tip == nota.tip;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valoare, data, elev, materie, profesor, tip);
    }

    @Override
    public String toString() {
        return "Nota{" + valoare
                + ", " + tip
                + ", " + materie.getNume()
                + ", " + data
                + ", elev=" + elev.getNumeComplet()
                + ", prof=" + profesor.getNumeComplet() + "}";
    }
}
