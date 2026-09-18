package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a promotion that applies a fixed percentage discount to
 * the total amount of a sale, regardless of the products it contains.
 */
public class PercentageDiscount extends Promotion {

    private double discountPercentage;

    /**
     * Creates a new percentage discount promotion with the given attributes.
     *
     * @param id                 the promotion's unique identifier
     * @param name               the promotion's name
     * @param startDate          the date the promotion becomes valid
     * @param endDate            the date the promotion stops being valid
     * @param discountPercentage the percentage of the sale's total to discount (0 to 100)
     */
    public PercentageDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage) {
        super(id, name, startDate, endDate);
        setDiscountPercentage(discountPercentage);
    }

    /**
     * Returns the percentage of the total that this promotion discounts.
     *
     * @return the discount percentage
     */
    public double getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * Sets the percentage of the total that this promotion discounts.
     *
     * @param discountPercentage the new discount percentage (0 to 100)
     * @throws IllegalArgumentException if discountPercentage is outside the 0-100 range
     */
    public void setDiscountPercentage(double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        this.discountPercentage = discountPercentage;
    }

    /**
     * Calculates the discount amount by applying this promotion's
     * percentage to the sale's total.
     *
     * @param sale the sale to evaluate
     * @return the discount amount in pesos that this promotion grants to the sale
     */
    @Override
    public double calculateDiscount(Sale sale) {
        return sale.calculateTotal() * (discountPercentage / 100);
    }
}
