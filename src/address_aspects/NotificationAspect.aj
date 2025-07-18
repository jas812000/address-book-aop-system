
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

import utilities.LogUtil;

/**
 * Aspect that logs notifications to the general log file after key contact operations.
 * 
 * Intercepts and logs successful add, delete, and update actions performed on contacts.
 * Replaces console output with centralized logging via LogUtil.
 * 
 * Notifications are labeled as "NOTIFICATION" in log.txt.
 */
public aspect NotificationAspect {

    /**
     * Pointcut that captures any call to add a contact via AddressBook.
     */
    pointcut afterAdd(): call(* address_book.AddressBook.addContact(..));
    
    /**
     * Logs a notification after a contact is successfully added.
     */
    after(): afterAdd() {
        LogUtil.logToFile("NOTIFICATION", "Contact added successfully.");
    }

    /**
     * Pointcut that captures any call to delete a contact via AddressBook.
     */
    pointcut afterDelete(): call(* address_book.AddressBook.deleteContact(..));
    
    /**
     * Logs a notification after a contact is successfully deleted.
     */
    after(): afterDelete() {
        LogUtil.logToFile("NOTIFICATION", "Contact deleted successfully.");
    }

    /**
     * Pointcut that captures any call to update a contact via AddressBook.
     */
    pointcut afterUpdate(): call(* address_book.AddressBook.updateContact(..));

    /**
     * Logs a notification after a contact is successfully updated.
     */
    after(): afterUpdate() {
        LogUtil.logToFile("NOTIFICATION", "Contact updated successfully.");
    }
}
