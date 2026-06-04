package com.pao.proiect.catalog.model;

import java.util.Objects;

public final class AnScolar {

    private final int anStart;
    private final int anEnd;
    private final String denumire;

    public AnScolar(int anStart) {
        if (anStart < 1900 || anStart > 2100) {
            throw new IllegalArgumentException("An scolar invalid: " + anStart);
        }
        this.anStart = anStart;
        this.anEnd = anStart + 1;
        this.denumire = anStart + "-" + this.anEnd;
    }

    public int getAnStart() {
        return anStart;
    }

    public int getAnEnd() {
        return anEnd;
    }

    public String getDenumire() {
        return denumire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnScolar)) return false;
        AnScolar that = (AnScolar) o;
        return anStart == that.anStart;
    }

    @Override
    public int hashCode() {
        return Objects.hash(anStart);
    }

    @Override
    public String toString() {
        return "AnScolar{" + denumire + "}";
    }
}
