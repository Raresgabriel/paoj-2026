package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;

/**
 * Bonus — Colector custom care produce un Snapshot analitic imutabil.
 */
public class Main {

    public record Transaction(int id, BigDecimal amount, LocalDate date, String country, String channel) {}

    public static final class Snapshot {
        private final Map<String, Long> countByCountry;
        private final Map<String, Long> countByChannel;
        private final BigDecimal totalAmount;
        private final List<Transaction> topTransactions;

        public Snapshot(Map<String, Long> byCountry, Map<String, Long> byChannel,
                        BigDecimal total, List<Transaction> top) {
            this.countByCountry = Collections.unmodifiableMap(new HashMap<>(byCountry));
            this.countByChannel = Collections.unmodifiableMap(new HashMap<>(byChannel));
            this.totalAmount = total;
            this.topTransactions = List.copyOf(top);
        }

        public Map<String, Long> getCountByCountry() { return countByCountry; }
        public Map<String, Long> getCountByChannel() { return countByChannel; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public List<Transaction> getTopTransactions() { return topTransactions; }
    }

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        class Agg {
            Map<String, Long> byCountry = new HashMap<>();
            Map<String, Long> byChannel = new HashMap<>();
            BigDecimal total = BigDecimal.ZERO;
            List<Transaction> all = new ArrayList<>();
        }
        return Collector.of(
            Agg::new,
            (agg, tx) -> {
                agg.byCountry.merge(tx.country(), 1L, Long::sum);
                agg.byChannel.merge(tx.channel(), 1L, Long::sum);
                agg.total = agg.total.add(tx.amount());
                agg.all.add(tx);
            },
            (a, b) -> {
                b.byCountry.forEach((k, v) -> a.byCountry.merge(k, v, Long::sum));
                b.byChannel.forEach((k, v) -> a.byChannel.merge(k, v, Long::sum));
                a.total = a.total.add(b.total);
                a.all.addAll(b.all);
                return a;
            },
            agg -> {
                List<Transaction> top = new ArrayList<>(agg.all);
                top.sort(Comparator.comparing(Transaction::amount).reversed()
                    .thenComparingInt(Transaction::id));
                if (top.size() > topN) top = top.subList(0, topN);
                return new Snapshot(agg.byCountry, agg.byChannel, agg.total, top);
            }
        );
    }

    public static void main(String[] args) {
        List<Transaction> data = List.of(
            new Transaction(1, new BigDecimal("1200.00"), LocalDate.of(2026, 5, 1), "RO", "WEB"),
            new Transaction(2, new BigDecimal("90.00"),   LocalDate.of(2026, 5, 2), "RU", "ATM"),
            new Transaction(3, new BigDecimal("6000.00"), LocalDate.of(2026, 5, 3), "NG", "APP"),
            new Transaction(4, new BigDecimal("500.00"),  LocalDate.of(2026, 6, 1), "RO", "POS"),
            new Transaction(5, new BigDecimal("100.00"),  LocalDate.of(2026, 6, 2), "KP", "CRYPTO"),
            new Transaction(6, new BigDecimal("3000.00"), LocalDate.of(2026, 6, 3), "RU", "WEB"),
            new Transaction(7, new BigDecimal("75.00"),   LocalDate.of(2026, 7, 1), "RO", "ATM"),
            new Transaction(8, new BigDecimal("2200.00"), LocalDate.of(2026, 7, 2), "IR", "APP")
        );

        Snapshot snap = data.stream().collect(toSnapshot(3));

        // Interogare 1: top 3 tranzacții după sumă
        System.out.println("=== Top tranzactii ===");
        snap.getTopTransactions().forEach(tx ->
            System.out.println("  [" + tx.id() + "] " + tx.amount() + " " + tx.country()));

        // Interogare 2: tari ordonate descendent după număr tranzacții
        System.out.println("=== Tranzactii per tara ===");
        snap.getCountByCountry().entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey()))
            .forEach(e -> System.out.println("  " + e.getKey() + ": " + e.getValue()));

        // Interogare 3: canale ordonate descendent după număr
        System.out.println("=== Tranzactii per canal ===");
        snap.getCountByChannel().entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey()))
            .forEach(e -> System.out.println("  " + e.getKey() + ": " + e.getValue()));

        System.out.println("=== Total: " + snap.getTotalAmount() + " RON ===");
    }
}
