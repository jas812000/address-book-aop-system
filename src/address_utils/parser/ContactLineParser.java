
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the Address Book project and may not be used, copied,
 * modified, or distributed without permission.
 */

package address_utils.parser;

import address_book.Contact;
import io.LineParser;

/**
 * Parses a line of CSV values into a {@link Contact} object.
 * <p>
 * This parser is used by the file loading system to convert raw CSV strings
 * into structured Contact instances.
 * <p>
 * Expected order of fields in the CSV:
 * <pre>
 * First Name, Last Name, Street, City, State, Zip, Phone, Email
 * </pre>
 * 
 * If a line does not contain at least 8 tokens, parsing fails and returns null.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class ContactLineParser implements LineParser<Contact> {

    /**
     * Converts an array of CSV tokens into a {@link Contact} object.
     * 
     * @param tokens an array of string values from a single CSV line
     * @return a new Contact if the line is valid; null otherwise
     */
    @Override
    public Contact parse(String[] tokens) {
        if (tokens.length < 8) return null;

        return new Contact(
            tokens[0].trim(), // First name
            tokens[1].trim(), // Last name
            tokens[2].trim(), // Street
            tokens[3].trim(), // City
            tokens[4].trim(), // State
            tokens[5].trim(), // ZIP code
            tokens[6].trim(), // Phone
            tokens[7].trim()  // Email
        );
    }
}


