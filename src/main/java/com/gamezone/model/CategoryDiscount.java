package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a promotion that applies a percentage discount only to
 * the products of a specific category ("VIDEOGAME" or "CONSOLE")
 * included in a sale.
 */
public class CategoryDiscount extends Promotion {

    private double discountPercentage;
    private String targetCategory;

    /**
     * Creates a new category discount promotion with the given attributes.
     *
     * @param id                 the promotion's unique identifier
     * @param name               the promotion's name
     * @param startDate          the date the promotion becomes valid
     * @param endDate            the date the promotion stops being valid
     * @param discountPercentage the percentage of the category subtotal to discount (0 to 100)
     * @param targetCategory     the category this promotion applies to ("VIDEOGAME" or "CONSOLE")
     */
    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage, String targetCategory) {
        super(id, name, startDate, endDate);
        setDiscountPercentage(discountPercentage);
        this.targetCategory = targetCategory;
    }

    /**
     * Returns the percentage of the category subtotal that this promotion discounts.
     *
     * @return the discount percentage
     */
    public double getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * Sets the percentage of the category subtotal that this promotion discounts.
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
     * Returns the category this promotion applies to.
     *
     * @return the target category ("VIDEOGAME" or "CONSOLE")
     */
    public String getTargetCategory() {
        return targetCategory;
    }

    /**
     * Sets the category this promotion applies to.
     *
     * @param targetCategory the new target category ("VIDEOGAME" or "CONSOLE")
     */
    public void setTargetCategory(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    /**
     * Calculates the discount amount by summing the price of the
     * products in the sale that belong to the target category, then
     * applying this promotion's percentage to that subtotal.
     *
     * @param sale the sale to evaluate
     * @return the discount amount in pesos that this promotion grants to the sale
     */
    @Override
    public double calculateDiscount(Sale sale) {
        double categorySubtotal = 0;

        for (Product product : sale.getProducts()) {
            if (belongsToTargetCategory(product)) {
                categorySubtotal += product.getPrice();
            }
        }

        return categorySubtotal * (discountPercentage / 100);
    }

    /**
     * Determines whether the given product belongs to this promotion's
     * target category.
     *
     * @param product the product to check
     * @return true if the product belongs to the target category, false otherwise
     */
    private boolean belongsToTargetCategory(Product product) {
        if ("VIDEOGAME".equalsIgnoreCase(targetCategory)) {
            return product instanceof VideoGame;
        } else if ("CONSOLE".equalsIgnoreCase(targetCategory)) {
            return product instanceof Console;
        }

        return false;
    }
}
