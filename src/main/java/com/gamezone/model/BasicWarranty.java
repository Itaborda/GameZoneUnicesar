package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents the basic warranty automatically assigned to every
 * console sold in the GameZone store.
 */
public class BasicWarranty extends Warranty {

    /**
     * Creates a new basic warranty for the given product and sale,
     * starting on the given date.
     *
     * @param id        the warranty's unique identifier
     * @param product   the product covered by this warranty
     * @param sale      the sale that generated this warranty
     * @param startDate the date the warranty becomes valid
     */
    public BasicWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    /**
     * Returns the basic warranty's fixed duration.
     *
     * @return 6, the duration in months of a basic warranty
     */
    @Override
    public int getDurationInMonths() {
        return 6;
    }

    /**
     * Returns the basic warranty's type name.
     *
     * @return "Garantia Basica"
     */
    @Override
    public String getWarrantyType() {
        return "Garantia Basica";
    }

    /**
     * Returns the basic warranty's additional cost, which is always zero.
     *
     * @return 0.0, since the basic warranty has no additional cost
     */
    @Override
    public double getAdditionalCost() {
        return 0.0;
    }
}
