package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.persistence.ReturnRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReturnService {
    private final ReturnRepository returnRepository;
    private final SaleService saleService;
    private final ProductService productService;
    private final List<Return> returns;

    public ReturnService(ProductService productService, ReturnRepository returnRepository, SaleService saleService, List<Return> returns) {
        this.productService = productService;
        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.returns = returns;
    }
    public Return registerReturn(String saleId, List<String> productIds, String reason) {

        Sale sale = null;
        for (Sale s : saleService.findAll()) {
            if (s.getSaleId().equals(saleId)) {
                sale = s;
                break;
            }
        }
        if (sale == null) {
            throw new IllegalArgumentException("La venta indicada no existe.");
        }
        if (!sale.canBeReturned()) {
            throw new IllegalArgumentException("La devolución no puede registrarse: han pasado más de 30 días desde la venta.");
        }

        List<Product> returnedProducts = new ArrayList<>();
        for (String productId : productIds) {
            Product matchedProduct = null;
            for (Product p : sale.getProducts()) {
                if (p.getId().equals(productId)) {
                    matchedProduct = p;
                    break;
                }
            }

            if (matchedProduct == null) {
                throw new IllegalArgumentException("El producto con id " + productId + " no pertenece a la venta indicada.");
            }

            returnedProducts.add(matchedProduct);
        }

        String returnId = "RET" + (returns.size() + 1);
        LocalDate returnDate = LocalDate.now();

        Return newReturn = new Return(returnId, returnDate, sale, returnedProducts, reason, 0);
        newReturn.calculateRefundAmount();

        for (Product product : returnedProducts) {
        productService.restoreStock(product.getId(), 1);
        }

        returns.add(newReturn);
        returnRepository.saveAll(returns);

        return newReturn;
    }
    public List<Return> viewAllReturns() {
        return returns;
    }
    public List<Return> viewReturnsByCustomer(String customerId) {
        List<Return> result = new ArrayList<>();

        for (Return r : returns) {
            if (r.getOriginalSale().getCustomer().getId().equals(customerId)) {
                result.add(r);
            }
        }

        return result;
    }
    public List<Return> viewReturnsBySale(String saleId) {
        List<Return> result = new ArrayList<>();

        for (Return r : returns) {
            if (r.getOriginalSale().getSaleId().equals(saleId)) {
                result.add(r);
            }
        }

        return result;
    }
    public double generateMonthlyBalance(int month, int year) {
        double totalSales = 0;
        double totalReturns = 0;

        for (Sale s : saleService.findAll()) {
            LocalDate saleDate = LocalDate.parse(s.getDate());
            if (saleDate.getMonthValue() == month && saleDate.getYear() == year) {
                totalSales += s.calculateTotal();
            }
        }



        for (Return r : returns) {
            if (r.getReturnDate().getMonthValue() == month && r.getReturnDate().getYear() == year) {
                totalReturns += r.getRefundAmount();
            }
        }

        return totalSales - totalReturns;
    }
}
