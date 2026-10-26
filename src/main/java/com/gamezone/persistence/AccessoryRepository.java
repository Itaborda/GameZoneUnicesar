package com.gamezone.persistence;

import com.gamezone.model.Accessory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles file-based persistence for {@link Accessory} objects.
 * This class is responsible for saving and loading controllers,
 * cables, and memories to and from a CSV-formatted text file.
 */
public class AccessoryRepository implements AccessoryStorage {

    private final String filePath;
    private final AccessoryCsvMapper mapper = new AccessoryCsvMapper();

    /**
     * Creates a new repository that reads from and writes to the
     * given file path.
     *
     * @param filePath the path of the file used to store accessory data
     */
    public AccessoryRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the given list of accessories to the file, overwriting its
     * previous content. Each line represents one accessory, with the
     * columns depending on whether it is a controller, a cable, or a
     * memory.
     *
     * @param accessories the list of accessories to save
     */
    @Override
    public void saveAll(List<Accessory> accessories) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("type,id,title,price,stockQuantity,compatibleConsoleIds,connectionType,lengthInMeters,connectorType,capacityInGigabytes,memoryType");
            writer.newLine();
            for (Accessory a : accessories) {
                writer.write(mapper.toLine(a));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads all accessories stored in the file and reconstructs them as
     * controller, cable, or memory objects based
     * on the type column of each line.
     *
     * @return the list of accessories loaded from the file, or an empty
     * list if the file does not exist
     */
    @Override
    public List<Accessory> loadAll() {
        List<Accessory> accessories = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return accessories;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                Accessory accessory = mapper.fromFields(data);
                if (accessory != null) {
                    accessories.add(accessory);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessories;
    }
}