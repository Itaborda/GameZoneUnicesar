package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.PromotionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionService {
    private PromotionRepository promotionRepository = new PromotionRepository("data/promotions.csv");
    private List<Promotion> promotions;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
        this.promotions = promotionRepository.loadAll();
    }
    public void registerPercentageDiscount(PercentageDiscount p) {
        promotions.add(p);
        promotionRepository.saveAll(promotions);
    }

    public void registerCategoryDiscount(CategoryDiscount p) {
        promotions.add(p);
        promotionRepository.saveAll(promotions);
    }
    public void registerBulkPurchaseDiscount(BulkPurchaseDiscount p) {
        promotions.add(p);
        promotionRepository.saveAll(promotions);
    }
    public List<Promotion> listAllPromotions() {
        return promotions;
    }
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

}
