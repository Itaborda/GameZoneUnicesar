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