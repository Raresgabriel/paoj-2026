package com.pao.proiect.catalog.exception;

public class ElevInexistentException extends RuntimeException {

    public ElevInexistentException(String mesaj) {
        super(mesaj);
    }

    public static ElevInexistentException pentruCnp(String cnp) {
        return new ElevInexistentException("Nu exista elev cu CNP-ul: " + cnp);
    }
}
