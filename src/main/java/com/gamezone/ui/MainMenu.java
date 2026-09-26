package com.gamezone.ui;

import com.gamezone.model.Console;
import com.gamezone.model.Customer;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Displays the main menu and the submenus of the GameZone application.
 */
public class MainMenu {

    private final Scanner scanner;
    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;

    /**
     * Creates a new main menu.
     *
     * @param productService service for product operations
     * @param personService service for person operations
     * @param saleService service for sale operations
     */
    public MainMenu(ProductService productService,
                    PersonService personService,
                    SaleService saleService) {

        scanner = new Scanner(System.in);
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
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
            scanner.nextLine();

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
            scanner.nextLine();

            switch (option) {

                case 1:
                    registerVideoGame();
                    break;

                case 2:
                    registerConsole();
                    break;

                case 3:
                    listProducts();
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
     * Registers a video game using ProductService.
     */
    private void registerVideoGame() {

        System.out.println("\n===== REGISTRAR VIDEOJUEGO =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("Precio: ");
        double price = scanner.nextDouble();

        System.out.print("Cantidad en stock: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Plataforma: ");
        String platform = scanner.nextLine();

        System.out.print("Género: ");
        String genre = scanner.nextLine();

        System.out.print("Clasificación por edad: ");
        String ageClassification = scanner.nextLine();

        VideoGame videoGame = new VideoGame(
                id,
                title,
                price,
                stockQuantity,
                platform,
                genre,
                ageClassification
        );

        productService.registerProduct(videoGame);

        System.out.println("Videojuego registrado correctamente.");
    }

    /**
     * Registers a console using ProductService.
     */
    private void registerConsole() {

        System.out.println("\n===== REGISTRAR CONSOLA =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("Precio: ");
        double price = scanner.nextDouble();

        System.out.print("Cantidad en stock: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Marca: ");
        String brand = scanner.nextLine();

        System.out.print("Modelo: ");
        String model = scanner.nextLine();

        System.out.print("Generación: ");
        String generation = scanner.nextLine();

        Console console = new Console(
                id,
                title,
                price,
                stockQuantity,
                brand,
                model,
                generation
        );

        productService.registerProduct(console);

        System.out.println("Consola registrada correctamente.");
    }

    /**
     * Displays all products using ProductService.
     */
    private void listProducts() {

        System.out.println("\n===== LISTA DE PRODUCTOS =====");

        if (productService.getAllProducts().isEmpty()) {

            System.out.println("No hay productos registrados.");

            return;
        }

        for (Product product : productService.getAllProducts()) {

            System.out.println(
                    "ID: " + product.getId()
                            + " | " + product.getDescription()
                            + " | Stock: " + product.getStockQuantity()
            );
        }
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
            scanner.nextLine();

            switch (option) {

                case 1:
                    registerCustomer();
                    break;

                case 2:
                    listCustomers();
                    break;

                case 3:
                    listSellers();
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
     * Registers a customer using PersonService.
     */
    private void registerCustomer() {

        System.out.println("\n===== REGISTRAR CLIENTE =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nombre: ");
        String name = scanner.nextLine();

        System.out.print("Teléfono: ");
        String phone = scanner.nextLine();

        System.out.print("Correo electrónico: ");
        String email = scanner.nextLine();

        Customer customer = new Customer(
                id,
                name,
                phone,
                email
        );

        personService.registerCustomer(customer);

        System.out.println("Cliente registrado correctamente.");
    }

    /**
     * Displays all customers using PersonService.
     */
    private void listCustomers() {

        System.out.println("\n===== LISTA DE CLIENTES =====");

        if (personService.getAllCustomers().isEmpty()) {

            System.out.println("No hay clientes registrados.");

            return;
        }

        personService.getAllCustomers().forEach(customer ->
                System.out.println(
                        "ID: " + customer.getId()
                                + " | Nombre: " + customer.getName()
                                + " | Teléfono: " + customer.getPhone()
                                + " | Email: " + customer.getEmail()
                )
        );
    }

    /**
     * Displays all sellers using PersonService.
     */
    private void listSellers() {

        System.out.println("\n===== LISTA DE VENDEDORES =====");

        if (personService.getAllSellers().isEmpty()) {

            System.out.println("No hay vendedores registrados.");

            return;
        }

        personService.getAllSellers().forEach(seller ->
                System.out.println(
                        "ID: " + seller.getId()
                                + " | Nombre: " + seller.getName()
                                + " | Teléfono: " + seller.getPhone()
                                + " | Código: " + seller.getCodeEmployee()
                                + " | Turno: " + seller.getShift()
                )
        );
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
            scanner.nextLine();

            switch (option) {

                case 1:
                    registerSale();
                    break;

                case 2:
                    listSales();
                    break;

                case 3:
                    System.out.println(
                            "El historial de compras todavía no está implementado "
                                    + "en SaleService."
                    );
                    break;

                case 4:
                    System.out.println(
                            "El historial de ventas todavía no está implementado "
                                    + "en SaleService."
                    );
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
     * Registers a sale and manages extended warranty selection.
     */
    private void registerSale() {

        System.out.println("\n===== REGISTRAR VENTA =====");

        if (personService.getAllCustomers().isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        if (personService.getAllSellers().isEmpty()) {
            System.out.println("No hay vendedores registrados.");
            return;
        }

        if (productService.getAllProducts().isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        System.out.print("ID de la venta: ");
        String saleId = scanner.nextLine();

        System.out.print("Fecha de la venta: ");
        String date = scanner.nextLine();

        System.out.println("\n===== CLIENTES =====");

        personService.getAllCustomers().forEach(customer ->
                System.out.println(
                        "ID: " + customer.getId()
                                + " | Nombre: " + customer.getName()
                )
        );

        System.out.print("ID del cliente: ");
        String customerId = scanner.nextLine();

        Customer customer = null;

        for (Customer c : personService.getAllCustomers()) {
            if (c.getId().equals(customerId)) {
                customer = c;
                break;
            }
        }

        if (customer == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.println("\n===== VENDEDORES =====");

        personService.getAllSellers().forEach(seller ->
                System.out.println(
                        "ID: " + seller.getId()
                                + " | Nombre: " + seller.getName()
                )
        );

        System.out.print("ID del vendedor: ");
        String sellerId = scanner.nextLine();

        Seller seller = null;

        for (Seller s : personService.getAllSellers()) {
            if (s.getId().equals(sellerId)) {
                seller = s;
                break;
            }
        }

        if (seller == null) {
            System.out.println("Vendedor no encontrado.");
            return;
        }

        System.out.println("\n===== PRODUCTOS =====");

        productService.getAllProducts().forEach(product ->
                System.out.println(
                        "ID: " + product.getId()
                                + " | " + product.getDescription()
                                + " | Precio: " + product.getPrice()
                                + " | Stock: " + product.getStockQuantity()
                )
        );

        System.out.print("ID del producto: ");
        String productId = scanner.nextLine();

        Product product = productService.findById(productId);

        if (product == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        if (product.getStockQuantity() < 1) {
            System.out.println("No hay stock disponible para este producto.");
            return;
        }

        List<Product> products = new ArrayList<>();
        products.add(product);

        List<String> productIdsWithExtendedWarranty = new ArrayList<>();

        if (product instanceof Console) {

            System.out.print(
                    "¿Desea agregar garantía extendida a esta consola? (S/N): "
            );

            String warrantyOption = scanner.nextLine();

            if (warrantyOption.equalsIgnoreCase("S")) {
                productIdsWithExtendedWarranty.add(product.getId());
            }
        }

        Sale sale = new Sale(
                saleId,
                date,
                customer,
                seller,
                products
        );

        try {

            saleService.registerSale(
                    sale,
                    productIdsWithExtendedWarranty
            );

            System.out.println("\nVenta registrada correctamente.");
            System.out.println(sale.generateReceipt());

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "No se pudo registrar la venta: " + e.getMessage()
            );
        }
    }

    /**
     * Displays all registered sales using SaleService.
     */
    private void listSales() {

        System.out.println("\n===== LISTA DE VENTAS =====");

        if (saleService.findAll().isEmpty()) {

            System.out.println("No hay ventas registradas.");

            return;
        }

        saleService.findAll().forEach(sale ->
                System.out.println(
                        "ID Venta: " + sale.getSaleId()
                                + " | Fecha: " + sale.getDate()
                                + " | Cliente: " + sale.getCustomer().getName()
                                + " | Vendedor: " + sale.getSeller().getName()
                                + " | Total: "
                                + (
                                sale.calculateTotal()
                                        - sale.getDiscountAmount()
                                        + sale.getWarrantyAdditionalCost()
                        )
                )
        );
    }
}