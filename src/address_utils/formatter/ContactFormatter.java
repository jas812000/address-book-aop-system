
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
 * Provides string formatting utilities for Contact objects.
 * 
 * Supports both:
 * - Full formatted output for detailed display
 * - Compact format for contact selection menus or summaries
 * 
 * Formatting helps standardize the appearance of contacts across UI views,
 * console logs, and file outputs.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class ContactFormatter {
    
    /**
     * Produces a multi-line, decorated string of all contact fields.
     * 
     * This method is commonly used for user-facing displays, logs, and file output.
     * 
     * @param contact the Contact to format
     * @return a multi-line string including all fields, or error message if null
     */
    public static String format(Contact contact) {
        if (contact == null) return "[Invalid Contact]";
        
        return "---------------------------------\n" +
               contact.getFirstName() + " " + contact.getLastName() + "\n" +
               contact.getStreet() + "\n" +
               contact.getCity() + ", " + contact.getState() + " " + contact.getZipCode() + "\n" +
               contact.getPhone() + "\n" +
               contact.getEmail() + "\n" +
               "---------------------------------";
    }

    /**
     * Produces a single-line string for identifying a contact.
     * 
     * This is useful for lists or dropdowns where only the name is needed.
     * 
     * @param contact the Contact to format
     * @return the compact name in "First Last" format
     */
    public static String formatCompact(Contact contact) {
        return contact.getFirstName() + " " + contact.getLastName();
    }
}


