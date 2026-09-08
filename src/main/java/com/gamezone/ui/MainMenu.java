package com.gamezone.ui;

import java.util.Scanner;

/**
 * Displays the main menu of the GameZone application.
 */
public class MainMenu {

    private final Scanner scanner;

    /**
     * Creates a new main menu.
     */
    public MainMenu() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the main menu options.
     */
    public void showMenu() {
        int option;

        do {
            System.out.println("\n===== GAMEZONE =====");
            System.out.println("1. Product management");
            System.out.println("2. Person management");
            System.out.println("3. Sale management");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Product management selected.");
                    break;

                case 2:
                    System.out.println("Person management selected.");
                    break;

                case 3:
                    System.out.println("Sale management selected.");
                    break;

                case 0:
                    System.out.println("Exiting GameZone...");
                    break;

                default:
                    System.out.println("Invalid option.");
            }

        } while (option != 0);
    }
}


