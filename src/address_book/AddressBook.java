
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

import java.util.*;

/**
 * Manages the list of contact entries in the address book.
 * 
 * Provides methods to add, delete, update, and retrieve contacts.
 * Acts as the core domain class for managing in-memory contact data.
 * Delegates contact selection to {@link ContactSearcher} and field
 * update interaction to {@link ContactUpdater}.
 * 
 * Contact validation is handled externally prior to insertion.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class AddressBook {

    private List<Contact> contacts = new ArrayList<>();

    /**
     * Retrieves the list of all stored contacts.
     * 
     * @return the current list of contacts
     */
    public List<Contact> getContacts() {
        return contacts;
    }

    /**
     * Replaces the current list of contacts (e.g., after loading from file).
     * 
     * @param contacts the new list to store
     */
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    /**
     * Adds a validated contact to the address book.
     * 
     * @param contact the contact to add
     */
    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    /**
     * Deletes a contact selected by the user.
     * 
     * Prompts the user to choose a search field, then uses
     * partial matching to find contacts. If one is selected and confirmed,
     * the contact is removed and returned.
     * 
     * @param scanner a Scanner for user input
     * @return the deleted contact, or null if none deleted
     */
    public Contact deleteContact(Scanner scanner) {
        String field = getFieldBySelection(scanner);
        if (field == null) return null;

        System.out.print("Enter value to search from AddressBook delete: ");
        String value = scanner.nextLine();

        List<Contact> matches = ContactSearcher.findMatches(contacts, field, value);
        Contact toDelete = ContactSearcher.selectFromList(matches, scanner);

        if (toDelete != null) {
            System.out.print("Are you sure you want to delete " + toDelete.getFirstName() + " " + toDelete.getLastName() + "? (y/n): ");
            String confirmation = scanner.nextLine().trim();

            if (confirmation.equalsIgnoreCase("y") || confirmation.equalsIgnoreCase("yes")) {
                contacts.remove(toDelete);
                System.out.println("The contact, " + toDelete.getFirstName() + " " + toDelete.getLastName() + ", has been deleted.");
                return toDelete;
            } else {
                System.out.println("Deletion cancelled.");
            }
        }
        return null;
    }

    /**
     * Updates an existing contact based on user input.
     * 
     * Searches for a contact using user-selected criteria, then allows
     * field-by-field update with confirmation. The method returns both
     * the original and updated version for audit/logging purposes.
     * 
     * @param scanner a Scanner for user input
     * @return a two-element array: [original, updated], or null if cancelled
     */
    public Contact[] updateContact(Scanner scanner) {
        String field = getFieldBySelection(scanner);
        if (field == null) return null;

        System.out.print("Enter value to search from AddressBook update: ");
        String value = scanner.nextLine();

        List<Contact> matches = ContactSearcher.findMatches(contacts, field, value);
        Contact toUpdate = ContactSearcher.selectFromList(matches, scanner);

        if (toUpdate != null) {
            Contact oldCopy = new Contact(toUpdate); // Snapshot before change
            ContactUpdater.updateFields(toUpdate, scanner); // Apply updates
            return new Contact[] { oldCopy, toUpdate };
        }

        return null;
    }

    /**
     * Returns a shallow copy of the current contact list.
     * 
     * Used for display purposes with formatting applied elsewhere.
     * 
     * @return a copy of the current list of contacts
     */
    public List<Contact> getFormattedContacts() {
        return new ArrayList<>(contacts);
    }

    /**
     * Displays a search menu and returns the field to search by.
     * 
     * Prompts the user with numbered options (e.g., name, phone, etc.).
     * Returns a lowercase keyword used internally for filtering.
     * 
     * @param scanner a Scanner for reading user input
     * @return the selected field identifier (e.g., "email"), or null if invalid
     */
    private String getFieldBySelection(Scanner scanner) {
        System.out.println("Search by:");
        System.out.println("1. First name");
        System.out.println("2. Last name");
        System.out.println("3. Full name");
        System.out.println("4. Email");
        System.out.println("5. Phone");
        System.out.print("Enter choice (1-5): ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": return "first";
            case "2": return "last";
            case "3": return "full";
            case "4": return "email";
            case "5": return "phone";
            default:
                System.out.println("Invalid selection.");
                return null;
        }
    }
}
