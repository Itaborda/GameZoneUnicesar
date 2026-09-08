package com.gamezone.service;

import com.gamezone.model.Sale;
import com.gamezone.persistence.SalePersistence;

import java.util.List;

/**
 * Handles the business logic related to sales.
 */
public class SaleService {

    private final SalePersistence salePersistence;

    /**
     * Creates a new sale service.
     *
     * @param salePersistence the persistence manager for sales
     */
    public SaleService(SalePersistence salePersistence) {
        this.salePersistence = salePersistence;
    }

    /**
     * Registers a sale if it contains at least one product.
     *
     * @param sale the sale to register
     * @throws IllegalArgumentException if the sale has no products
     */
    public void registerSale(Sale sale) {
        if (sale.getProducts() == null || sale.getProducts().isEmpty()) {
            throw new IllegalArgumentException(
                    "A sale must contain at least one product."
            );
        }

        salePersistence.save(sale);
    }

    /**
     * Returns all registered sales.
     *
     * @return the list of registered sales
     */
    public List<Sale> findAll() {
        return salePersistence.findAll();
    }
}