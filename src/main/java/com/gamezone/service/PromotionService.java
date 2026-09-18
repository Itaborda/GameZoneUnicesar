package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.PromotionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Provides the business logic for managing promotions in the
 * GameZone system. This service is the only class authorized to
 * interact with {@link PromotionRepository} for persistence
 * operations, keeping the current list of promotions in memory to
 * avoid reading the file on every operation. It is also responsible
 * for selecting the best applicable promotion for a given sale.
 */
public class PromotionService {
    private PromotionRepository promotionRepository = new PromotionRepository("data/promotions.csv");
    private List<Promotion> promotions;
    /**
     * Creates a new promotion service backed by the given repository,
     * loading the initial list of promotions from it.
     *
     * @param promotionRepository the repository used to persist promotions
     */
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
        this.promotions = promotionRepository.loadAll();
    }
    /**
     * Registers a new percentage discount, adding it to the in-memory
     * list and persisting the updated list to the repository.
     *
     * @param p the percentage discount to register
     */
    public void registerPercentageDiscount(PercentageDiscount p) {
        promotions.add(p);
        promotionRepository.saveAll(promotions);
    }
    /**
     * Registers a new category discount, adding it to the in-memory
     * list and persisting the updated list to the repository.
     *
     * @param p the category discount to register
     */
    public void registerCategoryDiscount(CategoryDiscount p) {
        promotions.add(p);
        promotionRepository.saveAll(promotions);
    }
    /**
     * Registers a new bulk purchase discount, adding it to the
     * in-memory list and persisting the updated list to the repository.
     *
     * @param p the bulk purchase discount to register
     */
    public void registerBulkPurchaseDiscount(BulkPurchaseDiscount p) {
        promotions.add(p);
        promotionRepository.saveAll(promotions);
    }
    /**
     * Returns all promotions registered in the system.
     *
     * @return the list of all registered promotions
     */
    public List<Promotion> listAllPromotions() {
        return promotions;
    }
    /**
     * Returns the promotions that are currently active, based on
     * today's date.
     *
     * @return the list of promotions active on the current date
     */
    public List<Promotion> listActivePromotions() {
        List<Promotion> active = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Promotion p : promotions) {
            if (p.isActive(today)) {
                active.add(p);
            }
        }
        return active;
    }
    /**
     * Finds, among the currently active promotions, the one that would
     * grant the highest monetary discount to the given sale.
     *
     * @param sale the sale to evaluate
     * @return the best applicable promotion, or {@code null} if none
     * of the active promotions apply or the maximum discount is zero
     */
    public Promotion findBestPromotionFor(Sale sale) {
        Promotion best = null;
        double bestDiscount = 0;

        for (Promotion p : listActivePromotions()) {
            double discount = p.calculateDiscount(sale);
            if (discount > bestDiscount) {
                bestDiscount = discount;
                best = p;
            }
        }
        return best;
    }

    /**
     * Finds a promotion by its unique identifier.
     *
     * @param id the promotion identifier
     * @return the matching promotion, or null if it does not exist
     */
    public Promotion findById(String id) {
        for (Promotion promotion : promotions) {
            if (promotion.getId().equals(id)) {
                return promotion;
            }
        }

        return null;
    }

}
