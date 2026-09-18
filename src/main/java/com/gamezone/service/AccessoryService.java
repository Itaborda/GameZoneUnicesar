package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;
import com.gamezone.persistence.AccessoryRepository;

import java.util.ArrayList;
import java.util.List;

public class AccessoryService {
    private AccessoryRepository accessoryRepository = new AccessoryRepository("data/accessories.csv");
    private List<Accessory> accessories;

    public AccessoryService(AccessoryRepository accessoryRepository) {
        this.accessoryRepository = accessoryRepository;
        this.accessories = accessoryRepository.loadAll();
    }

    public void registerController(Controller c) {
        accessories.add(c);
        accessoryRepository.saveAll(accessories);
    }
    public void registerCable(Cable cable) {
        accessories.add(cable);
        accessoryRepository.saveAll(accessories);
    }
    public void registerMemory(Memory memory) {
        accessories.add(memory);
        accessoryRepository.saveAll(accessories);
    }
    public List<Accessory> listAllAccessories() {
        return accessories;
    }
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
    public List<Accessory> findAccessoriesCompatibleWith(String consoleId) {
        List<Accessory> result = new ArrayList<>();
        for (Accessory a : accessories) {
            if (a.isCompatibleWith(consoleId)) {
                result.add(a);
            }
        }
        return result;
    }
    public Accessory findById(String id) {
        for (Accessory a : accessories) {
            if (id.equals(a.getId())) {
                return a;
            }
        }
        return null;
    }
    public void updateStock(String accessoryId, int quantity) {
        Accessory a = findById(accessoryId);
        if (a != null) {
            a.setStockQuantity(quantity);
            accessoryRepository.saveAll(accessories);
        }
    }
}

