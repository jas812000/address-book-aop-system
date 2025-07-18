
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
 * Runs the Address Book application.
 * 
 * This class contains the core application loop. It handles user interactions
 * through a command-line menu and delegates contact-related functionality
 * (add, delete, update, display, save) to {@link AddressBookController}.
 * 
 * It is the main runtime entry point for interactive execution, and is typically
 * invoked from the {@link Main} class.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class AddressBookApp {

    // Handles all user operations and data interactions
    private final AddressBookController controller = new AddressBookController();

    /**
     * Starts the main application loop.
     * 
     * - Loads saved contacts
     * - Displays a menu
     * - Executes user-selected actions
     * - Repeats until the user chooses to exit
     */
    public void run() {
        controller.load(); // Load existing contacts from storage

        while (true) {
            displayMenu(); // Show available options to the user

            // Get user's menu choice
            String choice = controller.getScanner().nextLine().trim();

            // Route the action based on choice
            switch (choice) {
                case "1":
                    controller.add();    // Add new contact
                    break;
                case "2":
                    controller.delete(); // Delete existing contact
                    break;
                case "3":
                    controller.update(); // Update contact info
                    break;
                case "4":
                    controller.display(); // Show all contacts
                    break;
                case "5":
                    controller.save();   // Save contacts before exiting
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice."); // Invalid input fallback
            }
        }
    }

    /**
     * Displays the application menu options to the user.
     */
    private void displayMenu() {
        System.out.println("\n--- Address Book Menu ---");
        System.out.println("1. Add Contact");
        System.out.println("2. Delete Contact");
        System.out.println("3. Update Contact");
        System.out.println("4. Display Contacts");
        System.out.println("5. Exit");
        System.out.print("Enter choice: ");
    }
}


