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