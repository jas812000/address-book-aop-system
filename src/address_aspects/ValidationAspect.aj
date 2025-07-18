/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the TicTacToe project and may not be used, copied,
 * modified, or distributed without permission.
 */

package address_aspects;

import address_book.Contact;
import validators.ContactIntegrityValidator;

/**
 * The ValidationAspect aspect enforces contact field validation at key operations.
 * 
 * It intercepts calls to 'addContact(Contact)' and 'updateContact(Scanner, Contact)' 
 * within the AddressBook class and validates the Contact object using the ContactIntegrityValidator.
 * 
 * If any field is invalid, an IllegalArgumentException is thrown before the method executes.
 * This centralizes validation logic for cleaner controller code and consistency.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public aspect ValidationAspect {

    /**
     * Pointcut matching calls to `addContact(Contact)` or `updateContact(Scanner, Contact)`
     * and extracts the Contact argument for validation.
     */
    pointcut validateContact(Contact c): 
        call(* address_book.AddressBook.addContact(..)) && args(c) || 
        call(* address_book.AddressBook.updateContact(..)) && args(*, c);

    /**
     * Validates the Contact object before the corresponding add/update method is executed.
     *
     * @param c the Contact to validate
     */
    before(Contact c): validateContact(c) {
    	ContactIntegrityValidator.validateContact(c);
    }
}

