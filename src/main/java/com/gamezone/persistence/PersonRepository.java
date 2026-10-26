package com.gamezone.persistence;
import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.Person;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles file-based persistence for {@link Person} objects.
 * This class is responsible for reading and writing the CSV file,
 * while the conversion of each person to and from a line is
 * delegated to {@link PersonCsvMapper}.
 */
public class PersonRepository implements PersonStorage {
    private final PersonCsvMapper mapper = new PersonCsvMapper();
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
     * previous content. Each line is produced by the mapper according
     * to the role of the person.
     *
     * @param person the list of people to save
     * @throws PersistenceException if the file cannot be written
     */
    @Override
    public void save (List<Person> person){
        File file = new File(filePath); File parent = file.getParentFile();
        if(parent!=null && !parent.exists()){
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("role,id,name,phone,email,codeEmployee,shift");
            writer.newLine();
            for (Person p : person) {
                writer.write(mapper.toLine(p));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new PersistenceException(
                    "Failed to write people to file '" + filePath + "' (operation: write)", e);
        }
    }
    /**
     * Loads all people stored in the file. Each line is converted by the
     * mapper, and lines whose role is not supported are skipped.
     *
     * @return the list of people loaded from the file
     * @throws PersistenceException if the file cannot be read
     */
    @Override
    public List<Person> findAll(){
        List<Person> person = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);
                Person p = mapper.fromFields(data);
                if (p != null) {
                    person.add(p);
                }
            }

        } catch (IOException e) {
            throw new PersistenceException(
                    "Failed to read people from file '" + filePath + "' (operation: read)", e);
        }

        return person;
    }
}
