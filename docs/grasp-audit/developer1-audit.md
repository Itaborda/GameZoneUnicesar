
# Developer 1 — GRASP Audit

## 1. Audit Scope

This report covers the responsibilities assigned to Developer 1 for Requirement 7, Phase 1.

### 1.1 Products — Workshop 1

Classes and methods included:
- `Product`
- `VideoGame`
- `Console`
- `ProductRepository`
- `ProductService`, excluding `restoreStock()`, which is assigned to the Technical Leader.

### 1.2 Accessories — Requirement 1

Classes included:
- `Accessory`
- `Controller`
- `Cable`
- `Memory`

### 1.3 Promotions — Requirement 2

Classes included:
- `Promotion`
- `PercentageDiscount`
- `CategoryDiscount`
- `BulkPurchaseDiscount`

### 1.4 Returns — Requirement 3

Classes and methods included:
- `Return`
- `Sale.canBeReturned()`

The audit of `Sale` is limited to `canBeReturned()`.

### 1.5 Warranties — Requirement 4

Classes included:
- `Warranty`
- `BasicWarranty`
- `ExtendedWarranty`

### 1.6 Exceptions and Validation — Requirement 6

The audit covers the assigned `exceptions` and `validation` packages, including the responsibilities of exception classes, validation rules, and their use by the assigned modules.

## 2. Audit Criteria

All nine GRASP patterns will be evaluated:

1. Information Expert
2. Creator
3. Controller
4. Low Coupling
5. High Cohesion
6. Polymorphism
7. Pure Fabrication
8. Indirection
9. Protected Variations

Each pattern must receive one verdict: `Correct`, `Violation`, or `Not Applicable`.

Each verdict must include code-based evidence and an explanation. A violation must additionally identify the affected module, design problem, consequences, proposed solution, and whether coordination with another developer is required.

Finding IDs in this report are provisional until the Technical Leader consolidates the team's findings.

## 3. Phase 1 Restrictions

This phase documents design findings. No Java source code will be modified during the audit. Proposed solutions must preserve existing observable behavior and be considered during the subsequent refactoring phase.

## 4. Products Module Audit — Workshop 1

### 4.1 Information Expert

**Verdict: Correct**

The `Product` class contains the common product data, while `VideoGame` and `Console` contain subtype-specific information. This assigns product-related data and behavior to the classes that have the relevant information.

### 4.2 Creator

**Verdict: Correct**

`ProductRepository.findAll()` reconstructs `VideoGame` and `Console` objects from the stored product records. The repository reads the persisted data and uses it to create the corresponding product objects.

### 4.3 Controller

**Verdict: Not Applicable**

The assigned product classes and `ProductService` are not the application's main user-interface event entry point. The system-level Controller responsibility must be evaluated alongside the application entry point and menu, which are outside this developer's assigned scope.

### 4.4 Low Coupling

**Verdict: Violation — V-01**

**Evidence:** `ProductService` directly creates a concrete `ProductRepository` and uses the hard-coded path `data/product.csv`.

**Explanation:** The service is coupled to a specific repository implementation and storage location instead of receiving these dependencies from outside.

**Consequence:** Changing the persistence implementation or configuring a different data location requires changes to the service.

**Proposed solution:** Inject the repository through the service constructor and centralize or configure the file path. Preserve existing behavior and coordinate any interface or constructor changes with the responsible team member.

**Requires another developer:** Potentially, if other classes instantiate `ProductService` or depend on its current constructor.

### 4.5 High Cohesion

**Verdict: Correct**

`ProductService` handles product-related operations, while `ProductRepository` handles product persistence. These responsibilities are separated into service and repository classes rather than being concentrated in one class.

### 4.6 Polymorphism

**Verdict: Violation — V-02**

**Evidence:** `ProductRepository.saveAll()` distinguishes `VideoGame` and `Console` using concrete type checks and casts before writing their subtype-specific fields.

**Explanation:** Persistence logic depends on explicit knowledge of the concrete product subclasses rather than a polymorphic serialization contract.

**Consequence:** Adding another product subtype may require additional conditional branches in the repository.

**Proposed solution:** Consider a polymorphic serialization or mapping design that preserves the existing stored format and supports the current product types.

**Requires another developer:** Coordinate changes to the product hierarchy or persisted-data format with the developers responsible for those contracts.

### 4.7 Pure Fabrication

**Verdict: Correct**

`ProductRepository` separates file persistence from the domain model, and `ProductValidator` centralizes product validation rules. These responsibilities are assigned to supporting classes instead of being placed entirely in the product entities.

### 4.8 Indirection

**Verdict: Correct**

`ProductService` accesses persisted products through `ProductRepository` rather than performing file operations directly. The repository provides an intermediary between the service and the file storage.

### 4.9 Protected Variations

**Verdict: Violation — V-03**

**Evidence:** `ProductRepository.saveAll()` builds records using explicit field concatenation, while `findAll()` parses records with `split(",")`, fixed field positions, and the type markers `VIDEOGAME` and `CONSOLE`.

**Explanation:** The persistence implementation depends directly on the current CSV layout and type markers.

**Consequence:** Changes to the record format can affect both writing and reading, and may make existing data incompatible.

**Proposed solution:** Encapsulate the CSV mapping and parsing rules so format-related changes are handled in a clearly defined place. Preserve compatibility with existing data.

**Requires another developer:** Coordinate with anyone who changes product fields or relies on the current persisted-data format.

**Consolidation note:** V-02 and V-03 are related but describe different design concerns. The Technical Leader should determine whether they remain separate findings or are consolidated into one finding to avoid duplicate remediation.
