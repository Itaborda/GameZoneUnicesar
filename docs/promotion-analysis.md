# Promotion Module Analysis

## 1. What promotion types are required?

The Promotion Module requires three different promotion types:

* **PercentageDiscount:** applies a percentage discount to the total amount of a sale.
* **CategoryDiscount:** applies a percentage discount only to products that belong to a specific category, such as `VIDEOGAME` or `CONSOLE`.
* **BulkPurchaseDiscount:** applies a percentage discount to the total sale when the sale contains at least a specified minimum number of products.

All promotion types must inherit from the abstract `Promotion` class and implement their own discount calculation logic.

## 2. When is a promotion considered active?

A promotion is considered active when the current date is within its configured start and end dates.

The `Promotion` class will provide an `isActive(LocalDate date)` method to determine whether a promotion is currently valid.

A promotion should only be considered for a sale when it is active on the date of the sale.

## 3. How should the system select the best promotion?

When registering a sale, the system must consult all currently active promotions.

For each active promotion, the system calculates the monetary discount that it would provide to the sale. The system then compares the calculated discounts and applies only the promotion that provides the greatest monetary discount.

If no active promotion provides a discount, the sale is registered without a promotion.

This selection logic will be implemented in `PromotionService` through the `findBestPromotionFor(Sale sale)` method.

## 4. How should promotions be persisted?

Promotions will be persisted in the file:

`data/promotions.csv`

The repository will use a discriminator field to identify the promotion type when saving and loading data.

`PromotionRepository` will provide the following main operations:

* `saveAll(List<Promotion>)`
* `loadAll()`

If the promotions file does not exist, the repository must return an empty list instead of generating an error.

The persistence logic will remain inside the persistence layer and will not be placed inside the promotion model classes.

## 5. How will promotions be integrated with sales?

The promotion module will be integrated into the existing sales process.

After a sale is created and its subtotal is calculated, `SaleService.registerSale()` will ask `PromotionService` to find the best applicable promotion.

If a promotion is found, the system will calculate its discount and store:

* The promotion name.
* The discount amount.

The final sale total will then be calculated as:

`Final Total = Subtotal - Discount`

The generated receipt must display the subtotal, the applied promotion and discount, and the final total.

The integration must preserve the existing sales, product, and accessory functionality while adding the new promotion behavior.
