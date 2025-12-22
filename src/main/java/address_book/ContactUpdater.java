
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
 * Provides functionality to update fields of an existing Contact object.
 * 
 * This class allows users to selectively modify contact information interactively.
 * Each field update is confirmed before being applied. A copy of the original contact
 * is returned to support logging or auditing prior to modification.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class ContactUpdater {

    /**
     * Interactively updates the fields of a contact based on user input.
     * 
     * For each field, the user is prompted for confirmation. If confirmed,
     * the new value is collected and applied to the contact.
     * 
     * @param contact the contact to be updated
     * @param scanner the Scanner used to collect user input
     * @return a snapshot of the original contact before updates
     */
    public static Contact updateFields(Contact contact, Scanner scanner) {
        // Create a snapshot of the original contact for logging
        Contact oldData = new Contact(
            contact.getFirstName(), contact.getLastName(), contact.getStreet(),
            contact.getCity(), contact.getState(), contact.getZipCode(),
            contact.getPhone(), contact.getEmail()
        );

        // Prompt user for each field and conditionally update
        if (confirm("Update first name? (y/n): ", scanner)) {
            System.out.print("New first name: ");
            contact.setFirstName(scanner.nextLine());
        }

        if (confirm("Update last name? (y/n): ", scanner)) {
            System.out.print("New last name: ");
            contact.setLastName(scanner.nextLine());
        }

        if (confirm("Update street address? (y/n): ", scanner)) {
            System.out.print("New street: ");
            contact.setStreet(scanner.nextLine());
        }

        if (confirm("Update city? (y/n): ", scanner)) {
            System.out.print("New city: ");
            contact.setCity(scanner.nextLine());
        }

        if (confirm("Update state? (y/n): ", scanner)) {
            System.out.print("New state: ");
            contact.setState(scanner.nextLine());
        }

        if (confirm("Update ZIP code? (y/n): ", scanner)) {
            System.out.print("New ZIP: ");
            contact.setZipCode(scanner.nextLine());
        }

        if (confirm("Update phone? (y/n): ", scanner)) {
            System.out.print("New phone: ");
            contact.setPhone(scanner.nextLine());
        }

        if (confirm("Update email? (y/n): ", scanner)) {
            System.out.print("New email: ");
            contact.setEmail(scanner.nextLine());
        }

        return oldData;
    }

    /**
     * Prompts the user for a yes/no confirmation.
     * 
     * Accepts 'y'/'yes' as confirmation and 'n'/'no' as rejection.
     * Repeats prompt until a valid response is entered.
     * 
     * @param message the message to display to the user
     * @param scanner the Scanner to read user input
     * @return true if confirmed, false otherwise
     */
    private static boolean confirm(String message, Scanner scanner) {
        while (true) {
            System.out.print(message);
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y") || response.equals("yes")) return true;
            if (response.equals("n") || response.equals("no")) return false;
            System.out.println("Please enter 'y' or 'n'.");
        }
    }
}
