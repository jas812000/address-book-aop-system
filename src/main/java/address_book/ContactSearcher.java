
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

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import address_utils.formatter.ContactFormatter;

/**
 * Provides search functionality within a list of contacts.
 * 
 * Supports searching by first name, last name, full name, email, or phone number,
 * and allows users to select a contact from a list of matching results.
 * 
 * Used during update and delete operations to locate a specific contact.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class ContactSearcher {

    /**
     * Finds contacts that match a given search term for a specified field.
     * 
     * Case-insensitive and supports partial matching.
     *
     * @param contacts the list of contacts to search within
     * @param field the field to search by: "first", "last", "full", "email", or "phone"
     * @param value the value to search for (case-insensitive)
     * @return list of matching contacts (can be empty if no match is found)
     */
    public static List<Contact> findMatches(List<Contact> contacts, String field, String value) {
        String searchTerm = value.toLowerCase();

        return contacts.stream().filter(c -> {
            switch (field.toLowerCase()) {
                case "first":
                    return c.getFirstName().toLowerCase().contains(searchTerm);
                case "last":
                    return c.getLastName().toLowerCase().contains(searchTerm);
                case "full":
                case "name":
                    String fullName = (c.getFirstName() + " " + c.getLastName()).toLowerCase();
                    return fullName.contains(searchTerm);
                case "email":
                    return c.getEmail().toLowerCase().contains(searchTerm);
                case "phone":
                    return c.getPhone().toLowerCase().contains(searchTerm);
                default:
                    return false;
            }
        }).collect(Collectors.toList());
    }

    /**
     * Prompts the user to select a contact from a list of matches.
     * 
     * If one match is found, it is returned immediately.
     * If multiple matches are found, the user selects by number.
     *
     * @param matches the list of matched contacts
     * @param scanner the Scanner to read user input
     * @return the selected contact, or null if no valid selection is made
     */
    public static Contact selectFromList(List<Contact> matches, Scanner scanner) {
        if (matches.isEmpty()) {
            System.out.println("No matching contacts found.");
            return null;
        } else if (matches.size() == 1) {
            return matches.get(0); 
        } else {
            System.out.println("Multiple matches found:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println((i + 1) + ". " + ContactFormatter.formatCompact(matches.get(i)));
            }

            while (true) {
                System.out.print("Select contact by number: ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    if (choice >= 1 && choice <= matches.size()) {
                        return matches.get(choice - 1);
                    }
                } catch (NumberFormatException ignored) {}
                System.out.println("Invalid selection. Try again.");
            }
        }
    }
}
