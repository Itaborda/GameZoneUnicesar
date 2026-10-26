
# GRASP Refactoring Log — GameZoneUnicesar

## 1. Refactoring Information

- **Project:** GameZoneUnicesar
- **Phase:** Phase 2 — GRASP Corrections
- **Branch:** feature/grasp-refactor
- **Baseline compilation:** Passed (`mvn clean compile`)
- **Baseline regression tests:** Pending manual execution
- **Scope:** Approved GRASP corrections only
- **Behavior requirement:** Preserve existing menu options, Spanish messages, workflows, and data-file contents.

## 2. Baseline Regression Strategy

Before implementing corrections, manually execute the existing application and record the actual results. Do not mark a test as passed unless it has been executed.

| ID | Operation | Test input or action | Expected result | Baseline result                         |
|---|---|---|---|-----------------------------------------|
| T-01 | Main menu | Launch the application | Main menu appears with existing options | Passed — Main menu displayed and exit option worked |
| T-02 | Products | Open product menu and list products | Existing products are displayed | Passed — Product listing checked        |
| T-03 | People | Open people menu and list people | Existing people are displayed | Passed — People listing checked                                        |
| T-04 | Accessories | Open accessory menu and list accessories | Existing accessories are displayed | Passed — Accessory menu checked successfully                                        |
| T-05 | Promotions | Open promotion menu and list promotions | Existing promotions are displayed | Passed — Promotion menu checked successfully                               |
| T-06 | Warranties | Open warranty menu and list warranties | Existing warranty options are displayed | Passed — Warranty menu checked successfully                                 |
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
| Pending | None | No corrections recorded yet | Pending | Pending | Pending | Pending | Pending |
| 2026-10-25 | `ProductService` | The repository is received through the constructor; fields are private and final. A deprecated no-argument constructor keeps the previous path (`data/product.csv`) until `Main` injects the repository. | V-01 | Low Coupling | `refactor: inject ProductRepository into ProductService` / `48dc0f8` | `<n>` | Developer 1 |
| 2026-10-25 | `ProductCsvMapper`, `VideoGameCsvMapper`, `ConsoleCsvMapper` (new) | One mapper per kind of product, with the same line format as before. | V-02 | Polymorphism | `refactor: add ProductCsvMapper strategy for product serialization (V-02)` / `af12561` | `<n>` | Developer 1 |
| 2026-10-25 | `ProductRepository` | `saveAll` and `findAll` use the mappers instead of `instanceof` and literal type markers; record encoding and decoding are in one place (V-03, centralization only). File format unchanged. | V-02, V-03 | Polymorphism, Protected Variations | `refactor: delegate ProductRepository serialization to product mappers` / `83b8751` | `<n>` | Developer 1 |
| 2026-10-25 | `ProductCategory` (new) | Enum with `VIDEOGAME`, `CONSOLE` and `ACCESSORY` and a case-insensitive `fromName`. | V-02 | Polymorphism | `refactor: add ProductCategory enum` / `84c0fa2` | `<n>` | Developer 1 |
| 2026-10-25 | `Product`, `VideoGame`, `Console`, `Accessory` | `Product.getCategory()` is abstract and each product type returns its own category. | V-02 | Polymorphism | `refactor: declare the category in each Product subclass` / `d18a847` | `<n>` | Developer 1 |
| 2026-10-25 | `CategoryDiscount` | `belongsToTargetCategory` compares `product.getCategory()` with the target category; the `instanceof` chain was removed. The target category is still text in the constructor, getter, setter and file. | V-02 | Polymorphism | `refactor: match CategoryDiscount target through Product.getCategory` / `bac3c10` | `<n>` | Developer 1 |
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

## 6. Developer 1 — Findings Left Out of Scope

Functional defects found in the code of Developer 1. Correcting them would change what the user sees or what is stored, so they are not fixed in this requirement.

ID	Where	Defect	Justification
F-01	Main, ProductService	The seed file Data/Products.csv is never loaded; the service uses data/product.csv.	Reading the other file changes the products shown at startup. The injection of V-01 keeps data/product.csv.
F-02	ProductRepository	Commas inside fields, malformed lines and unknown product kinds are not handled (the part of V-03 about escaping).	Changes files and errors.
F-03	ProductService.registerProduct	ProductValidator.validateProductData is never called.	Would reject data that is accepted today.

## 7. Developer 1 — Non-regression Evidence

Besides the manual tests of section 2, I compared a scripted run of the menu on develop (before the first correction) with the same run after each of the six code commits. The run registers products, accessories, customers and promotions (percentage, category and bulk), makes sales with and without extended warranty, a sale without stock, a return, queries warranties, lists, and starts the application a second time to reload the files. The output (1,190 lines) and the data files (29 lines) were identical in the six comparisons. A class-level check of 107 lines (product file read and write, stock messages, category discounts with different spellings, refunds and canBeReturned) was also identical before and after.