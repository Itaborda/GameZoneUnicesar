package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Converts {@link Warranty} objects to and from the raw rows of text handled
 * by {@code WarrantyRepository}. It keeps one converter for each kind of
 * warranty, selected by the class of the warranty when writing and by the
 * type tag when reading, so {@link WarrantyService} does not need to know the
 * row layout or check concrete types.
 * <p>
 * Row layout: {@code type, id, productId, saleId, startDate, endDate}
 */
class WarrantyRowMapper {

    private interface TypeConverter {
        Warranty create(String id, Product product, Sale sale, LocalDate startDate);
    }

    private final Map<Class<?>, String> typeByClass = new HashMap<>();
    private final Map<String, TypeConverter> converterByType = new HashMap<>();

    /**
     * Creates a mapper that knows basic and extended warranties.
     */
    WarrantyRowMapper() {
        register("BASIC", BasicWarranty.class, BasicWarranty::new);
        register("EXTENDED", ExtendedWarranty.class, ExtendedWarranty::new);
    }

    private void register(String type, Class<?> cls, TypeConverter converter) {
        typeByClass.put(cls, type);
        converterByType.put(type, converter);
    }

    /**
     * Converts a warranty into a raw row.
     *
     * @param warranty the warranty to convert
     * @return the row (type, id, productId, saleId, startDate, endDate), or
     * {@code null} if the kind of warranty is not supported
     */
    String[] toRow(Warranty warranty) {
        String type = typeByClass.get(warranty.getClass());
        if (type == null) {
            return null;
        }
        return new String[]{
                type,
                warranty.getId(),
                warranty.getProduct().getId(),
                warranty.getSale().getSaleId(),
                warranty.getStartDate().toString(),
                warranty.getEndDate().toString()
        };
    }

    /**
     * Builds a warranty from a raw row, resolving its product and sale
     * references through the given lookups.
     *
     * @param row            the raw row (type, id, productId, saleId, startDate, endDate)
     * @param productLookup  finds a product by its id, or returns {@code null}
     * @param saleLookup     finds a sale by its id, or returns {@code null}
     * @return the warranty, or {@code null} if the product or sale cannot be
     * resolved or the type tag is not supported
     */
    Warranty fromRow(String[] row, Function<String, Product> productLookup, Function<String, Sale> saleLookup) {
        String type = row[0];
        String id = row[1];
        LocalDate startDate = LocalDate.parse(row[4]);

        Product product = productLookup.apply(row[2]);
        Sale sale = saleLookup.apply(row[3]);
        if (product == null || sale == null) {
            return null;
        }

        TypeConverter converter = converterByType.get(type);
        return converter == null ? null : converter.create(id, product, sale, startDate);
    }
}