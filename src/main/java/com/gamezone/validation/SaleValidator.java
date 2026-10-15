package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Validation rules for the sales module.
 * <p>
 * This class only depends on the model and exceptions packages. It never
 * accesses repositories, services or files.
 * </p>
 */
public final class SaleValidator {

    private SaleValidator() {
    }

    /**
     * Validates the data required to register a sale.
     *
     * @param customer the customer of the sale (not null)
     * @param seller   the seller of the sale (not null)
     * @param products the products of the sale (not null or empty)
     * @throws InvalidDataException  if the customer or the seller is null
     * @throws BusinessRuleException if the sale has no products
     */
    public static void validateSaleData(Customer customer, Seller seller, List<Product> products) {
        if (customer == null) {
            throw new InvalidDataException("cliente", "es obligatorio");
        }
        if (seller == null) {
            throw new InvalidDataException("vendedor", "es obligatorio");
        }
        if (products == null || products.isEmpty()) {
            throw new BusinessRuleException("Una venta debe contener al menos un producto");
        }
    }

    /**
     * Validates that a sale was found.
     *
     * @param sale   the sale returned by a search
     * @param saleId the identifier that was searched
     * @throws ResourceNotFoundException if the sale is null
     */
    public static void validateSaleExists(Sale sale, String saleId) {
        if (sale == null) {
            throw new ResourceNotFoundException("venta", saleId);
        }
    }

    /**
     * Validates that a sale date is present and written as yyyy-MM-dd.
     *
     * @param date the date text to check
     * @throws InvalidDataException if the date is empty or has a wrong format
     */
    public static void validateSaleDate(String date) {
        if (date == null || date.isBlank()) {
            throw new InvalidDataException("fecha", "no puede estar vacío");
        }
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new InvalidDataException("fecha", "debe tener el formato AAAA-MM-DD");
        }
    }
}