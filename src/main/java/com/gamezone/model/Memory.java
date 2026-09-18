package com.gamezone.model;

import java.util.List;

/**
 * Represents a memory accessory sold in the GameZone store, such as
 * an SD card, a microSD card, or an internal storage card.
 */
public class Memory extends Accessory {

    private int capacityInGigabytes;
    private String memoryType;

    /**
     * Creates a new memory accessory with the given attributes.
     *
     * @param id                   the product's unique identifier
     * @param title                the product's title
     * @param price                the product's price
     * @param stockQuantity        the available quantity in inventory
     * @param compatibleConsoleIds the ids of the consoles this memory is compatible with
     * @param capacityInGigabytes  the memory's storage capacity, in gigabytes
     * @param memoryType           the memory's type (e.g. "SD", "MICRO_SD", "INTERNAL")
     */
    public Memory(String id, String title, double price, int stockQuantity, List<String> compatibleConsoleIds, int capacityInGigabytes, String memoryType) {
        super(id, title, price, stockQuantity, compatibleConsoleIds);
        this.capacityInGigabytes = capacityInGigabytes;
        this.memoryType = memoryType;
    }

    /**
     * Returns the memory's storage capacity, in gigabytes.
     *
     * @return the capacity in gigabytes
     */
    public int getCapacityInGigabytes() {
        return capacityInGigabytes;
    }

    /**
     * Sets the memory's storage capacity, in gigabytes.
     *
     * @param capacityInGigabytes the new capacity in gigabytes
     */
    public void setCapacityInGigabytes(int capacityInGigabytes) {
        this.capacityInGigabytes = capacityInGigabytes;
    }

    /**
     * Returns the memory's type.
     *
     * @return the memory type
     */
    public String getMemoryType() {
        return memoryType;
    }

    /**
     * Sets the memory's type.
     *
     * @param memoryType the new memory type
     */
    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    /**
     * Returns a full description of the memory accessory, combining
     * the accessory's common information with its capacity and type.
     *
     * @return a string describing the memory accessory
     */
    @Override
    public String getDescription() {
        return super.getDescription() + ", Capacidad: " + capacityInGigabytes + "GB, Tipo: " + memoryType;
    }
}
