
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the Address Book project and may not be used, copied,
 * modified, or distributed without permission.
 */

package address_utils.formatter;

import address_book.Contact;

/**
 * Utility class for converting {@link Contact} objects into CSV-formatted strings.
 * 
 * Supports:
 * - Generating a CSV header line
 * - Escaping fields with quotes and commas
 * - Outputting full contact records as comma-separated lines
 * 
 * Used primarily for data persistence in flat files.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class ContactCSVFormatter {

    /**
     * Returns the header row for the CSV file.
     * 
     * @return a comma-separated string representing column names
     */
    public static String header() {
        return "First Name,Last Name,Street,City,State,Zip,Phone,Email";
    }

    /**
     * Converts a Contact into a properly escaped CSV row.
     * 
     * Fields are quoted if they contain commas or quotes,
     * and internal quotes are escaped according to CSV standards.
     * 
     * @param c the Contact to format
     * @return a comma-separated string representing the contact
     */
    public static String toCSV(Contact c) {
        if (c == null) return "";

        return String.join(",",
            escape(c.getFirstName()),
            escape(c.getLastName()),
            escape(c.getStreet()),
            escape(c.getCity()),
            escape(c.getState()),
            escape(c.getZipCode()),
            escape(c.getPhone()),
            escape(c.getEmail())
        );
    }

    /**
     * Escapes special characters in a CSV field.
     * 
     * Fields with quotes or commas are wrapped in double quotes.
     * Quotes inside the field are duplicated.
     * 
     * @param value the raw field value
     * @return the escaped CSV-safe value
     */
    private static String escape(String value) {
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
