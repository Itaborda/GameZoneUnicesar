package com.gamezone.service;
import com.gamezone.validation.ProductValidator;
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
    private final ProductRepository repository;

    /**
     * In-memory cache of the list of products. It is loaded from the
     * repository when the service is created, so it is never {@code null}.
     */
    private final List<Product> products;

    /**
     * Creates the service with the repository it must use. The repository
     * is supplied by the caller (dependency injection), so the service does
     * not depend on a specific storage location.
     *
     * @param repository the repository used to persist and load products
     */
    public ProductService(ProductRepository repository) {
        this.repository = repository;
        this.products = repository.findAll();
    }

    /**
     * Creates the service using the legacy default repository
     * ({@code data/product.csv}).
     *
     * @deprecated kept only until {@code Main} injects its own repository
     *             through {@link #ProductService(ProductRepository)}; it will
     *             be removed afterwards.
     */
    @Deprecated
    public ProductService() {
        this(new ProductRepository("data/product.csv"));
    }

    /**
     * Registers a new product by appending it to the in-memory list
     * and persisting the entire collection back to the repository.
     *
     * @param product The {@link Product} entity to register.
     */
    public void registerProduct(Product product) {
        products.add(product);
        repository.saveAll(products);
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

        ProductValidator.validateProductExists(product, productId);
        ProductValidator.validateQuantity(quantity);
        ProductValidator.validateStockAvailability(product, quantity);

        product.setStockQuantity(product.getStockQuantity() - quantity);

        repository.saveAll(products);
    }

    /**
     * Restores a specified quantity of stock to a product.
     *
     * @param productId the product identifier
     * @param quantity  the units to restore
     * @throws com.gamezone.exceptions.ResourceNotFoundException if the product does not exist
     * @throws com.gamezone.exceptions.InvalidDataException if the quantity is not positive
     */
    public void restoreStock(String productId, int quantity) {
        Product product = findById(productId);

        ProductValidator.validateProductExists(product, productId);
        ProductValidator.validatePositiveQuantity(quantity);

        product.setStockQuantity(product.getStockQuantity() + quantity);

        repository.saveAll(products);
    }
}