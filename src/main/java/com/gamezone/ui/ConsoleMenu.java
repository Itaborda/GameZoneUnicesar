package com.gamezone.ui;

import com.gamezone.model.Console;
import com.gamezone.model.Customer;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Accessory;
import com.gamezone.model.Memory;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.model.Return;
import com.gamezone.model.Promotion;
import com.gamezone.model.Warranty;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.WarrantyService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Displays the main menu and the submenus of the GameZone application.
 */
public class ConsoleMenu {

    private final Scanner scanner;
    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;
    private final ReturnService returnService;
    private final AccessoryService accessoryService;
    private PromotionService promotionService;
    private final WarrantyService warrantyService;

    /**
     * Creates a new main menu.
     *
     * @param productService service for product operations
     * @param personService service for person operations
     * @param saleService service for sale operations
     * @param returnService service for return operations
     * @param accessoryService service for accessory operations
     * @param promotionService service for promotion operations
     * @param warrantyService service for warranty operations
     */
    public ConsoleMenu(ProductService productService,
                       PersonService personService,
                       SaleService saleService,
                       ReturnService returnService,
                       AccessoryService accessoryService,
                       PromotionService promotionService,
                       WarrantyService warrantyService) {

        scanner = new Scanner(System.in);
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.returnService = returnService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.warrantyService = warrantyService;
    }

    /**
     * Displays the main menu options.
     */
    public void showMenu() {

        int option;

        do {
            System.out.println("...:::GAME-ZONE::::...");
            System.out.println("1. Gestión de productos");
            System.out.println("2. Gestión de personas");
            System.out.println("3. Gestión de ventas");
            System.out.println("4. Gestión de devoluciones");
            System.out.println("5. Gestión de promociones");
            System.out.println("6. Gestión de accesorios");
            System.out.println("7. Gestión de garantías");
            System.out.println("0. Salir");

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

                case 4:
                    showReturnMenu();
                    break;

                case 5:
                    showPromotionMenu();
                    break;

                case 6:
                    accessoryMenu();
                    break;

                case 7:
                    showWarrantyMenu();
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
     * Displays the warranty management submenu.
     */
    private void showWarrantyMenu() {

        int option;

        do {
            System.out.println("\n===== GESTIÓN DE GARANTÍAS =====");
            System.out.println("1. Consultar garantía por producto y venta");
            System.out.println("2. Listar todas las garantías");
            System.out.println("3. Listar garantías activas");
            System.out.println("4. Listar garantías próximas a vencer");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {

                case 1:
                    findWarranty();
                    break;

                case 2:
                    listAllWarranties();
                    break;

                case 3:
                    listActiveWarranties();
                    break;

                case 4:
                    listWarrantiesExpiringSoon();
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
     * Finds a warranty associated with a product in a specific sale.
     */
    private void findWarranty() {

        System.out.println("\n===== CONSULTAR GARANTÍA =====");

        System.out.print("ID del producto: ");
        String productId = scanner.nextLine();

        System.out.print("ID de la venta: ");
        String saleId = scanner.nextLine();

        Warranty warranty =
                warrantyService.findWarrantyByProduct(
                        productId,
                        saleId
                );

        if (warranty == null) {
            System.out.println(
                    "No se encontró una garantía para ese producto y venta."
            );
            return;
        }

        System.out.println(warranty.generateWarrantyCertificate());
    }

    /**
     * Displays all registered warranties.
     */
    private void listAllWarranties() {

        System.out.println("\n===== TODAS LAS GARANTÍAS =====");

        List<Warranty> warranties =
                warrantyService.listAllWarranties();

        if (warranties.isEmpty()) {
            System.out.println("No hay garantías registradas.");
            return;
        }

        for (Warranty warranty : warranties) {
            System.out.println(
                    warranty.generateWarrantyCertificate()
            );
        }
    }

    /**
     * Displays all currently active warranties.
     */
    private void listActiveWarranties() {

        System.out.println("\n===== GARANTÍAS ACTIVAS =====");

        List<Warranty> warranties =
                warrantyService.listActiveWarranties();

        if (warranties.isEmpty()) {
            System.out.println("No hay garantías activas.");
            return;
        }

        for (Warranty warranty : warranties) {
            System.out.println(
                    warranty.generateWarrantyCertificate()
            );
        }
    }

    /**
     * Displays warranties expiring within a specified number of days.
     */
    private void listWarrantiesExpiringSoon() {

        System.out.println("\n===== GARANTÍAS PRÓXIMAS A VENCER =====");

        System.out.print(
                "¿Cuántos días hacia adelante desea consultar?: "
        );

        int daysAhead = scanner.nextInt();
        scanner.nextLine();

        if (daysAhead < 0) {
            System.out.println(
                    "Los días deben ser mayores o iguales a cero."
            );
            return;
        }

        List<Warranty> warranties =
                warrantyService.listWarrantiesExpiringSoon(daysAhead);

        if (warranties.isEmpty()) {
            System.out.println(
                    "No hay garantías que venzan dentro del período indicado."
            );
            return;
        }

        for (Warranty warranty : warranties) {
            System.out.println(
                    warranty.generateWarrantyCertificate()
            );
        }
    }

    /**
     * Displays the promotion management submenu.
     */
    private void showPromotionMenu() {

        int option;

        do {
            System.out.println("\n===== GESTIÓN DE PROMOCIONES =====");
            System.out.println("1. Registrar descuento porcentual");
            System.out.println("2. Registrar descuento por categoría");
            System.out.println("3. Registrar descuento por volumen");
            System.out.println("4. Listar todas las promociones");
            System.out.println("5. Listar promociones activas");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {

                case 1:
                    registerPercentageDiscount();
                    break;

                case 2:
                    registerCategoryDiscount();
                    break;

                case 3:
                    registerBulkPurchaseDiscount();
                    break;

                case 4:
                    listAllPromotions();
                    break;

                case 5:
                    listActivePromotions();
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
     * Registers a percentage discount promotion.
     */
    private void registerPercentageDiscount() {

        System.out.println("\n===== REGISTRAR DESCUENTO PORCENTUAL =====");

        try {
            System.out.print("ID de la promoción: ");
            String id = scanner.nextLine();

            System.out.print("Nombre de la promoción: ");
            String name = scanner.nextLine();

            System.out.print("Fecha de inicio (YYYY-MM-DD): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Fecha de finalización (YYYY-MM-DD): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Porcentaje de descuento: ");
            double discountPercentage = scanner.nextDouble();
            scanner.nextLine();

            PercentageDiscount promotion =
                    new PercentageDiscount(
                            id,
                            name,
                            startDate,
                            endDate,
                            discountPercentage
                    );

            promotionService.registerPercentageDiscount(promotion);

            System.out.println(
                    "Descuento porcentual registrado correctamente."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "No se pudo registrar la promoción: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Registers a category discount promotion.
     */
    private void registerCategoryDiscount() {

        System.out.println("\n===== REGISTRAR DESCUENTO POR CATEGORÍA =====");

        try {
            System.out.print("ID de la promoción: ");
            String id = scanner.nextLine();

            System.out.print("Nombre de la promoción: ");
            String name = scanner.nextLine();

            System.out.print("Fecha de inicio (YYYY-MM-DD): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Fecha de finalización (YYYY-MM-DD): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Porcentaje de descuento: ");
            double discountPercentage = scanner.nextDouble();
            scanner.nextLine();

            System.out.print(
                    "Categoría objetivo (VIDEOGAME/CONSOLE): "
            );
            String targetCategory = scanner.nextLine().trim().toUpperCase();

            CategoryDiscount promotion =
                    new CategoryDiscount(
                            id,
                            name,
                            startDate,
                            endDate,
                            discountPercentage,
                            targetCategory
                    );

            promotionService.registerCategoryDiscount(promotion);

            System.out.println(
                    "Descuento por categoría registrado correctamente."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "No se pudo registrar la promoción: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Registers a bulk purchase discount promotion.
     */
    private void registerBulkPurchaseDiscount() {

        System.out.println("\n===== REGISTRAR DESCUENTO POR VOLUMEN =====");

        try {
            System.out.print("ID de la promoción: ");
            String id = scanner.nextLine();

            System.out.print("Nombre de la promoción: ");
            String name = scanner.nextLine();

            System.out.print("Fecha de inicio (YYYY-MM-DD): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Fecha de finalización (YYYY-MM-DD): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Cantidad mínima de productos: ");
            int minimumQuantity = scanner.nextInt();

            System.out.print("Porcentaje de descuento: ");
            double discountPercentage = scanner.nextDouble();
            scanner.nextLine();

            BulkPurchaseDiscount promotion =
                    new BulkPurchaseDiscount(
                            id,
                            name,
                            startDate,
                            endDate,
                            minimumQuantity,
                            discountPercentage
                    );

            promotionService.registerBulkPurchaseDiscount(promotion);

            System.out.println(
                    "Descuento por volumen registrado correctamente."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "No se pudo registrar la promoción: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Displays all registered promotions.
     */
    private void listAllPromotions() {

        System.out.println("\n===== TODAS LAS PROMOCIONES =====");

        List<Promotion> promotions =
                promotionService.listAllPromotions();

        if (promotions.isEmpty()) {
            System.out.println("No hay promociones registradas.");
            return;
        }

        for (Promotion promotion : promotions) {
            printPromotion(promotion);
        }
    }

    /**
     * Displays all currently active promotions.
     */
    private void listActivePromotions() {

        System.out.println("\n===== PROMOCIONES ACTIVAS =====");

        List<Promotion> promotions =
                promotionService.listActivePromotions();

        if (promotions.isEmpty()) {
            System.out.println("No hay promociones activas.");
            return;
        }

        for (Promotion promotion : promotions) {
            printPromotion(promotion);
        }
    }

    /**
     * Displays the information of a promotion.
     *
     * @param promotion promotion to display
     */
    private void printPromotion(Promotion promotion) {

        System.out.println("----------------------------------");
        System.out.println("ID: " + promotion.getId());
        System.out.println("Nombre: " + promotion.getName());
        System.out.println("Fecha de inicio: " + promotion.getStartDate());
        System.out.println("Fecha de finalización: " + promotion.getEndDate());

        if (promotion instanceof PercentageDiscount) {

            PercentageDiscount percentageDiscount =
                    (PercentageDiscount) promotion;

            System.out.println("Tipo: Descuento porcentual");
            System.out.println(
                    "Descuento: "
                            + percentageDiscount.getDiscountPercentage()
                            + "%"
            );

        } else if (promotion instanceof CategoryDiscount) {

            CategoryDiscount categoryDiscount =
                    (CategoryDiscount) promotion;

            System.out.println("Tipo: Descuento por categoría");
            System.out.println(
                    "Descuento: "
                            + categoryDiscount.getDiscountPercentage()
                            + "%"
            );
            System.out.println(
                    "Categoría: "
                            + categoryDiscount.getTargetCategory()
            );

        } else if (promotion instanceof BulkPurchaseDiscount) {

            BulkPurchaseDiscount bulkPurchaseDiscount =
                    (BulkPurchaseDiscount) promotion;

            System.out.println("Tipo: Descuento por volumen");
            System.out.println(
                    "Cantidad mínima: "
                            + bulkPurchaseDiscount.getMinimumQuantity()
            );
            System.out.println(
                    "Descuento: "
                            + bulkPurchaseDiscount.getDiscountPercentage()
                            + "%"
            );
        }
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
     * Displays the accessory management submenu.
     */
    private void accessoryMenu() {

        int option;

        do {
            System.out.println("\n===== GESTIÓN DE ACCESORIOS =====");
            System.out.println("1. Registrar controller");
            System.out.println("2. Registrar cable");
            System.out.println("3. Registrar memoria");
            System.out.println("4. Listar todos los accesorios");
            System.out.println("5. Listar accesorios por tipo");
            System.out.println("6. Consultar accesorios compatibles con consola");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {

                case 1:
                    registerController();
                    break;

                case 2:
                    registerCable();
                    break;

                case 3:
                    registerMemory();
                    break;

                case 4:
                    listAllAccessories();
                    break;

                case 5:
                    listAccessoriesByType();
                    break;

                case 6:
                    findAccessoriesCompatibleWithConsole();
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
     * Registers a controller using AccessoryService.
     */
    private void registerController() {

        System.out.println("\n===== REGISTRAR CONTROLLER =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("Precio: ");
        double price = scanner.nextDouble();

        System.out.print("Cantidad en stock: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("IDs de consolas compatibles separados por coma: ");
        String compatibleInput = scanner.nextLine();

        List<String> compatibleConsoleIds = new ArrayList<>();

        for (String consoleId : compatibleInput.split(",")) {
            compatibleConsoleIds.add(consoleId.trim());
        }

        System.out.print("Tipo de conexión (WIRELESS/WIRED): ");
        String connectionType = scanner.nextLine();

        Controller controller = new Controller(
                id,
                title,
                price,
                stockQuantity,
                compatibleConsoleIds,
                connectionType
        );

        accessoryService.registerController(controller);

        System.out.println("Controller registrado correctamente.");
    }

    /**
     * Registers a cable using AccessoryService.
     */
    private void registerCable() {

        System.out.println("\n===== REGISTRAR CABLE =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("Precio: ");
        double price = scanner.nextDouble();

        System.out.print("Cantidad en stock: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("IDs de consolas compatibles separados por coma: ");
        String compatibleInput = scanner.nextLine();

        List<String> compatibleConsoleIds = new ArrayList<>();

        for (String consoleId : compatibleInput.split(",")) {
            compatibleConsoleIds.add(consoleId.trim());
        }

        System.out.print("Longitud del cable en metros: ");
        double lengthInMeters = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Tipo de conector (HDMI/USB/OPTICAL): ");
        String connectorType = scanner.nextLine();

        Cable cable = new Cable(
                id,
                title,
                price,
                stockQuantity,
                compatibleConsoleIds,
                lengthInMeters,
                connectorType
        );

        accessoryService.registerCable(cable);

        System.out.println("Cable registrado correctamente.");
    }

    /**
     * Registers a memory accessory using AccessoryService.
     */
    private void registerMemory() {

        System.out.println("\n===== REGISTRAR MEMORIA =====");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("Precio: ");
        double price = scanner.nextDouble();

        System.out.print("Cantidad en stock: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("IDs de consolas compatibles separados por coma: ");
        String compatibleInput = scanner.nextLine();

        List<String> compatibleConsoleIds = new ArrayList<>();

        for (String consoleId : compatibleInput.split(",")) {
            compatibleConsoleIds.add(consoleId.trim());
        }

        System.out.print("Capacidad en GB: ");
        int capacityInGigabytes = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Tipo de memoria (SD/MICRO_SD/INTERNAL): ");
        String memoryType = scanner.nextLine();

        Memory memory = new Memory(
                id,
                title,
                price,
                stockQuantity,
                compatibleConsoleIds,
                capacityInGigabytes,
                memoryType
        );

        accessoryService.registerMemory(memory);

        System.out.println("Memoria registrada correctamente.");
    }

    /**
     * Displays all registered accessories using AccessoryService.
     */
    private void listAllAccessories() {

        System.out.println("\n===== LISTA DE ACCESORIOS =====");

        if (accessoryService.listAllAccessories().isEmpty()) {
            System.out.println("No hay accesorios registrados.");
            return;
        }

        for (Accessory accessory : accessoryService.listAllAccessories()) {
            System.out.println(
                    "ID: " + accessory.getId()
                            + " | " + accessory.getDescription()
                            + " | Precio: " + accessory.getPrice()
                            + " | Stock: " + accessory.getStockQuantity()
            );
        }
    }

    /**
     * Displays accessories filtered by type.
     */
    private void listAccessoriesByType() {

        System.out.println("\n===== ACCESORIOS POR TIPO =====");
        System.out.println("Tipos disponibles: CONTROLLER, CABLE, MEMORY");
        System.out.print("Ingrese el tipo: ");

        String type = scanner.nextLine().trim();

        List<Accessory> accessories =
                accessoryService.listAccessoriesByType(type);

        if (accessories.isEmpty()) {
            System.out.println("No se encontraron accesorios de ese tipo.");
            return;
        }

        for (Accessory accessory : accessories) {
            System.out.println(
                    "ID: " + accessory.getId()
                            + " | " + accessory.getDescription()
                            + " | Precio: " + accessory.getPrice()
                            + " | Stock: " + accessory.getStockQuantity()
            );
        }
    }

    /**
     * Displays accessories compatible with a selected console.
     */
    private void findAccessoriesCompatibleWithConsole() {

        System.out.println("\n===== ACCESORIOS COMPATIBLES =====");

        System.out.print("Ingrese el ID de la consola: ");
        String consoleId = scanner.nextLine().trim();

        List<Accessory> accessories =
                accessoryService.findAccessoriesCompatibleWith(consoleId);

        if (accessories.isEmpty()) {
            System.out.println(
                    "No se encontraron accesorios compatibles con la consola."
            );
            return;
        }

        System.out.println(
                "Accesorios compatibles con la consola " + consoleId + ":"
        );

        for (Accessory accessory : accessories) {
            System.out.println(
                    "ID: " + accessory.getId()
                            + " | " + accessory.getDescription()
                            + " | Precio: " + accessory.getPrice()
                            + " | Stock: " + accessory.getStockQuantity()
            );
        }
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
     * Registers a sale and optionally assigns extended warranties
     * to selected consoles.
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

        if (productService.getAllProducts().isEmpty()
                && accessoryService.listAllAccessories().isEmpty()) {
            System.out.println("No hay productos ni accesorios registrados.");
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

        List<Product> products = new ArrayList<>();

        /*
         * Stores the IDs of consoles for which the customer
         * requested an extended warranty.
         */
        List<String> productIdsWithExtendedWarranty =
                new ArrayList<>();

        int option;

        do {
            System.out.println("\n===== AGREGAR PRODUCTO A LA VENTA =====");
            System.out.println("1. Agregar producto");
            System.out.println("2. Agregar accesorio");
            System.out.println("0. Finalizar venta");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {

                case 1:

                    if (productService.getAllProducts().isEmpty()) {
                        System.out.println("No hay productos registrados.");
                        break;
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
                        break;
                    }

                    if (product.getStockQuantity() < 1) {
                        System.out.println(
                                "No hay stock disponible para este producto."
                        );
                        break;
                    }

                    products.add(product);

                    /*
                     * Only consoles can receive an extended warranty.
                     * Every console automatically receives a basic warranty
                     * when the sale is registered.
                     */
                    if (product instanceof Console) {

                        System.out.print(
                                "¿Desea agregar garantía extendida "
                                        + "a esta consola? (s/n): "
                        );

                        String extendedWarrantyOption =
                                scanner.nextLine()
                                        .trim()
                                        .toLowerCase();

                        if (extendedWarrantyOption.equals("s")) {

                            productIdsWithExtendedWarranty.add(
                                    product.getId()
                            );

                            System.out.println(
                                    "Garantía extendida seleccionada."
                            );

                        } else {

                            System.out.println(
                                    "Solo se asignará la garantía básica."
                            );
                        }
                    }

                    System.out.println(
                            "Producto agregado a la venta correctamente."
                    );
                    break;

                case 2:

                    if (accessoryService.listAllAccessories().isEmpty()) {
                        System.out.println("No hay accesorios registrados.");
                        break;
                    }

                    System.out.println("\n===== ACCESORIOS =====");

                    accessoryService.listAllAccessories().forEach(accessory ->
                            System.out.println(
                                    "ID: " + accessory.getId()
                                            + " | "
                                            + accessory.getDescription()
                                            + " | Precio: "
                                            + accessory.getPrice()
                                            + " | Stock: "
                                            + accessory.getStockQuantity()
                            )
                    );

                    System.out.print("ID del accesorio: ");
                    String accessoryId = scanner.nextLine();

                    Accessory accessory =
                            accessoryService.findById(accessoryId);

                    if (accessory == null) {
                        System.out.println("Accesorio no encontrado.");
                        break;
                    }

                    if (accessory.getStockQuantity() < 1) {
                        System.out.println(
                                "No hay stock disponible para este accesorio."
                        );
                        break;
                    }

                    products.add(accessory);

                    System.out.println(
                            "Accesorio agregado a la venta correctamente."
                    );
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (option != 0);

        if (products.isEmpty()) {
            System.out.println(
                    "La venta debe contener al menos un producto o accesorio."
            );
            return;
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

        saleService.findAll().forEach(sale -> {

            double subtotal = sale.calculateTotal();
            double discount = sale.getDiscountAmount();
            double warrantyCost = sale.getWarrantyAdditionalCost();
            double finalTotal = subtotal - discount + warrantyCost;

            System.out.println(
                    "\nID Venta: " + sale.getSaleId()
                            + " | Fecha: " + sale.getDate()
                            + " | Cliente: " + sale.getCustomer().getName()
                            + " | Vendedor: " + sale.getSeller().getName()
            );

            System.out.println("Subtotal: $" + subtotal);

            if (sale.getAppliedPromotionName() != null
                    && !sale.getAppliedPromotionName().isBlank()) {

                System.out.println(
                        "Promoción aplicada: "
                                + sale.getAppliedPromotionName()
                );

                System.out.println("Descuento: $" + discount);

            } else {

                System.out.println("Promoción aplicada: Ninguna");
                System.out.println("Descuento: $0.0");
            }

            System.out.println(
                    "Garantía extendida: $" + warrantyCost
            );

            System.out.println("Total final: $" + finalTotal);
        });
    }

    /**
     * Displays the return management submenu.
     */
    private void showReturnMenu() {

        int option;

        do {
            System.out.println("\n===== GESTIÓN DE DEVOLUCIONES =====");
            System.out.println("1. Registrar devolución");
            System.out.println("2. Listar todas las devoluciones");
            System.out.println("3. Buscar devoluciones por cliente");
            System.out.println("4. Buscar devoluciones por venta");
            System.out.println("5. Balance mensual");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {

                case 1:
                    registerReturn();
                    break;

                case 2:
                    listReturns();
                    break;

                case 3:
                    listReturnsByCustomer();
                    break;

                case 4:
                    listReturnsBySale();
                    break;

                case 5:
                    showMonthlyBalance();
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
     * Registers a product return.
     */
    private void registerReturn() {

        System.out.println("\n===== REGISTRAR DEVOLUCIÓN =====");

        System.out.print("ID de la venta: ");
        String saleId = scanner.nextLine();

        Sale sale = null;

        for (Sale s : saleService.findAll()) {
            if (s.getSaleId().equals(saleId)) {
                sale = s;
                break;
            }
        }

        if (sale == null) {
            System.out.println("Venta no encontrada.");
            return;
        }

        System.out.println("\n===== PRODUCTOS DE LA VENTA =====");

        for (Product product : sale.getProducts()) {
            System.out.println(
                    "ID: " + product.getId()
                            + " | " + product.getDescription()
                            + " | Precio: " + product.getPrice()
            );
        }

        System.out.print(
                "Ingrese los IDs de los productos a devolver separados por coma: "
        );

        String input = scanner.nextLine();

        List<String> productIds = new ArrayList<>();

        for (String productId : input.split(",")) {
            productIds.add(productId.trim());
        }

        System.out.print("Motivo de la devolución: ");
        String reason = scanner.nextLine();

        try {

            Return newReturn =
                    returnService.registerReturn(
                            saleId,
                            productIds,
                            reason
                    );

            System.out.println("\nDevolución registrada correctamente.");
            System.out.println("ID de devolución: " + newReturn.getReturnId());
            System.out.println("Fecha: " + newReturn.getReturnDate());
            System.out.println(
                    "Valor reembolsado: " + newReturn.getRefundAmount()
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "No se pudo registrar la devolución: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Displays all registered returns.
     */
    private void listReturns() {

        System.out.println("\n===== LISTA DE DEVOLUCIONES =====");

        if (returnService.viewAllReturns().isEmpty()) {
            System.out.println("No hay devoluciones registradas.");
            return;
        }

        returnService.viewAllReturns().forEach(returnItem ->
                System.out.println(
                        "ID: " + returnItem.getReturnId()
                                + " | Fecha: " + returnItem.getReturnDate()
                                + " | Venta: "
                                + returnItem.getOriginalSale().getSaleId()
                                + " | Motivo: " + returnItem.getReason()
                                + " | Reembolso: "
                                + returnItem.getRefundAmount()
                )
        );
    }

    /**
     * Displays returns registered for a specific customer.
     */
    private void listReturnsByCustomer() {

        System.out.println("\n===== DEVOLUCIONES POR CLIENTE =====");

        System.out.print("ID del cliente: ");
        String customerId = scanner.nextLine();

        List<com.gamezone.model.Return> results =
                returnService.viewReturnsByCustomer(customerId);

        if (results.isEmpty()) {
            System.out.println(
                    "No hay devoluciones registradas para ese cliente."
            );
            return;
        }

        for (com.gamezone.model.Return returnItem : results) {

            System.out.println(
                    "ID: " + returnItem.getReturnId()
                            + " | Fecha: " + returnItem.getReturnDate()
                            + " | Venta: "
                            + returnItem.getOriginalSale().getSaleId()
                            + " | Motivo: " + returnItem.getReason()
                            + " | Reembolso: "
                            + returnItem.getRefundAmount()
            );
        }
    }

    /**
     * Displays returns registered for a specific sale.
     */
    private void listReturnsBySale() {

        System.out.println("\n===== DEVOLUCIONES POR VENTA =====");

        System.out.print("ID de la venta: ");
        String saleId = scanner.nextLine();

        List<com.gamezone.model.Return> results =
                returnService.viewReturnsBySale(saleId);

        if (results.isEmpty()) {
            System.out.println(
                    "No hay devoluciones registradas para esa venta."
            );
            return;
        }

        for (com.gamezone.model.Return returnItem : results) {

            System.out.println(
                    "ID: " + returnItem.getReturnId()
                            + " | Fecha: " + returnItem.getReturnDate()
                            + " | Venta: "
                            + returnItem.getOriginalSale().getSaleId()
                            + " | Motivo: " + returnItem.getReason()
                            + " | Reembolso: "
                            + returnItem.getRefundAmount()
            );
        }
    }

    /**
     * Displays the monthly balance of sales and returns.
     */
    private void showMonthlyBalance() {

        System.out.println("\n===== BALANCE MENSUAL =====");

        System.out.print("Ingrese el mes (1-12): ");
        int month = scanner.nextInt();

        System.out.print("Ingrese el año: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        if (month < 1 || month > 12) {
            System.out.println("El mes debe estar entre 1 y 12.");
            return;
        }

        double balance =
                returnService.generateMonthlyBalance(month, year);

        System.out.println(
                "Balance del mes " + month + "/" + year + ": $" + balance
        );
    }

}