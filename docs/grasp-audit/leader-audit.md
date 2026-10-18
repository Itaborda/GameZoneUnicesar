# Leader GRASP Audit
## 1. Audit Scope
   Responsible: Technical Leader
   Project: GameZoneUnicesar
   Assigned modules reviewed:
- Sale
- SaleRepository
- SaleService
- ConsoleMenu
- Main
- ProductService.restoreStock
- Integration of accessories, promotions, and warranties in sales and the console menu
## 2. GRASP Patterns Reviewed
- Information Expert
- Creator
- Controller
- Low Coupling
- High Cohesion
- Polymorphism
- Pure Fabrication
- Indirection
- Protected Variations
## 3. Findings
   V-01 — High Cohesion: ConsoleMenu combines all module interfaces
- ID: V-01
- Pattern: High Cohesion
- Module: com.gamezone.ui.ConsoleMenu
- Evidence: ConsoleMenu.java contains 2,684 lines and methods for products, people, sales, returns, promotions, accessories, warranties, input parsing, and exception messages. Examples include showWarrantyMenu() (around line 144), showPromotionMenu() (around line 420), accessoryMenu() (around line 895), showPersonMenu() (around line 1566), showSaleMenu() (around line 1769), and showReturnMenu() (around line 2195).
- Explanation: The class groups user-interface flows for many independent business modules, rather than focusing on a single cohesive interface responsibility.
- Consequence: A change to one module's interaction can affect a very large class; navigation and testing become harder, and unrelated features are more likely to conflict during team integration.
- Proposed Solution: Keep the main menu as the entry point and extract module-specific interaction flows into separate UI classes or menu components, preserving existing menu options, Spanish messages, and behavior.
- Other Member's Code Involved: Yes. The menu integrates products, people, accessories, promotions, returns, and warranties maintained across the team.
  V-02 — Low Coupling / High Cohesion: Sale registration repeats business checks in the UI
- ID: V-02
- Pattern: Low Coupling
- Module: ConsoleMenu.registerSale() and SaleService.registerSale()
- Evidence: ConsoleMenu.registerSale() checks that customers, sellers, and items exist (approximately lines 1825–1839), searches for the selected customer and seller (approximately lines 1859–1897), and checks product/accessory stock before adding items (approximately lines 1942–1954 and 2022–2035). SaleService.registerSale() also validates sale data and resolves items and stock availability (approximately lines 63–79).
- Explanation: Some business-rule checks and object resolution are performed in the UI and repeated in the service layer. This spreads responsibility across layers and makes the UI depend on details that should be enforced consistently by the business layer.
- Consequence: If a stock or existence rule changes, multiple code paths may need updates; the UI and service can drift and produce inconsistent behavior.
- Proposed Solution: Keep prompting, displaying choices, and printing Spanish messages in ConsoleMenu. Centralize authoritative business validation in services, and expose service operations for required lookups or validation so the same rules are not independently implemented in the UI. Preserve user-visible behavior.
- Other Member's Code Involved: Yes. The sale flow calls services for products, people, and accessories.
  V-03 — Polymorphism / Protected Variations: SaleService branches on the concrete item type for inventory handling
- ID: V-03
- Pattern: Polymorphism
- Module: SaleService.registerSale()
- Evidence: The method uses if (product instanceof Accessory) to choose between accessoryService.findById(...) and productService.findById(...) during validation (approximately lines 69–79), and repeats the same type-based split when updating stock (approximately lines 142–158).
- Explanation: Inventory behavior is selected through concrete type checks in more than one place. A new sellable item category or a change to inventory handling would require revisiting this service's conditional logic.
- Consequence: The sale workflow becomes more sensitive to changes in product categories and can accumulate additional branches as the system grows.
- Proposed Solution: Consider introducing a common inventory-handling abstraction or a small strategy/registry that resolves an item to the correct inventory operation. Keep the existing ProductService and AccessoryService implementations behind that abstraction and verify that sales and stock behavior remain unchanged.
- Other Member's Code Involved: Yes. The finding concerns integration with the accessory and product modules.

## 4. Audit Summary
   The review identified three candidate design findings related to cohesion, coupling between the UI and business logic, and concrete-type branching in sale inventory handling. These findings should be confirmed against the full team design and the expected scope of the GRASP refactoring before implementation. No refactoring has been performed as part of this audit.