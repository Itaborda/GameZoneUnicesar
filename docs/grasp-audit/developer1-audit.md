
# Developer 1 — GRASP Audit

## 1. Audit Scope

**Project:** GameZoneUnicesar  
**Role:** Developer 1 — Requirement 7, Phase 1.

The audit covers the following assigned classes and packages:

- **Products:** `Product`, `VideoGame`, `Console`, `ProductRepository`, and `ProductService`, excluding `restoreStock()`.
- **Accessories:** `Accessory`, `Controller`, `Cable`, and `Memory`.
- **Promotions:** `Promotion`, `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount`.
- **Returns:** `Return` and only `Sale.canBeReturned()`.
- **Warranties:** `Warranty`, `BasicWarranty`, and `ExtendedWarranty`.
- **Supporting classes:** `exceptions` and `validation` packages.

`ProductService.restoreStock()`, the remaining `Sale` methods, `SaleRepository`, `SaleService`, `ConsoleMenu`, and `Main` belong to the Technical Leader's audit. Findings are provisional until the Technical Leader consolidates the team's reports. No Java source code changes were made during Phase 1.

## 2. GRASP Pattern Assessment

### 2.1 Information Expert — Correct

Domain classes generally use the data they own. `Accessory.isCompatibleWith()` checks compatibility using compatible-console IDs; `Promotion.isActive()` evaluates the promotion dates; promotion subclasses calculate their own discounts; `Return` calculates refunds; and `Warranty` determines expiration using its start date and duration. `ProductValidator` and `SaleValidator` centralize reusable validation checks.

### 2.2 Creator — Correct

`ProductRepository.findAll()` reconstructs `VideoGame` and `Console` objects from persisted records. The repository reads the data required to recreate these objects. Concrete-type checks involved in this process are discussed under Polymorphism.

### 2.3 Controller — Not Applicable

The assigned scope focuses mainly on domain classes, repositories, and validators rather than the system-event entry point. `Main` and `ConsoleMenu` are reviewed by the Technical Leader. This verdict does not mean that the entire application lacks a Controller.

### 2.4 Low Coupling — Violation (V-01)

**Module:** Products.

**Evidence:** `ProductService` directly creates `new ProductRepository("data/product.csv")` and calls `findAll()` and `saveAll()`.

**Explanation and consequence:** The service depends on a concrete persistence implementation and hard-codes the file path. Changing the persistence configuration or testing the service independently becomes harder.

**Proposed solution:** Inject `ProductRepository` through the service constructor, preserving the current default path and behavior. Coordinate any required caller changes with the Technical Leader.

### 2.5 High Cohesion — Correct

The reviewed classes generally have focused responsibilities. `ProductRepository` manages product persistence, validators handle reusable checks, domain classes maintain related data and behavior, and exception subclasses represent different error categories.

### 2.6 Polymorphism — Violation (V-02)

**Modules:** Products and Promotions.

**Evidence:** `ProductRepository.saveAll()` uses `instanceof VideoGame` and `instanceof Console` with casts to access subtype fields. `findAll()` checks literal record markers. `CategoryDiscount.belongsToTargetCategory(Product)` also branches on `instanceof VideoGame`, `Console`, and `Accessory`.

**Explanation and consequence:** Some subtype-specific behavior depends on concrete-type checks rather than a shared polymorphic operation. Adding a new product type may require changes in several conditional branches.

**Proposed solution:** Evaluate a product-record mapper or serializer for persistence and a category-resolution abstraction for promotions. Preserve the current file format and discount behavior. The Technical Leader should consolidate overlapping findings if they share the same root cause.

### 2.7 Pure Fabrication — Correct

`ProductRepository` separates file persistence from domain entities. `ProductValidator` and `SaleValidator` centralize validation, while the exception hierarchy provides reusable error types. These supporting classes handle technical responsibilities outside the domain entities.

### 2.8 Indirection — Correct

Product services access file persistence through a repository, and validators centralize reusable checks. The direct construction of `ProductRepository` is documented separately in V-01; it does not eliminate the repository's intermediary role.

### 2.9 Protected Variations — Violation (V-03)

**Module:** Products.

**Evidence:** `ProductRepository.findAll()` parses records using `line.split(",")`, fixed indexes such as `data[1]` through `data[7]`, and literal type markers. `saveAll()` builds CSV records through string concatenation.

**Explanation and consequence:** Assumptions about the storage format are embedded in repository methods. A field containing a comma or a future format change could break record reconstruction.

**Proposed solution:** Centralize record encoding and decoding while preserving the current file representation. Review escaping, malformed-row handling, and migration behavior separately. V-02 and V-03 may overlap and should be consolidated if they share one root cause.

## 3. Additional Module Assessments

### 3.1 Accessories

`Accessory` owns compatible-console IDs and implements `isCompatibleWith()`, supporting Information Expert. `Controller`, `Cable`, and `Memory` override `getDescription()`, demonstrating Polymorphism. Their shared accessory state and focused description behavior support High Cohesion.

Creator, Controller, Pure Fabrication, and Indirection cannot be established from the domain subclasses alone; their applicability must be judged using the relevant application and supporting classes.

### 3.2 Promotions

`Promotion` evaluates its active date range through `isActive()`. `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount` implement their discount calculations, supporting Information Expert and Polymorphism.

However, `CategoryDiscount.belongsToTargetCategory(Product)` uses concrete-type checks for `VideoGame`, `Console`, and `Accessory`. This issue is included in V-02. The promotion classes otherwise have focused responsibilities. Creator, Controller, Pure Fabrication, and Indirection require evidence from their callers or supporting classes before receiving a definitive verdict.

### 3.3 Returns

`Return` calculates refund amounts and generates return receipts using return-related information. In the assigned method `Sale.canBeReturned()`, the date is checked against the return window and invalid date parsing returns `false`. The rest of `Sale` is outside this audit's scope.