package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a warranty assigned to a product sold in the GameZone
 * store. A warranty is always associated with the product it covers
 * and the sale that generated it, and its end date is derived
 * automatically from its duration.
 */
public abstract class Warranty {

    private String id;
    private Product product;
    private Sale sale;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a new warranty with the given information. The end date
     * is calculated automatically by adding this warranty's duration,
     * in months, to the given start date.
     *
     * @param id        the warranty's unique identifier
     * @param product   the product covered by this warranty
     * @param sale      the sale that generated this warranty
     * @param startDate the date the warranty becomes valid
     * @throws IllegalArgumentException if id is null or blank, or if product, sale, or startDate is null
     */
    public Warranty(String id, Product product, Sale sale, LocalDate startDate) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Warranty id cannot be null or blank");
        }

        if (product == null) {
            throw new IllegalArgumentException("Warranty product cannot be null");
        }

        if (sale == null) {
            throw new IllegalArgumentException("Warranty sale cannot be null");
        }

        if (startDate == null) {
            throw new IllegalArgumentException("Warranty start date cannot be null");
        }

        this.id = id;
        this.product = product;
        this.sale = sale;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(getDurationInMonths());
    }

    /**
     * Returns the warranty's unique identifier.
     *
     * @return the warranty's id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the product covered by this warranty.
     *
     * @return the covered product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Returns the sale that generated this warranty.
     *
     * @return the associated sale
     */
    public Sale getSale() {
        return sale;
    }

    /**
     * Returns the date the warranty becomes valid.
     *
     * @return the warranty's start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the date the warranty stops being valid.
     *
     * @return the warranty's end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns this warranty's duration, in months. Each concrete
     * subclass defines its own fixed duration.
     *
     * @return the warranty's duration in months
     */
    public abstract int getDurationInMonths();

    /**
     * Returns the name of this warranty's type.
     *
     * @return the warranty type's name
     */
    public abstract String getWarrantyType();

    /**
     * Returns the additional cost, in pesos, that this warranty adds
     * to the sale's total.
     *
     * @return the warranty's additional cost
     */
    public abstract double getAdditionalCost();

    /**
     * Determines whether this warranty is active on the given date,
     * that is, whether the date falls within its validity range
     * (inclusive of both the start and end dates).
     *
     * @param date the date to check
     * @return true if the date is within the warranty's validity range, false otherwise
     */
    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Generates a formatted certificate, in Spanish, describing this
     * warranty: its identifier, type, covered product, associated
     * sale, and validity range.
     *
     * @return a formatted string describing the warranty
     */
    public String generateWarrantyCertificate() {
        StringBuilder certificate = new StringBuilder();

        certificate.append("\n===== CERTIFICADO DE GARANTIA =====\n");
        certificate.append("Identificador: ").append(id).append("\n");
        certificate.append("Tipo: ").append(getWarrantyType()).append("\n");
        certificate.append("Producto cubierto: ").append(product.getTitle()).append("\n");
        certificate.append("Venta asociada: ").append(sale.getSaleId()).append("\n");
        certificate.append("Vigencia: ").append(startDate).append(" a ").append(endDate).append("\n");
        certificate.append("Costo adicional: $").append(getAdditionalCost()).append("\n");
        certificate.append("====================================\n");

        return certificate.toString();
    }
}
