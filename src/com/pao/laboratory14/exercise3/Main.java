package com.pao.laboratory14.exercise3;

import java.util.*;

/**
 * Bonus — Alocare Automata de Sali pentru Evenimente
 * Varianta 1 — greedy simplu O(N^2): prima sala disponibila
 * Varianta 2 — PriorityQueue O(N log N): min-heap de ore de final
 */
public class Main {

    record Eveniment(String nume, int startMin, int endMin) {}

    private static int toMin(String hhmm) {
        String[] p = hhmm.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }

    private static String toHHMM(int min) {
        return String.format("%02d:%02d", min / 60, min % 60);
    }

    public static void main(String[] args) {
        List<Eveniment> events = List.of(
            new Eveniment("Conferinta A",  toMin("09:00"), toMin("10:30")),
            new Eveniment("Workshop B",    toMin("09:30"), toMin("11:00")),
            new Eveniment("Seminar C",     toMin("10:00"), toMin("11:30")),
            new Eveniment("Prezentare D",  toMin("10:30"), toMin("12:00")),
            new Eveniment("Curs E",        toMin("11:00"), toMin("13:00")),
            new Eveniment("Demo F",        toMin("12:00"), toMin("14:00")),
            new Eveniment("Meetup G",      toMin("13:30"), toMin("15:00")),
            new Eveniment("Hackathon H",   toMin("14:00"), toMin("17:00"))
        );

        List<Eveniment> sorted = new ArrayList<>(events);
        sorted.sort(Comparator.comparingInt(Eveniment::startMin));

        // Varianta 1 — greedy O(N^2)
        System.out.println("=== Varianta 1 — Greedy O(N^2) ===");
        List<Integer> rooms = new ArrayList<>();
        Map<Eveniment, Integer> assignment = new LinkedHashMap<>();

        for (Eveniment ev : sorted) {
            int sala = -1;
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i) <= ev.startMin()) {
                    sala = i;
                    break;
                }
            }
            if (sala == -1) {
                rooms.add(ev.endMin());
                sala = rooms.size() - 1;
            } else {
                rooms.set(sala, ev.endMin());
            }
            assignment.put(ev, sala + 1);
        }

        for (Map.Entry<Eveniment, Integer> e : assignment.entrySet()) {
            Eveniment ev = e.getKey();
            System.out.printf("%-20s (%s - %s)  ->  Sala #%d%n",
                ev.nume(), toHHMM(ev.startMin()), toHHMM(ev.endMin()), e.getValue());
        }
        System.out.println("Numar minim sali (V1): " + rooms.size());

        // Varianta 2 — PriorityQueue O(N log N)
        System.out.println("\n=== Varianta 2 — PriorityQueue O(N log N) ===");
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (Eveniment ev : sorted) {
            if (!pq.isEmpty() && pq.peek() <= ev.startMin()) {
                pq.poll();
            }
            pq.offer(ev.endMin());
        }
        System.out.println("Numar minim sali (V2): " + pq.size());
    }
}
