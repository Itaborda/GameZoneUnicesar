package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts {@link Promotion} objects to and from the comma-separated lines
 * used by {@link PromotionRepository}. It keeps one converter for each kind
 * of promotion, selected by the class of the promotion when writing and by
 * the type tag when reading. It only works with text, so it never accesses
 * files.
 * <p>
 * Column layout:
 * {@code type,id,name,startDate,endDate,percentage,targetCategory,minimumQuantity}
 */
public class PromotionCsvMapper {

    private interface TypeConverter {
        String toLine(Promotion promotion);

        Promotion fromFields(String[] data, String id, String name, LocalDate startDate, LocalDate endDate);
    }

    private final Map<Class<?>, TypeConverter> byClass = new HashMap<>();
    private final Map<String, TypeConverter> byType = new HashMap<>();

    /**
     * Creates a mapper with one converter for each supported promotion:
     * percentage, category and bulk purchase discounts.
     */
    public PromotionCsvMapper() {
        register("PERCENTAGE", PercentageDiscount.class, new PercentageConverter());
        register("CATEGORY", CategoryDiscount.class, new CategoryConverter());
        register("BULK", BulkPurchaseDiscount.class, new BulkConverter());
    }

    private void register(String type, Class<?> cls, TypeConverter converter) {
        byClass.put(cls, converter);
        byType.put(type, converter);
    }

    /**
     * Converts a promotion into the CSV line used to store it.
     *
     * @param promotion the promotion to convert
     * @return the CSV line, or an empty string if the kind of promotion is not supported
     */
    public String toLine(Promotion promotion) {
        TypeConverter converter = byClass.get(promotion.getClass());
        return converter == null ? "" : converter.toLine(promotion);
    }

    /**
     * Builds a promotion from the fields of a CSV line. The common columns
     * (id, name and dates) are always read, so a malformed date is reported
     * even when the type is not supported.
     *
     * @param data the fields of the line, with the type tag in the first position
     * @return the promotion, or {@code null} if the type tag is not supported
     */
    public Promotion fromFields(String[] data) {
        String id = data[1];
        String name = data[2];
        LocalDate startDate = LocalDate.parse(data[3].trim());
        LocalDate endDate = LocalDate.parse(data[4].trim());

        TypeConverter converter = byType.get(data[0]);
        return converter == null ? null : converter.fromFields(data, id, name, startDate, endDate);
    }

    /** Converts percentage discounts; the category and quantity columns are left empty. */
    private static class PercentageConverter implements TypeConverter {
        @Override
        public String toLine(Promotion promotion) {
            PercentageDiscount pd = (PercentageDiscount) promotion;
            return "PERCENTAGE,"
                    + pd.getId() + ","
                    + pd.getName() + ","
                    + pd.getStartDate() + ","
                    + pd.getEndDate() + ","
                    + pd.getDiscountPercentage() + ", ,";
        }

        @Override
        public Promotion fromFields(String[] data, String id, String name, LocalDate startDate, LocalDate endDate) {
            double percentage = Double.parseDouble(data[5].trim());
            return new PercentageDiscount(id, name, startDate, endDate, percentage);
        }
    }

    /** Converts category discounts; the quantity column is left empty. */
    private static class CategoryConverter implements TypeConverter {
        @Override
        public String toLine(Promotion promotion) {
            CategoryDiscount cd = (CategoryDiscount) promotion;
            return "CATEGORY,"
                    + cd.getId() + ","
                    + cd.getName() + ","
                    + cd.getStartDate() + ","
                    + cd.getEndDate() + ","
                    + cd.getDiscountPercentage() + ","
                    + cd.getTargetCategory() + ",";
        }

        @Override
        public Promotion fromFields(String[] data, String id, String name, LocalDate startDate, LocalDate endDate) {
            double percentage = Double.parseDouble(data[5].trim());
            String targetCategory = data[6].trim();
            return new CategoryDiscount(id, name, startDate, endDate, percentage, targetCategory);
        }
    }

    /** Converts bulk purchase discounts; the category column is left empty. */
    private static class BulkConverter implements TypeConverter {
        @Override
        public String toLine(Promotion promotion) {
            BulkPurchaseDiscount bd = (BulkPurchaseDiscount) promotion;
            return "BULK,"
                    + bd.getId() + ","
                    + bd.getName() + ","
                    + bd.getStartDate() + ","
                    + bd.getEndDate() + ","
                    + bd.getDiscountPercentage() + ", ,"
                    + bd.getMinimumQuantity();
        }

        @Override
        public Promotion fromFields(String[] data, String id, String name, LocalDate startDate, LocalDate endDate) {
            double percentage = Double.parseDouble(data[5].trim());
            int minimumQuantity = Integer.parseInt(data[7].trim());
            return new BulkPurchaseDiscount(id, name, startDate, endDate, minimumQuantity, percentage);
        }
    }
}