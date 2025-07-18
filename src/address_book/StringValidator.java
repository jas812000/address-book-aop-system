
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
 * Functional interface for validating user input in the address book application.
 * 
 * Used in conjunction with lambdas or method references to abstract away
 * validation logic for strings (e.g., names, emails, states).
 * 
 * Implementations define custom rules to determine if input is acceptable.
 */
@FunctionalInterface
public interface StringValidator {

    /**
     * Checks whether a given input string satisfies validation constraints.
     *
     * @param input the string value to validate
     * @return true if the input is considered valid; false otherwise
     */
    boolean isValid(String input);
}


