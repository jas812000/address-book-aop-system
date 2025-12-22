
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

import utilities.ErrorUtil;

/**
 * Global exception handler for the Address Book application.
 * 
 * This aspect automatically wraps method calls to:
 * - `address_book.AddressBookController` (user interface logic)
 * - Any class within the `io` package (file and path operations)
 * 
 * If an exception is thrown:
 * - It is caught to prevent application crashes
 * - It is logged with a timestamp and full stack trace to log/error.txt
 * - A user-friendly fallback (null return) is provided
 */
public aspect ExceptionHandlingAspect {

    /**
     * Pointcut that captures all method executions in:
     * - The AddressBookController class
     * - Any class within the 'io' package
     */
    pointcut allAppMethods():
        execution(* address_book.AddressBookController.*(..)) || execution(* io..*(..));

    /**
     * Around advice that wraps each captured method with exception handling.
     * Logs any exception using LogUtil and returns null to preserve control flow.
     *
     * @return original return value or null on exception
     */
    Object around(): allAppMethods() {
        try {
            return proceed();
        } catch (Exception e) {
            ErrorUtil.logError(e);
            return null;
        }
    }
}

