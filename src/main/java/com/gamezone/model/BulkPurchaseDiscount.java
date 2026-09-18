package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a promotion that applies a percentage discount to the
 * total amount of a sale when the number of products purchased meets
 * or exceeds a minimum required quantity.
 */
public class BulkPurchaseDiscount extends Promotion {

    private int minimumQuantity;
    private double discountPercentage;

    /**
     * Creates a new bulk purchase discount promotion with the given attributes.
     *
     * @param id                 the promotion's unique identifier
     * @param name               the promotion's name
     * @param startDate          the date the promotion becomes valid
     * @param endDate            the date the promotion stops being valid
     * @param minimumQuantity    the minimum number of products required for the discount to apply
     * @param discountPercentage the percentage of the sale's total to discount (0 to 100)
     */
    public BulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate, int minimumQuantity, double discountPercentage) {
        super(id, name, startDate, endDate);
        this.minimumQuantity = minimumQuantity;
        setDiscountPercentage(discountPercentage);
    }

    /**
     * Returns the minimum number of products required for this
     * promotion's discount to apply.
     *
     * @return the minimum required quantity
     */
    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    /**
     * Sets the minimum number of products required for this
     * promotion's discount to apply.
     *
     * @param minimumQuantity the new minimum required quantity
     */
    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
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
     * percentage to the sale's total, but only when the sale contains
     * at least the minimum required quantity of products.
     *
     * @param sale the sale to evaluate
     * @return the discount amount in pesos, or zero if the minimum quantity is not met
     */
    @Override
    public double calculateDiscount(Sale sale) {
        if (sale.getProducts().size() < minimumQuantity) {
            return 0;
        }

        return sale.calculateTotal() * (discountPercentage / 100);
    }
}
