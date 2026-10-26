package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Seller;

import java.util.HashMap;
import java.util.Map;
/**
 * Converts people to and from the CSV lines used by
 * {@link PersonRepository}. It selects a converter according to the
 * role of each person, keeps the existing line format and only works
 * with text, so it never accesses files.
 */
public class PersonCsvMapper {
    private interface RoleConverter {
        String toLine(Person person);
        Person fromFields(String[] data);
    }

    private final Map<String, RoleConverter> converters = new HashMap<>();
    /**
     * Creates a mapper with one converter for each supported role:
     * customers and sellers.
     */
    public PersonCsvMapper() {
        converters.put("Customer", new CustomerConverter());
        converters.put("Seller", new SellerConverter());
    }

    /**
     * Converts a person into the CSV line used to store it.
     *
     * @param person the person to convert
     * @return the CSV line, or an empty string if the role is not supported
     */
    public String toLine(Person person) {
        RoleConverter converter = converters.get(person.getRole());
        return converter == null ? "" : converter.toLine(person);
    }

    /**
     * Builds a person from the fields of a CSV line.
     *
     * @param data the fields of the line, with the role in the first position
     * @return the person, or null if the role is not supported
     */
    public Person fromFields(String[] data) {
        RoleConverter converter = converters.get(data[0]);
        return converter == null ? null : converter.fromFields(data);
    }
    /** Converts customers; the seller columns are left empty. */
    private static class CustomerConverter implements RoleConverter {
        @Override
        public String toLine(Person person) {
            Customer c = (Customer) person;
            return c.getRole() + ","
                    + c.getId() + ","
                    + c.getName() + ","
                    + c.getPhone() + ","
                    + c.getEmail() + ", ,";
        }
        @Override
        public Person fromFields(String[] data) {
            return new Customer(data[1], data[2], data[3], data[4]);
        }
    }
    /** Converts sellers; the email column is left empty. */
    private static class SellerConverter implements RoleConverter {
        @Override
        public String toLine(Person person) {
            Seller s = (Seller) person;
            return s.getRole() + ","
                    + s.getId() + ","
                    + s.getName() + ","
                    + s.getPhone() + ", ,"
                    + s.getCodeEmployee() + ","
                    + s.getShift();
        }
        @Override
        public Person fromFields(String[] data) {
            return new Seller(data[1], data[2], data[3], data[5], data[6]);
        }
    }
}
