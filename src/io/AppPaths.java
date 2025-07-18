
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the Address Book project and may not be used, copied,
 * modified, or distributed without permission.
 */

package io;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Centralized file and directory path manager for the Address Book application.
 * <p>
 * Resolves absolute paths for data files and subdirectories under a common base.
 * The base path can be overridden using the APP_DATA_DIR environment variable;
 * otherwise, it defaults to a local "data" directory.
 */
public class AppPaths {

    private static final String ENV_VAR = "APP_DATA_DIR";

    /**
     * Base directory for storing all application data.
     * Derived from the APP_DATA_DIR environment variable or defaults to "data".
     */
    public static final Path BASE_DIRECTORY;

    /**
     * Path to the main address book CSV file.
     */
    public static final Path ADDRESS_BOOK_FILE;

    // Static initializer to configure paths
    static {
        String envPath = System.getenv(ENV_VAR);
        if (envPath != null && !envPath.isBlank()) {
            BASE_DIRECTORY = Paths.get(envPath);
        } else {
            BASE_DIRECTORY = Paths.get("data");
        }

        ADDRESS_BOOK_FILE = BASE_DIRECTORY.resolve("address_book.csv");
    }

    /**
     * Resolves a file path under the base directory.
     *
     * @param fileName the name of the file to resolve
     * @return absolute path to the file under the base directory
     */
    public static Path getFile(String fileName) {
        return BASE_DIRECTORY.resolve(fileName);
    }

    /**
     * Resolves a subdirectory path under the base directory.
     *
     * @param folderName the name of the subdirectory
     * @return absolute path to the subdirectory under the base directory
     */
    public static Path getSubDirectory(String folderName) {
        return BASE_DIRECTORY.resolve(folderName);
    }
}
