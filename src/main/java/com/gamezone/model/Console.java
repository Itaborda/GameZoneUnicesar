package com.gamezone.model;

/**
 * Represents a console product sold in the GameZone store.
 * Extends {@link Product} with attributes specific to consoles,
 * such as brand, model, and generation.
 */
public class Console extends Product{
    private String brand;
    private String model;
    private String generation;

    /**
     * Creates a new console with the given attributes.
     *
     * @param id         the product's unique identifier
     * @param title      the product's title
     * @param price      the product's price
     * @param stockQuantity the available quantity in inventory
     * @param brand      the console's brand
     * @param model      the console's model
     * @param generation the console's generation
     */
    public Console(String id, String title, double price, int stockQuantity, String brand, String model, String generation) {
        super(id, title, price, stockQuantity);
        this.brand = brand;
        this.model = model;
        this.generation = generation;
    }

    /**
     * Returns the console's brand.
     *
     * @return the console's brand
     */
    public String getBrand() {
        return brand;
    }


    /**
     * Sets the console's brand.
     *
     * @param brand the new brand for the console
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the console's model.
     *
     * @return the console's model
     */
    public String getModel() {
        return model;
    }


    /**
     * Sets the console's model.
     *
     * @param model the new model for the console
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Returns the console's generation.
     *
     * @return the console's generation
     */
    public String getGeneration() {
        return generation;
    }

    /**
     * Sets the console's generation.
     *
     * @param generation the new generation for the console
     */
    public void setGeneration(String generation) {
        this.generation = generation;
    }

    /**
     * Returns a full description of the console, combining its
     * inherited attributes with its brand, model, and generation.
     *
     * @return a string describing the console
     */
    @Override
    public String getDescription() {
        return "Titulo: " + getTitle() + ", Precio: " + getPrice() + ", Marca: " + brand + ", Modelo: " + model + ", Generacion: " + generation;
    }
}
