package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.model.Console;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for persisting and retrieving products
 * using a text file.
 *
 * The repository supports both video games and consoles. Products
 * are stored as comma-separated values, with the first value
 * identifying the type of product.
 */
public class ProductRepository {

    private String filePath;

    /**
     * Creates a new ProductRepository with the specified file path.
     *
     * @param filePath the path of the file used to store the products
     */
    public ProductRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves all products to the configured file.
     *
     * Each product is stored on a separate line using comma-separated
     * values. The first value indicates whether the product is a
     * video game or a console.
     *
     * @param products the list of products to save
     */
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

    /**
     * Retrieves all products stored in the configured file.
     *
     * The method reads each line of the file, identifies the type
     * of product, and creates the corresponding VideoGame or Console
     * object using the stored data.
     *
     * @return a list containing all products found in the file
     */
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
                            data[1],                     // id
                            data[2],                     // title
                            Double.parseDouble(data[3]), // price
                            Integer.parseInt(data[4]),   // stockQuantity
                            data[5],                     // platform
                            data[6],                     // genre
                            data[7]                      // ageClassification
                    );

                    products.add(vg);

                } else if (type.equals("CONSOLE")) {

                    Console c = new Console(
                            data[1],                     // id
                            data[2],                     // title
                            Double.parseDouble(data[3]), // price
                            Integer.parseInt(data[4]),   // stockQuantity
                            data[5],                     // brand
                            data[6],                     // model
                            data[7]                      // generation
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