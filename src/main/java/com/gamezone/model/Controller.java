package com.gamezone.model;

import java.util.List;

/**
 * Represents a game controller accessory sold in the GameZone store.
 */
public class Controller extends Accessory {

    private String connectionType;

    /**
     * Creates a new controller with the given attributes.
     *
     * @param id                   the product's unique identifier
     * @param title                the product's title
     * @param price                the product's price
     * @param stockQuantity        the available quantity in inventory
     * @param compatibleConsoleIds the ids of the consoles this controller is compatible with
     * @param connectionType       the controller's connection type ("WIRELESS" or "WIRED")
     */
    public Controller(String id, String title, double price, int stockQuantity, List<String> compatibleConsoleIds, String connectionType) {
        super(id, title, price, stockQuantity, compatibleConsoleIds);
        this.connectionType = connectionType;
    }

    /**
     * Returns the controller's connection type.
     *
     * @return the connection type ("WIRELESS" or "WIRED")
     */
    public String getConnectionType() {
        return connectionType;
    }

    /**
     * Sets the controller's connection type.
     *
     * @param connectionType the new connection type ("WIRELESS" or "WIRED")
     */
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * Returns a full description of the controller, combining the
     * accessory's common information with its connection type.
     *
     * @return a string describing the controller
     */
    @Override
    public String getDescription() {
        return super.getDescription() + ", Tipo de conexion: " + connectionType;
    }
}
