package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Person;

import java.util.List;

/**
 * Validation rules for the people module (customers and sellers).
 * <p>
 * This class only depends on the model and exceptions packages. It never
 * accesses repositories, services or files.
 * </p>
 */
public final class PersonValidator {

    private PersonValidator() {
    }

    /**
     * Validates the basic data of a person. The email is optional (sellers
     * do not have one), but when provided it must have a reasonable format.
     *
     * @param id    the person identifier (not null or blank)
     * @param name  the person name (not null or blank)
     * @param email the email; null or blank is accepted, otherwise it must
     *              contain an at sign followed by a dot
     * @param phone the phone number (not null or blank)
     * @throws InvalidDataException if any value is invalid
     */
    public static void validatePersonData(String id, String name, String email, String phone) {
        if (id == null || id.isBlank()) {
            throw new InvalidDataException("identificador", "no puede estar vacío");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("nombre", "no puede estar vacío");
        }
        if (phone == null || phone.isBlank()) {
            throw new InvalidDataException("teléfono", "no puede estar vacío");
        }
        if (email != null && !email.isBlank() && !hasReasonableEmailFormat(email.trim())) {
            throw new InvalidDataException("correo electrónico",
                    "debe tener un formato válido (por ejemplo: nombre@dominio.com)");
        }
    }

    /**
     * Validates that a person was found.
     *
     * @param person   the person returned by a search
     * @param personId the identifier that was searched
     * @throws ResourceNotFoundException if the person is null
     */
    public static void validatePersonExists(Person person, String personId) {
        if (person == null) {
            throw new ResourceNotFoundException("persona", personId);
        }
    }

    /**
     * Validates that no registered person already uses the given identifier.
     *
     * @param existing the people already registered
     * @param newId    the identifier of the person being registered
     * @throws BusinessRuleException if the identifier is already in use
     */
    public static void validateUniquePerson(List<Person> existing, String newId) {
        if (existing == null) {
            return;
        }
        for (Person person : existing) {
            if (person.getId() != null && person.getId().equals(newId)) {
                throw new BusinessRuleException(
                        "Ya existe una persona registrada con el identificador " + newId);
            }
        }
    }

    private static boolean hasReasonableEmailFormat(String email) {
        int at = email.indexOf('@');
        int dot = email.lastIndexOf('.');
        return at > 0 && dot > at + 1 && dot < email.length() - 1;
    }
}