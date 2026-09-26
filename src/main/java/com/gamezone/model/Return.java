package com.gamezone.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a return of previously purchased products in the
 * GameZone store. A return always references the original sale it
 * belongs to and holds the subset of products being returned, along
 * with the reason for the return and the refunded amount.
 */
public class Return {

    private String returnId;
    private LocalDate returnDate;
    private final Sale originalSale;
    private final List<Product> returnedProducts;
    private String reason;
    private double refundAmount;

    /**
     * Creates a new return with the given information.
     *
     * @param returnId         the return's unique identifier
     * @param returnDate       the date the return was registered
     * @param originalSale     the sale the returned products belong to
     * @param returnedProducts the products being returned
     * @param reason           the reason for the return
     * @param refundAmount     the amount to be refunded for the return
     */
    public Return(String returnId, LocalDate returnDate, Sale originalSale, List<Product> returnedProducts, String reason, double refundAmount) {
        this.returnId = returnId;
        this.returnDate = returnDate;
        this.originalSale = originalSale;
        this.returnedProducts = returnedProducts;
        this.reason = reason;
        this.refundAmount = refundAmount;
    }

    /**
     * Returns the return's unique identifier.
     *
     * @return the return's id
     */
    public String getReturnId() {
        return returnId;
    }

    /**
     * Returns the date the return was registered.
     *
     * @return the return's date
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Returns the sale the returned products belong to. This
     * relationship is immutable, so no setter is provided for it.
     *
     * @return the original sale
     */
    public Sale getOriginalSale() {
        return originalSale;
    }

    /**
     * Returns the products being returned. This relationship is
     * immutable, so no setter is provided for it.
     *
     * @return the list of returned products
     */
    public List<Product> getReturnedProducts() {
        return returnedProducts;
    }

    /**
     * Returns the reason given for the return.
     *
     * @return the return's reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns the amount refunded for this return.
     *
     * @return the refund amount
     */
    public double getRefundAmount() {
        return refundAmount;
    }

    /**
     * Calculates the discount ratio applied to the original sale, that
     * is, the fraction of the items' subtotal that the promotion
     * discounted. This ratio is then applied to each individual
     * returned item so that the refund reflects the discount the
     * customer actually paid for, instead of the full list price.
     *
     * @return the discount ratio (between 0 and 1), or 0 if the
     * original sale's items subtotal is zero
     */
    private double calculateDiscountRatio() {
        double itemsSubtotal = 0;

        for (Product product : originalSale.getProducts()) {
            itemsSubtotal += product.getPrice();
        }

        if (itemsSubtotal <= 0) {
            return 0;
        }

        return originalSale.getDiscountAmount() / itemsSubtotal;
    }

    /**
     * Calculates the refund amount by applying, to each returned
     * product, the same discount proportion that the original sale
     * received: price x (1 - discount / subtotal). This prevents
     * refunding more than what the customer actually paid when the
     * original sale had a promotion applied. Assigns the resulting
     * value to this return and returns it.
     *
     * @return the calculated refund amount
     */
    public double calculateRefundAmount() {
        double discountRatio = calculateDiscountRatio();
        double total = 0;

        for (Product product : returnedProducts) {
            total += product.getPrice() * (1 - discountRatio);
        }

        this.refundAmount = total;

        return refundAmount;
    }

    /**
     * Generates a formatted receipt, in Spanish, describing this
     * return: its identifier, date, the original sale it references,
     * the returned products with their prices, the reason, and the
     * refunded amount.
     *
     * @return a formatted string describing the return
     */
    public String generateReturnReceipt() {
        StringBuilder receipt = new StringBuilder();

        receipt.append("\n===== COMPROBANTE DE DEVOLUCION =====\n");
        receipt.append("Identificador: ").append(returnId).append("\n");
        receipt.append("Fecha: ").append(returnDate).append("\n");
        receipt.append("Venta original: ").append(originalSale.getSaleId()).append("\n");
        receipt.append("Productos devueltos:\n");

        for (Product product : returnedProducts) {
            receipt.append("- ").append(product.getTitle())
                    .append(" | Precio: $").append(product.getPrice())
                    .append("\n");
        }

        receipt.append("Motivo: ").append(reason).append("\n");
        receipt.append("Monto reembolsado: $").append(refundAmount).append("\n");
        receipt.append("====================================\n");

        return receipt.toString();
    }
}