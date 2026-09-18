package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles file-based persistence for {@link Promotion} objects.
 * This class is responsible for saving and loading percentage,
 * category, and bulk purchase promotions to and from a
 * CSV-formatted text file.
 */
public class PromotionRepository {
    private String filePath;
    /**
     * Creates a new PromotionRepository using the default file path.
     */
    public PromotionRepository(String filePath) {
        this.filePath = "data/promotion.csv";
    }
    /**
     * Saves the given list of promotions to the file, overwriting its
     * previous content. Each line represents one promotion, with the
     * columns depending on whether it is a percentage, category, or
     * bulk purchase discount.
     *
     * @param promotions the list of promotions to save
     */
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
                String line = "";
                if (p instanceof PercentageDiscount) {
                    PercentageDiscount pd = (PercentageDiscount) p;
                    line = "PERCENTAGE,"
                            + pd.getId() + ","
                            + pd.getName() + ","
                            + pd.getStartDate() + ","
                            + pd.getEndDate() + ","
                            + pd.getDiscountPercentage() + ", ,";
                } else if (p instanceof CategoryDiscount) {
                    CategoryDiscount cd = (CategoryDiscount) p;
                    line = "CATEGORY,"
                            + cd.getId() + ","
                            + cd.getName() + ","
                            + cd.getStartDate() + ","
                            + cd.getEndDate() + ","
                            + cd.getDiscountPercentage() + ","
                            + cd.getTargetCategory() + ",";
                } else if (p instanceof BulkPurchaseDiscount) {
                    BulkPurchaseDiscount bd = (BulkPurchaseDiscount) p;
                    line = "BULK,"
                            + bd.getId() + ","
                            + bd.getName() + ","
                            + bd.getStartDate() + ","
                            + bd.getEndDate() + ","
                            + bd.getDiscountPercentage() + ", ,"
                            + bd.getMinimumQuantity();
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads all promotions stored in the file and reconstructs them as
     * {@link PercentageDiscount}, {@link CategoryDiscount}, or
     * {@link BulkPurchaseDiscount} objects based on the type column
     * of each line.
     *
     * @return the list of promotions loaded from the file, or an empty
     * list if the file does not exist
     */
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
                String type = data[0];
                String id = data[1];
                String name = data[2];
                LocalDate startDate = LocalDate.parse(data[3].trim());
                LocalDate endDate = LocalDate.parse(data[4].trim());

                if (type.equals("PERCENTAGE")) {
                    double percentage = Double.parseDouble(data[5].trim());
                    PercentageDiscount pd = new PercentageDiscount(id, name, startDate, endDate, percentage);
                    promotions.add(pd);

                } else if (type.equals("CATEGORY")) {
                    double percentage = Double.parseDouble(data[5].trim());
                    String targetCategory = data[6].trim();
                    CategoryDiscount cd = new CategoryDiscount(id, name, startDate, endDate, percentage, targetCategory);
                    promotions.add(cd);

                } else if (type.equals("BULK")) {
                    double percentage = Double.parseDouble(data[5].trim());
                    int minimumQuantity = Integer.parseInt(data[7].trim());
                    BulkPurchaseDiscount bd = new BulkPurchaseDiscount(id, name, startDate, endDate, minimumQuantity, percentage);
                    promotions.add(bd);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return promotions;
    }

}
