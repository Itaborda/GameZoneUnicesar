package com.gamezone.persistence;

import com.gamezone.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles file-based persistence for {@link Warranty} objects.
 * This class is responsible for saving and loading basic and
 * extended warranties to and from a CSV-formatted text file,
 * using a type discriminator to reconstruct the correct concrete
 * subclass on load.
 */


public class WarrantyRepository {
    private String filePath;
    private ProductRepository productRepository;
    private SaleRepository saleRepository;
    /**
     * Creates a new WarrantyRepository using the default file path.
     *
     * @param productRepository the repository used to resolve product references
     * @param saleRepository    the repository used to resolve sale references
     */
    public WarrantyRepository(String filePath, ProductRepository productRepository, SaleRepository saleRepository) {
        this.filePath = "data/warranty.csv";
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
    }
    /**
     * Saves the given list of warranties to the file, overwriting its
     * previous content. Each line represents one warranty, with a
     * discriminator column indicating whether it is a basic or an
     * extended warranty.
     *
     * @param warranties the list of warranties to save
     */
    public void saveAll(List<Warranty> warranties) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("type,id,productId,saleId,startDate,endDate");
            writer.newLine();
            for (Warranty w : warranties) {
                String type;
                if (w instanceof BasicWarranty) {
                    type = "BASIC";
                } else if (w instanceof ExtendedWarranty) {
                    type = "EXTENDED";
                } else {
                    continue;
                }

                String line = type + ","
                        + w.getId() + ","
                        + w.getProduct().getId() + ","
                        + w.getSale().getSaleId() + ","
                        + w.getStartDate() + ","
                        + w.getEndDate();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads all warranties stored in the file and reconstructs them
     * as {@link BasicWarranty} or {@link ExtendedWarranty} objects
     * based on the type discriminator column, resolving their
     * product and sale references against the full lists returned
     * by the injected repositories. Returns an empty list if the
     * file does not exist.
     *
     * @return the list of warranties loaded from the file
     */
    public List<Warranty> loadAll() {
        List<Warranty> warranties = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return warranties;
        }

        List<Product> products = productRepository.findAll();
        List<Sale> sales = saleRepository.findAll();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                String type = data[0];
                String id = data[1];
                String productId = data[2];
                String saleId = data[3];
                LocalDate startDate = LocalDate.parse(data[4]);

                Product product = findProductById(products, productId);
                Sale sale = findSaleById(sales, saleId);

                if (product == null || sale == null) {
                    continue;
                }

                if (type.equals("BASIC")) {
                    warranties.add(new BasicWarranty(id, product, sale, startDate));
                } else if (type.equals("EXTENDED")) {
                    warranties.add(new ExtendedWarranty(id, product, sale, startDate));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return warranties;
    }
    /**
     * Searches the given list of products for the one matching the
     * given id.
     *
     * @param products  the products to search through
     * @param productId the id to look for
     * @return the matching product, or {@code null} if none is found
     */
    private Product findProductById(List<Product> products, String productId) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }
    /**
     * Searches the given list of sales for the one matching the
     * given id.
     *
     * @param sales  the sales to search through
     * @param saleId the id to look for
     * @return the matching sale, or {@code null} if none is found
     */

    private Sale findSaleById(List<Sale> sales, String saleId) {
        for (Sale s : sales) {
            if (s.getSaleId().equals(saleId)) {
                return s;
            }
        }
        return null;
    }

}
