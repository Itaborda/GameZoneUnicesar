# AI Usage Log - Developer 1 

## 2026-09-07 - CSV persistence design for polymorphic products
**Tool:** Claude
**Question or query:** How to structure ProductRepository to save VideoGame and Console in a single CSV file.
**Summary of response:** Suggested using a "type" column as a discriminator (VIDEOGAME/CONSOLE) to reconstruct the correct object when reading the file, explaining the general flow of saveAll()/findAll() without providing complete code.
**Decision made:** Adopted the type discriminator strategy. Implementation of ProductRepository using this logic is still pending.

---

## 2026-09-07 - Correct use of JavaDoc
**Tool:** Claude
**Question or query:** What JavaDoc is, what it's used for, and which tags (@param, @return) I should use in my product module classes.
**Summary of response:** Explained that JavaDoc is Java's standard documentation format that describes the purpose of classes and methods, using /** */ before each element, with @param for each parameter and @return for the returned value.
**Decision made:** Applied the format to Product, VideoGame, and Console, documenting the constructor, getters, setters, and the abstract method getDescription().

---

## 2026-09-07 - Code review of the product module
**Tool:** Claude
**Question or query:** Review of the already implemented code for Product, VideoGame, and Console to verify best practices.
**Summary of response:** Confirmed the structure complied with the class diagram (correct inheritance, abstract class, abstract method getDescription(), encapsulation with getters/setters, use of @Override). Suggested as an optional improvement translating the output text in getDescription() to English to keep consistency with the rest of the code being in English.
**Decision made:** Kept the structure as is, since it complies with the agreed design. Left pending whether to translate the getDescription() output text to English.
## 2026-09-07 - Code review of ProductRepository (CSV persistence)
**Tool:** Claude
**Question or query:** Review of my ProductRepository implementation (saveAll/findAll using CSV with a type discriminator for VideoGame/Console).
**Summary of response:** Confirmed the logic was correct, noted the casting bug my teammate had made in a similar class (casting the wrong variable in an instanceof check) so I could avoid it in mine, and pointed out non-blocking edge cases (commas inside title values, malformed lines) as known limitations rather than required fixes for this scope.
**Decision made:** Kept my implementation as written, since it avoided the casting issue and matched the class diagram. Noted the CSV edge cases as a conscious design limitation to mention in the oral defense.

---

## 2026-09-07 - Code review of ProductService and design decisions
**Tool:** Claude
**Question or query:** Review of my ProductService implementation (registerProduct, getAllProducts, findById, updateStock) against the class diagram and the business rules from the context document.
**Summary of response:** Confirmed the class matched the diagram's fields and method signatures, and that it satisfied the business rules (auto-load on construction, auto-save after changes, stock validation before deducting inventory). Flagged that throwing IllegalArgumentException means the caller (SaleService) must catch it, and that exception messages should stay in English (technical/developer-facing) while user-facing text (like getDescription()) can stay in Spanish, since they serve different audiences.
**Decision made:** Kept the IllegalArgumentException approach and will inform the Technical Lead that SaleService needs a try-catch around updateStock() calls.

---

## 2026-09-16 - Return domain model fix and return validation
**Tool:** Claude / Query support
**Question or query:** Review of the Return class and the canBeReturned() method in Sale, which were left incomplete in a previous session, to verify which adjustments were still needed according to the requirements.
**Summary of response:** It was identified that Return had an unused import (ArrayList), disorganized imports, and missing JavaDoc documentation. In canBeReturned(), it was detected that the method did not validate whether the date was null or blank (which could throw a DateTimeParseException), and that the 30-day range calculation needed to be adjusted using ChronoUnit.DAYS.between().
**Decision made:** I cleaned up the imports, added the corresponding JavaDoc to both parts, and restructured the logic of canBeReturned() by adding null/blank validation and exception handling. I also decided to split the work into atomic commits (imports, JavaDoc, and logic separately) to keep the repository history organized.

---

## 2026-09-17 - Accessory hierarchy design for the accessory module
**Tool:** Claude / Consultation support
**Question or query:** Reviewing the best way to structure the `Accessory` class hierarchy (`Controller`, `Cable`, and `Memory` extending `Product`) to reuse existing attributes and properly model console compatibility according to Requirement 1.
**Summary of response:** Reviewed the approach of making `Accessory` extend `Product` directly so it inherits base fields (`id`, `title`, `price`, `stockQuantity`). Analyzed modeling compatibility using a `List<String>` of console IDs in `Accessory` to keep it decoupled from the full `Console` object, which simplifies filtering in `AccessoryService`. Also looked at having `Accessory` define a base `getDescription()` that subclasses override using `super.getDescription()` to append specific details without repeating code.
**Decision made:** Created `Accessory` as an abstract class with the compatibility list and helper methods (`addCompatibleConsole`, `removeCompatibleConsole`, `isCompatibleWith`). Implemented the concrete subclasses `Controller` (with `connectionType`), `Cable` (with `lengthInMeters`, `connectorType`), and `Memory` (with `capacityInGigabytes`, `memoryType`). Documented these design choices to answer the team's analysis questions in `docs/accessory-analysis.md`.

---

## 18/09/2026 - Date range validation for the `Promotion` class
**Tool:** Claude
**Question or query:** Claude was asked to review the already implemented `Promotion` class hierarchy (`Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`) to verify full compliance with the constraints specified in Requirement 2.
**Summary of response:** Claude reviewed the existing code and recommended adding validation to the `Promotion` class to ensure that `startDate` is never later than `endDate`. The current implementation did not prevent this, silently causing `isActive()` to always return `false` without triggering an error.
**Decision made:** The recommendation was followed, and a private `validateDateRange()` method was added to `Promotion`. This method is invoked by the constructor and by `setStartDate()`/`setEndDate()`, thereby centralizing the check in a single location rather than repeating it in three places.

---

## 18/09/2026 - Review of validations for the `Warranty` module
**Tool:** Claude
**Question or query:** Claude was asked to review the already implemented `Warranty` class hierarchy (`Warranty`, `BasicWarranty`, `ExtendedWarranty`) to identify any missing validations before finalizing Requirement 4.
**Summary of response:** Claude reviewed the existing code and suggested modifications to two files. For `Warranty`, it recommended validating that `id`, `product`, `sale`, and `startDate` are not null within the constructor; otherwise, a null `startDate` would trigger a `NullPointerException` when calculating the end date. For `BasicWarranty`, it recommended validating that the associated product is an instance of `Console`, based on the business rule that only consoles generate an automatic basic warranty.
**Decision made:** Both recommendations were adopted: null-value validation was added to the `Warranty` constructor, and a check was incorporated in the `BasicWarranty` constructor that throws an `IllegalArgumentException` if the product is not a `Console`.


---

## 2026-09-25 - Integration adjustment A1: accessory category discount (feature/accessory-category-discount)
**Tool:** Claude
**Question or query:** I consulted Claude regarding integration adjustment A1 to understand how to extend `CategoryDiscount` so that it recognizes accessories as a valid category after integrating the four modules. I also asked for guidance on which other parts of the system needed adjustment to ensure end-to-end support.
**Summary of response:** The AI explained that `Sale.getProducts()` already processes accessories natively because `Accessory` inherits from `Product`; therefore, the only necessary change in `CategoryDiscount` was to add the `ACCESSORY` branch within the `belongsToTargetCategory()` method. It also pointed out that `PromotionService.registerCategoryDiscount()` lacked validation for `targetCategory` (which could allow invalid data to be saved without warning), suggesting that the parameter be validated against the three permitted categories and that an `IllegalArgumentException` with a clear message in Spanish be thrown. Additionally, it warned me of a pre-existing issue unrelated to this task: the `PromotionRepository` constructor ignores the `filePath` argument and always points to `data/promotion.csv` (singular), whereas the rest of the system uses `data/promotions.csv`.
**Decision made:** Based on the assistant's analysis and recommendations, I proceeded to implement the three suggested changes in `CategoryDiscount`, `PromotionService`, and the menu text in `ConsoleMenu`. I also added the seed promotion for accessories to `data/promotions.csv`, as required by adjustment A1. I decided to leave the inconsistency in the `PromotionRepository` path unchanged, as it pertains to the persistence layer of the promotions module and falls outside the scope of adjustment A1; I will, however, formally report it to the team.
**Commit related:** d27c69b, 07cfab2, 9c0b73b, 032ad5e