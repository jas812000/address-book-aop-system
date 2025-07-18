
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

/**
 * Centralized validation logic for contact attributes.
 * 
 * This class provides static methods for validating individual fields
 * such as name, address, city, state, ZIP code, phone number, and email.
 * These methods are used by input handlers and validation-related aspects.
 * 
 * Each method returns {@code true} if the input is valid, {@code false} otherwise.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class FieldValidator {

    /**
     * Validates first name: allows letters, apostrophes, hyphens, and spaces.
     * Length must be between 2 and 30 characters.
     * 
     * @param name the first name input
     * @return true if valid, false otherwise
     */
    public static boolean isValidFirstName(String name) {
        return name.matches("^[A-Za-z][A-Za-z'\\-\\s]{1,29}$");
    }

    /**
     * Validates last name: allows letters, apostrophes, hyphens, and spaces.
     * Length must be between 2 and 30 characters.
     * 
     * @param name the last name input
     * @return true if valid, false otherwise
     */
    public static boolean isValidLastName(String name) {
        return name.matches("^[A-Za-z][A-Za-z'\\-\\s]{1,29}$");
    }

    /**
     * Validates street address: allows alphanumeric characters and common symbols
     * such as dots, commas, hashes, and hyphens.
     * Length must be between 5 and 100 characters.
     * 
     * @param street the street address input
     * @return true if valid, false otherwise
     */
    public static boolean isValidStreetAddress(String street) {
        return street.matches("^[A-Za-z0-9 .#,-]{5,100}$");
    }

    /**
     * Validates city: only letters and spaces allowed.
     * Length must be between 2 and 50 characters.
     * 
     * @param city the city name input
     * @return true if valid, false otherwise
     */
    public static boolean isValidCity(String city) {
        return city.matches("^[A-Za-z][A-Za-z\\s]{1,49}$");
    }

    /**
     * Validates state: alphabetic characters and spaces only.
     * Length must be between 2 and 50 characters, cannot be blank.
     * 
     * @param state the state name input
     * @return true if valid, false otherwise
     */
    public static boolean isValidState(String state) {
        return state.matches("^[A-Za-z ]{2,50}$") && !state.trim().isEmpty();
    }

    /**
     * Validates ZIP code: either 5 digits or ZIP+4 format (e.g., 12345-6789).
     * 
     * @param zip the ZIP code input
     * @return true if valid, false otherwise
     */
    public static boolean isValidZipCode(String zip) {
        return zip.matches("^\\d{5}(-\\d{4})?$");
    }

    /**
     * Validates phone number: must be exactly 10 digits after stripping
     * non-digit characters. Accepts common formatting styles.
     * 
     * @param phone the phone number input
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhoneNumberFormatted(String phone) {
        String digits = phone.replaceAll("\\D", "");
        return digits.matches("\\d{10}");
    }

    /**
     * Validates email address using a general pattern.
     * Accepts subdomains and typical email characters.
     * 
     * @param email the email address input
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }
}
