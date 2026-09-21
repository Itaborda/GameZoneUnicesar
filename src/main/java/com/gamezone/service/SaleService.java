package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.persistence.SaleRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Handles the business logic related to sales.
 */
public class SaleService {

    private SaleRepository salePersistence;
    private ProductService productService;
    private AccessoryService accessoryService;
    private PromotionService promotionService;
    private WarrantyService warrantyService;

    /**
     * Creates a sale service with its required dependencies.
     *
     * @param salePersistence repository used to store sales
     * @param productService service used to manage product stock
     * @param accessoryService service used to manage accessory stock
     * @param promotionService service used to manage promotions
     * @param warrantyService service used to manage warranties
     */
    public SaleService(SaleRepository salePersistence,
                       ProductService productService,
                       AccessoryService accessoryService,
                       PromotionService promotionService,
                       WarrantyService warrantyService) {
        this.salePersistence = salePersistence;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.warrantyService = warrantyService;
    }

    /**
     * Registers a sale by validating its items, applying the best promotion,
     * assigning warranties, updating inventory, and persisting the sale.
     *
     * @param sale the sale to register
     * @param productIdsWithExtendedWarranty product identifiers selected
     *                                        for extended warranty
     * @throws IllegalArgumentException if the sale has no items,
     *                                  an item does not exist,
     *                                  or there is insufficient stock
     */
    public void registerSale(
            Sale sale,
            List<String> productIdsWithExtendedWarranty) {

        // Step 1: Validate that the sale contains at least one item.
        if (sale.getProducts() == null || sale.getProducts().isEmpty()) {
            throw new IllegalArgumentException(
                    "A sale must contain at least one item."
            );
        }

        // Step 2: Resolve every item and validate its available stock.
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

        // Step 3: The sale subtotal is calculated by Sale.calculateTotal().

        // Step 4: Apply the best active promotion.
        Promotion bestPromotion =
                promotionService.findBestPromotionFor(sale);

        if (bestPromotion != null) {

            double discount =
                    bestPromotion.calculateDiscount(sale);

            sale.setAppliedPromotionName(bestPromotion.getName());
            sale.setDiscountAmount(discount);

        } else {

            sale.setAppliedPromotionName(null);
            sale.setDiscountAmount(0.0);
        }

        // Step 5: Assign basic and requested extended warranties.
        LocalDate saleDate = LocalDate.parse(sale.getDate());

        if (productIdsWithExtendedWarranty == null) {
            productIdsWithExtendedWarranty = List.of();
        }

        for (Product product : sale.getProducts()) {

            if (product instanceof Console) {

                // Every console receives a basic warranty automatically.
                warrantyService.assignBasicWarranty(
                        product,
                        sale,
                        saleDate
                );

                // Assign extended warranty when selected by the customer.
                if (productIdsWithExtendedWarranty.contains(product.getId())) {

                    ExtendedWarranty warranty =
                            warrantyService.assignExtendedWarranty(
                                    product,
                                    sale,
                                    saleDate
                            );

                    sale.setWarrantyAdditionalCost(
                            sale.getWarrantyAdditionalCost()
                                    + warranty.getAdditionalCost()
                    );
                }
            }
        }

        // Step 6: The final total is calculated by Sale.generateReceipt()
        // using subtotal - discount + extended warranty cost.

        // Step 7: Update inventory according to the item type.
        for (Product product : sale.getProducts()) {

            if (product instanceof Accessory) {

                accessoryService.updateStock(
                        product.getId(),
                        1
                );

            } else {

                productService.updateStock(
                        product.getId(),
                        1
                );
            }
        }

        // Step 8: Persist the sale after all previous steps succeed.
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