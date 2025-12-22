
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
 * Aspect for logging contact deletions.
 * 
 * This aspect intercepts calls to deleteContact(..) within the AddressBook class,
 * logs the deleted contact's details, and writes a notification message to the log file.
 */
public aspect DeleteContactLoggingAspect {

    /**
     * Pointcut that matches the execution of deleteContact(..) method
     * in the AddressBook class, which returns a Contact.
     */
    pointcut deleteContactCall(): 
        execution(address_book.Contact address_book.AddressBook.deleteContact(..));

    /**
     * After-returning advice that triggers after a contact is successfully deleted.
     * If the returned Contact object is not null, it logs the contact and a deletion notification.
     *
     * @param deleted the Contact that was deleted
     */
    after() returning(Contact deleted): deleteContactCall() {
        if (deleted != null) {
            LogUtil.logToFile("DELETED", deleted.toString());
            LogUtil.logToFile("NOTIFICATION", "Contact deleted successfully.");
        }
    }
}
