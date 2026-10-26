package com.gamezone.persistence;
import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.Product;
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

    private final String filePath;

    /**
     * Mappers that know how to write and read each kind of product.
     */
    private final List<ProductCsvMapper> mappers;

    /**
     * Creates a new ProductRepository using the default file path.
     */
    public ProductRepository() {
        this("data/product.csv");
    }

    /**
     * Creates a new ProductRepository with the specified file path.
     *
     * @param filePath the path of the file used to store the products
     */
    public ProductRepository(String filePath) {
        this(filePath, List.of(new VideoGameCsvMapper(), new ConsoleCsvMapper()));
    }

    /**
     * Creates a new ProductRepository with the specified file path and the
     * mappers that handle each kind of product. Supporting a new kind of
     * product only requires supplying one more mapper.
     *
     * @param filePath the path of the file used to store the products
     * @param mappers  the mappers used to write and read each kind of product
     */
    public ProductRepository(String filePath, List<ProductCsvMapper> mappers) {
        this.filePath = filePath;
        this.mappers = mappers;
    }

    /**
     * Saves all products to the configured file.
     *
     * Each product is stored on a separate line using comma-separated
     * values. The first value indicates whether the product is a
     * video game or a console.
     *
     * @param products the list of products to save
     * @throws PersistenceException if the file cannot be written
     */
    public void saveAll(List<Product> products) {
        File file = new File(filePath); File parent = file.getParentFile();
        if(parent!=null && !parent.exists()){
            parent.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Product product : products) {
                writer.write(toCsvLine(product));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new PersistenceException(
                    "Failed to write products to file '" + filePath + "' (operation: write)", e);
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
     * @throws PersistenceException if the file be read
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
                ProductCsvMapper mapper = findMapperByTag(data[0]);

                if (mapper != null) {
                    products.add(mapper.fromCsvFields(data));
                }
            }

        } catch (IOException e) {
            throw new PersistenceException(
                    "Failed to read products from file '" + filePath + "' (operation: read)", e);
        }

        return products;
    }

    /**
     * Converts a product to its line using the mapper that supports it.
     * A product no mapper supports is written as an empty line, as the
     * repository did before the mappers were introduced.
     *
     * @param product the product to convert
     * @return the line to write in the file
     */
    private String toCsvLine(Product product) {
        for (ProductCsvMapper mapper : mappers) {
            if (mapper.supports(product)) {
                return mapper.toCsvLine(product);
            }
        }

        return "";
    }

    /**
     * Finds the mapper that handles the given type tag.
     *
     * @param typeTag the value of the first column of a line
     * @return the matching mapper, or {@code null} if the tag is unknown
     */
    private ProductCsvMapper findMapperByTag(String typeTag) {
        for (ProductCsvMapper mapper : mappers) {
            if (mapper.getTypeTag().equals(typeTag)) {
                return mapper;
            }
        }

        return null;
    }
}