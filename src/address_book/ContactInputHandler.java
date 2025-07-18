
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the Address Book project and may not be used, copied,
 * modified, or distributed without permission.
 */

package address_book;

import java.util.Scanner;

/**
 * Handles user input for creating or updating a contact.
 * 
 * This class collects and validates each contact field using the
 * {@link FieldValidator} and ensures that only valid data is accepted.
 * If a field fails validation, the user is prompted again until input is valid.
 * 
 * Used by AddressBook for adding or updating contact records.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class ContactInputHandler {

    /**
     * Prompts the user interactively to enter details for a new contact.
     * All fields are validated before creating the Contact object.
     *
     * @param scanner the scanner used to read input from the user
     * @return a valid Contact object with all fields populated
     */
    public Contact promptContactDetails(Scanner scanner) {
        String firstName = promptUntilValid(scanner, "Enter the first name: ", "first name", FieldValidator::isValidFirstName);
        String lastName = promptUntilValid(scanner, "Enter the last name: ", "last name", FieldValidator::isValidLastName);
        String street = promptUntilValid(scanner, "Enter the street address (include Apt# or P.O. Box if any): ", "street address", FieldValidator::isValidStreetAddress);
        String city = promptUntilValid(scanner, "Enter the city: ", "city name", FieldValidator::isValidCity);
        String state = promptUntilValid(scanner, "Enter the state: ", "state name", FieldValidator::isValidState);
        String zipCode = promptUntilValid(scanner, "Enter the zip code (12345 or 12345-6789): ", "zip code", FieldValidator::isValidZipCode);
        String phone = promptUntilValid(scanner, "Enter the phone number (e.g. 2105551212): ", "phone number", FieldValidator::isValidPhoneNumberFormatted);
        String email = promptUntilValid(scanner, "Enter the email address: ", "email", FieldValidator::isValidEmail);

        return new Contact(firstName, lastName, street, city, state, zipCode, phone, email);
    }

    /**
     * Prompts the user until a valid input is received based on validation rules.
     *
     * @param scanner    the scanner for reading user input
     * @param prompt     the prompt message shown to the user
     * @param fieldName  human-readable name of the field (used in error feedback)
     * @param validator  the validation function used to verify input
     * @return a valid input string accepted by the validator
     */
    private String promptUntilValid(Scanner scanner, String prompt, String fieldName, StringValidator validator) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (validator.isValid(input)) {
                return input;
            }
            System.out.println("Invalid " + fieldName + ". Please try again.");
        }
    }
}
