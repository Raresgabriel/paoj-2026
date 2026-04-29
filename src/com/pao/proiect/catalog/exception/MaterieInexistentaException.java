package com.pao.proiect.catalog.exception;

public class MaterieInexistentaException extends RuntimeException {

    public MaterieInexistentaException(String mesaj) {
        super(mesaj);
    }

    public static MaterieInexistentaException pentruCod(String cod) {
        return new MaterieInexistentaException("Nu exista materie cu codul: " + cod);
    }
}
