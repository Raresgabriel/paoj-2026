package com.pao.proiect.catalog.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static final String FISIER_AUDIT = "audit.csv";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static AuditService instance;

    private AuditService() {
        java.io.File f = new java.io.File(FISIER_AUDIT);
        if (!f.exists() || f.length() == 0) {
            scrieHeader();
        }
    }

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public synchronized void log(String numeActiune) {
        String linie = numeActiune + "," + LocalDateTime.now().format(FMT);
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FISIER_AUDIT, true))) {
            bw.write(linie);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("[AUDIT ERROR] Nu pot scrie in " + FISIER_AUDIT + ": " + e.getMessage());
        }
    }

    private void scrieHeader() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FISIER_AUDIT, false))) {
            bw.write("nume_actiune,timestamp");
            bw.newLine();
        } catch (IOException e) {
            System.err.println("[AUDIT ERROR] Nu pot crea " + FISIER_AUDIT + ": " + e.getMessage());
        }
    }
}
