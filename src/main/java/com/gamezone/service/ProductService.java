package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.persistence.ProductRepository;
import java.util.List;

public class ProductService {

    private ProductRepository productRepository;
    private List<Product> products;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.products = productRepository.findAll();
    }

    public void registerProduct(Product product) {
        products.add(product);
        productRepository.saveAll(products);
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product findById(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }

        return null;
    }

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