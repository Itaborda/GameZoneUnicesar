# AI Usage Log - Leader

## Project
GameZoneUnicesar

## Role
Leader - Sale Module, Project Configuration and Integration

## AI Tool
ChatGPT

---

## Entry 1 - Project Structure

**Topic:** Project architecture and package organization.

**Question:** I asked for clarification about how to organize the GameZone project into the required model, persistence, service, and ui layers.

**Use of AI:** Explanation of the responsibilities of each layer and the dependency direction between them.

**Decision taken:** I used the explanation to organize the project according to the required layered architecture.

---

## Entry 2 - Git and GitHub

**Topic:** Git Flow and branch management.

**Question:** I asked how to create and manage the main, develop, and feature branches and how to work with Pull Requests.

**Use of AI:** Explanation of the Git Flow required for the project, including feature branches, develop integration, Pull Requests, commits, and branch synchronization.

**Decision taken:** I used the explanation to organize my work with the feature/leader-configuration branch and the develop branch.

---

## Entry 3 - Maven and Project Configuration

**Topic:** Maven project configuration.

**Question:** I asked how the Maven configuration and project structure should be organized for the GameZone application.

**Use of AI:** Explanation of the pom.xml file, Maven project structure, Java version, and package organization.

**Decision taken:** I used the explanation to configure the project structure and understand the role of Maven in the application.

---

## Entry 4 - Sale Model

**Topic:** Sale domain model.

**Question:** I asked how the Sale class should represent a sale involving a customer, seller, and one or more products.

**Use of AI:** Conceptual explanation of the relationships between Sale, Customer, Seller, and Product.

**Decision taken:** I implemented the Sale class with the required attributes and relationships.

---

## Entry 5 - Sale Total

**Topic:** Sale total calculation.

**Question:** I asked how to calculate the total value of a sale using the products included in it.

**Use of AI:** Explanation of how to iterate through the products associated with a sale and add their prices.

**Decision taken:** I implemented the calculateTotal() method in the Sale class.

---

## Entry 6 - Sale Repository

**Topic:** Persistence of sales.

**Question:** I asked how the sales information should be stored using a persistence class.

**Use of AI:** Explanation of the repository responsibility and file-based persistence.

**Decision taken:** I implemented the SaleRepository class to manage the storage of sales information.

---

## Entry 7 - Sale Service

**Topic:** Business rules for registering sales.

**Question:** I asked how the SaleService should validate a sale before saving it.

**Use of AI:** Explanation of validation rules, product lookup, stock verification, stock updating, and saving the sale.

**Decision taken:** I implemented validation to ensure that a sale contains at least one product, verify that products exist and have available stock, update the stock, and save the sale.

---

## Entry 8 - Main Menu

**Topic:** Main menu and submenus.

**Question:** I asked how to organize the console menu for the different modules of the GameZone application.

**Use of AI:** Explanation of how to separate the main menu into product, person, and sales submenus.

**Decision taken:** I implemented the MainMenu with separate submenus for products, people, and sales. User-facing messages were kept in Spanish while class and method names remained in English.

---

## Entry 9 - JavaDoc

**Topic:** JavaDoc documentation.

**Question:** I asked about the purpose and correct use of JavaDoc in the Java classes and methods.

**Use of AI:** Explanation of JavaDoc conventions and documentation practices.

**Decision taken:** I used the explanation to document the classes and relevant methods developed in my module.

---

## Entry 10 - Git Integration

**Topic:** Synchronizing the feature branch with develop.

**Question:** I asked how to update my feature branch with the latest changes from develop without losing my local changes.

**Use of AI:** Explanation of using Git stash and pulling changes from the develop branch.

**Decision taken:** I temporarily saved my local changes, updated my feature branch with develop, and continued working with my changes preserved.