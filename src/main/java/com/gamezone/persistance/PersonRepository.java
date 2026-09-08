package com.gamezone.persistance;

import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Seller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles file-based persistence for {@link Person} objects.
 * This class is responsible for saving and loading customers and
 * sellers to and from a CSV-formatted text file.
 */
public class PersonRepository {
    private String filePath;
    /**
     * Creates a new repository that reads from and writes to the
     * given file path.
     *
     * @param filePath the path of the file used to store person data
     */
    public PersonRepository(String filePath) {
        this.filePath = filePath;
    }
    /**
     * Saves the given list of people to the file, overwriting its
     * previous content. Each line represents one person, with the
     * columns depending on whether the person is a customer or a
     * seller.
     *
     * @param person the list of people to save
     */
    public void save (List<Person> person){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("role,id,name,phone,email,codeEmployee,shift");
            writer.newLine();
            for(Person p: person){
                String line="";
                if (p instanceof Customer) {
                    Customer c = (Customer) person;
                    line = c.getRole() + ","
                            + c.getId() + ","
                            + c.getName() + ","
                            + c.getPhone() + ","
                            + c.getEmail() + ", ,";
                } else if (p instanceof Seller) {
                    Seller s = (Seller) person;
                    line= s.getRole() + ","
                            + s.getId() + ","
                            + s.getName() + ","
                            + s.getPhone() + ", ,"
                            + s.getCodeEmployee() + ","
                            + s.getShift();
                }
                writer.write(line);
                writer.newLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads all people stored in the file and reconstructs them as
     * {@link Customer} or {@link Seller} objects based on the role
     * column of each line.
     *
     * @return the list of people loaded from the file
     */
    public List<Person> findAll(){
        List<Person> person = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                String role = data[0];
                String id = data[1];
                String name = data[2];
                String phone = data[3];

                if (role.equals("Customer")) {
                    String email = data[4];
                    Customer customer = new Customer(id, name, phone, email);
                    person.add(customer);

                } else if (role.equals("Seller")) {
                    String codeEmployee = data[5];
                    String shift = data[6];

                    Seller seller = new Seller(id, name, phone, codeEmployee, shift);

                    person.add(seller);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return person;
    }
}
