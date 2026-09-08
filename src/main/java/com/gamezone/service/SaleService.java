package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.persistence.SalePersistence;

import java.util.List;

/**
 * Handles the business logic related to sales.
 */
public class SaleService {

    private final SalePersistence salePersistence;
    private final ProductService productService;

    /**
     * Creates a new sale service.
     *
     * @param salePersistence the persistence manager for sales
     * @param productService the service used to manage products and stock
     */
    public SaleService(SalePersistence salePersistence, ProductService productService) {
        this.salePersistence = salePersistence;
        this.productService = productService;
    }

    /**
     * Registers a sale if it contains at least one product and
     * all products have enough stock available.
     *
     * @param sale the sale to register
     * @throws IllegalArgumentException if the sale has no products,
     *                                  a product does not exist,
     *                                  or there is insufficient stock
     */
    public void registerSale(Sale sale) {
        if (sale.getProducts() == null || sale.getProducts().isEmpty()) {
            throw new IllegalArgumentException(
                    "A sale must contain at least one product."
            );
        }

        for (Product product : sale.getProducts()) {
            Product storedProduct = productService.findById(product.getId());

            if (storedProduct == null) {
                throw new IllegalArgumentException(
                        "Product not found: " + product.getId()
                );
            }

            if (storedProduct.getStockQuantity() < 1) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: " + product.getId()
                );
            }
        }

        for (Product product : sale.getProducts()) {
            productService.updateStock(product.getId(), 1);
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