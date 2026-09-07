package com.gamezone.model;

public abstract class Product {
    private String id;
    private String title;
    private double price;
    private int stockQuantity;

    public Product(String id, double price, String title, int stockQuantity) {
        this.id = id;
        this.price = price;
        this.title = title;
        this.stockQuantity = stockQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public abstract String getDescription();
}
