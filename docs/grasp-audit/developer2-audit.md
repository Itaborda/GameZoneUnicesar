# Developer 2 Audit
## Scope

### Module person
- **Audited branch:** `develop`
- **Audited commit:** `6f0a034`
- **Auditor:** Developer 2
- **Audited code:** classes and methods developed by Developer 2, grouped
  by module and requirement, according to the responsibility table of
  the requirement.

### Accessories module (Requirement 1)
AccessoryRepository: constructor, saveAll(), loadAll()
AccessoryService: constructor, registerController(), registerCable(), registerMemory(), listAllAccessories(), listAccessoriesByType(), findAccessoriesCompatibleWith(), findById(), updateStock(), restoreStock()
### Promotions module (Requirement 2)
- PromotionRepository: constructor, saveAll(), loadAll()
- - PromotionService: constructor, registerPercentageDiscount(), registerCategoryDiscount(), registerBulkPurchaseDiscount(), listAllPromotions(), listActivePromotions(), findBestPromotionFor(), findById()

## 1. Information Expert
### Person module
Verdict: correct
Where: Person, Customer and Seller hold their own data (id, name, phone,
email, employee code, shift) and expose it through getters.
PersonService.getAllCustomers() and findCustomerById() work on the list
the service itself owns.
### Accesory module (requerimiento 1)
Verdict for this module: violation
AccessoryService.updateStock() and restoreStock() read an accessory's stock,
do the arithmetic and write it back, although the stock belongs to the
accessory itself. Correct usage: findAccessoriesCompatibleWith() asks each
accessory (isCompatibleWith) instead of inspecting its data
##### V-06
- **ID:** V-06
- **Pattern:** Information Expert
- **Module:** Accessories (Requirement 1), AccessoryService
- **Evidence:** AccessoryService.java, updateStock(), lines 133-135, and restoreStock(), lines 158-160:
`accessory.setStockQuantity(accessory.getStockQuantity() - quantity)`.
- **Explanation:** The service reads the stock, calculates and writes it back,
although the stock belongs to the accessory.
- **Consequence:** Every class that changes stock repeats the arithmetic and
the rules (for example, not allowing negative stock) are scattered.
- **Proposed solution:** Accessory offers methods that decrease and increase
its own stock; the service calls them and persists.
- **Other developer's code:** Yes. Developer 1 (Accessory, and Product if the
same change is applied there).
#### Promotions (Requirement 2)
Verdict for this module: violation.
##### V-09
- **ID:** V-09
- **Pattern:** Information Expert
- **Module:** Promotions (Requirement 2), PromotionService
- - **Evidence:** PromotionService.java, lines 23-24
(`ALLOWED_CATEGORY_DISCOUNT_TARGETS = Set.of("VIDEOGAME", "CONSOLE",
  "ACCESSORY")`) and registerCategoryDiscount(), lines 57-66. The same three
categories are also handled inside CategoryDiscount.belongsToTargetCategory().
- **Explanation:** The service holds knowledge about which categories a
CategoryDiscount accepts, which belongs to CategoryDiscount.
- **Consequence:** Adding a category requires editing the service and the
model class separately; they can get out of sync.
- **Proposed solution:** CategoryDiscount (or a validator) exposes whether a
target category is valid, and the service delegates to it.
- **Other developer's code:** Yes. Developer 1 (CategoryDiscount)
PromotionService keeps its own list of valid target categories, knowledge
that CategoryDiscount already holds. Correct usage: findBestPromotionFor()
and listActivePromotions() ask each promotion (calculateDiscount, isActive)
instead of computing the discount themselves.

## 2. Creator
### Person module
Verdict: violation (see V-01)
PersonService creates its own PersonRepository in a field initializer
instead of receiving it. By contrast, PersonRepository.findAll() creating
Customer and Seller objects is acceptable because it holds the data read
from the file.
### Accesory module (requerimiento 1)
Verdict for this module: violation (see V-05).
AccessoryService instantiates its own AccessoryRepository in a field
initializer instead of receiving it. Acceptable: AccessoryRepository.loadAll()
creates Controller, Cable and Memory because it holds the data read from the
file; AccessoryService never creates accessories, it receives them.
#### Promotions (Requirement 2)
Verdict for this module: violation (see V-08).
PromotionService instantiates its own PromotionRepository in a field
initializer. Acceptable: PromotionRepository.loadAll() creates the promotion
objects because it holds the data read from the file.

## 3. Controller
### Person module
Verdict: not applicable
Why: the audited classes do not receive system events. That responsibility
belongs to ConsoleMenu, which is audited by the Technical Leader.
### Accesory module (requerimiento 1)
Verdict for this module: not applicable.
These classes do not receive system events; the menu operations are handled
by ConsoleMenu, audited by the Technical Leader.
Patrón 3 · Controlador: no aplica
#### Promotions (Requirement 2)
Verdict for this module: not applicable.
These classes do not receive system events; ConsoleMenu does.

## 4. Low Coupling
### Person module
Verdict: violation
### V-01
- **ID:** V-01
- **Pattern:** Low Coupling
- **Module:** People (Requerimiento 1)
- **Evidence:** PersonService.java, line 22:
  `PersonRepository personRepository = new PersonRepository("data/product.csv");`
- **Explanation:** ...
- **Consequence:** ...
- **Proposed solution:** ...
- **Other developer's code:** No.
### Accesory module (requerimiento 1)
Verdict for this module: violation
##### V-05
-**ID:** V -05
- **Pattern:** Low Coupling
- **Module:** Accessories (Requirement 1), AccessoryService
- **Evidence:** AccessoryService.java, line 19:
`private AccessoryRepository accessoryRepository =
new AccessoryRepository("data/accessories.csv");`. The constructor
(lines 27-28) overwrites it and receives the concrete class.
- **Explanation:** The service creates and depends on a concrete repository.
The initializer is dead code but still fixes a hard-coded path.
- **Consequence:** Changing the storage (another file format or a database)
requires editing the service. The default path ("data/accessories.csv")
differs from the one used in Main ("Data/Accessories.csv").
- **Proposed solution:** An interface in the persistence package implemented
by AccessoryRepository, received by the service only through the
constructor; remove the initializer and make the field final
(Low Coupling, Indirection).
- **Other developer's code:** No. Main keeps passing the concrete repository.
AccessoryService depends on the concrete AccessoryRepository class, both in
the field and in the constructor parameter

#### Promotions (Requirement 2)
Verdict for this module: violation.
##### V-08
- **ID:** V-08
- **Pattern:** Low Coupling
- **Module:** Promotions (Requirement 2), PromotionService
- **Evidence:** PromotionService.java, line 26:
`private PromotionRepository promotionRepository =
new PromotionRepository("data/promotions.csv");`. The constructor
(lines 34-36) overwrites it and receives the concrete class.
- **Explanation:** The service creates and depends on a concrete repository.
- **Consequence:** Changing the storage requires editing the service.
- **Proposed solution:** An interface in the persistence package implemented by
PromotionRepository and received only through the constructor; remove the
initializer (Low Coupling, Indirection).
- **Other developer's code:** No.
Main keeps passing the concrete repository
PromotionService depends on the concrete PromotionRepository class.

## 5. High Cohesion
### Person module
Verdict: correct
Where: the Person hierarchy only represents data and its role;
PersonService only manages the in-memory list of people and delegates
validation and persistence.
### Accesory module (requerimiento 1)
Verdict for this module: correct.
AccessoryRepository only reads and writes the file. AccessoryService only
manages the accessory list (register, list, search, stock) and delegates
persistence to the repository.
#### Promotions (Requirement 2)
Verdict for this module: correct.
PromotionRepository only handles file access. PromotionService registers,
lists and selects promotions, and delegates persistence.

## 6. Polymorphism
### Person module
### V-02
Veredicto: violación
- **ID:** V-02
- **Pattern:** Polymorphism
- **Module:** People module (Taller 1), PersonRepository
- **Evidence:** PersonRepository.java
    - `save(List<Person>)`, line 53: `if (p instanceof Customer) {` and
      line 60: `} else if (p instanceof Seller) {`, each branch building a
      different CSV line.
    - `findAll()`, line 97: `if (role.equals("Customer")) {` and
      line 102: `} else if (role.equals("Seller")) {`, each branch
      creating a different object.
    - Lines 55 and 62 write `getRole()` as the role column, while
      `findAll()` compares that column against hard-coded literals.
- **Explanation:** The repository chooses the CSV layout and the object to
  create by checking the concrete type (`instanceof`) and the role
  text, instead of letting each variation handle its own conversion. The
  literals "Customer" and "Seller" are duplicated between
  `Customer.getRole()`, `Seller.getRole()` and `findAll()`.
- **Consequence:** Adding a new type of person (for example, a supplier)
  requires editing both `save` and `findAll`, and if the role literal
  changes in one place the file can no longer be read back. A type that is
  missing from `save` is written as an empty line.
- **Proposed solution:** Create a CSV mapping class in the persistence
  package (Pure Fabrication) with one converter per role, each knowing how
  to turn its type into a line and a line into an object, selected by
  role. `PersonRepository` would only handle file access and delegate the
  conversion. The file header, the line format and the silent skipping of
  unknown roles must stay exactly the same, so the data files and the menu
  behavior do not change.
- **Other developer's code:** No. The change stays inside
  `PersonRepository` and the new class; `Main` and `ConsoleMenu` are not
  affected.
  Uso correcto: getRole() es abstracto en Person y se sobrescribe en
  Customer y Seller.
### Accesory module (requerimiento 1)
Verdict for this module: violation
##### V-03- 
**ID:** V-03- 
**Pattern:** Polymorphism- 
**Module:** Accessories (Requirement 1), AccessoryRepository-
**Evidence:** AccessoryRepository.java
- saveAll(), lines 51, 60 and 70: `a instanceof Controller`,
  `a instanceof Cable`, `a instanceof Memory`, each branch building a
  different CSV line.
- loadAll(), lines 117, 122 and 128: `type.equals("CONTROLLER")`,
  `"CABLE"`, `"MEMORY"`, each branch creating a different object.- 
- **Explanation:** The repository selects the CSV layout and the object to
  create by checking the concrete type and a type text, instead of letting
  each variant handle its own conversion.- 
- **Consequence:** A new accessory type requires editing saveAll() and
  loadAll(). A type missing in saveAll() is written as an empty line.- 
- **Proposed solution:** Create a CSV mapper class in the persistence package
  (Pure Fabrication) with one converter per accessory type. The repository
  keeps only file access. The header, the line format and the silent skip of
  unknown types must stay identical so the data files do not change.- 
- **Other developer's code:** No.
##### V-04- 
**ID:** V-04- 
**Pattern:** Polymorphism- 
**Module:** Accessories (Requirement 1), AccessoryService- 
**Evidence:** AccessoryService.java, listAccessoriesByType(), lines 69-81:
`type.equalsIgnoreCase("CONTROLLER") && a instanceof Controller`, and the
same pair for "CABLE"/Cable and "MEMORY"/Memory.
- **Explanation:** The service filters by a type name and a concrete class
instead of asking each accessory for its type.
- **Consequence:** A new accessory type requires editing this method, and the
type names are repeated in several classes.
- **Proposed solution:** Each accessory exposes its own type (for example an
abstract method in Accessory implemented by Controller, Cable and Memory)
and the service filters by that value (Polymorphism).
- **Other developer's code:** Yes. Developer 1 (Accessory, Controller, Cable,
Memory). Reported to the Technical Leader as a boundary finding.
Correct usage: findAccessoriesCompatibleWith() calls a.isCompatibleWith(id)
on each accessory without checking its type.
#### Promotions (Requirement 2)
Verdict for this module: violation 
##### V-07
- **ID:** V-07
- **Pattern:** Polymorphism
-  **Module:** Promotions (Requirement 2), PromotionRepository
- **Evidence:** PromotionRepository.java
- saveAll(), lines 48, 56 and 65: `p instanceof PercentageDiscount`,
  `CategoryDiscount`, `BulkPurchaseDiscount`, each branch building a
  different CSV line.
- loadAll(), lines 109, 114 and 120: `type.equals("PERCENTAGE")`,
  `"CATEGORY"`, `"BULK"`, each branch creating a different object.
- **Explanation:** The repository chooses the layout and the object by
  checking the concrete type and a type text.
- **Consequence:** A new promotion type requires editing both methods. A type
  missing in saveAll() is written as an empty line.
- **Proposed solution:** A CSV mapper class in the persistence package (Pure
  Fabrication), one converter per promotion type, keeping the exact file
  format and the silent skip of unknown types.
- **Other developer's code:** No.
Correct usage: findBestPromotionFor() works with the Promotion abstraction
and calls calculateDiscount() on each one without checking its type.

## 7. Pure Fabrication
### Person module
Verdict: correct
Where: PersonRepository and PersonValidator keep file access and
validation out of the model classes. PersonService.registerCustomer()
delegates to PersonValidator instead of validating inline.
### Accesory module (requerimiento 1)
Verdict for this module: correct.
AccessoryRepository and AccessoryService are classes that do not exist in the
store's domain; they keep file access and application logic out of the
Accessory hierarchy, which stays free of persistence code.
#### Promotions (Requirement 2)
Verdict for this module: correct.
PromotionRepository and PromotionService keep file access and application
logic out of the Promotion hierarchy.

## 8. Indirection
### Person module
Verdict: violation (see V-01)
There is no abstraction between PersonService and PersonRepository, so
both are directly tied.
### Accesory module (requerimiento 1)
Verdict for this module: violation (see V-05).
No abstraction sits between AccessoryService and AccessoryRepository, so the
service is tied directly to the concrete class.
#### Promotions (Requirement 2)
Verdict for this module: violation (see V-08).
No abstraction sits between PromotionService and PromotionRepository.

## 9. Protected Variations
### Person module
Verdict: violation (see V-01 and V-02)
A change of storage mechanism or a new type of person requires editing
existing classes.
### Accesory module (requerimiento 1)
Verdict for this module: violation (see V-03, V-04 and V-05).
Two foreseeable changes are not protected: a new accessory type requires
editing saveAll(), loadAll() and listAccessoriesByType(), and a different
storage mechanism requires editing AccessoryService.
#### Promotions (Requirement 2)
Verdict for this module: violation (see V-07, V-08 and V-09).
A new promotion type, a new valid category or a different storage mechanism
requires editing existing classes