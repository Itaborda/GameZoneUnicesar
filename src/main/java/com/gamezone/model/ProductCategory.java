package com.gamezone.model;

import java.util.Optional;

/**
 * Categories into which the products of the store are classified.
 * Promotions refer to a category by its name.
 */
public enum ProductCategory {

    /** Video games. */
    VIDEOGAME,

    /** Consoles. */
    CONSOLE,

    /** Accessories such as controllers, cables and memories. */
    ACCESSORY;

    /**
     * Finds the category with the given name, ignoring case.
     *
     * @param name the name of the category (for example "console")
     * @return the matching category, or an empty value if the name is {@code null} or unknown
     */
    public static Optional<ProductCategory> fromName(String name) {
        if (name == null) {
            return Optional.empty();
        }

        for (ProductCategory category : values()) {
            if (category.name().equalsIgnoreCase(name)) {
                return Optional.of(category);
            }
        }

        return Optional.empty();
    }
}