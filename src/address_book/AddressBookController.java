
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

import address_utils.storage.AddressBookStorage;

/**
 * Handles user-driven operations such as adding, deleting, updating, and displaying contacts.
 * 
 * This controller delegates functionality to supporting components such as:
 * - {@link AddressBook} for in-memory contact management
 * - {@link AddressBookStorage} for persistent storage
 * - {@link ContactInputHandler} for collecting validated user input
 * 
 * It is designed for use in CLI-based applications and is managed by {@link AddressBookApp}.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class AddressBookController {

    private final Scanner scanner = new Scanner(System.in);
    private final AddressBook addressBook = new AddressBook();
    private final AddressBookStorage storage = new AddressBookStorage();
    private final ContactInputHandler inputHandler = new ContactInputHandler();

    /**
     * Provides access to the input scanner.
     * 
     * @return the shared Scanner instance
     */
    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Loads contact data from persistent storage into memory.
     */
    public void load() {
        addressBook.setContacts(storage.load());
    }

    /**
     * Saves current in-memory contact list to disk.
     */
    public void save() {
        storage.save(addressBook.getContacts());  
    }

    /**
     * Prompts the user to enter new contact information and adds it to the address book.
     * Data is then saved to persistent storage.
     */
    public void add() {
        Contact contact = inputHandler.promptContactDetails(scanner);
        addressBook.addContact(contact);
        save();
    }

    /**
     * Prompts the user to select a contact to delete.
     * If deletion is confirmed and successful, data is saved.
     */
    public void delete() {
        Contact deleted = addressBook.deleteContact(scanner);
        if (deleted != null) {
            save();
        } else {
            System.out.println("No contact selected.");
        }
    }

    /**
     * Prompts the user to search and update a contact.
     * If the update is completed, changes are saved.
     */
    public void update() {
        Contact[] result = addressBook.updateContact(scanner);  
        if (result != null) {
            save();
        } else {
            System.out.println("No contact selected.");
        }
    }

    /**
     * Displays all formatted contacts to the console.
     * If no contacts exist, displays a corresponding message.
     */
    public void display() {
        List<Contact> contactList = addressBook.getFormattedContacts();

        if (contactList.isEmpty()) {
            System.out.println("Address book is empty.");
        } else {
            System.out.println("\n---------- Address Book Contacts ----------\n");
            for (Contact c : contactList) {
                System.out.println("------------------------------------------");
                System.out.println(c);
                System.out.println("------------------------------------------");
            }
        }
    }
}

