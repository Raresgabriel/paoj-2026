package com.pao.laboratory14.exercise1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collector;

public class Main {

    static class Bilet {
        int id;
        String eveniment;
        TipBilet tip;
        double pret;

        Bilet(int id, String eveniment, TipBilet tip, double pret) {
            this.id = id;
            this.eveniment = eveniment;
            this.tip = tip;
            this.pret = pret;
        }
    }

    static class RaportVanzari {
        final Map<TipBilet, Long> numarPerTip;
        final Map<TipBilet, Double> incasariPerTip;
        final double totalGlobal;
        final double medieGlobala;
        final TipBilet tipCelMaiPopular;

        RaportVanzari(Map<TipBilet, Long> numar, Map<TipBilet, Double> incasari,
                      double total, double medie, TipBilet popular) {
            this.numarPerTip = Collections.unmodifiableMap(numar);
            this.incasariPerTip = Collections.unmodifiableMap(incasari);
            this.totalGlobal = total;
            this.medieGlobala = medie;
            this.tipCelMaiPopular = popular;
        }
    }

    static Collector<Bilet, ?, RaportVanzari> toRaport() {
        class Agg {
            Map<TipBilet, long[]> counts = new EnumMap<>(TipBilet.class);
            Map<TipBilet, double[]> sums = new EnumMap<>(TipBilet.class);
            long total = 0;
            double totalAmt = 0;
        }
        return Collector.of(
            Agg::new,
            (agg, b) -> {
                agg.counts.computeIfAbsent(b.tip, k -> new long[1])[0]++;
                agg.sums.computeIfAbsent(b.tip, k -> new double[1])[0] += b.pret;
                agg.total++;
                agg.totalAmt += b.pret;
            },
            (a, b) -> {
                b.counts.forEach((t, v) -> a.counts.computeIfAbsent(t, k -> new long[1])[0] += v[0]);
                b.sums.forEach((t, v) -> a.sums.computeIfAbsent(t, k -> new double[1])[0] += v[0]);
                a.total += b.total;
                a.totalAmt += b.totalAmt;
                return a;
            },
            agg -> {
                Map<TipBilet, Long> numar = new EnumMap<>(TipBilet.class);
                Map<TipBilet, Double> incasari = new EnumMap<>(TipBilet.class);
                agg.counts.forEach((t, v) -> numar.put(t, v[0]));
                agg.sums.forEach((t, v) -> incasari.put(t, v[0]));
                double medie = agg.total == 0 ? 0 : agg.totalAmt / agg.total;
                TipBilet popular = numar.entrySet().stream()
                    .max(Comparator.comparingLong(Map.Entry<TipBilet, Long>::getValue)
                        .thenComparing((a, b) -> b.getKey().name().compareTo(a.getKey().name())))
                    .map(Map.Entry::getKey).orElse(null);
                return new RaportVanzari(numar, incasari, agg.totalAmt, medie, popular);
            }
        );
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String first = nextNonEmpty(br);
        if (first == null) return;
        int n = Integer.parseInt(first);
        List<Bilet> bilete = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String line = nextNonEmpty(br);
            if (line == null) break;
            String[] tok = line.split("\\s+");
            bilete.add(new Bilet(
                Integer.parseInt(tok[0]),
                tok[1],
                TipBilet.valueOf(tok[2]),
                Double.parseDouble(tok[3])
            ));
        }
        String cmd = nextNonEmpty(br);
        if (cmd == null) return;

        RaportVanzari raport = bilete.stream().collect(toRaport());

        // Print per-tip lines sorted alphabetically (enum declaration order = alphabetical)
        Arrays.stream(TipBilet.values())
            .filter(t -> raport.numarPerTip.containsKey(t))
            .forEach(t -> System.out.printf("%s: count=%d incasari=%.2f RON%n",
                t.name(), raport.numarPerTip.get(t), raport.incasariPerTip.get(t)));

        if ("RAPORT_COMPLET".equals(cmd)) {
            System.out.println("---");
            System.out.printf("Total: %.2f RON%n", raport.totalGlobal);
            System.out.printf("Medie: %.2f RON%n", raport.medieGlobala);
            System.out.println("Cel mai popular: " + raport.tipCelMaiPopular);
        }
    }

    private static String nextNonEmpty(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) return line.trim();
        }
        return null;
    }
}
