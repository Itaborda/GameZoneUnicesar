package com.gamezone.persistence;
import com.gamezone.model.Accessory;

import java.util.List;

/**
 * Defines the persistence operations required by the accessory service,
 * hiding how and where accessories are stored.
 */
public interface AccessoryStorage {
    /**
     * Saves the given accessories, replacing the previous content.
     *
     * @param accessories the list of accessories to save
     */
    void saveAll(List<Accessory> accessories);

    /**
     * Loads all stored accessories.
     *
     * @return the list of accessories loaded, or an empty list if there is no data
     */
    List<Accessory> loadAll();
}
