package com.gamezone.ui;

import java.util.Scanner;

/**
 * Displays the main menu and the submenus of the GameZone application.
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
            System.out.println("1. Gestión de productos");
            System.out.println("2. Gestión de personas");
            System.out.println("3. Gestión de ventas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    showProductMenu();
                    break;

                case 2:
                    showPersonMenu();
                    break;

                case 3:
                    showSaleMenu();
                    break;

                case 0:
                    System.out.println("Saliendo de GameZone...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (option != 0);
    }

    /**
     * Displays the product management submenu.
     */
    private void showProductMenu() {
        int option;

        do {
            System.out.println("\n===== GESTIÓN DE PRODUCTOS =====");
            System.out.println("1. Registrar videojuego");
            System.out.println("2. Registrar consola");
            System.out.println("3. Listar productos");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Registrar videojuego seleccionado.");
                    break;

                case 2:
                    System.out.println("Registrar consola seleccionado.");
                    break;

                case 3:
                    System.out.println("Listar productos seleccionado.");
                    break;

                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (option != 0);
    }

    /**
     * Displays the person management submenu.
     */
    private void showPersonMenu() {
        int option;

        do {
            System.out.println("\n===== GESTIÓN DE PERSONAS =====");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Listar vendedores");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Registrar cliente seleccionado.");
                    break;

                case 2:
                    System.out.println("Listar clientes seleccionado.");
                    break;

                case 3:
                    System.out.println("Listar vendedores seleccionado.");
                    break;

                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (option != 0);
    }

    /**
     * Displays the sale management submenu.
     */
    private void showSaleMenu() {
        int option;

        do {
            System.out.println("\n===== GESTIÓN DE VENTAS =====");
            System.out.println("1. Registrar venta");
            System.out.println("2. Listar ventas");
            System.out.println("3. Historial de compras del cliente");
            System.out.println("4. Historial de ventas del vendedor");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Registrar venta seleccionado.");
                    break;

                case 2:
                    System.out.println("Listar ventas seleccionado.");
                    break;

                case 3:
                    System.out.println("Historial de compras del cliente seleccionado.");
                    break;

                case 4:
                    System.out.println("Historial de ventas del vendedor seleccionado.");
                    break;

                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (option != 0);
    }
}

