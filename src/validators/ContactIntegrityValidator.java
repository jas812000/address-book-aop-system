
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the TicTacToe project and may not be used, copied,
 * modified, or distributed without permission.
 */

package validators;

import address_book.Contact;

/**
 * The {@code ContactIntegrityValidator} class provides a centralized validation mechanism
 * for all fields of a {@link Contact} object.
 * 
 * It uses regular expressions to ensure proper formatting and throws
 * {@code IllegalArgumentException} when fields do not meet the expected criteria.
 * 
 * This class is intended for use by both aspects and user input layers.
 * 
 * Example usage:
 *     Contact contact = new Contact(...);
 *     ContactIntegrityValidator.validateContact(contact);
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class ContactIntegrityValidator {

    /**
     * Validates all individual fields of the specified {@link Contact}.
     *
     * @param c the Contact object to validate
     * @throws IllegalArgumentException if any field is invalid
     */
    public static void validateContact(Contact c) {
        if (!c.getFirstName().matches("^[A-Za-z'\\- ]{2,50}$")) {
            throw new IllegalArgumentException("Invalid first name.");
        }

        if (!c.getLastName().matches("^[A-Za-z'\\- ]{2,50}$")) {
            throw new IllegalArgumentException("Invalid last name.");
        }

        if (!c.getStreet().matches("^[A-Za-z0-9 .,'#\\-]{5,100}$")) {
            throw new IllegalArgumentException("Invalid street address.");
        }

        if (!c.getCity().matches("^[A-Za-z'\\- ]{2,}$")) {
            throw new IllegalArgumentException("Invalid city name.");
        }

        if (!c.getState().matches("^[A-Za-z ]{2,50}$")) {
            throw new IllegalArgumentException("Invalid state name.");
        }

        if (!c.getZipCode().matches("^\\d{5}(-\\d{4})?$")) {
            throw new IllegalArgumentException("Invalid ZIP code. Use 12345 or 12345-6789.");
        }

        String digits = c.getPhone().replaceAll("\\D", "");
        if (!digits.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Phone must contain exactly 10 digits.");
        }

        if (!c.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}
