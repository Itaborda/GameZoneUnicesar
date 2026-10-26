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
- PromotionService: constructor, registerPercentageDiscount(), registerCategoryDiscount(), registerBulkPurchaseDiscount(), listAllPromotions(), listActivePromotions(), findBestPromotionFor(), findById()
### Returns module (Requirement 3)
- ReturnRepository: constructor, saveAll(), loadAll()
- ReturnService: constructor, registerReturn(), viewAllReturns(),
viewReturnsByCustomer(), viewReturnsBySale(), generateMonthlyBalance(),
calculateMonthlySales(), calculateMonthlyReturns()
### Warranties module (Requirement 4)
- WarrantyRepository: constructor, saveAll(), loadAll()
- WarrantyService: constructor, assignBasicWarranty(), assignExtendedWarranty(), findWarrantyByProduct(), listAllWarranties(), 
- listActiveWarranties(), listWarrantiesExpiringSoon(), cancelWarranties(),
persist(), and the private helpers resolveAll(), generateId() and
findSaleById()
### Exceptions and validations module (Requirement 6)
- Use of exceptions and validators in PersonService, PersonRepository, AccessoryService, AccessoryRepository, PromotionService,
PromotionRepository, ReturnService, ReturnRepository, WarrantyService and
WarrantyRepository.

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
#### Returns (Requirement 3)
Verdict for this module: violation.
##### V-11
- **ID:** V-11
- **Pattern:** Information Expert
- **Module:** Returns (Requirement 3) and Warranties (Requirement 4)
- **Evidence:** the same lookup of a sale by id is repeated in
ReturnRepository.loadAll() (lines 122-128), ReturnService.registerReturn()
(lines 66-72) and WarrantyService.findSaleById() (lines 215-222, used at
line 157).
- **Explanation:** The sales are owned by the sales module (SaleService and
SaleRepository), but three other classes implement their own search.
- **Consequence:** A change in how a sale is identified requires editing three
classes.- **Proposed solution:** Add findById(saleId) to SaleService (and to
SaleRepository, which WarrantyService uses to avoid a cycle) and make the
three classes call it.
- **Other developer's code:** Yes. Technical Leader (SaleService and
SaleRepository). It is a boundary finding.
Three classes repeat the lookup of a sale by its id, although the sales are
owned by the sales module. Correct usage: registerReturn() asks
sale.canBeReturned() and newReturn.calculateRefundAmount() instead of
computing those rules itself
#### Warranties (Requirement 4)
Verdict for this module: violation (see V-11).
WarrantyService.findSaleById() repeats the sale lookup owned by the sales
module. Correct usage: findWarrantyByProduct(), listActiveWarranties() and
cancelWarranties() ask each warranty (isActive, getEndDate,
getAdditionalCost) instead of computing those values themselves
#### Exceptions and validations (Requirement 6)
Verdict for this module: not applicable
the use of exceptions and validators does not create objects or select behavior by
type
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
#### Returns (Requirement 3)
Verdict for this module: correct.
ReturnService creates the Return objects (registerReturn(), line 100) because
it records them in its list and has the data to initialize them. In the same
way, ReturnRepository.loadAll() creates Return objects from the file data.
#### Warranties (Requirement 4)
Verdict for this module: correct.
WarrantyService creates BasicWarranty and ExtendedWarranty objects
(assignBasicWarranty(), assignExtendedWarranty(), resolveAll()) because it
records them in its list and has the data needed to initialize them.
#### Exceptions and validations (Requirement 6)
Verdict for this module: not applicable
the use of exceptions and validators does not create objects or select behavior by
type

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
#### Returns (Requirement 3)
Verdict for this module: not applicable.
These classes do not receive system events; ConsoleMenu does.
#### Warranties (Requirement 4)
Verdict for this module: not applicable.
These classes do not receive system events; ConsoleMenu does.
#### Exceptions and validations (Requirement 6)
Verdict for this module: not applicable
the use of exceptions and validators does not create objects or select behavior by
type

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
#### Returns (Requirement 3)
Verdict for this module: violation.
##### V-10
- **ID:** V-10
- **Pattern:** Low Coupling
- **Module:** Returns (Requirement 3), ReturnRepository
- **Evidence:** ReturnRepository.java, imports at lines 6-8 (AccessoryService,
ProductService, SaleService), fields at lines 26-28, constructor at lines
37-46, and loadAll() lines 123-137, which calls saleService.findAll(),
productService.findById() and accessoryService.findById().
- **Explanation:** A persistence class depends on three service classes. This
goes against the dependency direction ui -> service -> persistence -> model
and creates a cycle: ReturnService -> ReturnRepository -> the services.
- **Consequence:** The repository cannot be used or tested without three
services, and changes in those services ripple into persistence.
- **Proposed solution:** ReturnRepository returns raw rows (ids as text), as
WarrantyRepository already does, and ReturnService resolves the sale and
the products in the service layer (Low Coupling, Indirection).
- **Other developer's code:** Yes. Technical Leader (Main creates
ReturnRepository with the three services and calls loadAll()).
ReturnRepository (persistence layer) depends on three services, and
ReturnService keeps a dependency it never uses.
##### V-14
- **ID:** V-14
- **Pattern:** Low Coupling
- **Module:** Returns (Requirement 3), ReturnService
- **Evidence:** ReturnService.java, field at line 19, import at line 5 and
assignment at line 37 of SaleRepository, which no method of the class uses.
- **Explanation:** The class receives and stores a dependency it never uses.
- **Consequence:** An unnecessary dependency on another module's repository
and a longer constructor.
- **Proposed solution:** Remove the unused dependency from the class and from
its constructor.
- **Other developer's code:** Yes. Technical Leader (Main passes
saleRepository to the constructor).

#### Warranties (Requirement 4)
Verdict for this module: correct.
WarrantyRepository handles only raw rows of text and depends on no service
or other repository, as its own documentation states. WarrantyService
depends on ProductService and SaleRepository, which are in lower or
equal layers, and does not create a dependency cycle with SaleService
#### Exceptions and validations (Requirement 6)
Verdict for this module: not applicable
the use of exceptions and validators does not create objects or select behavior by
type

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
#### Returns (Requirement 3)
Verdict for this module: violation (see V-13, full table below).
ReturnService mixes the management of returns with the monthly sales
reporting
##### V-13- **ID:** V-13
- **Pattern:** High Cohesion
- **Module:** Returns (Requirement 3), ReturnService (monthly balance)
- **Evidence:** ReturnService.java, lines 176-213: generateMonthlyBalance(),
calculateMonthlySales() (iterates saleService.findAll(), parses each sale
date and sums its total) and calculateMonthlyReturns().
- **Explanation:** The class combines the management of returns with a
financial report that depends mostly on sales data.
- **Consequence:** Any change in the report touches the returns service, and
the class keeps a dependency on SaleService partly for reporting.
- **Proposed solution:** Move the monthly balance to a separate class (Pure
Fabrication) that combines sales and returns, or move the monthly sales
total to SaleService (Information Expert). ReturnService can keep the three
public methods as thin delegates so ConsoleMenu does not change.
- **Other developer's code:** No if the delegating methods are kept. Yes
  (Technical Leader, ConsoleMenu lines 2558-2564) if they are removed.
#### Warranties (Requirement 4)
Verdict for this module: correct.
WarrantyService is focused on warranties (assign, search, list, cancel). The
conversion between warranties and file rows is kept in private helpers; the
type checks inside those helpers are covered by V-15.
#### Exceptions and validations (Requirement 6)
Verdict for this module: violation .
##### V-16
- **ID:** V-16
- **Pattern:** High Cohesion
- **Module:** Exceptions and validations (Requirement 6)
- **Evidence:** inline validations that throw IllegalArgumentException:
AccessoryService.updateStock() and restoreStock() (lines 121-131 and
150-156), PromotionService.registerCategoryDiscount() (lines 57-66) and
ReturnService.registerReturn() (lines 73-78 and 90-92).
- **Explanation:** These services validate inline and throw a generic
exception, while the services refactored in Requirement 6 delegate to the
validators and throw typed exceptions.
- **Consequence:** Validation rules are scattered across the services and
error handling is inconsistent; ConsoleMenu needs a generic fallback catch.
- **Proposed solution:** Move these checks to validators and throw the typed
exceptions.
- **Other developer's code:** Yes. Developer 1 (validators package) and
Technical Leader (ConsoleMenu catch blocks).

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
#### Returns (Requirement 3)
Verdict for this module: violation 
##### V-12- **ID:** V-12
- **Pattern:** Polymorphism
- **Module:** Returns (Requirement 3), ReturnService
- **Evidence:** ReturnService.java, registerReturn(): line 105
`product instanceof Console` (warranty cancellation) and lines 111-116
`product instanceof Accessory` to choose between
accessoryService.restoreStock() and productService.restoreStock().
- **Explanation:** The method picks the collaborator by checking the concrete
type of each product.
- **Consequence:** A new product category with its own stock service requires
editing this method (and the same structure in SaleService).
- **Proposed solution:** A common stock-handling abstraction implemented by
ProductService and AccessoryService, so the returns service does not
decide by type.
- **Other developer's code:** Yes. Technical Leader (ProductService.restoreStock
and SaleService) and Developer 1 (Product and Accessory hierarchies).

#### Warranties (Requirement 4)
Verdict for this module: violation.
##### V-15
- **ID:** V-15
- **Pattern:** Polymorphism
- **Module:** Warranties (Requirement 4), WarrantyService
- **Evidence:** WarrantyService.java
- persist(), lines 186-192: `w instanceof BasicWarranty` -> "BASIC" and
  `w instanceof ExtendedWarranty` -> "EXTENDED".
- resolveAll(), lines 162-166: `type.equals("BASIC")` and
  `type.equals("EXTENDED")`, each creating a different object.
- **Explanation:** The service converts between objects and rows by checking
  the concrete type and a type text, and it knows the row layout.
- **Consequence:** A new warranty type requires editing persist() and
  resolveAll(). A row layout change also changes the service.
- **Proposed solution:** A mapper class (Pure Fabrication) in the service
  package with one converter per warranty type, used by persist() and
  resolveAll(); the file format must stay identical.
- **Other developer's code:** No.
Correct usage: cancelWarranties() uses w.getAdditionalCost() and
listActiveWarranties() uses w.isActive() without checking the warranty type.
#### Exceptions and validations (Requirement 6)
Verdict for this module: not applicable
the use of exceptions and validators does not create objects or select behavior by
type

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
#### Returns (Requirement 3)
Verdict for this module: correct.
ReturnRepository and ReturnService are classes outside the store domain that
keep persistence and application logic out of Return. The dependency problem
of ReturnRepository is covered by V-10 under Low Coupling.
#### Warranties (Requirement 4)
Verdict for this module: correct.
WarrantyRepository and WarrantyService keep file access and conversion
logic out of the Warranty hierarchy.
#### Exceptions and validations (Requirement 6)
Verdict for this module: correct.
PersonService.registerCustomer() delegates the checks to PersonValidator
instead of validating inline, keeping validation outside the service and the
model.

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
#### Returns (Requirement 3)
Verdict for this module: violation (see V-10).
ReturnRepository calls three services directly instead of working through an
abstraction. By contrast, WarrantyRepository returns raw rows and lets the
service resolve the references.
#### Warranties (Requirement 4)
Verdict for this module: correct.
The raw rows (type, id, product id, sale id, dates) work as an intermediary
between WarrantyRepository and the domain: the repository does not know the
Product or Sale classes, and WarrantyService resolves the references.
#### Exceptions and validations (Requirement 6)
Verdict for this module: correct.
The repositories refactored in Requirement 6 translate IOException into
PersistenceException, so the upper layers do not depend on java.io.

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
#### Returns (Requirement 3)
Verdict for this module: violation (see V-10 and V-12).
A change in any of the three services used by ReturnRepository, or a new
product category with its own stock service, forces edits in the returns
classes.
#### Warranties (Requirement 4)
Verdict for this module: violation (see V-15).
A new warranty type requires editing persist() and resolveAll(). Correct
point: the repository is isolated from changes in the domain because it only
handles raw rows.