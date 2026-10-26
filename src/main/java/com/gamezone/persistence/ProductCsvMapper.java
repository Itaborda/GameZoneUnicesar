package com.gamezone.persistence;

import com.gamezone.model.Product;

/**
 * Strategy that converts one concrete kind of {@link Product} to and from
 * its comma-separated representation. {@link ProductRepository} delegates
 * to these mappers, so supporting a new kind of product only requires a
 * new mapper and no change to the repository.
 */
public interface ProductCsvMapper {

    /**
     * Returns the value stored in the first column of the line to identify
     * the kind of product (for example {@code VIDEOGAME}).
     *
     * @return the type tag of the products handled by this mapper
     */
    String getTypeTag();

    /**
     * Indicates whether this mapper knows how to write the given product.
     *
     * @param product the product to check
     * @return {@code true} if the product is of the kind handled by this mapper
     */
    boolean supports(Product product);

    /**
     * Converts a product into one comma-separated line, starting with the type tag.
     *
     * @param product the product to convert; it must be supported by this mapper
     * @return the line to write in the file
     */
    String toCsvLine(Product product);

    /**
     * Rebuilds a product from the fields of one line of the file.
     *
     * @param fields the line split by commas; the first field is the type tag
     * @return the product described by the fields
     */
    Product fromCsvFields(String[] fields);
}