package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.persistence.SaleRepository;

import java.util.List;

/**
 * Handles the business logic related to sales.
 */
public class SaleService {

    private SaleRepository salePersistence;
    private ProductService productService;
    private AccessoryService accessoryService;
    private PromotionService promotionService;

    /**
     * Creates a sale service with its required dependencies.
     *
     * @param salePersistence repository used to store sales
     * @param productService service used to manage product stock
     * @param accessoryService service used to manage accessory stock
     * @param promotionService service used to manage promotions
     */
    public SaleService(SaleRepository salePersistence,
                       ProductService productService,
                       AccessoryService accessoryService,
                       PromotionService promotionService) {
        this.salePersistence = salePersistence;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
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

        // Validate stock for all products and accessories
        for (Product product : sale.getProducts()) {

            if (product instanceof Accessory) {

                Accessory accessory =
                        accessoryService.findById(product.getId());

                if (accessory == null) {
                    throw new IllegalArgumentException(
                            "Accessory not found: " + product.getId()
                    );
                }

                if (accessory.getStockQuantity() < 1) {
                    throw new IllegalArgumentException(
                            "Insufficient stock for accessory: "
                                    + product.getId()
                    );
                }

            } else {

                Product storedProduct =
                        productService.findById(product.getId());

                if (storedProduct == null) {
                    throw new IllegalArgumentException(
                            "Product not found: " + product.getId()
                    );
                }

                if (storedProduct.getStockQuantity() < 1) {
                    throw new IllegalArgumentException(
                            "Insufficient stock for product: "
                                    + product.getId()
                    );
                }
            }
        }

        // Update stock according to the product type
        for (Product product : sale.getProducts()) {

            if (product instanceof Accessory) {
                accessoryService.updateStock(product.getId(), 1);
            } else {
                productService.updateStock(product.getId(), 1);
            }
        }

        // Apply the best active promotion to the sale
        Promotion bestPromotion =
                promotionService.findBestPromotionFor(sale);

        if (bestPromotion != null) {
            double discount =
                    bestPromotion.calculateDiscount(sale);

            sale.setAppliedPromotionName(bestPromotion.getName());
            sale.setDiscountAmount(discount);
        }

        // Save the sale after successfully updating the stock
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