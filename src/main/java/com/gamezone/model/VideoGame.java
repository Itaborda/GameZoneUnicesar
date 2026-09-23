package com.gamezone.model;

/**
 * Represents a video game product sold in the GameZone store.
 * Extends {@link Product} with attributes specific to video games,
 * such as platform, genre, and age classification.
 */
public class VideoGame extends Product{

    private String platform;
    private String genre;
    private String ageClassification;

    /**
     * Creates a new video game with the given attributes.
     *
     * @param id                the product's unique identifier
     * @param title             the product's title
     * @param price             the product's price
     * @param stockQuantity     the available quantity in inventory
     * @param platform          the platform the game was developed for
     * @param genre             the game's genre
     * @param ageClassification the recommended age classification for the game
     */
    public VideoGame(String id, String title, double price, int stockQuantity, String platform, String genre, String ageClassification) {
        super(id, title, price, stockQuantity);
        this.platform = platform;
        this.genre = genre;
        this.ageClassification = ageClassification;
    }

    /**
     * Returns the platform the game was developed for.
     *
     * @return the game's platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Sets the platform the game was developed for.
     *
     * @param platform the new platform for the game
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * Returns the game's genre.
     *
     * @return the game's genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the game's genre.
     *
     * @param genre the new genre for the game
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Returns the recommended age classification for the game.
     *
     * @return the game's age classification
     */
    public String getAgeClassification() {
        return ageClassification;
    }

    /**
     * Sets the recommended age classification for the game.
     *
     * @param ageClassification the new age classification for the game
     */
    public void setAgeClassification(String ageClassification) {
        this.ageClassification = ageClassification;
    }

    /**
     * Returns a full description of the video game, combining its
     * inherited attributes with its platform, genre, and age
     * classification.
     *
     * @return a string describing the video game
     */
    @Override
    public String getDescription() {
        return "Titulo: " + getTitle() + ", Plataforma: " + platform + ", Genero: " + genre + ", Clasificacion: " + ageClassification + ", Precio: " + getPrice();
    }
}
