# GameZoneUnicesar

Taller 2 - GameZone Unicesar - Programación III UPC

## Description

GameZone Unicesar is a console-based system for managing products, accessories, customers, sellers, and sales in a video game store.

The system follows a four-layer architecture:

* Model
* Persistence
* Service
* UI

The project uses this structure to separate responsibilities and make the system easier to maintain and extend.

## Accessory Management

The system includes accessory management for video game products.

The supported accessory types are:

* Controllers
* Cables
* Memories

Each accessory contains common product information such as identifier, title, price, and available stock.

The system also supports:

* Registering controllers, cables, and memories.
* Listing all accessories.
* Listing accessories by type.
* Querying accessories compatible with a specific console.
* Managing accessory inventory.
* Including accessories in sales together with consoles and video games.

## Compatibility

Controllers and compatible memories can be associated with consoles available in the inventory. The system allows users to consult the accessories compatible with a specific console before making a sale.

## Sales Integration

The sale registration process integrates products and accessories into a single flow.

When a sale is registered, the system:

1. Verifies that the sale contains at least one item.
2. Checks that each product or accessory exists and has available stock.
3. Calculates the sale subtotal.
4. Searches for the best available promotion and applies the corresponding discount.
5. Assigns a basic warranty automatically to consoles.
6. Adds an extended warranty when requested by the customer.
7. Updates the inventory according to the type of item sold.
8. Saves the sale after the required validations and operations are completed.

Promotions are calculated using the sale subtotal, without including the additional cost of an extended warranty.

## Promotions

The system supports different promotion types that can be applied during the sale process.

The sale uses the best applicable promotion and records the promotion name and discount amount.

The final sale value is calculated as:

**Final Total = Subtotal - Discount + Extended Warranty Cost**

## Warranties

Consoles receive a basic warranty automatically when they are included in a sale.

Customers can also request an extended warranty for individual consoles. When an extended warranty is selected, its additional cost is added to the final value of the sale.

The warranty information is persisted together with the corresponding sale process.

## Inventory Management

Inventory is updated according to the type of item included in the sale.

* Products are managed through `ProductService`.
* Accessories are managed through `AccessoryService`.

Before updating the inventory, the system verifies that the requested item exists and has available stock.

## Sale Receipt

The sale receipt provides information about the different values involved in the transaction, including:

* Subtotal.
* Applied promotion.
* Discount amount.
* Extended warranty cost.
* Final total.

This allows the customer to see how the final value of the sale was calculated.

## Project Integration

The main goal of the integration is to allow accessories, promotions, returns, and warranties to work together with the existing product and sales modules.

The business logic is concentrated in the service layer, while the UI is responsible for interacting with the user and the persistence layer is responsible for storing the information.

This structure helps keep the different modules connected without mixing their responsibilities.
