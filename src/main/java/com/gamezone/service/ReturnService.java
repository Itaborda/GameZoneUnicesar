package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.persistence.ReturnRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Service responsible for managing product returns within the system.
 * <p>
 * Handles the registration of new returns, validation against the original
 * sale, stock restoration for returned products, and querying of existing
 * returns by customer or sale. It also supports generating a monthly
 * financial balance that accounts for both sales and returns.
 */
public class ReturnService {
    private final ReturnRepository returnRepository;
    private final AccessoryService accessoryService;
    private final SaleService saleService;
    private final ProductService productService;
    private final List<Return> returns;
    /**
     * Constructs a new {@code ReturnService}.
     *
     * @param productService   the service used to manage product stock
     * @param returnRepository the repository used to persist returns
     * @param saleService      the service used to look up existing sales
     * @param returns          the in-memory list of registered returns
     * @param accessoryService the service used to manage accessory stock
     */
    public ReturnService(AccessoryService accessoryService, ReturnRepository returnRepository, SaleService saleService, ProductService productService, List<Return> returns) {
        this.accessoryService = accessoryService;
        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.productService = productService;
        this.returns = returns;
    }

    /**
     * Registers a new return for a given sale.
     * <p>
     * Validates that the sale exists and is still within the allowed return
     * period (30 days). Each product id provided must belong to the original
     * sale; otherwise, an exception is thrown. Once validated, the refund
     * amount is calculated, the stock of each returned item is restored
     * through {@code ProductService} or {@code AccessoryService} depending
     * on its type, and the new return is persisted.
     *
     * @param saleId     the id of the original sale
     * @param productIds the ids of the products being returned
     * @param reason     the reason for the return
     * @return the newly created {@link Return}
     * @throws IllegalArgumentException if the sale does not exist, if it can
     *                                   no longer be returned (more than 30
     *                                   days have passed), or if any product
     *                                   id does not belong to the sale
     */
    public Return registerReturn(String saleId, List<String> productIds, String reason) {

        Sale sale = null;
        for (Sale s : saleService.findAll()) {
            if (s.getSaleId().equals(saleId)) {
                sale = s;
                break;
            }
        }
        if (sale == null) {
            throw new IllegalArgumentException("La venta indicada no existe.");
        }
        if (!sale.canBeReturned()) {
            throw new IllegalArgumentException("La devolución no puede registrarse: han pasado más de 30 días desde la venta.");
        }

        List<Product> returnedProducts = new ArrayList<>();
        for (String productId : productIds) {
            Product matchedProduct = null;
            for (Product p : sale.getProducts()) {
                if (p.getId().equals(productId)) {
                    matchedProduct = p;
                    break;
                }
            }

            if (matchedProduct == null) {
                throw new IllegalArgumentException("El producto con id " + productId + " no pertenece a la venta indicada.");
            }

            returnedProducts.add(matchedProduct);
        }

        String returnId = "RET" + (returns.size() + 1);
        LocalDate returnDate = LocalDate.now();

        Return newReturn = new Return(returnId, returnDate, sale, returnedProducts, reason, 0);
        newReturn.calculateRefundAmount();

        for (Product product : returnedProducts) {
            if (product instanceof Accessory) {
                accessoryService.restoreStock(product.getId(), 1);
            } else {
                productService.restoreStock(product.getId(), 1);
            }
        }

        returns.add(newReturn);
        returnRepository.saveAll(returns);

        return newReturn;
    }
    /**
     * Returns all registered returns.
     *
     * @return a list containing every {@link Return} registered in the system
     */
    public List<Return> viewAllReturns() {
        return returns;
    }
    /**
     * Retrieves all returns associated with a given customer.
     *
     * @param customerId the id of the customer
     * @return a list of {@link Return} instances belonging to sales made by
     *         the specified customer
     */
    public List<Return> viewReturnsByCustomer(String customerId) {
        List<Return> result = new ArrayList<>();

        for (Return r : returns) {
            if (r.getOriginalSale().getCustomer().getId().equals(customerId)) {
                result.add(r);
            }
        }

        return result;
    }
    /**
     * Retrieves all returns associated with a given sale.
     *
     * @param saleId the id of the original sale
     * @return a list of {@link Return} instances linked to the specified sale
     */
    public List<Return> viewReturnsBySale(String saleId) {
        List<Return> result = new ArrayList<>();

        for (Return r : returns) {
            if (r.getOriginalSale().getSaleId().equals(saleId)) {
                result.add(r);
            }
        }

        return result;
    }
    /**
     * Calculates the net monthly balance for a given month and year, defined
     * as the total sales amount minus the total refunded amount from returns.
     *
     * @param month the month to evaluate (1-12)
     * @param year  the year to evaluate
     * @return the net balance (total sales minus total returns) for the
     *         specified month and year
     */
    public double generateMonthlyBalance(int month, int year) {
        double totalSales = 0;
        double totalReturns = 0;

        for (Sale s : saleService.findAll()) {
            LocalDate saleDate = LocalDate.parse(s.getDate());
            if (saleDate.getMonthValue() == month && saleDate.getYear() == year) {
                totalSales += s.calculateTotal();
            }
        }



        for (Return r : returns) {
            if (r.getReturnDate().getMonthValue() == month && r.getReturnDate().getYear() == year) {
                totalReturns += r.getRefundAmount();
            }
        }

        return totalSales - totalReturns;
    }
}
