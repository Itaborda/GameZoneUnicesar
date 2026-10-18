
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
