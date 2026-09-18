package com.gamezone.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic accessory sold in the GameZone store, such as
 * a controller, a cable, or a memory card. Extends {@link Product}
 * to reuse the common attributes and behavior of a sellable product,
 * and adds the notion of compatibility with specific consoles.
 */
public abstract class Accessory extends Product {

    private List<String> compatibleConsoleIds;

    /**
     * Creates a new accessory with the given attributes.
     *
     * @param id                   the product's unique identifier
     * @param title                the product's title
     * @param price                the product's price
     * @param stockQuantity        the available quantity in inventory
     * @param compatibleConsoleIds the ids of the consoles this accessory is compatible with
     */
    public Accessory(String id, String title, double price, int stockQuantity, List<String> compatibleConsoleIds) {
        super(id, title, price, stockQuantity);
        this.compatibleConsoleIds = compatibleConsoleIds;
    }

    /**
     * Returns the ids of the consoles this accessory is compatible with.
     *
     * @return the list of compatible console ids
     */
    public List<String> getCompatibleConsoleIds() {
        return compatibleConsoleIds;
    }

    /**
     * Sets the ids of the consoles this accessory is compatible with.
     *
     * @param compatibleConsoleIds the new list of compatible console ids
     */
    public void setCompatibleConsoleIds(List<String> compatibleConsoleIds) {
        this.compatibleConsoleIds = compatibleConsoleIds;
    }

    /**
     * Adds a console to this accessory's list of compatible consoles,
     * if it is not already present.
     *
     * @param consoleId the id of the console to add
     */
    public void addCompatibleConsole(String consoleId) {
        if (compatibleConsoleIds == null) {
            compatibleConsoleIds = new ArrayList<>();
        }

        if (!compatibleConsoleIds.contains(consoleId)) {
            compatibleConsoleIds.add(consoleId);
        }
    }

    /**
     * Removes a console from this accessory's list of compatible consoles.
     *
     * @param consoleId the id of the console to remove
     */
    public void removeCompatibleConsole(String consoleId) {
        if (compatibleConsoleIds != null) {
            compatibleConsoleIds.remove(consoleId);
        }
    }

    /**
     * Determines whether this accessory is compatible with the given console.
     *
     * @param consoleId the id of the console to check
     * @return true if the console is in this accessory's compatibility list, false otherwise
     */
    public boolean isCompatibleWith(String consoleId) {
        return compatibleConsoleIds != null && compatibleConsoleIds.contains(consoleId);
    }

    /**
     * Returns a description of the accessory's common information:
     * title, price, and the consoles it is compatible with. Concrete
     * subclasses extend this description with their own specific
     * attributes.
     *
     * @return a string describing the accessory's common information
     */
    @Override
    public String getDescription() {
        return "Titulo: " + getTitle() + ", Precio: " + getPrice() + ", Consolas compatibles: " + compatibleConsoleIds;
    }
}
