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
