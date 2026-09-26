
# AI Usage Log - Developer 2

## Project
GameZoneUnicesar

## Role
Developer 2 - Person Module

## AI Tool
ChatGPT

---

## Entry 1 - Understanding the People module

**Topic:** People model hierarchy.

**Question:** I asked for clarification about how the abstract base Person class and its derived classes should be organized.

**Use of AI:** Conceptual explanation of inheritance and abstract classes in Java.

**Decision taken:** I used the explanation to better understand the relationship between Person, Customer, and Seller before implementing my own classes.

---

## Entry 2 - JavaDoc

**Topic:** JavaDoc documentation.

**Question:** I asked about the purpose and correct use of JavaDoc in Java classes and methods.

**Use of AI:** Explanation of JavaDoc conventions and documentation practices.

**Decision taken:** I documented the classes and methods of my People model in English.

---

## Entry 3 - Git Flow and Pull Requests

**Topic:** Git branches and Pull Requests.

**Question:** I asked how to create and organize a Pull Request from my feature branch to the develop branch.

**Use of AI:** Explanation of Git Flow, feature branches, Pull Requests, and review requirements.


## Entry 4 - Repository organization

**Topic:** Project documentation structure.

**Question:** I asked where the required documentation files should be located in the repository.

**Use of AI:** Clarification of the required repository structure.

## Entry 5 -Persistence (PersonRepository)

- Asked Claude for help with saving and loading person data to/from a file
  (CSV format), and to add JavaDoc documentation to the class.
## Entry 6 - Service (PersonService)

- Asked Claude for help with JavaDoc documentation for the PersonService class.

## Entry 7 - persistence and Service(ReturRepository, ReturnService) - requeriment 3
- Asked Claude for git/commit conventions and Pull Request wording.
- Asked Claude to explain constructor design choices.
- Claude wrote the JavaDoc comments for the new classes and methods.

## Entry 8 - persistence and service(AccessoryRepository, AccessoryService) - requeriment 1
-Asked Claude for git/commit conventions and Pull Request wording.
- Asked Claude to explain constructor design choices.
- Claude wrote the JavaDoc comments for the new classes and methods.

## Entry 9 - persistence and service (PromotionRepository, PromotionService) - requeriment 2
-Asked Claude for git/commit conventions and Pull Request wording.
- Asked Claude to explain constructor design choices.
- Claude wrote the JavaDoc comments for the new classes and methods.
- Claude provided guidance on CSV file storage design (header row,
  discriminator column, `saveAll`/`loadAll` structure).

## Entry 11 - Service and Persistence (ReturnService, AccessoryService, ReturnRepository) - Requirement 5, adjustment A4

**Topic:** Restoring accessory stock on returns (accessories were not being restocked when returned).

**Question:** I asked Claude how to modify ReturnService, AccessoryService and ReturnRepository so returned accessories would have their stock restored, given that Accessory extends Product.

**Use of AI:**
- Claude clarified that since Accessory extends Product, Sale and Return could already hold accessories inside their List<Product> without model changes.
- Claude wrote the JavaDoc comments for the modified classes and methods.
- Claude helped fix the constructor calls in Main after the signatures changed (parameter order and missing arguments).
- Asked Claude for commit message wording and Pull Request wording (problem, cause, solution, verification) for the fix/ branch.

**Decision taken:** I applied the instanceof-based solution to distinguish accessories from products when restoring stock, updated all four affected classes (AccessoryService, ReturnService, ReturnRepository, Main), and used the suggested commit and Pull Request wording for branch fix/return-accessory-stock.
## Entry 10 - Persistence and Service (WarrantyRepository, WarrantyService) - Requirement 5, adjustment A2

**Topic:** Circular dependency fix in the warranty module (SaleService → WarrantyService → WarrantyRepository → SaleService).

**Question:** I asked Claude how to redesign WarrantyRepository and WarrantyService so the repository would no longer depend on SaleRepository/ProductRepository, keeping only identifiers instead of resolved Product/Sale objects.

**Use of AI:**
- Claude explained why the circular dependency happened and proposed moving reference resolution from WarrantyRepository to WarrantyService.
- Claude recommended the use of raw String[] rows instead of resolved objects.
- Claude wrote the JavaDoc comments for the modified classes and methods.
- Claude helped identify and fix the constructor calls in Main after the signatures changed.
- Claude helped update docs/warranty-class-diagram.md to reflect the new dependencies (WarrantyService now depends on SaleRepository and ProductService).
- Asked Claude for git commands (merge conflict resolution on .idea/misc.xml, git push --set-upstream for a new branch) and for commit message wording.
- Asked Claude for Pull Request wording (problem, cause, solution, verification) for the fix/ branch.

**Decision taken:** I adopted the String[]-based solution instead of creating an additional DTO class, applied the suggested changes to WarrantyRepository, WarrantyService and Main, updated the class diagram, and used the suggested commit and Pull Request wording for branch fix/warranty-circular-dependency.
