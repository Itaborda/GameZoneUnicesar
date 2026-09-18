package com.gamezone.model;

import java.util.List;

/**
 * Represents a cable accessory sold in the GameZone store.
 */
public class Cable extends Accessory {

    private double lengthInMeters;
    private String connectorType;

    /**
     * Creates a new cable with the given attributes.
     *
     * @param id                   the product's unique identifier
     * @param title                the product's title
     * @param price                the product's price
     * @param stockQuantity        the available quantity in inventory
     * @param compatibleConsoleIds the ids of the consoles this cable is compatible with
     * @param lengthInMeters       the cable's length, in meters
     * @param connectorType        the cable's connector type (e.g. "HDMI", "USB", "OPTICAL")
     */
    public Cable(String id, String title, double price, int stockQuantity, List<String> compatibleConsoleIds, double lengthInMeters, String connectorType) {
        super(id, title, price, stockQuantity, compatibleConsoleIds);
        this.lengthInMeters = lengthInMeters;
        this.connectorType = connectorType;
    }

    /**
     * Returns the cable's length, in meters.
     *
     * @return the cable's length in meters
     */
    public double getLengthInMeters() {
        return lengthInMeters;
    }

    /**
     * Sets the cable's length, in meters.
     *
     * @param lengthInMeters the new length in meters
     */
    public void setLengthInMeters(double lengthInMeters) {
        this.lengthInMeters = lengthInMeters;
    }

    /**
     * Returns the cable's connector type.
     *
     * @return the connector type
     */
    public String getConnectorType() {
        return connectorType;
    }

    /**
     * Sets the cable's connector type.
     *
     * @param connectorType the new connector type
     */
    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    /**
     * Returns a full description of the cable, combining the
     * accessory's common information with its length and connector type.
     *
     * @return a string describing the cable
     */
    @Override
    public String getDescription() {
        return super.getDescription() + ", Longitud: " + lengthInMeters + "m, Conector: " + connectorType;
    }
}
