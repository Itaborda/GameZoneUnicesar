package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionRepository {
    private String filePath;

    public PromotionRepository(String filePath) {
        this.filePath = "data/promotion.csv";
    }
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
