package com.gamezone.model;

import java.time.LocalDate; import java.util.ArrayList; import
        java.util.List;

public class Return {

    private String returnId;
    private LocalDate returnDate;
    private final Sale originalSale;
    private final List<Product> returnedProducts;
    private String reason;
    private double refundAmount;

    public Return(Sale originalSale, List<Product> returnedProducts, String returnId, LocalDate returnDate, String reason, double refundAmount) {
        this.originalSale = originalSale;
        this.returnedProducts = returnedProducts;
        this.returnId = returnId;
        this.returnDate = returnDate;
        this.reason = reason;
        this.refundAmount = refundAmount;
    }

    public String getReturnId() {
        return returnId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Sale getOriginalSale() {
        return originalSale;
    }

    public List<Product> getReturnedProducts() {
        return returnedProducts;
    }

    public String getReason() {
        return reason;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public double calculateRefundAmount() {
    double total = 0;

    for (Product product : returnedProducts) {
        total += product.getPrice();
    }

    refundAmount = total;
    return refundAmount;
    }
 public String generateReturnReceipt() {
    StringBuilder receipt = new StringBuilder();

    receipt.append("\n===== COMPROBANTE DE DEVOLUCIÓN =====\n");
    receipt.append("Identificador: ").append(returnId).append("\n");
    receipt.append("Fecha: ").append(returnDate).append("\n");
    receipt.append("Venta original: ")
            .append(originalSale.getSaleId())
            .append("\n");

    receipt.append("Productos devueltos:\n");

    for (Product product : returnedProducts) {
        receipt.append("- ")
                .append(product.getTitle())
                .append(" | Precio: $")
                .append(product.getPrice())
                .append("\n");
    }

    receipt.append("Motivo: ").append(reason).append("\n");
    receipt.append("Monto reembolsado: $")
            .append(refundAmount)
            .append("\n");

    receipt.append("====================================\n");

    return receipt.toString();
}

}