# Return Module Analysis

## 1. What is the relationship between Return and Sale?

The relationship between Return and Sale is an association. A Return references the original Sale, but it is not a type or component of the Sale. This relationship is represented by the `originalSale` attribute.

## 2. How are partial product returns represented?

* Partial returns are represented by the `returnedProducts` list. It contains only the products that the customer wants to return, allowing the system to return one or more products without modifying the original Sale.

## 3. Where should the thirty-day return rule be enforced?

* The thirty-day rule should be enforced in the service layer. `ReturnService` uses `Sale.canBeReturned()` to check the eligibility of the sale, and `ChronoUnit.DAYS.between()` can be used to calculate the number of calendar days.

## 4. How should stock be restored after a return?

* Stock should be restored using `ProductService.restoreStock(String productId, int quantity)`. `ReturnService` calls this method after all return validations have been successfully completed.

## 5. Where should the monthly balance report be generated?

* The monthly balance report should be generated in `ReturnService` because it is business logic. The net balance is calculated as total monthly sales minus total monthly returns.