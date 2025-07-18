
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
 * Aspect that logs specific system messages sent to System.out.
 * 
 * This aspect intercepts any `System.out.println(String)` call
 * and logs messages that start with known debug prefixes (e.g., "[AppPaths]").
 * 
 * Logged messages are written to the general log using LogUtil.
 */
public aspect SystemMessageLoggingAspect {

    /**
     * Pointcut that matches all calls to `System.out.println(String)`
     * and captures the message argument.
     * 
     * @param message the string being printed to System.out
     */
    pointcut printlnCall(String message):
        call(void java.io.PrintStream.println(String)) && args(message);

    /**
     * After advice that triggers after each println call.
     * If the printed message begins with "[AppPaths]", it is logged to file.
     * 
     * @param message the actual message sent to System.out
     */
    after(String message): printlnCall(message) {
        if (message.startsWith("[AppPaths]")) {
            LogUtil.logToFile("INFO", message);  
        }
    }
}
