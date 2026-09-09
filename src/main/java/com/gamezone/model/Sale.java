package com.gamezone.model;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;


    /**
        * Represents a sale made in the GameZone store.
 */

public class Sale {

    private String saleId;
    private String date;
    private Customer customer;
    private Seller seller;
    private List<Product> products;

    /**
     * Creates a new sale with the given information.
     *
     * @param saleId    the sale's unique identifier
     * @param date      the date of the sale
     * @param customer  the customer who made the purchase
     * @param seller    the seller who made the sale
     * @param products  the products included in the sale
     */

    public Sale(String saleId, String date, Customer customer, Seller seller, List<Product> products) {
        this.saleId = saleId;
        this.date = date;
        this.customer = customer;
        this.seller = seller;
        this.products = products;
    }

        /**
         * Returns the sale's unique identifier.
         *
         * @return the sale's id
         */
    public String getSaleId() {
        return saleId;
    }

        /**
         * Sets the sale's unique identifier.
         *
         * @param saleId the new sale id
         */

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

        /**
         * Returns the date of the sale.
         *
         * @return the sale's date
         */
    public String getDate() {
        return date;
    }

        /**
         * Sets the date of the sale.
         *
         * @param date the new sale date
         */

    public void setDate(String date) {
        this.date = date;
    }
        /**
         * Returns the customer associated with the sale.
         *
         * @return the sale's customer
         */

    public Customer getCustomer() {
        return customer;
    }

        /**
         * Sets the customer associated with the sale.
         *
         * @param customer the new customer
         */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

        /**
         * Returns the seller associated with the sale.
         *
         * @return the sale's seller
         */


    public Seller getSeller() {
        return seller;
    }
        /**
         * Sets the seller associated with the sale.
         *
         * @param seller the new seller
         */

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
        /**
         * Returns the products included in the sale.
         *
         * @return the list of products
         */

    public List<Product> getProducts() {
        return products;
    }

        /**
         * Sets the products included in the sale.
         *
         * @param products the new list of products
         */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Calculates the total price of all products in the sale.
     *
     * @return the total price of the sale
     * */
    public double calculateTotal() {
        double total = 0;

        for (Product product : products) {
            total += product.getPrice();
        }

        return total;
    }

        public boolean canBeReturned() { if (date == null || date.isBlank()) {
            return false; }

            try {
                LocalDate saleDate = LocalDate.parse(date);
                LocalDate today = LocalDate.now();

                long days = ChronoUnit.DAYS.between(saleDate, today);

                return days >= 0 && days <= 30;

            } catch (DateTimeParseException e) {
                return false;
            }

        }
}
