package com.gamezone.model;

public class VideoGame extends Product{

    private String platform;
    private String genre;
    private String ageClassification;

    public VideoGame(String id, String title, double price, int stockQuantity, String platform, String genre, String ageClassification) {
        super(id, title, price, stockQuantity);
        this.platform = platform;
        this.genre = genre;
        this.ageClassification = ageClassification;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAgeClassification() {
        return ageClassification;
    }

    public void setAgeClassification(String ageClassification) {
        this.ageClassification = ageClassification;
    }

    @Override
    public String getDescription() {
        return "Titulo: " + getTitle() + ", Plataforma: " + platform + ", Genero: " + genre + ", Clasificacion: " + ageClassification + ", Precio: " + getPrice();
    }
}
