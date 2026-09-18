package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.PromotionRepository;
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
 * avoid reading the file on every operation.
 */
public class WarrentyService {
    private WarrantyRepository warrantyRepository;
    private List<Warranty> warranties;
    /**
     * Creates a new warranty service backed by the given repository,
     * loading the initial list of warranties from persistence.
     *
     * @param warrantyRepository the repository used to persist warranties
     */
    public WarrentyService(WarrantyRepository warrantyRepository) {
        this.warrantyRepository = warrantyRepository;
        this.warranties = warrantyRepository.loadAll();
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
        warrantyRepository.saveAll(warranties);
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
        warrantyRepository.saveAll(warranties);
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
     * Generates a unique identifier for a new warranty.
     *
     * @return a newly generated identifier
     */
    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
