
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
 * Utility class for logging general information to a centralized log file.
 * <p>
 * Log entries are written to "log/log.txt" and include a timestamp, label, and message content.
 * Errors during logging are forwarded to {@link ErrorUtil} for fallback logging.
 */
public class LogUtil {

    /**
     * Logs a labeled message to the log file ("log/log.txt").
     * <p>
     * Automatically creates the log directory if it does not exist.
     *
     * @param label   a short label describing the type of log (e.g., "INFO", "ADDED", "DELETED")
     * @param content the detailed message to log
     */
    public static void logToFile(String label, String content) {
        try {
            File logDir = new File("log");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            try (PrintWriter out = new PrintWriter(new FileWriter("log/log.txt", true))) {
                out.println("[" + LocalDateTime.now() + "] " + label);
                out.println(content);
                out.println("--------------------------------------------------");
            }

        } catch (Exception e) {
            ErrorUtil.logError(e); // Delegate to error logger
        }
    }
}
