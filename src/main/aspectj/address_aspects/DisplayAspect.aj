
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
import address_utils.formatter.ContactFormatter;

/**
 * Aspect that intercepts all calls to Contact.toString() and overrides
 * its behavior with a custom formatted string via the ContactFormatter utility.
 *
 * This ensures that contact information is consistently displayed
 * in a readable format across the application.
 */
public aspect DisplayAspect {

    /**
     * Pointcut that captures calls to the toString() method
     * of Contact objects anywhere in the application.
     *
     * @param c the Contact whose toString() is being called
     */
    pointcut displayCall(Contact c):
        call(String address_book.Contact.toString()) && target(c);

    /**
     * Around advice that intercepts the toString() call and returns
     * a custom-formatted string using ContactFormatter.
     *
     * @param c the Contact being printed
     * @return the formatted string representation of the contact
     */
    String around(Contact c): displayCall(c) {
        return ContactFormatter.format(c);
    }
}
