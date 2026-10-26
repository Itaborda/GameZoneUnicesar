package com.gamezone.persistence;

import com.gamezone.model.Promotion;

import java.util.List;

/**
 * Defines the persistence operations required by the promotion service,
 * hiding how and where promotions are stored.
 */
public interface PromotionStorage {

    /**
     * Saves the given promotions, replacing the previous content.
     *
     * @param promotions the list of promotions to save
     */
    void saveAll(List<Promotion> promotions);

    /**
     * Loads all stored promotions.
     *
     * @return the list of promotions loaded, or an empty list if there is no data
     */
    List<Promotion> loadAll();
}