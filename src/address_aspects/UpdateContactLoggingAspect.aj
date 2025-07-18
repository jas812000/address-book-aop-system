
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
import java.util.Scanner;

/**
 * Aspect that logs contact updates.
 *
 * It intercepts calls to AddressBook.updateContact(Scanner), assuming the method returns a Contact[]
 * where the first element is the old version and the second is the updated version.
 * 
 * It logs:
 * - The data before the update
 * - The updated data
 * - A notification message indicating successful update
 */
public aspect UpdateContactLoggingAspect {

    /**
     * Pointcut that matches updateContact method returning old contact
     * and accepting a Scanner as the only argument.
     */
    pointcut updateContactCall(Scanner scanner): 
        execution(Contact[] address_book.AddressBook.updateContact(..)) && args(scanner);

    /**
     * Around advice that intercepts the updateContact call,
     * retrieves the returned Contact[] containing old and new versions,
     * and logs both states as well as a notification.
     *
     * @param scanner the Scanner passed into updateContact
     * @return the Contact[] containing old and updated contacts
     */
    Contact[] around(Scanner scanner): updateContactCall(scanner) {
        Contact[] result = proceed(scanner);

        if (result != null && result.length == 2) {
            Contact oldData = result[0];
            Contact newData = result[1];

            LogUtil.logToFile("Data before update", oldData.toString());
            LogUtil.logToFile("UPDATED", newData.toString());
            LogUtil.logToFile("NOTIFICATION", "Contact updated successfully.");
        }

        return result;
    }
}

