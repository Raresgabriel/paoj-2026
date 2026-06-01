package com.pao.laboratory14.exercise2;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.repository.EvenimentRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EvenimentRepository repo = new EvenimentRepository();
        try {
            repo.initSchema();
        } catch (SQLException e) {
            System.err.println("ERR schema: " + e.getMessage());
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] tok = line.split("\\s+");
                String cmd = tok[0];

                switch (cmd) {
                    case "ADD": {
                        String nume = tok[1];
                        String data = tok[2];
                        int cap = Integer.parseInt(tok[3]);
                        TipBilet tip = TipBilet.valueOf(tok[4]);
                        Eveniment ev = new Eveniment(nume, data, cap, tip);
                        repo.save(ev);
                        System.out.println("Adaugat: [" + ev.getId() + "] " + ev.getNume());
                        break;
                    }
                    case "LIST": {
                        List<Eveniment> list = repo.findAll();
                        for (Eveniment ev : list) {
                            System.out.println(ev);
                        }
                        break;
                    }
                    case "DELETE": {
                        int id = Integer.parseInt(tok[1]);
                        int rows = repo.deleteImpl(id);
                        if (rows > 0) {
                            System.out.println("Sters: " + id);
                        } else {
                            System.out.println("Nu exista: " + id);
                        }
                        break;
                    }
                    case "COUNT": {
                        System.out.println("Total: " + repo.count());
                        break;
                    }
                    default:
                        break;
                }
            }
        } catch (IOException | SQLException e) {
            System.err.println("ERR: " + e.getMessage());
        }
    }
}
