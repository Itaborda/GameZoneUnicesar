package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.model.Console;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private String filePath;

    public ProductRepository(String filePath) {
        this.filePath = filePath;
    }

    public void saveAll(List<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Product product : products) {
                String line = "";

                if (product instanceof VideoGame) {
                    VideoGame vg = (VideoGame) product;

                    line = "VIDEOGAME,"
                            + vg.getId() + ","
                            + vg.getTitle() + ","
                            + vg.getPrice() + ","
                            + vg.getStockQuantity() + ","
                            + vg.getPlatform() + ","
                            + vg.getGenre() + ","
                            + vg.getAgeClassification();

                } else if (product instanceof Console) {
                    Console c = (Console) product;

                    line = "CONSOLE,"
                            + c.getId() + ","
                            + c.getTitle() + ","
                            + c.getPrice() + ","
                            + c.getStockQuantity() + ","
                            + c.getBrand() + ","
                            + c.getModel() + ","
                            + c.getGeneration();
                }

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return products;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");
                String type = data[0];

                if (type.equals("VIDEOGAME")) {

                    VideoGame vg = new VideoGame(
                            data[1],                    // id
                            data[2],                    // title
                            Double.parseDouble(data[3]), // price
                            Integer.parseInt(data[4]),   // stockQuantity
                            data[5],                    // platform
                            data[6],                    // genre
                            data[7]                     // ageClassification
                    );

                    products.add(vg);

                } else if (type.equals("CONSOLE")) {

                    Console c = new Console(
                            data[1],                    // id
                            data[2],                    // title
                            Double.parseDouble(data[3]), // price
                            Integer.parseInt(data[4]),   // stockQuantity
                            data[5],                    // brand
                            data[6],                    // model
                            data[7]                     // generation
                    );

                    products.add(c);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }
}