package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Provides the business logic for managing warranties in the
 * GameZone system. This service is the only class authorized to
 * interact with {@link WarrantyRepository} for persistence
 * operations, keeping the current list of warranties in memory to
 * avoid reading the file on every operation. It resolves product
 * and sale references from raw rows of text using
 * {@link ProductService} and {@link SaleRepository}, so that the
 * repository layer itself stays free of those dependencies.
 */
public class WarrantyService {
    private SaleRepository saleRepository;
    private ProductService productService;
    private WarrantyRepository warrantyRepository;
    private List<Warranty> warranties;
    /**
     * Creates a new warranty service backed by the given repository,
     * loading and resolving the initial list of warranties from
     * persistence.
     *
     * @param warrantyRepository the repository used to persist raw warranty rows
     * @param saleRepository     the repository used to resolve sale references
     * @param productService     the service used to resolve product references
     */

    public WarrantyService(WarrantyRepository warrantyRepository, SaleRepository saleRepository, ProductService productService) {
        this.warrantyRepository = warrantyRepository;
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.warranties = resolveAll(warrantyRepository.loadAll());
    }
    /**
     * Creates and persists an automatic basic warranty for the given
     * product and sale.
     *
     * @param product   the product covered by the warranty
     * @param sale      the sale the warranty is associated with
     * @param startDate the date the warranty starts
     * @return the newly created basic warranty
     */

    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) {
        BasicWarranty warranty = new BasicWarranty(generateId(), product, sale, startDate);
        warranties.add(warranty);
        persist();
        return warranty;
    }

    /**
     * Creates and persists an extended warranty for the given
     * product and sale.
     *
     * @param product   the product covered by the warranty
     * @param sale      the sale the warranty is associated with
     * @param startDate the date the warranty starts
     * @return the newly created extended warranty
     */
    public ExtendedWarranty assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) {
        ExtendedWarranty warranty = new ExtendedWarranty(generateId(), product, sale, startDate);
        warranties.add(warranty);
        persist();
        return warranty;
    }
    /**
     * Finds the warranty associated with a specific product within a
     * specific sale.
     *
     * @param productId the identifier of the product
     * @param saleId    the identifier of the sale
     * @return the matching warranty, or {@code null} if none is found
     */
    public Warranty findWarrantyByProduct(String productId, String saleId) {
        for (Warranty w : warranties) {
            if (w.getProduct().getId().equals(productId) && w.getSale().getSaleId().equals(saleId)) {
                return w;
            }
        }
        return null;
    }
    /**
     * Returns all warranties registered in the system.
     *
     * @return the list of all warranties
     */
    public List<Warranty> listAllWarranties() {
        return warranties;
    }
    /**
     * Returns the warranties that are currently active on today's
     * date.
     *
     * @return the list of active warranties
     */
    public List<Warranty> listActiveWarranties() {
        List<Warranty> active = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Warranty w : warranties) {
            if (w.isActive(today)) {
                active.add(w);
            }
        }
        return active;
    }
    /**
     * Returns the warranties whose end date falls within the given
     * number of days from today.
     *
     * @param daysAhead the number of days ahead to check
     * @return the list of warranties expiring soon
     */

    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {
        List<Warranty> expiringSoon = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(daysAhead);
        for (Warranty w : warranties) {
            LocalDate endDate = w.getEndDate();
            if (!endDate.isBefore(today) && !endDate.isAfter(limit)) {
                expiringSoon.add(w);
            }
        }
        return expiringSoon;
    }
    /**
     * Resolves a list of raw warranty rows into fully-formed
     * {@code Warranty} objects by looking up their product and sale
     * references. Rows whose product or sale cannot be resolved
     * are skipped.
     *
     * @param rows the raw rows loaded from persistence, in the
     *             order: type, id, productId, saleId, startDate, endDate
     * @return the list of resolved warranties
     */
    private List<Warranty> resolveAll(List<String[]> rows) {
        List<Warranty> resolved = new ArrayList<>();
        List<Sale> sales = saleRepository.findAll();

        for (String[] row : rows) {
            String type = row[0];
            String id = row[1];
            String productId = row[2];
            String saleId = row[3];
            LocalDate startDate = LocalDate.parse(row[4]);

            Product product = productService.findById(productId);
            Sale sale = findSaleById(sales, saleId);
            if (product == null || sale == null) {
                continue;
            }

            if (type.equals("BASIC")) {
                resolved.add(new BasicWarranty(id, product, sale, startDate));
            } else if (type.equals("EXTENDED")) {
                resolved.add(new ExtendedWarranty(id, product, sale, startDate));
            }
        }
        return resolved;
    }
    /**
     * Generates a unique identifier for a new warranty.
     *
     * @return a newly generated identifier
     */
    private String generateId() {
        return UUID.randomUUID().toString();
    }
    /**
     * Converts the in-memory warranties into raw rows of text and
     * saves them through the repository.
     */
    public void persist(){
        List<String[]> rows = new ArrayList<>();
        for (Warranty w : warranties) {
            String type;
            if (w instanceof BasicWarranty) {
                type = "BASIC";
            } else if (w instanceof ExtendedWarranty) {
                type = "EXTENDED";
            } else {
                continue;
            }

            String[] row = {
                    type,
                    w.getId(),
                    w.getProduct().getId(),
                    w.getSale().getSaleId(),
                    w.getStartDate().toString(),
                    w.getEndDate().toString()
            };
            rows.add(row);
        }
        warrantyRepository.saveAll(rows);

    }
    /**
     * Searches the given list of sales for the one matching the
     * given id.
     *
     * @param sales  the sales to search through
     * @param saleId the id to look for
     * @return the matching sale, or {@code null} if none is found
     */
    private Sale findSaleById(List<Sale> sales, String saleId) {
        for (Sale s : sales) {
            if (s.getSaleId().equals(saleId)) {
                return s;
            }
        }
        return null;
    }
    /**
     * Cancels every warranty associated with the given product within
     * the given sale. This is used when a console is returned, since a
     * returned console cannot keep an active warranty. Returns the total
     * refundable cost of the canceled warranties: zero for a basic
     * warranty, and its additional cost for an extended warranty.
     *
     * @param productId the identifier of the product whose warranties are canceled
     * @param saleId    the identifier of the sale the warranties belong to
     * @return the total refundable amount from the canceled warranties
     */
    public double cancelWarranties(String productId, String saleId) {
        double refundableAmount = 0.0;
        List<Warranty> toRemove = new ArrayList<>();

        for (Warranty w : warranties) {
            if (w.getProduct().getId().equals(productId) && w.getSale().getSaleId().equals(saleId)) {
                refundableAmount += w.getAdditionalCost();
                toRemove.add(w);
            }
        }

        warranties.removeAll(toRemove);
        persist();

        return refundableAmount;
    }

}
