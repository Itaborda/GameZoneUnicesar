package com.gamezone.model;
/**
 * Represents a generic person in the GameZone system.
 * This class is declared abstract because a "generic person"
 * without a specific role (customer or seller) should not be
 * instantiated directly. Every subclass must define its own role.
 */
public abstract class Person {
    private String id;
    private String name;
    private String phone;
    /**
     * Creates a new person with the given identification, name and phone.
     *
     * @param id    the unique identification of the person
     * @param name  the full name of the person
     * @param phone the contact phone number of the person
     */
    public Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
    /**
     * Returns the identification of this person.
     *
     * @return the person's id
     */
    public String getId() {
        return id;
    }
    /**
     * Updates the identification of this person.
     *
     * @param id the new id to assign
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * Returns the name of this person.
     *
     * @return the person's name
     */
    public String getName() {
        return name;
    }
    /**
     * Updates the name of this person.
     *
     * @param name the new name to assign
     */

    public void setName(String name) {
        this.name = name;
    }
    /**
     * Returns the contact phone number of this person.
     *
     * @return the person's phone number
     */
    public String getPhone() {
        return phone;
    }
    /**
     * Updates the contact phone number of this person.
     *
     * @param phone the new phone number to assign
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * Returns the role this person plays in the system
     * (e.g. "Customer" or "Seller"). Each subclass must
     * implement this method according to its own role.
     *
     * @return the role of this person as a string
     */
    public abstract String getRole();
}
