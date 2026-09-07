package com.gamezone.model;
/**
 * Represents a customer of the GameZone store.
 * A customer is a person who buys products and is identified
 * additionally by an email address.
 */
public class Customer extends Person {
    private String email;
    /**
     * Creates a new customer with the given identification, name,
     * phone and email.
     *
     * @param id    the unique identification of the customer
     * @param name  the full name of the customer
     * @param phone the contact phone number of the customer
     * @param email the email address of the customer
     */
    public Customer(String id, String name, String phone, String email) {
        super(id, name, phone);
        this.email = email;
    }
    /**
     * Returns the email address of this customer.
     *
     * @return the customer's email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Updates the email address of this customer.
     *
     * @param email the new email address to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Returns the role of this person in the system.
     *
     * @return the literal string "Customer"
     */
    @Override
    public String getRole() {
        return "Customer";
    }
}
