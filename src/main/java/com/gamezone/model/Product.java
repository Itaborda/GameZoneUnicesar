package com.gamezone.model;

/**
 *  Represents a generic product sold in the GameZone store.
 *  This is an abstract base class; concrete subclasses must
 *  implement their own description logic.
 */
public abstract class Product {
    private String id;
    private String title;
    private double price;
    private int stockQuantity;

    /**
     * Creates a new product with the given attributes.
     *
     * @param id            the product's unique identifier
     * @param title         the product's title
     * @param price         the product's price
     * @param stockQuantity the available quantity in inventory
     */
    public Product(String id, String title, double price, int stockQuantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    /**
     * Returns the product's unique identifier.
     *
     * @return the product's id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the product's unique identifier.
     *
     * @param id the new id for the product
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the product's title.
     *
     * @return the product's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the product's title.
     *
     * @param title the new title for the product
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the product's price.
     *
     * @return the product's price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the product's price.
     *
     * @param price the new price for the product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the available quantity of this product in inventory.
     *
     * @return the current stock quantity
     */
    public int getStockQuantity() {
        return stockQuantity;
    }

    /**
     * Sets the available quantity of this product in inventory.
     *
     * @param stockQuantity the new stock quantity
     */
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Returns a full description of the product, integrating its
     * particular characteristics. Each subclass must provide its
     * own implementation.
     *
     * @return a string describing the product
     */
    public abstract String getDescription();
}
