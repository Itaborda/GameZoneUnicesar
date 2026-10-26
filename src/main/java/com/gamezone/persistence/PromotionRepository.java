package com.gamezone.persistence;

import com.gamezone.model.Promotion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles file-based persistence for {@link Promotion} objects.
 * This class is responsible for reading and writing the CSV file,
 * while the conversion of each promotion to and from a line is
 * delegated to {@link PromotionCsvMapper}.
 */
public class PromotionRepository implements PromotionStorage {
    private final PromotionCsvMapper mapper = new PromotionCsvMapper();
    private String filePath;
    /**
     * Creates a new PromotionRepository using the default file path.
     */

    public PromotionRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the given list of promotions to the file, overwriting its
     * previous content. Each line is produced by the mapper according
     * to the kind of promotion.
     *
     * @param promotions the list of promotions to save
     */
    @Override
    public void saveAll(List<Promotion> promotions) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("type,id,name,startDate,endDate,percentage,targetCategory,minimumQuantity");
            writer.newLine();
            for (Promotion p : promotions) {
                writer.write(mapper.toLine(p));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads all promotions stored in the file. Each line is converted by
     * the mapper, and lines whose type is not supported are skipped.
     *
     * @return the list of promotions loaded from the file, or an empty
     * list if the file does not exist
     */
    @Override
    public List<Promotion> loadAll() {
        List<Promotion> promotions = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return promotions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                Promotion promotion = mapper.fromFields(data);
                if (promotion != null) {
                    promotions.add(promotion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return promotions;
    }

}