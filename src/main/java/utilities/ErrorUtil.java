
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the Address Book project and may not be used, copied,
 * modified, or distributed without permission.
 */

package utilities;

import java.io.*;
import java.time.LocalDateTime;

/**
 * Utility class for logging exceptions and error messages.
 * <p>
 * Logs are written to "log/error.txt" and include timestamp, exception type,
 * message, and full stack trace. Ensures fallback behavior if error logging itself fails.
 */
public class ErrorUtil {

    /**
     * Logs detailed exception information to "log/error.txt".
     * <p>
     * Includes:
     * <ul>
     *   <li>Timestamp of the error</li>
     *   <li>Exception class name</li>
     *   <li>Exception message</li>
     *   <li>Stack trace</li>
     * </ul>
     *
     * @param e the exception to log
     */
    public static void logError(Exception e) {
        try {
            File logDir = new File("log");
            if (!logDir.exists()) logDir.mkdirs();

            try (PrintWriter out = new PrintWriter(new FileWriter("log/error.txt", true))) {
                out.println("[" + LocalDateTime.now() + "] ERROR");
                out.println("Exception: " + e.getClass().getSimpleName());
                out.println("Message: Logging failed: " + e.getMessage());
                out.println("Stack Trace:");
                for (StackTraceElement ste : e.getStackTrace()) {
                    out.println("\tat " + ste);
                }
                out.println("--------------------------------------------------");
            }
        } catch (Exception ex) {
            // If even error logging fails, fallback to minimal console output
            System.out.println("[ERROR] Unable to log an error to the error log.");
        }
    }
}
