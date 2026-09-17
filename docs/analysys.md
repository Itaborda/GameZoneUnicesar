# System Analysis and Design - GameZone Unicesar

## 1. Questions About System Persons

### Question 1
*What attributes are common to all persons interacting with the store, and which ones are specific to each specific type of person? How is this distinction reflected in a class hierarchy?*

**Analysis & Answer:**
* **Common Attributes (Base Class):** All persons share `id` (identification), `name`, and `phone`.
* **Specific Attributes (Subclasses):**
    * `Customer` contains: `email` and `purchaseHistory`.
    * `Seller` (or `Employee`) contains: `employeeCode` and `workShift`.
* **Hierarchy Reflection:** This is modeled using **Inheritance (Generalization)**. We create a base class called `Person`, and two subclasses (`Customer` and `Seller`) that extend `Person` to inherit its common attributes.

### Question 2
*Should there be a class representing a "generic person" without specifying their role? Why or why not? What implication does this decision have on the possibility of instantiating such a class?*

**Analysis & Answer:**
* **Decision:** No, a generic person should not exist independently in the system. Every person must have a defined role (either a Customer or a Seller).
* **Implication:** The `Person` class must be declared as **`abstract`**. This means it **cannot be instantiated** directly using the `new` keyword. It only serves as a blueprint for its concrete subclasses.

## 2. Questions About System Products

### Question 3
*What characteristics do all products sold by the store have in common, regardless of their type? What characteristics are specific to each type of product?*

**Analysis & Answer:**
* **Common Attributes:** Every product in the store shares `id` (identifier), `title`, `price`, and `stockQuantity` (available inventory).
* **Specific Attributes:**
    * `VideoGame` contains: `platform`, `genre`, and `ageClassification`.
    * `Console` contains: `brand`, `model`, and `generation`.

### Question 4
*Each type of product must be able to present a full description that integrates its particular characteristics. How should this behavior be declared in the base class to guarantee that all subclasses implement it in their own way? What mechanism of object-oriented programming allows this?*

**Analysis & Answer:**
* **Declaration:** The behavior should be declared in the base class as an **abstract method** (e.g., `public abstract String getDescription();`). It has no body/implementation in the parent class.
* **OOP Mechanism:** The mechanism is **Polymorphism and Method Overriding (`@Override`)**. By declaring the method as abstract in the parent class, Java strictly forces `VideoGame` and `Console` to implement their own custom version of `getDescription()`, combining their unique attributes.

## 3. Questions About Sales and Relationships Between Entities

### Question 5
*A sale involves a customer, a seller, and one or more products. What type of relationships exist between the class that represents the sale and the other classes in the system? Are these relationships inheritance, association, composition, or another type? Justify.*

**Analysis & Answer:**
* **Sale to Customer and Sale to Seller:** These are **Associations (1 to 1)**. A `Sale` object holds a reference to a single `Customer` and a single `Seller` who performed the transaction. It is not inheritance because a sale is not a person.
* **Sale to Products:** This is an **Aggregation (1 to Many)** or **Association with Multiplicity (1..*)**. A `Sale` contains a collection (like a list) of `Product` objects. It is an aggregation because if a `Sale` object is deleted from the history, the actual `Product` objects do not disappear from the store's global inventory.

### Question 6
*Should the sale be responsible for calculating its own total, or should this responsibility fall on another class? Argue your decision.*

**Analysis & Answer:**
* **Decision:** The `Sale` class itself should be responsible for calculating its own total.
* **Justification:** According to the **Information Expert** principle in object-oriented design, a responsibility should be assigned to the class that has the information necessary to fulfill it. Since `Sale` holds the list of products purchased and their prices, it is the most cohesive place to implement a method (e.g., `calculateTotal()`) that loops through the products and sums up their prices.

## 4. Questions About Business Restrictions

### Question 7
*How is it guaranteed in the design that a sale cannot be registered without at least one product? At what point in the system should this rule be validated?*

**Analysis & Answer:**
* **Design Guarantee:** In the `SaleService` class, the method responsible for registering a sale must include a conditional statement (e.g., `if (productsList.isEmpty())`) to throw a business exception or prevent execution if the list is empty.
* **Validation Point:** This rule must be validated in the **Service Layer (`service` package)**. The Service Layer is responsible for enforcing business rules before sending data to the Persistence Layer. Additionally, basic input validation can be handled in the UI Layer to notify the user immediately.

### Question 8
*How is the automatic inventory update reflected in the design when a sale is registered? What classes are involved in this operation?*

**Analysis & Answer:**
* **Design Reflection:** When a transaction is processed, the system must loop through the selected products and reduce their available quantity using a setter method (e.g., `setStockQuantity()`).
* **Involved Classes:**
    * `SaleService`: Coordinates the whole process, verifies if there is enough stock available, and calls the stock reduction logic.
    * `ProductService`: Updates the product details and memory list.
    * `ProductRepository` (Persistence): Saves the updated product stock into the text/CSV files so the changes are not lost when closing the app.

## 5. Questions About Layered Organization

### Question 9
*The system must be organized into four layers: model, persistence, service, and user interface. What type of classes belong to each layer? What criterion allows deciding in which layer a class should be located?*

**Analysis & Answer:**
* **Classes per Layer:**
    * `model`: Contains pure domain entities (e.g., `Person`, `Product`, `Sale`) with attributes, constructors, getters, and setters.
    * `persistence`: Contains data access classes (Repositories) responsible for saving and loading data from text/CSV files.
    * `service`: Contains the business logic, input validation, and use cases (e.g., checking stock or calculating totals).
    * `ui`: Contains the user interface classes (console menus, scanners) to interact with the end user.
* **Criterion:** The Separation of Concerns (SoC) principle. A class is placed in a layer based on its primary responsibility: data structure (`model`), storage (`persistence`), business rules (`service`), or presentation (`ui`).

### Question 10
*Why should the logic for saving and retrieving data from files not be inside the domain classes? What problems are generated when these responsibilities are mixed?*

**Analysis & Answer:**
* **Reason:** Domain classes should remain pure and decoupled from data storage mechanisms.
* **Problems Generated:** Mixing responsibilities breaks the Single Responsibility Principle (SRP) and High Cohesion. It results in hard-to-maintain code. For example, if you change from text files to a database, you would have to modify the core `Product` or `Person` code, increasing the risk of introducing critical bugs in business logic.

### Question 11
*What dependencies are allowed between layers and which ones are prohibited? Justify the direction of the allowed dependencies.*

**Analysis & Answer:**
* **Allowed Dependencies:** The flow is strictly unidirectional from top to bottom: `ui` depends on `service`; `service` depends on `persistence` and `model`; `persistence` depends only on `model`; `model` does not depend on any other layer.
* **Prohibited Dependencies:** Cross-layer jumps (e.g., `ui` accessing `persistence` directly) and circular dependencies (e.g., `model` referencing `ui` or `service`) are strictly forbidden.
* **Justification:** This structure ensures loose coupling and maintainability. Upper layers can use the features of lower layers, but lower layers remain completely unaware of how their data is displayed or where it comes from.
  GAMEZONE UNICESAR PLAN DE IMPLEMENTACIÓN COMPLETO – REQUERIMIENTO 3 /
  TALLER DE DEVOLUCIONES Programación III – Universidad Popular del Cesar

Equipo: - Isabela – Líder de desarrollo / Líder Técnico - Juan Sebastián
– Dev 1 / Desarrollador de dominio - Ailyn – Dev 2 / Persistencia y
servicios

REPOSITORIO: https://github.com/Itaborda/GameZoneUnicesar.git

OBJETIVO DEL DOCUMENTO

Este documento es una guía ejecutable para llevar el repositorio actual
de GameZoneUnicesar hasta los requerimientos del nuevo taller. Está
basada en la estructura real encontrada en el ZIP del proyecto, no en
una estructura suposicional.

ESTADO REAL DEL ZIP

El proyecto contiene:

src/main/java/com/gamezone/ Main.java model/ Console.java Customer.java
Person.java Product.java Sale.java Seller.java VideoGame.java
persistence/ PersonRepository.java ProductRepository.java
SaleRepository.java service/ PersonService.java ProductService.java
SaleService.java ui/ MainMenu.java

docs/ return-analysis.md analysys.md Class-diagram.md
hierarchy-diagram.md layers-diagram.md ai-usage/

También existe pom.xml.

IMPORTANTE: el taller menciona explícitamente ConsoleMenu, pero el ZIP
actual tiene MainMenu.java. Para cumplir literalmente con el taller, la
integración final debe quedar en com.gamezone.ui.ConsoleMenu y Main.java
debe instanciar ConsoleMenu. No conviene inventar una segunda interfaz
paralela y dejar MainMenu activo como fuente de verdad.

Otra diferencia importante: Sale.date actualmente es String. El taller
pide que Return use LocalDate y que la regla de 30 días se calcule con
fechas. No es obligatorio rehacer toda Sale para cambiar el atributo si
el profesor espera una modificación aditiva. La solución recomendada es
conservar String en Sale y hacer que canBeReturned() convierta la fecha
almacenada a LocalDate. El formato de fecha debe ser ISO: yyyy-MM-dd.

La configuración actual de Maven declara Java 25. El entorno usado para
auditar el ZIP tenía Java 21 y no tenía Maven instalado, por lo que no
se puede afirmar desde esta auditoría que el build ya pase. Antes de
desarrollar, cada integrante debe ejecutar el build en su máquina con la
versión de Java que exige el proyecto. Si el docente entregó una versión
específica de Java, usar exactamente esa versión.

====================================================================== 1.
REQUERIMIENTOS QUE DEBEN QUEDAR FUNCIONANDO
======================================================================

El sistema debe permitir:

1.  Registrar una devolución.

  -   Recibe saleId.
  -   Recibe uno o varios productIds.
  -   Recibe reason.
  -   La fecha de devolución es LocalDate.now().
  -   La venta debe existir.
  -   La venta debe estar dentro de los 30 días.
  -   Cada producto indicado debe aparecer en la venta original.
  -   Si hay productos repetidos en una venta, el número de unidades
      devueltas no puede superar el número de unidades compradas.
  -   El reembolso se calcula automáticamente.
  -   El stock se incrementa automáticamente mediante
      ProductService.restoreStock().
  -   La devolución se guarda en data/returns.csv.

2.  Consultar todas las devoluciones.

3.  Consultar devoluciones por cliente.

4.  Consultar devoluciones por venta.

5.  Generar balance mensual.

  -   mes
  -   año
  -   total ventas del mes
  -   total devoluciones del mes
  -   balance = ventas - devoluciones

6.  Documentar las cinco preguntas en inglés: docs/return-analysis.md

7.  Documentar el diagrama Mermaid: docs/return-class-diagram.md

8.  Mantener arquitectura por capas: model persistence service ui

9.  Mantener Git Flow solicitado: develop -> feature/return-module ->
    PRs -> develop

10. Mínimo seis commits atómicos por persona.

11. Mínimo tres Pull Requests aprobados y fusionados.

12. Ningún commit directo a main o develop.

13. Ningún push –force.

14. Mensajes de commit en inglés y Conventional Commits.

======================================================================
2. DISTRIBUCIÓN EXACTA DEL TRABAJO
   ======================================================================

ISABELA – LÍDER DE DESARROLLO

Responsabilidades: - Crear/coordininar feature/return-module desde
develop. - Implementar integración del módulo. - Extender ProductService
con restoreStock(). - Integrar ConsoleMenu. - Integrar balance mensual
en el menú. - Coordinar las dependencias. - Revisar Pull Requests. -
Actualizar README.md. - Ejecutar pruebas de integración. - Hacer merge
final y borrar rama remota al finalizar.

Archivos principales:
src/main/java/com/gamezone/service/ProductService.java
src/main/java/com/gamezone/ui/ConsoleMenu.java
src/main/java/com/gamezone/Main.java README.md

No debe modificar arbitrariamente la lógica de Return de Sebastián ni la
persistencia de Ailyn. Si necesita corregir algo, primero coordinarlo y
hacer un commit separado.

SEBASTIÁN – DEV 1

Responsabilidades: - Clase com.gamezone.model.Return. - Método
Sale.canBeReturned(). - Validaciones internas de Return. - Cálculo del
reembolso. - Generación del recibo.

Archivos: src/main/java/com/gamezone/model/Return.java
src/main/java/com/gamezone/model/Sale.java

No debe implementar: - ReturnRepository - ReturnService - menú -
restoreStock

AILYN – DEV 2

Responsabilidades: - ReturnRepository. - ReturnService. - Registro de
devolución. - Validación de plazo. - Validación de pertenencia. -
Actualización de stock mediante ProductService.restoreStock(). -
Consultas. - Balance mensual.

Archivos: src/main/java/com/gamezone/persistence/ReturnRepository.java
src/main/java/com/gamezone/service/ReturnService.java

DEPENDENCIAS: ReturnRepository necesita SaleService y ProductService
para reconstruir referencias. ReturnService necesita ReturnRepository,
SaleService y ProductService.

======================================================================
3. REGLA CRÍTICA SOBRE LA RAMA
   ======================================================================

El taller exige una sola rama:

feature/return-module

Esto significa que NO se deben crear ramas personales como:
feature/sebastian-return feature/ailyn-return feature/isabela-menu

Todos deben trabajar sobre feature/return-module.

Esto genera un problema práctico: varias personas trabajando
simultáneamente sobre la misma rama pueden sobrescribirse cambios. Por
eso el orden de trabajo debe ser coordinado.

Orden recomendado:

FASE 1 Isabela crea feature/return-module.

FASE 2 Sebastián implementa model.

FASE 3 Ailyn implementa persistence/service cuando el modelo ya esté
disponible.

FASE 4 Isabela integra ProductService y ConsoleMenu.

FASE 5 Documentación, pruebas y PRs.

Para minimizar conflictos, cada integrante debe hacer pull antes de
comenzar cada bloque de trabajo y antes de continuar después de que otro
integrante haya subido cambios.

======================================================================
4. PASO 0 – PREPARAR LAS TRES COMPUTADORAS
   ======================================================================

Cada integrante debe tener:

-   Git
-   JDK compatible con pom.xml
-   IntelliJ IDEA, NetBeans o VS Code
-   Maven o Maven Wrapper si el proyecto lo incorpora
-   conexión a Internet
-   acceso al repositorio GitHub

PRIMER COMANDO:

git –version

SEGUNDO:

java -version

TERCERO:

git config –global user.name “NOMBRE REAL” git config –global user.email
“CORREO_DE_GITHUB”

NO usar correos o nombres de otro integrante.

======================================================================
5. PASO 1 – CLONAR Y REVISAR EL REPOSITORIO
   ======================================================================

En una carpeta de trabajo:

git clone https://github.com/Itaborda/GameZoneUnicesar.git

Entrar:

cd GameZoneUnicesar

Revisar:

git status

Ver ramas:

git branch -a

Ver historial:

git log –oneline –decorate –graph –all -20

MUY IMPORTANTE: El ZIP entregado tiene HEAD local en main, mientras
origin/develop apunta a un commit anterior. Por lo tanto, no se debe
asumir que main es el punto de partida del examen.

El punto de partida obligatorio del taller es:

origin/develop

Comprobar:

git fetch origin

git checkout develop

git pull origin develop

git status

Debe aparecer:

On branch develop Your branch is up to date with ‘origin/develop’.

Si develop no existe localmente:

git checkout -b develop origin/develop

Luego:

git pull origin develop

======================================================================
6. PASO 2 – CREAR LA RAMA OBLIGATORIA
   ======================================================================

SOLO ISABELA debe crearla inicialmente.

Desde develop:

git checkout develop git pull origin develop git checkout -b
feature/return-module

Verificar:

git branch

Debe aparecer:

-   feature/return-module develop

Publicar:

git push -u origin feature/return-module

Después de esto, Sebastián y Ailyn NO deben crear otra rama.

Ellos deben:

git fetch origin git checkout feature/return-module git pull origin
feature/return-module

======================================================================
7. PASO 3 – PRIMER COMMIT DE COORDINACIÓN
   ======================================================================

Antes de modificar código, Isabela puede agregar una pequeña
actualización de documentación si el equipo necesita registrar el inicio
del módulo.

Ejemplo:

git status git add README.md git commit -m “docs: prepare return module”
git push origin feature/return-module

Este commit debe ser pequeño y atómico.

IMPORTANTE: El requisito de seis commits por integrante NO significa
hacer seis commits artificiales de una línea. Deben ser seis cambios
funcionales/documentales reales y atómicos.

======================================================================
8. DISEÑO DEL MODELO Return
   ======================================================================

Archivo:

src/main/java/com/gamezone/model/Return.java

Paquete:

package com.gamezone.model;

Atributos exactos exigidos:

private String returnId; private LocalDate returnDate; private Sale
originalSale; private List returnedProducts; private String reason;
private double refundAmount;

IMPORTS:

import java.time.LocalDate; import java.util.List;

La relación Return -> Sale es ASOCIACIÓN.

No es herencia: Return no es un tipo de Sale.

No es composición: La existencia de Return no depende de que se
cree/destruya una Sale como parte de ella.

No es agregación: La relación no representa un objeto “todo” formado por
objetos “parte”.

Es asociación porque Return necesita una referencia a Sale.

======================================================================
9. CÓDIGO COMPLETO DE Return.java
   ======================================================================

Sebastián debe crear:

src/main/java/com/gamezone/model/Return.java

Código recomendado:

package com.gamezone.model;

import java.time.LocalDate; import java.util.ArrayList; import
java.util.List;

public class Return {

    private String returnId;
    private LocalDate returnDate;
    private final Sale originalSale;
    private final List<Product> returnedProducts;
    private String reason;
    private double refundAmount;

    public Return(
            String returnId,
            LocalDate returnDate,
            Sale originalSale,
            List<Product> returnedProducts,
            String reason,
            double refundAmount) {

        if (returnId == null || returnId.isBlank()) {
            throw new IllegalArgumentException("El identificador de devolución es obligatorio.");
        }

        if (returnDate == null) {
            throw new IllegalArgumentException("La fecha de devolución es obligatoria.");
        }

        if (originalSale == null) {
            throw new IllegalArgumentException("La venta original es obligatoria.");
        }

        if (returnedProducts == null || returnedProducts.isEmpty()) {
            throw new IllegalArgumentException("Debe existir al menos un producto devuelto.");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("El motivo de devolución es obligatorio.");
        }

        this.returnId = returnId;
        this.returnDate = returnDate;
        this.originalSale = originalSale;
        this.returnedProducts = new ArrayList<>(returnedProducts);
        this.reason = reason;
        this.refundAmount = refundAmount;
    }

    public String getReturnId() {
        return returnId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Sale getOriginalSale() {
        return originalSale;
    }

    public List<Product> getReturnedProducts() {
        return new ArrayList<>(returnedProducts);
    }

    public String getReason() {
        return reason;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public double calculateRefundAmount() {
        double total = 0;

        for (Product product : returnedProducts) {
            total += product.getPrice();
        }

        refundAmount = total;
        return refundAmount;
    }

    public String generateReturnReceipt() {
        StringBuilder receipt = new StringBuilder();

        receipt.append("\n===== COMPROBANTE DE DEVOLUCIÓN =====\n");
        receipt.append("Identificador: ").append(returnId).append("\n");
        receipt.append("Fecha: ").append(returnDate).append("\n");
        receipt.append("Venta original: ")
                .append(originalSale.getSaleId())
                .append("\n");

        receipt.append("Productos devueltos:\n");

        for (Product product : returnedProducts) {
            receipt.append("- ")
                    .append(product.getTitle())
                    .append(" | Precio: $")
                    .append(product.getPrice())
                    .append("\n");
        }

        receipt.append("Motivo: ").append(reason).append("\n");
        receipt.append("Monto reembolsado: $")
                .append(refundAmount)
                .append("\n");

        receipt.append("====================================\n");

        return receipt.toString();
    }

}

NOTA: El constructor recibe refundAmount porque el taller exige
constructor completo. Sin embargo, ReturnService debe llamar
calculateRefundAmount() después de crear la devolución para que el
sistema sea la fuente del valor.

======================================================================
10. MODIFICACIÓN DE Sale.canBeReturned()
    ======================================================================

Archivo:

src/main/java/com/gamezone/model/Sale.java

Agregar imports:

import java.time.LocalDate; import
java.time.format.DateTimeParseException; import
java.time.temporal.ChronoUnit;

Agregar este método dentro de Sale:

public boolean canBeReturned() { if (date == null || date.isBlank()) {
return false; }

    try {
        LocalDate saleDate = LocalDate.parse(date);
        LocalDate today = LocalDate.now();

        long days = ChronoUnit.DAYS.between(saleDate, today);

        return days >= 0 && days <= 30;

    } catch (DateTimeParseException e) {
        return false;
    }

}

POR QUÉ: ChronoUnit.DAYS.between(fechaVenta, fechaActual) calcula la
diferencia en días calendario.

Ejemplos: Venta 2026-09-01, devolución 2026-09-01 -> 0 días ->
permitida. Venta 2026-08-20, devolución 2026-09-09 -> 20 días ->
permitida. Venta 2026-08-10, devolución 2026-09-09 -> 30 días ->
permitida. Venta 2026-08-09, devolución 2026-09-09 -> 31 días ->
rechazada.

El futuro también se rechaza porque days sería negativo.

======================================================================
11. COMMIT DE SEBASTIÁN – MODELO
    ======================================================================

Después de Return.java:

git status

git add src/main/java/com/gamezone/model/Return.java

git commit -m “feat: add return domain model”

git push origin feature/return-module

Luego Sale:

git add src/main/java/com/gamezone/model/Sale.java git commit -m “feat:
add return eligibility to sale” git push origin feature/return-module

No mezclar ambos cambios en un commit si el equipo quiere demostrar
mejor la atomicidad.

======================================================================
12. RETURN REPOSITORY
    ======================================================================

Archivo:

src/main/java/com/gamezone/persistence/ReturnRepository.java

Responsabilidad: - guardar devoluciones - cargar devoluciones -
reconstruir referencias a Sale - reconstruir referencias a Product

Dependencias:

private final String filePath; private final SaleService saleService;
private final ProductService productService;

Constructor:

public ReturnRepository( String filePath, SaleService saleService,
ProductService productService)

La ruta obligatoria es:

data/returns.csv

Antes de guardar se debe asegurar que exista data/.

======================================================================
13. FORMATO DE returns.csv
    ======================================================================

Para evitar agregar una estructura de base de datos que el taller no
solicita, se usará archivo CSV.

Formato conceptual:

returnId,returnDate,saleId,productIds,reason,refundAmount

Ejemplo:

RET-001,2026-09-09,S001,P001;P002,Producto incompatible,150000.0

Los productIds se separan con punto y coma.

IMPORTANTE: El sistema existente utiliza CSV muy simple y no tiene un
parser CSV avanzado. Para este taller, el motivo debe evitar comas si se
conserva el parser simple. En el menú se puede reemplazar coma por punto
y coma antes de guardar, o implementar una función pequeña de escape. La
opción más simple y estable para el examen es normalizar:

reason = reason.replace(“,”, “;”);

No cambiar innecesariamente todos los repositorios existentes.

======================================================================
14. CÓDIGO COMPLETO RECOMENDADO DE ReturnRepository.java
    ======================================================================

package com.gamezone.persistence;

import com.gamezone.model.Product; import com.gamezone.model.Return;
import com.gamezone.model.Sale; import
com.gamezone.service.ProductService; import
com.gamezone.service.SaleService;

import java.io.*; import java.time.LocalDate; import
java.util.ArrayList; import java.util.List;

public class ReturnRepository {

    private final String filePath;
    private final SaleService saleService;
    private final ProductService productService;

    public ReturnRepository(
            String filePath,
            SaleService saleService,
            ProductService productService) {

        this.filePath = filePath;
        this.saleService = saleService;
        this.productService = productService;
    }

    public void saveAll(List<Return> returns) {
        File file = new File(filePath);

        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath))) {

            for (Return returnItem : returns) {

                String productIds = returnItem.getReturnedProducts()
                        .stream()
                        .map(Product::getId)
                        .reduce((a, b) -> a + ";" + b)
                        .orElse("");

                String reason = returnItem.getReason()
                        .replace(",", ";");

                String line =
                        returnItem.getReturnId() + "," +
                        returnItem.getReturnDate() + "," +
                        returnItem.getOriginalSale().getSaleId() + "," +
                        productIds + "," +
                        reason + "," +
                        returnItem.getRefundAmount();

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudieron guardar las devoluciones.", e);
        }
    }

    public List<Return> loadAll() {
        List<Return> returns = new ArrayList<>();

        File file = new File(filePath);

        if (!file.exists()) {
            return returns;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split(",", -1);

                if (data.length < 6) {
                    continue;
                }

                String returnId = data[0];
                LocalDate returnDate = LocalDate.parse(data[1]);
                String saleId = data[2];
                String productIdsData = data[3];
                String reason = data[4];
                double refundAmount = Double.parseDouble(data[5]);

                Sale sale = findSaleById(saleId);

                if (sale == null) {
                    continue;
                }

                List<Product> products = new ArrayList<>();

                String[] productIds = productIdsData.split(";");

                for (String productId : productIds) {
                    Product product = productService.findById(productId);

                    if (product != null) {
                        products.add(product);
                    }
                }

                if (products.isEmpty()) {
                    continue;
                }

                Return returnItem = new Return(
                        returnId,
                        returnDate,
                        sale,
                        products,
                        reason,
                        refundAmount
                );

                returns.add(returnItem);
            }

        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException(
                    "No se pudieron cargar las devoluciones.", e);
        }

        return returns;
    }

    private Sale findSaleById(String saleId) {
        for (Sale sale : saleService.findAll()) {
            if (sale.getSaleId().equals(saleId)) {
                return sale;
            }
        }

        return null;
    }

}

IMPORTANTE: El taller dice saveAll(List) y loadAll(). La persistencia
debe ser completa: al guardar una nueva devolución no se deben perder
las anteriores.

======================================================================
15. PROBLEMA IMPORTANTE CON LAS VENTAS ACTUALES
    ======================================================================

SaleRepository actual guarda ventas en sales.csv, pero findAll() retorna
una lista en memoria y no carga sales.csv al iniciar.

Eso significa que returns.csv puede persistir, pero después de reiniciar
el programa las referencias a ventas pueden no estar disponibles.

Para el examen hay dos caminos:

CAMINO A – MÍNIMO Y SEGURO Mantener la implementación del taller
anterior y trabajar con las ventas disponibles durante la ejecución.

CAMINO B – MEJORA Modificar SaleRepository para cargar las ventas desde
archivo y resolver Customer, Seller y Product.

NO implementar el camino B durante el examen salvo que el profesor exija
persistencia entre reinicios de ventas. Es un cambio mayor y puede
romper la lógica existente.

El requerimiento actual pide explícitamente ReturnRepository con
dependencias para resolver Sale y Product, por lo que la solución A es
suficiente para la funcionalidad del flujo del examen.

======================================================================
16. ReturnService
    ======================================================================

Archivo:

src/main/java/com/gamezone/service/ReturnService.java

Dependencias:

private final ReturnRepository returnRepository; private final
SaleService saleService; private final ProductService productService;
private final List returns;

Constructor:

public ReturnService( ReturnRepository returnRepository, SaleService
saleService, ProductService productService)

Debe cargar las devoluciones:

this.returns = returnRepository.loadAll();

======================================================================
17. LÓGICA EXACTA DE registerReturn()
    ======================================================================

Orden obligatorio:

1.  Buscar venta.
2.  Si no existe -> error.
3.  Validar canBeReturned().
4.  Si no puede -> error de 30 días.
5.  Validar que productIds no esté vacío.
6.  Buscar productos reales.
7.  Verificar pertenencia a la venta.
8.  Verificar cantidades si hay IDs repetidos.
9.  Crear Return.
10. calculateRefundAmount().
11. restoreStock() por cada producto devuelto.
12. Agregar Return a lista.
13. saveAll().
14. Retornar Return.

NO actualizar stock antes de validar todo.

Esto es crítico.

Si se actualiza stock y después se descubre que un producto no pertenece
a la venta, el inventario queda incorrecto.

======================================================================
18. CÓDIGO COMPLETO RECOMENDADO DE ReturnService.java
    ======================================================================

package com.gamezone.service;

import com.gamezone.model.Product; import com.gamezone.model.Return;
import com.gamezone.model.Sale; import
com.gamezone.persistence.ReturnRepository;

import java.time.LocalDate; import java.util.ArrayList; import
java.util.List;

public class ReturnService {

    private final ReturnRepository returnRepository;
    private final SaleService saleService;
    private final ProductService productService;
    private final List<Return> returns;

    public ReturnService(
            ReturnRepository returnRepository,
            SaleService saleService,
            ProductService productService) {

        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.productService = productService;
        this.returns = returnRepository.loadAll();
    }

    public Return registerReturn(
            String saleId,
            List<String> productIds,
            String reason) {

        Sale sale = findSaleById(saleId);

        if (sale == null) {
            throw new IllegalArgumentException(
                    "La venta indicada no existe.");
        }

        if (!sale.canBeReturned()) {
            throw new IllegalArgumentException(
                    "La devolución no puede registrarse porque han pasado más de 30 días desde la venta.");
        }

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe seleccionar al menos un producto para devolver.");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException(
                    "Debe indicar el motivo de la devolución.");
        }

        List<Product> saleProducts = sale.getProducts();
        List<Product> productsToReturn = new ArrayList<>();

        for (String productId : productIds) {

            Product product = productService.findById(productId);

            if (product == null) {
                throw new IllegalArgumentException(
                        "El producto indicado no existe.");
            }

            boolean belongsToSale = false;

            for (Product saleProduct : saleProducts) {
                if (saleProduct.getId().equals(productId)) {
                    belongsToSale = true;
                    break;
                }
            }

            if (!belongsToSale) {
                throw new IllegalArgumentException(
                        "El producto " + productId +
                        " no pertenece a la venta indicada.");
            }

            productsToReturn.add(product);
        }

        validateReturnedQuantities(saleProducts, productIds);

        String returnId = generateReturnId();

        Return returnItem = new Return(
                returnId,
                LocalDate.now(),
                sale,
                productsToReturn,
                reason,
                0
        );

        double refundAmount = returnItem.calculateRefundAmount();

        for (Product product : productsToReturn) {
            productService.restoreStock(product.getId(), 1);
        }

        returns.add(returnItem);
        returnRepository.saveAll(returns);

        return returnItem;
    }

    public List<Return> viewAllReturns() {
        return new ArrayList<>(returns);
    }

    public List<Return> viewReturnsByCustomer(String customerId) {
        List<Return> result = new ArrayList<>();

        for (Return returnItem : returns) {

            Sale sale = returnItem.getOriginalSale();

            if (sale.getCustomer() != null
                    && sale.getCustomer().getId().equals(customerId)) {

                result.add(returnItem);
            }
        }

        return result;
    }

    public List<Return> viewReturnsBySale(String saleId) {
        List<Return> result = new ArrayList<>();

        for (Return returnItem : returns) {

            if (returnItem.getOriginalSale()
                    .getSaleId()
                    .equals(saleId)) {

                result.add(returnItem);
            }
        }

        return result;
    }

    public double generateMonthlyBalance(int month, int year) {

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(
                    "El mes debe estar entre 1 y 12.");
        }

        if (year < 1) {
            throw new IllegalArgumentException(
                    "El año no es válido.");
        }

        double totalSales = 0;

        for (Sale sale : saleService.findAll()) {

            LocalDate saleDate = parseDate(sale.getDate());

            if (saleDate.getMonthValue() == month
                    && saleDate.getYear() == year) {

                totalSales += sale.calculateTotal();
            }
        }

        double totalReturns = 0;

        for (Return returnItem : returns) {

            LocalDate returnDate = returnItem.getReturnDate();

            if (returnDate.getMonthValue() == month
                    && returnDate.getYear() == year) {

                totalReturns += returnItem.getRefundAmount();
            }
        }

        return totalSales - totalReturns;
    }

    public double calculateMonthlySales(int month, int year) {

        double total = 0;

        for (Sale sale : saleService.findAll()) {

            LocalDate date = parseDate(sale.getDate());

            if (date.getMonthValue() == month
                    && date.getYear() == year) {

                total += sale.calculateTotal();
            }
        }

        return total;
    }

    public double calculateMonthlyReturns(int month, int year) {

        double total = 0;

        for (Return returnItem : returns) {

            LocalDate date = returnItem.getReturnDate();

            if (date.getMonthValue() == month
                    && date.getYear() == year) {

                total += returnItem.getRefundAmount();
            }
        }

        return total;
    }

    private Sale findSaleById(String saleId) {

        for (Sale sale : saleService.findAll()) {

            if (sale.getSaleId().equals(saleId)) {
                return sale;
            }
        }

        return null;
    }

    private LocalDate parseDate(String date) {

        try {
            return LocalDate.parse(date);

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "La fecha de la venta debe tener el formato yyyy-MM-dd.");
        }
    }

    private void validateReturnedQuantities(
            List<Product> saleProducts,
            List<String> productIds) {

        for (String productId : productIds) {

            int requested = 0;

            for (String id : productIds) {
                if (id.equals(productId)) {
                    requested++;
                }
            }

            int purchased = 0;

            for (Product product : saleProducts) {
                if (product.getId().equals(productId)) {
                    purchased++;
                }
            }

            if (requested > purchased) {
                throw new IllegalArgumentException(
                        "La cantidad a devolver del producto " +
                        productId +
                        " supera la cantidad comprada.");
            }
        }
    }

    private String generateReturnId() {
        return "RET-" + String.format("%03d", returns.size() + 1);
    }

}

OBSERVACIÓN: La variable refundAmount se puede eliminar del método si no
se usa después del cálculo; el cálculo modifica el atributo de Return.
El código anterior puede dejarla como evidencia de que se calculó.

======================================================================
19. MUY IMPORTANTE: RETURN NO DEBE DUPLICAR LA LÓGICA DE STOCK
    ======================================================================

NO hacer:

product.setStockQuantity(product.getStockQuantity() + 1);

dentro de ReturnService.

El taller exige:

ProductService.restoreStock(String productId, int quantity)

Por lo tanto ReturnService debe llamar:

productService.restoreStock(product.getId(), 1);

Esto demuestra reutilización de lógica existente.

======================================================================
20. ProductService.restoreStock()
    ======================================================================

Responsable: Isabela.

Archivo:

src/main/java/com/gamezone/service/ProductService.java

Agregar:

public void restoreStock(String productId, int quantity) {

    Product product = findById(productId);

    if (product == null) {
        throw new IllegalArgumentException(
                "Product not found");
    }

    if (quantity <= 0) {
        throw new IllegalArgumentException(
                "Quantity must be greater than zero");
    }

    product.setStockQuantity(
            product.getStockQuantity() + quantity);

    productRepository.saveAll(products);

}

Puede traducirse el mensaje al español si se desea coherencia con el
nuevo módulo, pero no cambiar innecesariamente updateStock().

Versión recomendada en español:

public void restoreStock(String productId, int quantity) {

    Product product = findById(productId);

    if (product == null) {
        throw new IllegalArgumentException(
                "El producto no existe.");
    }

    if (quantity <= 0) {
        throw new IllegalArgumentException(
                "La cantidad a restaurar debe ser mayor que cero.");
    }

    product.setStockQuantity(
            product.getStockQuantity() + quantity);

    productRepository.saveAll(products);

}

======================================================================
21. CONSTRUCCIÓN DE LAS DEPENDENCIAS EN Main
    ======================================================================

El Main actual crea:

ProductRepository PersonRepository SaleRepository ProductService
PersonService SaleService MainMenu

Ahora debe crear también:

ReturnRepository ReturnService ConsoleMenu

Orden:

ProductRepository productRepository = new
ProductRepository(“products.dat”);

PersonRepository personRepository = new PersonRepository(“persons.dat”);

SaleRepository saleRepository = new SaleRepository();

ProductService productService = new ProductService(productRepository);

PersonService personService = new PersonService(personRepository, new
ArrayList<>());

SaleService saleService = new SaleService(saleRepository,
productService);

ReturnRepository returnRepository = new ReturnRepository(
“data/returns.csv”, saleService, productService);

ReturnService returnService = new ReturnService( returnRepository,
saleService, productService);

ConsoleMenu menu = new ConsoleMenu( productService, personService,
saleService, returnService);

menu.showMenu();

======================================================================
22. ConsoleMenu
    ======================================================================

El ZIP actual tiene MainMenu.java, pero el requerimiento dice:

com.gamezone.ui.ConsoleMenu

Por lo tanto la opción recomendada es:

1.  Crear ConsoleMenu.java.
2.  Copiar la estructura de MainMenu.
3.  Cambiar el nombre de la clase a ConsoleMenu.
4.  Inyectar servicios en el constructor.
5.  Agregar el submenú de devoluciones.
6.  Agregar el balance mensual.
7.  Cambiar Main para usar ConsoleMenu.
8.  Cuando todo compile, eliminar MainMenu.java si ya no tiene
    referencias.

NO mantener dos menús diferentes funcionando.

======================================================================
23. CÓDIGO BASE DE ConsoleMenu.java
    ======================================================================

package com.gamezone.ui;

import com.gamezone.model.Product; import com.gamezone.model.Return;
import com.gamezone.model.Sale; import
com.gamezone.service.PersonService; import
com.gamezone.service.ProductService; import
com.gamezone.service.ReturnService; import
com.gamezone.service.SaleService;

import java.util.ArrayList; import java.util.List; import
java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner;

    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;
    private final ReturnService returnService;

    public ConsoleMenu(
            ProductService productService,
            PersonService personService,
            SaleService saleService,
            ReturnService returnService) {

        this.scanner = new Scanner(System.in);
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.returnService = returnService;
    }

    public void showMenu() {

        int option;

        do {
            System.out.println("\n===== GAMEZONE =====");
            System.out.println("1. Gestión de productos");
            System.out.println("2. Gestión de personas");
            System.out.println("3. Gestión de ventas");
            System.out.println("4. Gestión de devoluciones");
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

                case 4:
                    showReturnMenu();
                    break;

                case 0:
                    System.out.println("Saliendo de GameZone...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (option != 0);
    }

    private void showReturnMenu() {

        int option;

        do {
            System.out.println("\n===== GESTIÓN DE DEVOLUCIONES =====");
            System.out.println("1. Registrar nueva devolución");
            System.out.println("2. Consultar todas las devoluciones");
            System.out.println("3. Consultar devoluciones por cliente");
            System.out.println("4. Consultar devoluciones por venta");
            System.out.println("5. Consultar balance mensual");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (option) {

                    case 1:
                        registerReturn();
                        break;

                    case 2:
                        showAllReturns();
                        break;

                    case 3:
                        showReturnsByCustomer();
                        break;

                    case 4:
                        showReturnsBySale();
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

            } catch (IllegalArgumentException e) {

                System.out.println("Error: " + e.getMessage());
            }

        } while (option != 0);
    }

    private void registerReturn() {

        System.out.println("\n===== REGISTRAR DEVOLUCIÓN =====");

        System.out.print("Identificador de la venta: ");
        String saleId = scanner.nextLine();

        Sale sale = null;

        for (Sale currentSale : saleService.findAll()) {
            if (currentSale.getSaleId().equals(saleId)) {
                sale = currentSale;
                break;
            }
        }

        if (sale == null) {
            System.out.println("La venta no existe.");
            return;
        }

        System.out.println("Productos de la venta:");

        List<Product> saleProducts = sale.getProducts();

        for (Product product : saleProducts) {
            System.out.println(
                    "- ID: " + product.getId()
                    + " | " + product.getTitle()
                    + " | $" + product.getPrice()
            );
        }

        System.out.print(
                "Ingrese los IDs de productos a devolver separados por coma: ");

        String productsInput = scanner.nextLine();

        List<String> productIds = new ArrayList<>();

        for (String productId : productsInput.split(",")) {

            String cleanId = productId.trim();

            if (!cleanId.isBlank()) {
                productIds.add(cleanId);
            }
        }

        System.out.print("Motivo de la devolución: ");
        String reason = scanner.nextLine();

        Return returnItem =
                returnService.registerReturn(
                        saleId,
                        productIds,
                        reason);

        System.out.println(
                returnItem.generateReturnReceipt());
    }

    private void showAllReturns() {

        List<Return> returns =
                returnService.viewAllReturns();

        if (returns.isEmpty()) {
            System.out.println("No hay devoluciones registradas.");
            return;
        }

        for (Return returnItem : returns) {
            System.out.println(
                    returnItem.generateReturnReceipt());
        }
    }

    private void showReturnsByCustomer() {

        System.out.print("Identificación del cliente: ");
        String customerId = scanner.nextLine();

        List<Return> returns =
                returnService.viewReturnsByCustomer(customerId);

        if (returns.isEmpty()) {
            System.out.println(
                    "No se encontraron devoluciones para ese cliente.");
            return;
        }

        for (Return returnItem : returns) {
            System.out.println(
                    returnItem.generateReturnReceipt());
        }
    }

    private void showReturnsBySale() {

        System.out.print("Identificador de la venta: ");
        String saleId = scanner.nextLine();

        List<Return> returns =
                returnService.viewReturnsBySale(saleId);

        if (returns.isEmpty()) {
            System.out.println(
                    "No se encontraron devoluciones para esa venta.");
            return;
        }

        for (Return returnItem : returns) {
            System.out.println(
                    returnItem.generateReturnReceipt());
        }
    }

    private void showMonthlyBalance() {

        System.out.print("Mes (1-12): ");
        int month = scanner.nextInt();

        System.out.print("Año: ");
        int year = scanner.nextInt();

        scanner.nextLine();

        double sales =
                returnService.calculateMonthlySales(
                        month, year);

        double returns =
                returnService.calculateMonthlyReturns(
                        month, year);

        double balance =
                returnService.generateMonthlyBalance(
                        month, year);

        System.out.println("\n===== BALANCE MENSUAL =====");
        System.out.println("Mes: " + month + "/" + year);
        System.out.println("Total ventas: $" + sales);
        System.out.println("Total devoluciones: $" + returns);
        System.out.println("Balance neto: $" + balance);
    }

    private void showProductMenu() {
        // Conservar la lógica existente del MainMenu.
    }

    private void showPersonMenu() {
        // Conservar la lógica existente del MainMenu.
    }

    private void showSaleMenu() {
        // Conservar la lógica existente del MainMenu.
    }

}

MUY IMPORTANTE: Los tres métodos existentes de producto/persona/ventas
NO deben quedar literalmente vacíos en la versión final. Se debe copiar
el contenido actual de MainMenu y únicamente adaptar el constructor para
utilizar los servicios.

======================================================================
24. CONTROL DE scanner
    ======================================================================

El MainMenu actual usa:

option = scanner.nextInt();

y después vuelve a pedir datos.

Cuando se usa nextInt() seguido de nextLine(), hay que consumir el
salto:

scanner.nextLine();

Por eso en ConsoleMenu cada nextInt() debe ser seguido de
scanner.nextLine() cuando después se espera texto.

Esto evita que el motivo de devolución quede vacío.

======================================================================
25. GENERACIÓN DEL BALANCE
    ======================================================================

El profesor pide:

total ventas del mes total devoluciones del mes balance neto

El método obligatorio es:

generateMonthlyBalance(int month, int year): double

Debe hacer:

balance = totalVentas - totalDevoluciones

IMPORTANTE: El mes de la venta se determina por Sale.date.

El mes de la devolución se determina por Return.returnDate.

Ejemplo:

Ventas septiembre: $1.000.000

Devoluciones septiembre: $200.000

Balance:

$800.000

======================================================================
26. UNA DECISIÓN IMPORTANTE SOBRE EL REPORTE
    ======================================================================

El requerimiento dice generateMonthlyBalance retorna double, pero el
menú necesita mostrar también los dos componentes.

Por eso se recomienda agregar métodos auxiliares:

calculateMonthlySales() calculateMonthlyReturns()

y mantener el método obligatorio:

generateMonthlyBalance()

No cambiar la firma del método que pide el profesor.

======================================================================
27. DOCUMENTO return-analysis.md
    ======================================================================

Archivo:

docs/return-analysis.md

Debe estar redactado en inglés.

El documento actual está incompleto: solo responde parcialmente la
pregunta 1.

Debe reemplazarse por un documento completo.

CONTENIDO RECOMENDADO:

Return Module Analysis

1. Relationship between Return and Sale

The relationship between Return and Sale is an association.

A Return is not a specialized type of Sale, so inheritance is not
appropriate. It is also not a composition or aggregation because the
Return is not a physical part of the Sale. Instead, the Return
references the original Sale to identify the transaction from which the
returned products were purchased.

The relationship is represented by the originalSale attribute in Return.

2. Partial product returns

A Return can contain only some products from the original Sale.

This situation is represented by:

private List returnedProducts;

The list contains the specific Product objects being returned. If a sale
contains multiple units of the same product, repeated product
identifiers can represent the number of units being returned. The
service validates that the requested number of units does not exceed the
quantity present in the original sale.

This design allows partial returns without modifying the original Sale.

3. Thirty-day business rule

The thirty-day rule belongs to the service layer because it is a
business rule that determines whether a return operation is allowed.

The Sale model exposes the additive canBeReturned() method, which
provides a simple domain-level check. ReturnService uses that method
before creating a Return.

Java’s java.time API is used to calculate the difference between dates.
Specifically, ChronoUnit.DAYS.between() calculates the number of
calendar days between the original sale date and the current date.

For example:

long days = ChronoUnit.DAYS.between(saleDate, LocalDate.now());

A return is accepted when the result is between 0 and 30 inclusive.

4. Reusing the stock method

The return module reuses the restoreStock(String productId, int
quantity) method added to ProductService.

ReturnService invokes:

productService.restoreStock(product.getId(), 1);

Reusing this method keeps inventory logic centralized in ProductService.
Duplicating stock manipulation inside ReturnService would increase code
duplication and could cause inconsistencies if inventory rules change
later.

Centralizing stock updates improves maintainability, consistency, and
separation of responsibilities.

5. Monthly balance report

The monthly balance report belongs to ReturnService because it is a
business operation that combines information from two related business
modules: sales and returns.

ReturnService depends on SaleService to obtain sales and on
ReturnRepository/its in-memory return collection to obtain registered
returns. ProductService is also injected into ReturnService because it
is required when processing the stock restoration associated with a
successful return.

The calculation is:

net balance = monthly sales - monthly returns

This location is coherent with layered architecture because the UI only
asks for the report and displays the result, while the service layer
performs the business calculation.

======================================================================
28. DIAGRAMA Mermaid
    ======================================================================

Archivo obligatorio:

docs/return-class-diagram.md

Debe mostrar: - Return - Sale - ProductService - ReturnRepository -
ReturnService - ConsoleMenu - relación Return-Sale - integración con
ProductService - integración con SaleService - menú

Contenido recomendado:

Return Module Class Diagram

    classDiagram
        direction TB

        namespace model_layer {

            class Return {
                -String returnId
                -LocalDate returnDate
                -Sale originalSale
                -List~Product~ returnedProducts
                -String reason
                -double refundAmount
                +Return(returnId, returnDate, originalSale, returnedProducts, reason, refundAmount)
                +getReturnId() String
                +getReturnDate() LocalDate
                +getOriginalSale() Sale
                +getReturnedProducts() List~Product~
                +getReason() String
                +getRefundAmount() double
                +calculateRefundAmount() double
                +generateReturnReceipt() String
            }

            class Sale {
                -String saleId
                -String date
                -Customer customer
                -Seller seller
                -List~Product~ products
                +Sale(...)
                +calculateTotal() double
                +canBeReturned() boolean
            }

            class Product {
                <<abstract>>
                -String id
                -String title
                -double price
                -int stockQuantity
            }
        }

        namespace persistence_layer {

            class ReturnRepository {
                -String filePath
                -SaleService saleService
                -ProductService productService
                +ReturnRepository(filePath, saleService, productService)
                +saveAll(returns) void
                +loadAll() List~Return~
            }
        }

        namespace service_layer {

            class ReturnService {
                -ReturnRepository returnRepository
                -SaleService saleService
                -ProductService productService
                -List~Return~ returns
                +ReturnService(returnRepository, saleService, productService)
                +registerReturn(saleId, productIds, reason) Return
                +viewAllReturns() List~Return~
                +viewReturnsByCustomer(customerId) List~Return~
                +viewReturnsBySale(saleId) List~Return~
                +generateMonthlyBalance(month, year) double
            }

            class SaleService {
                +findAll() List~Sale~
            }

            class ProductService {
                +findById(id) Product
                +restoreStock(productId, quantity) void
            }
        }

        namespace ui_layer {

            class ConsoleMenu {
                -Scanner scanner
                -ProductService productService
                -PersonService personService
                -SaleService saleService
                -ReturnService returnService
                +ConsoleMenu(productService, personService, saleService, returnService)
                +showMenu() void
                -showReturnMenu() void
                -registerReturn() void
                -showAllReturns() void
                -showReturnsByCustomer() void
                -showReturnsBySale() void
                -showMonthlyBalance() void
            }
        }

        Return --> Sale : references original sale
        Return o-- Product : returned products

        ReturnService --> ReturnRepository : persists
        ReturnRepository --> SaleService : resolves sales
        ReturnRepository --> ProductService : resolves products

        ReturnService --> SaleService : queries sales
        ReturnService --> ProductService : restores stock

        ConsoleMenu --> ReturnService : manages returns
        ConsoleMenu --> SaleService : queries sales

        Sale --> Product : contains products

NOTA: La relación Return -> Sale debe ser una flecha de asociación, no
herencia.

======================================================================
29. README.md
    ======================================================================

Responsable: Isabela.

Agregar una sección:

Return Management

The system now supports formal product return management.

Features: - Register partial product returns. - Validate the original
sale. - Enforce the 30-day return period. - Validate product ownership
within the original sale. - Automatically restore inventory stock. -
Automatically calculate refund amounts. - Query returns by customer and
sale. - Generate monthly sales, returns and net balance reports.

También documentar:

Data persistence

Returns are stored in:

data/returns.csv

Monthly balance

Net monthly balance:

monthly sales - monthly returns

No borrar la descripción existente del proyecto.

======================================================================
30. PLAN DE COMMITS – SEBASTIÁN
    ======================================================================

Debe tener al menos 6 commits.

COMMIT 1

Archivo: Return.java

Mensaje:

git add src/main/java/com/gamezone/model/Return.java git commit -m
“feat: add return domain model” git push origin feature/return-module

COMMIT 2

Sale.java:

git add src/main/java/com/gamezone/model/Sale.java git commit -m “feat:
add return eligibility to sale” git push origin feature/return-module

COMMIT 3

Mejorar validaciones internas de Return:

git add src/main/java/com/gamezone/model/Return.java git commit -m “fix:
validate return domain data” git push origin feature/return-module

COMMIT 4

Implementar/refinar calculateRefundAmount():

git add src/main/java/com/gamezone/model/Return.java git commit -m
“feat: calculate return refund amount” git push origin
feature/return-module

COMMIT 5

Implementar/refinar generateReturnReceipt():

git add src/main/java/com/gamezone/model/Return.java git commit -m
“feat: generate return receipt” git push origin feature/return-module

COMMIT 6

Documentación de análisis:

git add docs/return-analysis.md git commit -m “docs: complete return
module analysis” git push origin feature/return-module

NO crear commits artificiales solo para cumplir seis.

======================================================================
31. PLAN DE COMMITS – AILYN
    ======================================================================

COMMIT 1

Crear ReturnRepository:

git add src/main/java/com/gamezone/persistence/ReturnRepository.java git
commit -m “feat: add return repository” git push origin
feature/return-module

COMMIT 2

Implementar saveAll():

git add src/main/java/com/gamezone/persistence/ReturnRepository.java git
commit -m “feat: persist return records” git push origin
feature/return-module

COMMIT 3

Implementar loadAll():

git add src/main/java/com/gamezone/persistence/ReturnRepository.java git
commit -m “feat: load return records” git push origin
feature/return-module

COMMIT 4

Crear ReturnService y registerReturn():

git add src/main/java/com/gamezone/service/ReturnService.java git commit
-m “feat: register product returns” git push origin
feature/return-module

COMMIT 5

Consultas:

git add src/main/java/com/gamezone/service/ReturnService.java git commit
-m “feat: query return records” git push origin feature/return-module

COMMIT 6

Balance mensual:

git add src/main/java/com/gamezone/service/ReturnService.java git commit
-m “feat: generate monthly return balance” git push origin
feature/return-module

Si además se agrega documentación o validación adicional, puede haber
más commits.

======================================================================
32. PLAN DE COMMITS – ISABELA
    ======================================================================

COMMIT 1

restoreStock():

git add src/main/java/com/gamezone/service/ProductService.java git
commit -m “feat: restore product stock” git push origin
feature/return-module

COMMIT 2

Crear/integrar ConsoleMenu:

git add src/main/java/com/gamezone/ui/ConsoleMenu.java git commit -m
“feat: add console return menu” git push origin feature/return-module

COMMIT 3

Registrar devolución desde menú:

git add src/main/java/com/gamezone/ui/ConsoleMenu.java git commit -m
“feat: register returns from console” git push origin
feature/return-module

COMMIT 4

Consultas:

git add src/main/java/com/gamezone/ui/ConsoleMenu.java git commit -m
“feat: add return queries to console” git push origin
feature/return-module

COMMIT 5

Balance mensual:

git add src/main/java/com/gamezone/ui/ConsoleMenu.java git commit -m
“feat: add monthly balance menu” git push origin feature/return-module

COMMIT 6

README y/o Main:

git add src/main/java/com/gamezone/Main.java README.md git commit -m
“docs: update return module documentation” git push origin
feature/return-module

Si Main es un cambio funcional, puede usarse:

git commit -m “feat: integrate return services into application”

======================================================================
33. PRs
    ======================================================================

El taller exige tres PR aprobados y fusionados, uno por integrante.

IMPORTANTE: Todos salen de:

feature/return-module

y apuntan a:

develop

PR 1: Cambios de Sebastián.

Título recomendado:

feat: implement return domain model

Descripción:

Summary

-   Added Return domain entity.
-   Added return eligibility rule to Sale.
-   Added refund calculation and receipt generation.
-   Completed return analysis.

Validation

-   Project compiles.
-   Return validation works.
-   Sale canBeReturned() respects the 30-day rule.

PR 2: Cambios de Ailyn.

Título:

feat: implement return persistence and service

PR 3: Cambios de Isabela.

Título:

feat: integrate return management into console

ADVERTENCIA: GitHub puede mostrar en PR posteriores commits ya incluidos
en un PR anterior dependiendo del momento exacto del merge. Para evitar
confusión:

1.  Isabela debe revisar el PR antes de fusionarlo.
2.  Solo fusionar cuando el conjunto de commits nuevos corresponda al
    integrante.
3.  Después de cada merge, sincronizar la rama.
4.  No hacer squash si el docente necesita ver los commits individuales,
    salvo que el profesor lo permita.
5.  Preferiblemente usar Merge commit para conservar evidencia del
    historial.

======================================================================
34. PROBLEMA DE TRABAJO EN LA MISMA RAMA
    ======================================================================

Antes de comenzar cualquier tarea:

git status

git fetch origin

git checkout feature/return-module

git pull origin feature/return-module

Después de que otro integrante haga push, volver a ejecutar:

git pull origin feature/return-module

Si Git indica conflictos: NO ejecutar comandos destructivos.

No usar:

git reset –hard

git push –force

sin autorización explícita del equipo/profesor.

======================================================================
35. PRUEBAS FUNCIONALES OBLIGATORIAS
    ======================================================================

CASO 1 – DEVOLUCIÓN EXITOSA

Crear una venta con:

Sale: S001

Fecha: hoy - 10 días

Productos: P001 P002

Stocks antes: P001 = 5 P002 = 3

Registrar devolución:

S001 P001

Resultado esperado:

-   Return creada.
-   returnDate = hoy.
-   returnedProducts contiene P001.
-   refundAmount = precio P001.
-   stock P001 aumenta de 5 a 6.
-   returns.csv contiene la devolución.
-   recibo se muestra.

CASO 2 – DEVOLVER MÁS DE 30 DÍAS

Venta: S002

Fecha: hoy - 31 días

Intentar devolver:

P003

Resultado:

“La devolución no puede registrarse porque han pasado más de 30 días
desde la venta.”

NO debe: - crear Return - cambiar stock - escribir devolución

CASO 3 – PRODUCTO QUE NO PERTENECE

Venta S003 contiene: P001

Intentar devolver: P999

Resultado: “El producto P999 no pertenece a la venta indicada.”

NO cambiar stock.

CASO 4 – DEVOLUCIÓN PARCIAL

Venta: S004

Productos: P001 P002 P003

Devolver: P001

Resultado: La venta sigue representando la compra original y Return solo
contiene P001.

CASO 5 – DOS PRODUCTOS

Devolver: P001 P002

Refund: precio P001 + precio P002.

Stock: +1 P001 +1 P002.

CASO 6 – CANTIDAD REPETIDA

Venta: P001 P001 P002

Devolver: P001 P001

Debe permitirse.

Intentar: P001 P001 P001

Debe rechazarse.

CASO 7 – CONSULTA POR CLIENTE

Registrar dos devoluciones de ventas del mismo cliente.

Consultar customerId.

Deben aparecer ambas.

CASO 8 – CONSULTA POR VENTA

Consultar S001.

Solo deben aparecer devoluciones asociadas a S001.

CASO 9 – BALANCE

Ventas del mes: $500.000

Devoluciones: $100.000

Resultado: $400.000

======================================================================
36. PRUEBA DE INVENTARIO
    ======================================================================

Antes de devolución:

productService.findById(“P001”).getStockQuantity()

Registrar devolución.

Después:

productService.findById(“P001”).getStockQuantity()

Debe ser:

stockAntes + cantidadDevuelta

No:

stockAntes + 1 siempre.

Si se devuelven tres unidades del mismo producto, debe aumentar en 3.

======================================================================
37. PRUEBA DE PERSISTENCIA
    ======================================================================

Después de registrar:

cat data/returns.csv

o en Windows:

type data.csv

Debe aparecer el registro.

Cerrar aplicación.

Volver a iniciar.

ReturnRepository debe intentar cargar returns.csv.

NOTA: La resolución de Sale depende de las ventas disponibles en
SaleService, porque el repositorio de ventas actual no carga
automáticamente sales.csv.

======================================================================
38. PRUEBA DEL MENÚ
    ======================================================================

Ejecutar:

mvn clean test

o, si no hay Maven instalado pero el IDE está configurado:

Run Main.java

Menú esperado:

===== GAMEZONE ===== 1. Gestión de productos 2. Gestión de personas 3.
Gestión de ventas 4. Gestión de devoluciones 0. Salir

Entrar en 4:

===== GESTIÓN DE DEVOLUCIONES ===== 1. Registrar nueva devolución 2.
Consultar todas las devoluciones 3. Consultar devoluciones por cliente
4. Consultar devoluciones por venta 5. Consultar balance mensual 0.
   Volver

======================================================================
39. COMPILACIÓN
    ======================================================================

Cada integrante debe ejecutar antes de hacer PR:

mvn clean test

Si el proyecto no tiene tests, al menos:

mvn clean package

El resultado esperado debe terminar sin BUILD FAILURE.

Si Maven no está disponible:

mvn -version

Instalar/configurar Maven o usar Maven Wrapper si existe.

También comprobar Java:

java -version

El pom actual declara Java 25. Si la máquina tiene Java 21, no asumir
que funcionará: primero configurar JDK 25 o alinear el proyecto con la
versión oficial entregada por el docente.

======================================================================
40. CHECKLIST DE SEBASTIÁN
    ======================================================================

[ ] Return.java existe en com.gamezone.model. [ ] Atributos privados
exactos. [ ] LocalDate usado para returnDate. [ ] Sale usado como
originalSale. [ ] List usado. [ ] Constructor completo. [ ] Getters. [ ]
Sin setter para originalSale. [ ] calculateRefundAmount(). [ ]
generateReturnReceipt(). [ ] Validaciones internas. [ ]
Sale.canBeReturned(). [ ] ChronoUnit.DAYS.between(). [ ] Fecha 0..30
permitida. [ ] Fecha 31 rechazada. [ ] Al menos seis commits. [ ]
Commits en inglés. [ ] Todos push. [ ] Documentación en inglés. [ ] PR
creado.

======================================================================
41. CHECKLIST DE AILYN
    ======================================================================

[ ] ReturnRepository existe. [ ] Paquete persistence. [ ] Ruta
data/returns.csv. [ ] Constructor recibe SaleService. [ ] Constructor
recibe ProductService. [ ] saveAll(). [ ] loadAll(). [ ] Archivo
inexistente devuelve lista vacía. [ ] Referencias a Sale resueltas. [ ]
Referencias a Product resueltas. [ ] ReturnService existe. [ ]
registerReturn(). [ ] Validación de venta. [ ] Validación 30 días. [ ]
Validación pertenencia. [ ] Validación cantidad. [ ]
calculateRefundAmount(). [ ] restoreStock(). [ ] Persistencia. [ ]
Consultar todas. [ ] Consultar cliente. [ ] Consultar venta. [ ] Balance
mensual. [ ] Al menos seis commits. [ ] Todos push. [ ] PR creado.

======================================================================
42. CHECKLIST DE ISABELA
    ======================================================================

[ ] feature/return-module creada desde develop. [ ] restoreStock(). [ ]
ConsoleMenu. [ ] Dependencias inyectadas. [ ] Main actualizado. [ ] Menú
de devoluciones. [ ] Registrar devolución. [ ] Consultar todas. [ ]
Consultar cliente. [ ] Consultar venta. [ ] Balance mensual. [ ] Manejo
de errores. [ ] Scanner correcto. [ ] README actualizado. [ ] Revisión
de PR Sebastián. [ ] Revisión de PR Ailyn. [ ] PR propio. [ ] Al menos
seis commits. [ ] Merge final. [ ] Rama remota eliminada.

======================================================================
43. VALIDACIÓN DE LA ARQUITECTURA
    ======================================================================

MODEL

Return: Representa datos y comportamiento propio de una devolución.

Sale: Conoce si una venta está dentro del plazo.

PERSISTENCE

ReturnRepository: Lee/escribe returns.csv.

SERVICE

ReturnService: Orquesta reglas de negocio.

ProductService: Gestiona stock.

SaleService: Proporciona ventas.

UI

ConsoleMenu: Recibe entrada del usuario y muestra resultados.

NO poner: - FileWriter en ConsoleMenu. - reglas de 30 días en
ConsoleMenu. - manipulación directa de stock en ConsoleMenu. - saveAll()
de ReturnRepository en UI. - cálculo de balance en UI.

La UI llama al servicio.

======================================================================
44. ERROR COMÚN QUE DEBEN EVITAR
    ======================================================================

ERROR:

ReturnService cambia stock directamente.

CORRECTO:

ReturnService | v ProductService.restoreStock() | v
ProductRepository.saveAll()

ERROR:

ConsoleMenu calcula los 30 días.

CORRECTO:

ConsoleMenu | v ReturnService | v Sale.canBeReturned()

ERROR:

ReturnService escribe returns.csv directamente.

CORRECTO:

ReturnService | v ReturnRepository.saveAll()

ERROR:

Return hereda de Sale.

CORRECTO:

Return –> Sale associación.

======================================================================
45. INTEGRACIÓN FINAL
    ======================================================================

Cuando los tres hayan terminado:

Isabela:

git checkout feature/return-module git pull origin feature/return-module

Compilar.

Corregir errores de integración.

Después:

git status git log –oneline –decorate –graph -30

Revisar que existan commits de los tres.

Verificar archivos:

src/main/java/com/gamezone/model/Return.java
src/main/java/com/gamezone/persistence/ReturnRepository.java
src/main/java/com/gamezone/service/ReturnService.java
src/main/java/com/gamezone/ui/ConsoleMenu.java docs/return-analysis.md
docs/return-class-diagram.md README.md

======================================================================
46. REVISIÓN ANTES DEL PR FINAL
    ======================================================================

Comandos:

git fetch origin

git status

git diff develop…feature/return-module

Revisar archivos modificados.

Después:

mvn clean test

o:

mvn clean package

Luego ejecutar la aplicación.

Probar: - registro - 30 días - pertenencia - parcial - stock -
consultas - balance

======================================================================
47. MERGE A DEVELOP
    ======================================================================

Una vez aprobados los tres PR:

git checkout develop

git pull origin develop

Si los PR ya están fusionados en GitHub:

git pull origin develop

Ver:

git log –oneline –decorate –graph -30

Comprobar que feature/return-module ya fue fusionada.

======================================================================
48. ELIMINACIÓN DE LA RAMA REMOTA
    ======================================================================

SOLO después de confirmar que todo está fusionado:

git push origin –delete feature/return-module

NO hacerlo antes.

Luego:

git fetch –prune

Comprobar:

git branch -a

Debe quedar:

origin/develop origin/main

y no:

origin/feature/return-module

La rama local también puede eliminarse después:

git branch -d feature/return-module

======================================================================
49. ESTADO FINAL ESPERADO
    ======================================================================

develop | +– Return.java +– Sale.canBeReturned() +–
ReturnRepository.java +– ReturnService.java +–
ProductService.restoreStock() +– ConsoleMenu.java +– Main actualizado +–
README actualizado +– return-analysis.md +– return-class-diagram.md +–
data/returns.csv

FUNCIONAMIENTO:

Usuario | v ConsoleMenu | v ReturnService | +—-> SaleService —-> Sale |
+—-> ProductService –> ProductRepository –> products.dat | +—->
ReturnRepository –> data/returns.csv | v Return

======================================================================
50. DEMOSTRACIÓN IDEAL AL PROFESOR
    ======================================================================

La demostración debe hacerse en este orden:

1.  Mostrar GitHub.
2.  Mostrar develop.
3.  Mostrar PRs.
4.  Mostrar commits.
5.  Mostrar arquitectura.
6.  Abrir Return.java.
7.  Mostrar Sale.canBeReturned().
8.  Mostrar ReturnService.
9.  Mostrar restoreStock().
10. Mostrar ReturnRepository.
11. Mostrar ConsoleMenu.
12. Ejecutar programa.
13. Crear/usar una venta.
14. Registrar devolución parcial.
15. Mostrar comprobante.
16. Mostrar stock antes/después.
17. Intentar devolución con 31 días.
18. Mostrar rechazo.
19. Intentar producto que no pertenece.
20. Mostrar rechazo.
21. Consultar todas.
22. Consultar por cliente.
23. Consultar por venta.
24. Mostrar balance mensual.
25. Abrir returns.csv.
26. Mostrar diagrama Mermaid.
27. Mostrar return-analysis.md.

======================================================================
51. RESPUESTAS CORTAS PARA LAS CINCO PREGUNTAS DEL PROFESOR
    ======================================================================

PREGUNTA 1: Association, because Return references the original Sale but
is not a subtype or component of Sale.

PREGUNTA 2: List returnedProducts. It contains only the products
returned by the customer, allowing partial returns. Repeated products
can represent multiple returned units.

PREGUNTA 3: The business rule is enforced in ReturnService.
Sale.canBeReturned() provides the domain-level check.
ChronoUnit.DAYS.between() calculates the calendar-day difference.

PREGUNTA 4: ProductService.restoreStock(String productId, int quantity).
ReturnService calls it after all return validations succeed. This
centralizes stock logic.

PREGUNTA 5: ReturnService. It already coordinates return business rules
and can obtain sales from SaleService and return information from its
return repository/data. The balance is a business calculation, not a UI
responsibility.

======================================================================
52. ORDEN FINAL DE EJECUCIÓN DEL EQUIPO
    ======================================================================

DÍA / BLOQUE 1

Isabela: - develop actualizado - crea feature/return-module - push

Sebastián: - checkout feature - Return.java - Sale.canBeReturned() -
commits y push

DÍA / BLOQUE 2

Ailyn: - pull feature - ReturnRepository - ReturnService - commits y
push

DÍA / BLOQUE 3

Isabela: - pull feature - ProductService.restoreStock - ConsoleMenu -
Main - README - commits y push

DÍA / BLOQUE 4

Equipo: - pruebas - correcciones - documentación - diagrama - PRs

DÍA / BLOQUE 5

Isabela: - revisar PRs - fusionar - develop actualizado - eliminar
feature remota

======================================================================
53. REGLAS DE ORO DURANTE EL EXAMEN
    ======================================================================

1.  Siempre trabajar desde feature/return-module.
2.  Nunca commit directo a develop.
3.  Nunca commit directo a main.
4.  Nunca push –force.
5.  Siempre escribir commits en inglés.
6.  Usar Conventional Commits.
7.  Hacer push después de cada commit.
8.  Antes de comenzar, hacer pull.
9.  No sobrescribir trabajo de otro integrante.
10. No cambiar arquitectura sin coordinar.
11. No duplicar lógica de stock.
12. No poner reglas de negocio en UI.
13. No escribir archivos desde service.
14. No hacer cálculo del balance en UI.
15. Validar todo antes de cambiar stock.
16. Mantener mensajes de error en español para el usuario.
17. Mantener docs/return-analysis.md en inglés.
18. Mantener el diagrama en Mermaid.
19. Probar casos exitosos y casos de rechazo.
20. No eliminar feature/return-module hasta que todo esté fusionado.

======================================================================
54. CRITERIO DE “TERMINADO”
    ======================================================================

El módulo NO se considera terminado simplemente porque compile.

Se considera terminado cuando:

[ ] Return existe. [ ] canBeReturned existe. [ ] ReturnRepository
existe. [ ] ReturnService existe. [ ] restoreStock existe. [ ]
ConsoleMenu existe. [ ] Main integra todas las dependencias. [ ]
returns.csv se genera. [ ] devolución parcial funciona. [ ] plazo 30
días funciona. [ ] producto ajeno se rechaza. [ ] stock se restaura
automáticamente. [ ] monto se calcula automáticamente. [ ] consulta
general funciona. [ ] consulta por cliente funciona. [ ] consulta por
venta funciona. [ ] balance funciona. [ ] análisis en inglés está
completo. [ ] diagrama Mermaid está completo. [ ] README está
actualizado. [ ] tres PRs están aprobados y fusionados. [ ] cada
integrante tiene mínimo seis commits atómicos. [ ] feature/return-module
fue fusionada a develop. [ ] feature/return-module fue eliminada del
remoto. [ ] no hubo commits directos a develop/main. [ ] no hubo push
–force.

======================================================================
55. ÚLTIMA RECOMENDACIÓN
    ======================================================================

NO empiecen pegando todo el código de una vez.

El orden correcto es:

1.  Sincronizar Git.
2.  Crear feature/return-module.
3.  Modelo Return + Sale.canBeReturned.
4.  ReturnRepository.
5.  ReturnService.
6.  restoreStock.
7.  ConsoleMenu.
8.  Main.
9.  Documentación.
10. Pruebas.
11. PRs.
12. Merge.
13. Eliminar rama.

Si aparece un error de compilación después de integrar una clase,
primero identificar exactamente qué dependencia falta. No solucionar el
problema copiando clases enteras de otras capas.

La arquitectura debe permanecer:

UI -> Service -> Persistence | v Model

El nuevo flujo central es:

ConsoleMenu | v ReturnService | +–> SaleService | +–>
ProductService.restoreStock() | +–> ReturnRepository | v
data/returns.csv

FIN DEL PLAN
