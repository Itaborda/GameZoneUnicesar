package com.gamezone.services;

import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;

import java.util.ArrayList;
import java.util.List;
/**
 * Provides the business logic for managing people (customers and
 * sellers) in the GameZone system. This service is the only class
 * authorized to interact with {@link PersonRepository} for
 * persistence operations, keeping the current list of people in
 * memory to avoid reading the file on every operation.
 */
public class PersonService {
    private PersonRepository personRepository;
    private List<Person> person;
    /**
     * Creates a new person service backed by the given repository
     * and initialized with the given list of people already loaded.
     *
     * @param personRepository the repository used to persist people
     * @param person           the initial list of people managed by this service
     */
    public PersonService(PersonRepository personRepository, List<Person> person) {
        this.personRepository = personRepository;
        this.person = person;
    }
    /**
     * Registers a new customer, adding it to the in-memory list and
     * persisting the updated list to the repository.
     *
     * @param c the customer to register
     */
    public void registerCustomer(Customer c){
        person.add(c);
        personRepository.save(person);
    }
    /**
     * Returns all people managed by this service that are customers.
     *
     * @return the list of registered customers
     */
    public List<Customer> getAllCustomers() {
        List<Customer> c = new ArrayList<>();
        for (Person p : person) {
            if (p instanceof Customer) {
                c.add((Customer) p);
            }
        }
        return c;
    }
    /**
     * Returns all people managed by this service that are sellers.
     *
     * @return the list of registered sellers
     */
    public List<Seller> getAllSellers(){
        List<Seller> s = new ArrayList<>();
        for(Person p: person){
            if(p instanceof Seller){
                s.add((Seller) p);
            }
        }
        return s;
    }
    /**
     * Finds a customer by their identification.
     *
     * @param id the identification to search for
     * @return the matching customer, or {@code null} if none is found
     */
    public Customer findCustomerById(String id){
        for(Customer c: getAllCustomers()){
            if(id.equals(c.getId())){
                return c;
            }
        }
        return null;
    }
    /**
     * Finds a seller by their identification.
     *
     * @param id the identification to search for
     * @return the matching seller, or {@code null} if none is found
     */
    public Seller findSellerById(String id){
        for(Seller s: getAllSellers()){
            if(id.equals(s.getId())){
                return s;
            }
        }
        return null;
    }


}
