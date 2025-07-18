
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
 * Entry point of the Address Book Application.
 * 
 * This class contains the main method and is responsible for launching
 * the application by creating and running the AddressBookApp instance.
 * 
 * No command-line arguments are required to run the program.
 * All user interaction occurs via the console.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class Main {

    /**
     * The main method that starts the Address Book application.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        AddressBookApp app = new AddressBookApp();
        app.run();
    }
}
