package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.persistence.ProductRepository;
import java.util.List;

/**
 * Service class responsible for managing product-related business operations,
 * including product registration, lookup, and stock management.
 */
public class ProductService {

    /**
     * Repository used for persisting and retrieving product data.
     */
    private ProductRepository productRepository;

    /**
     * In-memory cache of the list of products.
     */
    private List<Product> products;

    /**
     * Constructs a new {@code ProductService} instance and initializes the
     * in-memory product list with all records currently stored in the repository.
     *
     * @param productRepository The repository instance to be used for data operations.
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.products = productRepository.findAll();
    }

    /**
     * Registers a new product by appending it to the in-memory list
     * and persisting the entire collection back to the repository.
     *
     * @param product The {@link Product} entity to register.
     */
    public void registerProduct(Product product) {
        products.add(product);
        productRepository.saveAll(products);
    }

    /**
     * Retrieves the complete list of products currently cached in memory.
     *
     * @return A {@link List} of all {@link Product} entities.
     */
    public List<Product> getAllProducts() {
        return products;
    }

    /**
     * Searches for a product matching the specified unique identifier within the in-memory list.
     *
     * @param id The unique identifier of the product to find.
     * @return The matching {@link Product} entity, or {@code null} if no matching product is found.
     */
    public Product findById(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }

        return null;
    }

    /**
     * Deducts the specified quantity from a product's available stock and updates
     * the repository state.
     *
     * @param productId The unique identifier of the product whose stock is to be updated.
     * @param quantity The number of units to deduct from the current stock.
     * @throws IllegalArgumentException If no product is found for the given ID,
     *                                  if the requested quantity is negative,
     *                                  or if the current stock is less than the requested quantity.
     */
    public void updateStock(String productId, int quantity) {
        Product product = findById(productId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);

        productRepository.saveAll(products);
    }
}