package com.pao.laboratory05.angajati;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajati =====");
            System.out.println("1. Adauga angajat");
            System.out.println("2. Listare dupa salariu");
            System.out.println("3. Cauta dupa departament");
            System.out.println("0. Iesire");
            System.out.print("Optiune: ");

            int optiune = Integer.parseInt(scanner.nextLine().trim());

            if (optiune == 0) {
                System.out.println("La revedere!");
                break;
            } else if (optiune == 1) {
                System.out.print("Nume: ");
                String nume = scanner.nextLine();
                System.out.print("Departament (nume): ");
                String numeDept = scanner.nextLine();
                System.out.print("Departament (locatie): ");
                String locatie = scanner.nextLine();
                System.out.print("Salariu: ");
                double salariu = Double.parseDouble(scanner.nextLine());
                service.addAngajat(new Angajat(nume, new Departament(numeDept, locatie), salariu));
            } else if (optiune == 2) {
                System.out.println("--- Angajati dupa salariu (descrescator) ---");
                service.listBySalary();
            } else if (optiune == 3) {
                System.out.print("Departament: ");
                String numeDept = scanner.nextLine();
                System.out.println("--- Angajati din " + numeDept + " ---");
                service.findByDepartament(numeDept);
            }
        }

        scanner.close();
    }
}
