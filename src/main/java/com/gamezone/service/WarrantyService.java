package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarrantyService {
    private SaleRepository saleRepository;
    private ProductService productService;
    private WarrantyRepository warrantyRepository;
    private List<Warranty> warranties;

    public WarrantyService(WarrantyRepository warrantyRepository, SaleRepository saleRepository, ProductService productService) {
        this.warrantyRepository = warrantyRepository;
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.warranties = resolveAll(warrantyRepository.loadAll());
    }

    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) {
        BasicWarranty warranty = new BasicWarranty(generateId(), product, sale, startDate);
        warranties.add(warranty);
        persist();
        return warranty;
    }
    /**
     * Creates and persists an extended warranty for the given
     * product and sale.
     *
     * @param product   the product covered by the warranty
     * @param sale      the sale the warranty is associated with
     * @param startDate the date the warranty starts
     * @return the newly created extended warranty
     */
    public ExtendedWarranty assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) {
        ExtendedWarranty warranty = new ExtendedWarranty(generateId(), product, sale, startDate);
        warranties.add(warranty);
        persist();
        return warranty;
    }

    public Warranty findWarrantyByProduct(String productId, String saleId) {
        for (Warranty w : warranties) {
            if (w.getProduct().getId().equals(productId) && w.getSale().getSaleId().equals(saleId)) {
                return w;
            }
        }
        return null;
    }

    public List<Warranty> listAllWarranties() {
        return warranties;
    }

    public List<Warranty> listActiveWarranties() {
        List<Warranty> active = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Warranty w : warranties) {
            if (w.isActive(today)) {
                active.add(w);
            }
        }
        return active;
    }

    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {
        List<Warranty> expiringSoon = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(daysAhead);
        for (Warranty w : warranties) {
            LocalDate endDate = w.getEndDate();
            if (!endDate.isBefore(today) && !endDate.isAfter(limit)) {
                expiringSoon.add(w);
            }
        }
        return expiringSoon;
    }
    private List<Warranty> resolveAll(List<String[]> rows) {
        List<Warranty> resolved = new ArrayList<>();
        List<Sale> sales = saleRepository.findAll();

        for (String[] row : rows) {
            String type = row[0];
            String id = row[1];
            String productId = row[2];
            String saleId = row[3];
            LocalDate startDate = LocalDate.parse(row[4]);

            Product product = productService.findById(productId);
            Sale sale = findSaleById(sales, saleId);
            if (product == null || sale == null) {
                continue;
            }

            if (type.equals("BASIC")) {
                resolved.add(new BasicWarranty(id, product, sale, startDate));
            } else if (type.equals("EXTENDED")) {
                resolved.add(new ExtendedWarranty(id, product, sale, startDate));
            }
        }
        return resolved;
    }
    
    private String generateId() {
        return UUID.randomUUID().toString();
    }
    public void persist(){
        List<String[]> rows = new ArrayList<>();
        for (Warranty w : warranties) {
            String type;
            if (w instanceof BasicWarranty) {
                type = "BASIC";
            } else if (w instanceof ExtendedWarranty) {
                type = "EXTENDED";
            } else {
                continue;
            }

            String[] row = {
                    type,
                    w.getId(),
                    w.getProduct().getId(),
                    w.getSale().getSaleId(),
                    w.getStartDate().toString(),
                    w.getEndDate().toString()
            };
            rows.add(row);
        }
        warrantyRepository.saveAll(rows);

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
