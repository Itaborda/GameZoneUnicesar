package com.gamezone.persistence;

import com.gamezone.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WarrantyRepository {
    private String filePath;
    private ProductRepository productRepository;
    private SaleRepository saleRepository;

    public WarrantyRepository(String filePath, ProductRepository productRepository, SaleRepository saleRepository) {
        this.filePath = "data/warranty.csv";
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
    }
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
    private Product findProductById(List<Product> products, String productId) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }
    private Sale findSaleById(List<Sale> sales, String saleId) {
        for (Sale s : sales) {
            if (s.getSaleId().equals(saleId)) {
                return s;
            }
        }
        return null;
    }

}
