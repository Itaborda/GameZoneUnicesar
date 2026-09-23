# GameZoneUnicesar

Taller 2 - GameZone Unicesar - Programación III UPC

## Description

GameZone Unicesar is a console-based system for managing products, customers, sellers, and sales in a video game store.

The system follows a four-layer architecture:

* Model
* Persistence
* Service
* UI

## Accessory Management

The system now includes accessory management for video game products.

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

Accessories can be included in the same sale as existing products. Stock validation and inventory updates are applied according to the type of item being sold.
