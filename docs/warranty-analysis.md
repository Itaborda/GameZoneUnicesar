# Warranty Module Analysis

## 1. Warranty hierarchy

The `Warranty` class should be an abstract class because both basic and extended warranties share common attributes and behavior. The common attributes are `id`, `product`, `sale`, `startDate`, and `endDate`.

`BasicWarranty` and `ExtendedWarranty` inherit from `Warranty`, but they have different business rules. The basic warranty lasts 6 months and has no additional cost, while the extended warranty lasts 12 months and has an additional cost of 10% of the product price.

The abstract methods `getDurationInMonths()`, `getWarrantyType()`, and `getAdditionalCost()` allow each subclass to implement its own behavior. This uses polymorphism and avoids duplicating the common warranty logic in both subclasses.

## 2. Automatic basic warranty for consoles

The basic warranty must be assigned automatically only when the product being sold is a console. Video games do not receive an automatic basic warranty.

The runtime type of the product can be verified using the Java `instanceof` operator. This verification should take place during the sale registration process because `SaleService` is responsible for processing the products included in a sale.

When the product is an instance of `Console`, the sale process should call `WarrantyService.assignBasicWarranty(...)`. The responsibility for creating and managing the warranty belongs to the service layer, while the menu should only collect information from the user.

## 3. Warranty duration calculation

The warranty start date is the sale date, and the end date must be calculated automatically.

The `Warranty` constructor receives the start date and calculates the end date using the duration provided by the concrete warranty type. The method `getDurationInMonths()` is abstract so that each subclass can define its own duration.

`BasicWarranty` returns 6 months, while `ExtendedWarranty` returns 12 months. This design keeps the common date calculation in the parent class while allowing each subclass to define its specific duration.

Using the constructor for this calculation ensures that a warranty is created with a valid end date and avoids requiring another step to initialize this value.

## 4. Extended warranty cost

The extended warranty is optional and can only be selected when a console is included in a sale. Its additional cost is 10% of the associated product price.

The calculation of this additional cost should be handled by `ExtendedWarranty` through the `getAdditionalCost()` method. During `SaleService.registerSale`, if the product was selected for an extended warranty, the service should call `WarrantyService.assignExtendedWarranty(...)`.

The returned warranty provides the additional cost, which must then be added to the total amount of the sale. Therefore, the sale service integrates the warranty cost into the final sale total.

The basic warranty returns an additional cost of `0.0`, so it does not modify the sale total.

## 5. Expiring soon warranties

The functionality for finding warranties that are expiring soon should be implemented in `WarrantyService`.

The service obtains the warranties from `WarrantyRepository`, iterates through the list, and compares each warranty's `endDate` with the current date. The `daysAhead` parameter determines the number of days to consider when searching for warranties that will expire soon.

The repository is responsible for persistence, such as loading and saving warranties from `data/warranties.csv`. The service is responsible for applying the business rules and filtering the warranties. The `ConsoleMenu` should only request the number of days from the user and display the results.

This separation follows the layered architecture because each layer has a specific responsibility.

## Design consideration

The requirement creates a possible ambiguity because every console receives an automatic basic warranty and the user can additionally request an extended warranty for the same console in the same sale.

However, the method `findWarrantyByProduct(String productId, String saleId)` is specified to return a single `Warranty`. Therefore, the team should define how this query behaves when both a basic and an extended warranty exist for the same product and sale.

This decision should be documented before implementing the query functionality to avoid inconsistent behavior between the service and the menu.
