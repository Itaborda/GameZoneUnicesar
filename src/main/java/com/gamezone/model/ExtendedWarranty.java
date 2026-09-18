package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents the extended warranty that a customer can request when
 * purchasing a product in the GameZone store, at an additional cost.
 */
public class ExtendedWarranty extends Warranty {

    /**
     * Creates a new extended warranty for the given product and sale,
     * starting on the given date.
     *
     * @param id        the warranty's unique identifier
     * @param product   the product covered by this warranty
     * @param sale      the sale that generated this warranty
     * @param startDate the date the warranty becomes valid
     */
    public ExtendedWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    /**
     * Returns the extended warranty's fixed duration.
     *
     * @return 12, the duration in months of an extended warranty
     */
    @Override
    public int getDurationInMonths() {
        return 12;
    }

    /**
     * Returns the extended warranty's type name.
     *
     * @return "Garantia Extendida"
     */
    @Override
    public String getWarrantyType() {
        return "Garantia Extendida";
    }

    /**
     * Returns the extended warranty's additional cost, calculated as
     * 10% of the covered product's price.
     *
     * @return 10% of the covered product's price
     */
    @Override
    public double getAdditionalCost() {
        return getProduct().getPrice() * 0.10;
    }
}
