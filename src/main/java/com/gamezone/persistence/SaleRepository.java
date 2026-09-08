package com.gamezone.persistence;

import com.gamezone.model.Sale;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the persistence of sales.
 */
public class SaleRepository {

    private final List<Sale> sales;
    private final String filePath;

    /**
     * Creates a new sale persistence manager.
     */
    public SaleRepository() {
        this.sales = new ArrayList<>();
        this.filePath = "sales.csv";
    }

    /**
     * Saves a sale to the CSV file.
     *
     * @param sale the sale to save
     */
    public void save(Sale sale) {
        sales.add(sale);

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath, true))) {

            writer.write(
                    sale.getSaleId() + "," +
                            sale.getDate() + "," +
                            sale.getCustomer().getId() + "," +
                            sale.getSeller().getId() + "," +
                            sale.calculateTotal()
            );

            writer.newLine();

        } catch (IOException e) {
            System.out.println("Error saving sale: " + e.getMessage());
        }
    }

    /**
     * Returns all registered sales.
     *
     * @return the list of sales
     */
    public List<Sale> findAll() {
        return sales;
    }
}