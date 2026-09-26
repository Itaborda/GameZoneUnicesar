package com.gamezone.persistence;

import com.gamezone.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file-based persistence for warranty records.
 * This class is responsible for saving and loading basic and
 * extended warranty data to and from a CSV-formatted text file.
 * It stores and returns only raw rows of text (type, id, product
 * id, sale id, start date, end date) instead of resolved
 * {@code Product} and {@code Sale} objects, so it has no
 * dependency on other repositories or services. Resolving the
 * actual references is the responsibility of the service layer.
 */

public class WarrantyRepository {
    private String filePath;
    /**
     * Creates a new WarrantyRepository using the default file path.
     */
    public WarrantyRepository() {
        this.filePath = "data/warranties.csv";

    }
    /**
     * Saves the given list of raw warranty rows to the file,
     * overwriting its previous content. Each row must follow the
     * column order: type, id, productId, saleId, startDate, endDate.
     *
     * @param rows the list of raw warranty rows to save
     */
    public void saveAll(List<String[]> rows) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("type,id,productId,saleId,startDate,endDate");
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads all warranty rows stored in the file as raw text data,
     * without resolving product or sale references. Each returned
     * row follows the column order: type, id, productId, saleId,
     * startDate, endDate. Returns an empty list if the file does
     * not exist.
     *
     * @return the list of raw warranty rows loaded from the file
     */
    public List<String[]> loadAll() {
        List<String[]> rows = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return rows;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                rows.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

}
