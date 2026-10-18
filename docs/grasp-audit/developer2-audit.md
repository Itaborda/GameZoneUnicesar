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

## 3. Controller
### Person module
Verdict: not applicable
Why: the audited classes do not receive system events. That responsibility
belongs to ConsoleMenu, which is audited by the Technical Leader.
### Accesory module (requerimiento 1)
Verdict for this module: not applicable.
These classes do not receive system events; the menu operations are handled
by ConsoleMenu, audited by the Technical Leader.

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
Verdict for this module: violation (see V-05, full table below).
AccessoryService depends on the concrete AccessoryRepository class, both in
the field and in the constructor parameter

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

## 8. Indirection
### Person module
Verdict: violation (see V-01)
There is no abstraction between PersonService and PersonRepository, so
both are directly tied.
### Accesory module (requerimiento 1)
Verdict for this module: violation (see V-05).
No abstraction sits between AccessoryService and AccessoryRepository, so the
service is tied directly to the concrete class.
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