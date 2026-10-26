package com.gamezone.persistence;
import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.Person;

import java.util.List;

/**
 * Defines the persistence operations required by the person service,
 * hiding how and where people are stored.
 */

public interface PersonStorage {
    /**
     * Saves the given people, replacing the previous content.
     *
     * @param person the list of people to save
     * @throws PersistenceException if the data cannot be written
     */
    void save(List<Person> person);

    /**
     * Loads all stored people.
     *
     * @return the list of people loaded
     * @throws PersistenceException if the data cannot be read
     */
    List<Person> findAll();
}
