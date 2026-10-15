package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Product;

/**
 * Validation rules for the products module.
 * <p>
 * This class only depends on the model and exceptions packages. It never
 * accesses repositories, services or files.
 * </p>
 */
public final class ProductValidator {

    private ProductValidator() {
    }

    /**
     * Validates the basic data of a product.
     *
     * @param id    the product identifier (not null or blank)
     * @param title the product title (not null or blank)
     * @param price the product price (greater than zero)
     * @param stock the stock quantity (zero or greater)
     * @throws InvalidDataException if any value is invalid
     */
    public static void validateProductData(String id, String title, double price, int stock) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("identificador", "no puede estar vacío");
        }
        if (title == null || title.isBlank()) {
            throw new InvalidDataException("título", "no puede estar vacío");
        }
        if (Double.isNaN(price) || price <= 0) {
            throw new InvalidDataException("precio", "debe ser mayor que cero");
        }
        if (stock < 0) {
            throw new InvalidDataException("stock", "no puede ser negativo");
        }
    }

    /**
     * Validates that a product has enough stock for the requested quantity.
     * The product must not be null; call
     * {@link #validateProductExists(Product, String)} first if unsure.
     *
     * @param product           the product to check
     * @param requestedQuantity the quantity requested
     * @throws BusinessRuleException if the stock is insufficient
     */
    public static void validateStockAvailability(Product product, int requestedQuantity) {
        if (product.getStockQuantity() < requestedQuantity) {
            throw new BusinessRuleException("Stock insuficiente para el producto '"
                    + product.getTitle() + "': disponible "
                    + product.getStockQuantity() + ", solicitado " + requestedQuantity);
        }
    }

    /**
     * Validates that a product was found.
     *
     * @param product   the product returned by a search
     * @param productId the identifier that was searched
     * @throws ResourceNotFoundException if the product is null
     */
    public static void validateProductExists(Product product, String productId) {
        if (product == null) {
            throw new ResourceNotFoundException("producto", productId);
        }
    }

    /**
     * Validates that a quantity is not negative.
     *
     * @param quantity the quantity to check
     * @throws InvalidDataException if the quantity is negative
     */
    public static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new InvalidDataException("cantidad", "no puede ser negativo");
        }
    }

    /**
     * Validates that a quantity is greater than zero.
     *
     * @param quantity the quantity to check
     * @throws InvalidDataException if the quantity is zero or negative
     */
    public static void validatePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidDataException("cantidad", "debe ser mayor que cero");
        }
    }
}