package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;

/**
 * Converts {@link VideoGame} products to and from their comma-separated representation.
 */
final class VideoGameCsvMapper implements ProductCsvMapper {

    private static final String TYPE_TAG = "VIDEOGAME";

    /**
     * Returns the type tag used for video games.
     *
     * @return {@code VIDEOGAME}
     */
    @Override
    public String getTypeTag() {
        return TYPE_TAG;
    }

    /**
     * Indicates whether the product is a video game.
     *
     * @param product the product to check
     * @return {@code true} if the product is a {@link VideoGame}
     */
    @Override
    public boolean supports(Product product) {
        return product instanceof VideoGame;
    }

    /**
     * Converts a video game into one comma-separated line.
     *
     * @param product the video game to convert
     * @return the line to write in the file
     */
    @Override
    public String toCsvLine(Product product) {
        VideoGame vg = (VideoGame) product;

        return TYPE_TAG + ","
                + vg.getId() + ","
                + vg.getTitle() + ","
                + vg.getPrice() + ","
                + vg.getStockQuantity() + ","
                + vg.getPlatform() + ","
                + vg.getGenre() + ","
                + vg.getAgeClassification();
    }

    /**
     * Rebuilds a video game from the fields of one line of the file.
     *
     * @param fields the line split by commas
     * @return the video game described by the fields
     */
    @Override
    public Product fromCsvFields(String[] fields) {
        return new VideoGame(
                fields[1],                     // id
                fields[2],                     // title
                Double.parseDouble(fields[3]), // price
                Integer.parseInt(fields[4]),   // stockQuantity
                fields[5],                     // platform
                fields[6],                     // genre
                fields[7]                      // ageClassification
        );
    }
}