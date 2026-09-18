package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Handles file-based persistence for {@link Accessory} objects.
 * This class is responsible for saving and loading controllers,
 * cables, and memories to and from a CSV-formatted text file.
 */
public class AccessoryRepository {
    private String filePath;
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
     * Creates a new AccessoryRepository using the default file path.
     */
    public AccessoryRepository() {
        this.filePath = "data/accessories.csv";
    }
    /**
     * Saves the given list of accessories to the file, overwriting its
     * previous content. Each line represents one accessory, with the
     * columns depending on whether it is a controller, a cable, or a
     * memory.
     *
     * @param accessories the list of accessories to save
     */
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
                String consoles = a.getCompatibleConsoleIds() == null
                        ? ""
                        : String.join(";", a.getCompatibleConsoleIds());
                String line = "";
                if (a instanceof Controller) {
                    Controller c = (Controller) a;
                    line = "CONTROLLER,"
                            + c.getId() + ","
                            + c.getTitle() + ","
                            + c.getPrice() + ","
                            + c.getStockQuantity() + ","
                            + consoles + ","
                            + c.getConnectionType() + ", , , ,";
                } else if (a instanceof Cable) {
                    Cable c = (Cable) a;
                    line = "CABLE,"
                            + c.getId() + ","
                            + c.getTitle() + ","
                            + c.getPrice() + ","
                            + c.getStockQuantity() + ","
                            + consoles + ", ,"
                            + c.getLengthInMeters() + ","
                            + c.getConnectorType() + ", ,";
                } else if (a instanceof Memory) {
                    Memory m = (Memory) a;
                    line = "MEMORY,"
                            + m.getId() + ","
                            + m.getTitle() + ","
                            + m.getPrice() + ","
                            + m.getStockQuantity() + ","
                            + consoles + ", , , ,"
                            + m.getCapacityInGigabytes() + ","
                            + m.getMemoryType();
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads all accessories stored in the file and reconstructs them as
     * {@link Controller}, {@link Cable}, or {@link Memory} objects based
     * on the type column of each line.
     *
     * @return the list of accessories loaded from the file, or an empty
     * list if the file does not exist
     */
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
                String type = data[0];
                String id = data[1];
                String title = data[2];
                double price = Double.parseDouble(data[3].trim());
                int stockQuantity = Integer.parseInt(data[4].trim());
                List<String> compatibleConsoleIds = data[5].trim().isEmpty()
                        ? new ArrayList<>()
                        : new ArrayList<>(Arrays.asList(data[5].split(";")));

                if (type.equals("CONTROLLER")) {
                    String connectionType = data[6].trim();
                    Controller controller = new Controller(id, title, price, stockQuantity, compatibleConsoleIds, connectionType);
                    accessories.add(controller);

                } else if (type.equals("CABLE")) {
                    double lengthInMeters = Double.parseDouble(data[7].trim());
                    String connectorType = data[8].trim();
                    Cable cable = new Cable(id, title, price, stockQuantity, compatibleConsoleIds, lengthInMeters, connectorType);
                    accessories.add(cable);

                } else if (type.equals("MEMORY")) {
                    int capacityInGigabytes = Integer.parseInt(data[9].trim());
                    String memoryType = data[10].trim();
                    Memory memory = new Memory(id, title, price, stockQuantity, compatibleConsoleIds, capacityInGigabytes, memoryType);
                    accessories.add(memory);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessories;
    }
}

