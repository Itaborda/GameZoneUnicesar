package com.gamezone.service;

import com.gamezone.model.Return;
import com.gamezone.model.Sale;

import java.time.LocalDate;
import java.util.List;

/**
 * Builds the monthly financial balance of the store by combining the
 * registered sales with the registered returns. It is a report-only class:
 * it reads sales and returns but never changes them.
 */
public class MonthlyBalanceReport {
    private final SaleService saleService;
    private final List<Return> returns;

    /**
     * Creates a report over the given sales and returns.
     *
     * @param saleService the service used to read the registered sales
     * @param returns     the in-memory list of registered returns
     */
    public MonthlyBalanceReport(SaleService saleService, List<Return> returns) {
        this.saleService = saleService;
        this.returns = returns;
    }

    /**
     * Calculates the net monthly balance for a given month and year, defined
     * as the total sales amount minus the total refunded amount from returns.
     *
     * @param month the month to evaluate (1-12)
     * @param year  the year to evaluate
     * @return the net balance (total sales minus total returns) for the
     *         specified month and year
     */
    public double generateMonthlyBalance(int month, int year) {
        return calculateMonthlySales(month, year) - calculateMonthlyReturns(month, year);
    }

    /**
     * Calculates the total sales amount for the given month and year,
     * using each sale's final total (subtotal minus discount plus
     * extended warranty cost).
     *
     * @param month the month to evaluate (1-12)
     * @param year  the year to evaluate
     * @return the sum of the final total of every sale registered in that month
     */
    public double calculateMonthlySales(int month, int year) {
        double totalSales = 0.0;
        for (Sale s : saleService.findAll()) {
            LocalDate saleDate = LocalDate.parse(s.getDate());
            if (saleDate.getMonthValue() == month && saleDate.getYear() == year) {
                totalSales += s.calculateTotal();
            }
        }
        return totalSales;
    }

    /**
     * Calculates the total refunded amount for the given month and year.
     *
     * @param month the month to evaluate (1-12)
     * @param year  the year to evaluate
     * @return the sum of the refund amount of every return registered in that month
     */
    public double calculateMonthlyReturns(int month, int year) {
        double totalReturns = 0.0;
        for (Return r : returns) {
            if (r.getReturnDate().getMonthValue() == month && r.getReturnDate().getYear() == year) {
                totalReturns += r.getRefundAmount();
            }
        }
        return totalReturns;
    }
}