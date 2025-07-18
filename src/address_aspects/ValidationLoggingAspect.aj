
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.io.IOException;
import io.AppPaths;
import io.FileSaver;

/**
 * The ValidationLoggingAspect is responsible for logging input validation failures.
 * 
 * This aspect intercepts calls to any static method in the ContactValidator class
 * that starts with `isValid` and takes a single `String` argument.
 * If the validation returns false, it logs the failed input and method name to
 * `log/validation_errors.txt` using the centralized FileSaver and AppPaths utilities.
 * 
 * Each log entry includes a timestamp, method name, and invalid input value.
 * This supports better debugging and traceability for user input errors.
 * 
 * Example:
 *   [2025-07-01T14:25:36.123456] Invalid isValidState: Californi@
 * 
 * Logging failures (e.g. I/O issues) will print a fallback message to the console.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public aspect ValidationLoggingAspect {

    /**
     * Pointcut that captures any static isValid* method in ContactValidator
     * that takes a single String argument.
     * 
     * Matches methods like:
     * - isValidState(String)
     * - isValidEmail(String)
     */
    pointcut failedValidation(String input):
        call(static boolean address_book.FieldValidator.isValid*(String)) && args(input);

    /**
     * Around advice that logs failed validations.
     * 
     * Executes the actual validation logic and logs if the result is false.
     * The log includes:
     * - The method name (e.g., isValidZipCode)
     * - The input value that failed
     * - A timestamp
     * 
     * @param input the user input passed to the validation method
     * @return the result of the validation (true or false)
     */
    boolean around(String input): failedValidation(input) {
        boolean result = proceed(input);
        if (!result) {
            try {
                String timestamped = "[" + LocalDateTime.now() + "] " +
                                     "Invalid " + thisJoinPointStaticPart.getSignature().getName() +
                                     ": " + input;
                FileSaver.saveLines(
                    AppPaths.getSubDirectory("log").resolve("validation_errors.txt"),
                    Collections.singletonList(timestamped + "\n")
                );
            } catch (IOException e) {
                System.out.println("Validation logging failed: " + e.getMessage());
            }
        }
        return result;
    }
}
