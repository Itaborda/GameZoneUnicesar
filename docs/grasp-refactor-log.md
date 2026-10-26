
# GRASP Refactoring Log â€” GameZoneUnicesar

## 1. Refactoring Information

- **Project:** GameZoneUnicesar
- **Phase:** Phase 2 â€” GRASP Corrections
- **Branch:** feature/grasp-refactor
- **Baseline compilation:** Passed (`mvn clean compile`)
- **Baseline regression tests:** Pending manual execution
- **Scope:** Approved GRASP corrections only
- **Behavior requirement:** Preserve existing menu options, Spanish messages, workflows, and data-file contents.

## 2. Baseline Regression Strategy

Before implementing corrections, manually execute the existing application and record the actual results. Do not mark a test as passed unless it has been executed.

| ID | Operation | Test input or action | Expected result | Baseline result                         |
|---|---|---|---|-----------------------------------------|
| T-01 | Main menu | Launch the application | Main menu appears with existing options | Passed â€” Main menu displayed and exit option worked |
| T-02 | Products | Open product menu and list products | Existing products are displayed | Passed â€” Product listing checked        |
| T-03 | People | Open people menu and list people | Existing people are displayed | Passed â€” People listing checked                                        |
| T-04 | Accessories | Open accessory menu and list accessories | Existing accessories are displayed | Passed â€” Accessory menu checked successfully                                        |
| T-05 | Promotions | Open promotion menu and list promotions | Existing promotions are displayed | Passed â€” Promotion menu checked successfully                               |
| T-06 | Warranties | Open warranty menu and list warranties | Existing warranty options are displayed | Passed â€” Warranty menu checked successfully                                 |
| T-07 | Register sale | Register a sale using valid existing records and available stock | Sale is registered and totals are displayed | Pending                                 |
| T-08 | List sales | Open the sales listing | Existing and newly registered sales are displayed | Pending                                 |
| T-09 | Invalid sale | Attempt a sale with invalid or unavailable input | Existing validation and Spanish messages are preserved | Pending                                 |
| T-10 | Register return | Register a return for an eligible existing sale | Return follows existing rules and updates the expected records | Pending                                 |
| T-11 | Invalid return | Attempt a return that violates existing rules | Existing validation behavior is preserved | Pending                                 |
| T-12 | Inventory | Verify stock before and after a valid sale and return | Stock changes according to existing rules | Pending                                 |
| T-13 | Persistence | Restart the application after a valid operation | Saved data can be loaded consistently | Pending                                 |
| T-14 | Exit | Select the existing exit option | Application exits as before | Pending                                 |

## 3. Refactoring Change Log

Record each implemented correction in the following format.

| Date    | Class or component | Change | Finding ID | GRASP pattern | Commit | Pull request | Owner |
|---------|---|---|---|---|---|---|---|
| 2026-10-25 | `ProductService` | The repository is received through the constructor; fields are private and final. A deprecated no-argument constructor keeps the previous path (`data/product.csv`) until `Main` injects the repository. | V-01 | Low Coupling | `refactor: inject ProductRepository into ProductService` / `48dc0f8` | `<n>` | Developer 1 |
| 2026-10-25 | `ProductCsvMapper`, `VideoGameCsvMapper`, `ConsoleCsvMapper` (new) | One mapper per kind of product, with the same line format as before. | V-02 | Polymorphism | `refactor: add ProductCsvMapper strategy for product serialization (V-02)` / `af12561` | `<n>` | Developer 1 |
| 2026-10-25 | `ProductRepository` | `saveAll` and `findAll` use the mappers instead of `instanceof` and literal type markers; record encoding and decoding are in one place (V-03, centralization only). File format unchanged. | V-02, V-03 | Polymorphism, Protected Variations | `refactor: delegate ProductRepository serialization to product mappers` / `83b8751` | `<n>` | Developer 1 |
| 2026-10-25 | `ProductCategory` (new) | Enum with `VIDEOGAME`, `CONSOLE` and `ACCESSORY` and a case-insensitive `fromName`. | V-02 | Polymorphism | `refactor: add ProductCategory enum` / `84c0fa2` | `<n>` | Developer 1 |
| 2026-10-25 | `Product`, `VideoGame`, `Console`, `Accessory` | `Product.getCategory()` is abstract and each product type returns its own category. | V-02 | Polymorphism | `refactor: declare the category in each Product subclass` / `d18a847` | `<n>` | Developer 1 |
| 2026-10-25 | `CategoryDiscount` | `belongsToTargetCategory` compares `product.getCategory()` with the target category; the `instanceof` chain was removed. The target category is still text in the constructor, getter, setter and file. | V-02 | Polymorphism | `refactor: match CategoryDiscount target through Product.getCategory` / `bac3c10` | `<n>` | Developer 1 |

**Traceability note:**
Finding IDs in the change log currently refer to the source audit reports and remain provisional until the consolidated audit assigns unique identifiers. The team must preserve the original references when recording final consolidated IDs. Pull request references must be replaced with verified PR numbers; placeholders must not be treated as completed traceability.
The existing Product and People corrections are recorded in the `feature/grasp-refactor` branch. The team must verify each correction against the corresponding finding and preserve the distinction between code changes, compilation results, scripted non-regression evidence, and pending manual regression scenarios.



## 4. Validation Rules

1. Run `mvn clean compile` before each pull request.
2. Execute the regression tests affected by each correction.
3. Record actual results and explain any differences.
4. Run the complete regression suite after all corrections.
5. Preserve existing observable behavior and data formats.
6. Do not fix unrelated functional defects as part of GRASP refactoring.
7. Confirm finding identifiers and ownership against the approved consolidated audit.
8. Keep this log synchronized with the actual commits and pull requests.

## 5. Final Results

- **Initial compilation:** Passed.
- **Initial manual regression:** Pending.
- **Affected-operation regression:** Pending.
- **Final compilation:** Pending.
- **Final manual regression:** Pending.
- **Outstanding deviations:** Pending review.

## 6. Developer 1 â€” Findings Left Out of Scope

Functional defects found in the code of Developer 1. Correcting them would change what the user sees or what is stored, so they are not fixed in this requirement.

ID	Where	Defect	Justification
F-01	Main, ProductService	The seed file Data/Products.csv is never loaded; the service uses data/product.csv.	Reading the other file changes the products shown at startup. The injection of V-01 keeps data/product.csv.
F-02	ProductRepository	Commas inside fields, malformed lines and unknown product kinds are not handled (the part of V-03 about escaping).	Changes files and errors.
F-03	ProductService.registerProduct	ProductValidator.validateProductData is never called.	Would reject data that is accepted today.

## 7. Developer 1 â€” Non-regression Evidence

Besides the manual tests of section 2, I compared a scripted run of the menu on develop (before the first correction) with the same run after each of the six code commits. The run registers products, accessories, customers and promotions (percentage, category and bulk), makes sales with and without extended warranty, a sale without stock, a return, queries warranties, lists, and starts the application a second time to reload the files. The output (1,190 lines) and the data files (29 lines) were identical in the six comparisons. A class-level check of 107 lines (product file read and write, stock messages, category discounts with different spellings, refunds and canBeReturned) was also identical before and after.


## 8. Developer 2 (V-01, V-05,)
| 2026-10-25 | `PersonService`, `PersonRepository`, `PersonStorage` (new) | `PersonStorage` interface added in `persistence`; `PersonRepository` implements it and `PersonService` receives it through the constructor. The hard-coded repository initializer and the unused `ProductRepository` import were removed, and the field is now final. `Main` is unchanged and file format and messages are the same. | V-01 | Low Coupling, Indirection | `refactor: make PersonService depend on a PersonStorage abstraction (V-01)` / `5a9bc5a` | `<n>` | Developer 2 |
| 2026-10-25 | `AccessoryService`, `AccessoryRepository`, `AccessoryStorage` (new) | `AccessoryStorage` interface added in `persistence`; `AccessoryRepository` implements it and `AccessoryService` receives it through the constructor. The hard-coded repository initializer (`data/accessories.csv`) was removed and the field is now final. `Main` is unchanged and file format and messages are the same. | V-05 | Low Coupling, Indirection | `refactor: make AccessoryService depend on an AccessoryStorage abstraction (V-05)` / `6b77c14` | `<n>` | Developer 2 |
| 2026-10-25 | `PromotionService`, `PromotionRepository`, `PromotionStorage` (new) | `PromotionStorage` interface added in `persistence`; `PromotionRepository` implements it and `PromotionService` receives it through the constructor. The hard-coded repository initializer (`data/promotions.csv`) was removed and the field is now final. `Main` is unchanged and file format and messages are the same. | V-08 | Low Coupling, Indirection | `refactor: make PromotionService depend on a PromotionStorage abstraction (V-08)` / `12ac470` | `<n>` | Developer 2 |
| 2026-10-25 | `PersonCsvMapper` (new), `PersonRepository` | One converter per role (Customer, Seller) turns a person into a CSV line and a line into a person, selected by role. `PersonRepository` now only handles file access and delegates the conversion to the mapper; the `instanceof` chain and the role literals in `findAll` were removed. Header, line format and the skipping of unknown roles are unchanged. | V-02 | Polymorphism, Pure Fabrication | `refactor: extract person CSV conversion into PersonCsvMapper (V-02)` / `8f2bacd` | `<n>` | Developer 2 |
| 2026-10-26 | `PromotionCsvMapper` (new), `PromotionRepository` | One converter per promotion type (percentage, category, bulk) turns a promotion into a CSV line and a line into a promotion. `PromotionRepository` only handles file access and delegates the conversion; the `instanceof` chain and the type literals were removed. Header, line format, the skipping of unknown types and the error for malformed dates are unchanged. | V-07 | Polymorphism, Pure Fabrication | `refactor: extract promotion CSV conversion into PromotionCsvMapper (V-07)` / `ebaa98a` | `<n>` | Developer 2 |