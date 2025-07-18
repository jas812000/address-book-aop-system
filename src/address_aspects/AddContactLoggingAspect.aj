
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the Address Book project and may not be used, copied,
 * modified, or distributed without permission.
 */

package address_aspects;

import address_book.Contact;
import utilities.LogUtil;

/**
 * Aspect for logging contact creation events within the Address Book system.
 *
 * This aspect intercepts calls to add contacts via AddressBook.addContact(Contact),
 * and logs the added contact's details to a persistent log file.
 * 
 * It also writes a standardized "[NOTIFICATION]" entry confirming success.
 */
public aspect AddContactLoggingAspect {

    /**
     * Pointcut that matches the execution of AddressBook.addContact(Contact).
     */
    pointcut addContactCall(): execution(* address_book.AddressBook.addContact(..));

    /**
     * After advice that triggers after a contact is added.
     * If the contact is not null, logs the contact's information
     * along with a notification.
     *
     * @param contact the Contact object added to the address book
     */
    after(Contact contact): addContactCall() && args(contact) {
        if (contact != null) {
            LogUtil.logToFile("ADDED", contact.toString());
            LogUtil.logToFile("NOTIFICATION", "Contact added successfully.");
        }
    }
}
