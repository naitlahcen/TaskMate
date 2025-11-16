package dev.taskmate;

import dev.taskmate.model.Task;
import dev.taskmate.repo.TaskRepository;
import dev.taskmate.service.TaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {

    // Farben (ANSI Codes)
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        TaskService service = new TaskService(new TaskRepository());

        boolean running = true;

        while (running) {

            // Schönes Menü
            System.out.println(BLUE + "\n==============================");
            System.out.println("        TASKMATE  v1.0");
            System.out.println("==============================" + RESET);

            System.out.println(CYAN + "1) Aufgaben anzeigen" + RESET);
            System.out.println(CYAN + "2) Aufgabe hinzufügen" + RESET);
            System.out.println(CYAN + "3) Aufgabe erledigt markieren" + RESET);
            System.out.println(CYAN + "4) Aufgabe löschen" + RESET);
            System.out.println(RED + "0) Beenden" + RESET);

            System.out.print(YELLOW + "\nDeine Wahl: " + RESET);
            String choice = in.nextLine().trim();

            switch (choice) {

                // LISTE ANZEIGEN
                case "1" -> {
                    List<Task> list = service.list(false, service.sortByDueAsc());

                    if (list.isEmpty()) {
                        System.out.println(RED + "Keine Aufgaben vorhanden." + RESET);
                    } else {
                        System.out.println(GREEN + "\n=== Aufgabenliste ===" + RESET);
                        for (Task t : list) {

                            String status;
                            if (t.isDone()) status = GREEN + "[X]" + RESET;
                            else status = RED + "[ ]" + RESET;

                            String due;
                            if (t.getDueDate() != null)
                                due = " (Fällig: " + t.getDueDate() + ")";
                            else
                                due = "";

                            String prio;
                            if (t.getPriority() == Task.Priority.HIGH) {
                                prio = RED + "[HIGH]" + RESET;
                            } else if (t.getPriority() == Task.Priority.MEDIUM) {
                                prio = YELLOW + "[MEDIUM]" + RESET;
                            } else {
                                prio = BLUE + "[LOW]" + RESET;
                            }

                            System.out.println(status + " #" + t.getId() + ": " + t.getTitle() + due + " " + prio);
                        }
                    }
                }

                // HINZUFÜGEN
                case "2" -> {
                    System.out.println(GREEN + "\n=== Neue Aufgabe hinzufügen ===" + RESET);

                    System.out.print("Titel: ");
                    String title = in.nextLine().trim();
                    if (title.isEmpty()) {
                        System.out.println(RED + "Titel darf nicht leer sein!" + RESET);
                        break;
                    }

                    System.out.print("Fälligkeitsdatum (YYYY-MM-DD) oder Enter: ");
                    String dateInput = in.nextLine().trim();

                    LocalDate dueDate;
                    if (dateInput.isEmpty()) {
                        dueDate = null;
                    } else {
                        try {
                            dueDate = LocalDate.parse(dateInput);
                        } catch (Exception e) {
                            System.out.println(RED + "Ungültiges Datum. Es wird ignoriert." + RESET);
                            dueDate = null;
                        }
                    }

                    System.out.print("Priorität (LOW, MEDIUM, HIGH) oder Enter: ");
                    String prioInput = in.nextLine().trim();

                    Task.Priority prio;
                    if (prioInput.isEmpty()) {
                        prio = Task.Priority.MEDIUM;
                    } else if (prioInput.equalsIgnoreCase("LOW")) {
                        prio = Task.Priority.LOW;
                    } else if (prioInput.equalsIgnoreCase("MEDIUM")) {
                        prio = Task.Priority.MEDIUM;
                    } else if (prioInput.equalsIgnoreCase("HIGH")) {
                        prio = Task.Priority.HIGH;
                    } else {
                        System.out.println(YELLOW + "Unbekannte Eingabe → MEDIUM gesetzt." + RESET);
                        prio = Task.Priority.MEDIUM;
                    }

                    Task neu = service.add(title, dueDate, prio);
                    System.out.println(GREEN + "Aufgabe hinzugefügt: #" + neu.getId() + RESET);
                }

                // ERLEDIGT MARKIEREN
                case "3" -> {
                    System.out.println("\n=== Aufgabe erledigt markieren ===");
                    System.out.print("ID eingeben: ");

                    String input = in.nextLine().trim();

                    int id;
                    try {
                        id = Integer.parseInt(input);
                    } catch (Exception e) {
                        System.out.println(RED + "Ungültige ID!" + RESET);
                        break;
                    }

                    boolean ok = service.markDone(id);

                    if (ok) System.out.println(GREEN + "Aufgabe #" + id + " erledigt!" + RESET);
                    else System.out.println(RED + "ID nicht gefunden." + RESET);
                }

                // LÖSCHEN
                case "4" -> {
                    System.out.println("\n=== Aufgabe löschen ===");
                    System.out.print("ID eingeben: ");

                    String input = in.nextLine().trim();

                    int id;
                    try {
                        id = Integer.parseInt(input);
                    } catch (Exception e) {
                        System.out.println(RED + "Ungültige ID!" + RESET);
                        break;
                    }

                    boolean ok = service.delete(id);

                    if (ok) System.out.println(GREEN + "Aufgabe #" + id + " wurde gelöscht." + RESET);
                    else System.out.println(RED + "ID nicht gefunden." + RESET);
                }

                // BEENDEN
                case "0" -> {
                    System.out.println(BLUE + "TaskMate wird beendet..." + RESET);
                    running = false;
                }

                // FEHLER
                default -> System.out.println(RED + "Ungültige Eingabe!" + RESET);
            }
        }
    }
}
