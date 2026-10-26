package com.gamezone.persistence;

import com.gamezone.model.Console;
import com.gamezone.model.Product;

/**
 * Converts {@link Console} products to and from their comma-separated representation.
 */
final class ConsoleCsvMapper implements ProductCsvMapper {

    private static final String TYPE_TAG = "CONSOLE";

    /**
     * Returns the type tag used for consoles.
     *
     * @return {@code CONSOLE}
     */
    @Override
    public String getTypeTag() {
        return TYPE_TAG;
    }

    /**
     * Indicates whether the product is a console.
     *
     * @param product the product to check
     * @return {@code true} if the product is a {@link Console}
     */
    @Override
    public boolean supports(Product product) {
        return product instanceof Console;
    }

    /**
     * Converts a console into one comma-separated line.
     *
     * @param product the console to convert
     * @return the line to write in the file
     */
    @Override
    public String toCsvLine(Product product) {
        Console c = (Console) product;

        return TYPE_TAG + ","
                + c.getId() + ","
                + c.getTitle() + ","
                + c.getPrice() + ","
                + c.getStockQuantity() + ","
                + c.getBrand() + ","
                + c.getModel() + ","
                + c.getGeneration();
    }

    /**
     * Rebuilds a console from the fields of one line of the file.
     *
     * @param fields the line split by commas
     * @return the console described by the fields
     */
    @Override
    public Product fromCsvFields(String[] fields) {
        return new Console(
                fields[1],                     // id
                fields[2],                     // title
                Double.parseDouble(fields[3]), // price
                Integer.parseInt(fields[4]),   // stockQuantity
                fields[5],                     // brand
                fields[6],                     // model
                fields[7]                      // generation
        );
    }
}