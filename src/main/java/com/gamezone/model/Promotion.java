package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a generic promotion that can be applied to a sale in the
 * GameZone store. This is an abstract base class; concrete subclasses
 * must implement their own discount calculation logic.
 */
public abstract class Promotion {

    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a new promotion with the given attributes.
     *
     * @param id        the promotion's unique identifier
     * @param name      the promotion's name
     * @param startDate the date the promotion becomes valid
     * @param endDate   the date the promotion stops being valid
     */
    public Promotion(String id, String name, LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);

        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the promotion's unique identifier.
     *
     * @return the promotion's id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the promotion's unique identifier.
     *
     * @param id the new id for the promotion
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the promotion's name.
     *
     * @return the promotion's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the promotion's name.
     *
     * @param name the new name for the promotion
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the date the promotion becomes valid.
     *
     * @return the promotion's start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the date the promotion becomes valid.
     *
     * @param startDate the new start date for the promotion
     * @throws IllegalArgumentException if startDate is after the current end date
     */
    public void setStartDate(LocalDate startDate) {
        validateDateRange(startDate, this.endDate);
        this.startDate = startDate;
    }

    /**
     * Returns the date the promotion stops being valid.
     *
     * @return the promotion's end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the date the promotion stops being valid.
     *
     * @param endDate the new end date for the promotion
     * @throws IllegalArgumentException if endDate is before the current start date
     */
    public void setEndDate(LocalDate endDate) {
        validateDateRange(this.startDate, endDate);
        this.endDate = endDate;
    }

    /**
     * Ensures that a promotion's start date is not after its end date.
     *
     * @param startDate the start date to validate
     * @param endDate   the end date to validate
     * @throws IllegalArgumentException if startDate is after endDate
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    /**
     * Determines whether this promotion is active on the given date,
     * that is, whether the date falls within its validity range
     * (inclusive of both the start and end dates).
     *
     * @param date the date to check
     * @return true if the date is within the promotion's validity range, false otherwise
     */
    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Calculates the discount amount, in pesos, that this promotion
     * would grant to the given sale. Each subclass must provide its
     * own implementation.
     *
     * @param sale the sale to evaluate
     * @return the discount amount in pesos that this promotion grants to the sale
     */
    public abstract double calculateDiscount(Sale sale);
}
