package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;
import com.gamezone.persistence.AccessoryRepository;

import java.util.ArrayList;
import java.util.List;
/**
 * Provides the business logic for managing accessories (controllers,
 * cables, and memories) in the GameZone system. This service is the
 * only class authorized to interact with {@link AccessoryRepository}
 * for persistence operations, keeping the current list of accessories
 * in memory to avoid reading the file on every operation.
 */
public class AccessoryService {
    private AccessoryRepository accessoryRepository = new AccessoryRepository("data/accessories.csv");
    private List<Accessory> accessories;
    /**
     * Creates a new accessory service backed by the given repository,
     * loading the initial list of accessories from it.
     *
     * @param accessoryRepository the repository used to persist accessories
     */
    public AccessoryService(AccessoryRepository accessoryRepository) {
        this.accessoryRepository = accessoryRepository;
        this.accessories = accessoryRepository.loadAll();
    }
    /**
     * Registers a new controller, adding it to the in-memory list and
     * persisting the updated list to the repository.
     *
     * @param c the controller to register
     */
    public void registerController(Controller c) {
        accessories.add(c);
        accessoryRepository.saveAll(accessories);
    }
    /**
     * Registers a new cable, adding it to the in-memory list and
     * persisting the updated list to the repository.
     *
     * @param cable the cable to register
     */
    public void registerCable(Cable cable) {
        accessories.add(cable);
        accessoryRepository.saveAll(accessories);
    }
    /**
     * Registers a new memory, adding it to the in-memory list and
     * persisting the updated list to the repository.
     *
     * @param m the memory to register
     */
    public void registerMemory(Memory m) {
        accessories.add(m);
        accessoryRepository.saveAll(accessories);
    }
    public List<Accessory> listAllAccessories() {
        return accessories;
    }
    /**
     * Returns all accessories managed by this service.
     *
     * @return the list of registered accessories
     */
    public List<Accessory> listAccessoriesByType(String type) {
        List<Accessory> result = new ArrayList<>();
        for (Accessory a : accessories) {
            if (type.equalsIgnoreCase("CONTROLLER") && a instanceof Controller) {
                result.add(a);
            } else if (type.equalsIgnoreCase("CABLE") && a instanceof Cable) {
                result.add(a);
            } else if (type.equalsIgnoreCase("MEMORY") && a instanceof Memory) {
                result.add(a);
            }
        }
        return result;
    }
    /**
     * Returns all accessories compatible with the given console.
     *
     * @param consoleId the id of the console to check
     * @return the list of accessories compatible with the given console
     */
    public List<Accessory> findAccessoriesCompatibleWith(String consoleId) {
        List<Accessory> result = new ArrayList<>();
        for (Accessory a : accessories) {
            if (a.isCompatibleWith(consoleId)) {
                result.add(a);
            }
        }
        return result;
    }
    /**
     * Finds an accessory by its identifier.
     *
     * @param id the identifier to search for
     * @return the matching accessory, or {@code null} if none is found
     */
    public Accessory findById(String id) {
        for (Accessory a : accessories) {
            if (id.equals(a.getId())) {
                return a;
            }
        }
        return null;
    }
    /**
     * Updates the stock quantity of the accessory with the given id,
     * persisting the change to the repository.
     *
     * @param accessoryId the id of the accessory to update
     * @param quantity    the new stock quantity
     */
    public void updateStock(String accessoryId, int quantity) {
        Accessory a = findById(accessoryId);
        if (a != null) {
            a.setStockQuantity(quantity);
            accessoryRepository.saveAll(accessories);
        }
    }
}

