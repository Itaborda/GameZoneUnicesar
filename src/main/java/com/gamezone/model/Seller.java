package com.gamezone.model;
/**
 * Represents a seller (employee) of the GameZone store.
 * A seller is a person who attends customers and registers sales,
 * identified additionally by an employee code and a work shift.
 */
public class Seller extends Person{
    private String codeEmployee;
    private String shift;
    /**
     * Creates a new seller with the given identification, name, phone,
     * employee code and work shift.
     *
     * @param id           the unique identification of the seller
     * @param name         the full name of the seller
     * @param phone        the contact phone number of the seller
     * @param codeEmployee the unique employee code assigned to the seller
     * @param shift        the work shift assigned to the seller
     */
    public Seller(String id, String name, String phone, String codeEmployee, String shift) {
        super(id, name, phone);
        this.codeEmployee = codeEmployee;
        this.shift = shift;
    }
    /**
     * Returns the employee code of this seller.
     *
     * @return the seller's employee code
     */
    public String getCodeEmployee() {
        return codeEmployee;
    }
    /**
     * Updates the employee code of this seller.
     *
     * @param codeEmployee the new employee code to assign
     */
    public void setCodeEmployee(String codeEmployee) {
        this.codeEmployee = codeEmployee;
    }
    /**
     * Returns the work shift of this seller.
     *
     * @return the seller's work shift
     */
    public String getShift() {
        return shift;
    }
    /**
     * Updates the work shift of this seller.
     *
     * @param shift the new work shift to assign
     */
    public void setShift(String shift) {
        this.shift = shift;
    }
    /**
     * Returns the role of this person in the system.
     *
     * @return the literal string "Seller"
     */
    @Override
    public String getRole() {
        return "Seller";
    }
}
