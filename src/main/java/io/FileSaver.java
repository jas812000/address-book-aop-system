
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Utility class for writing text content to files.
 * Supports writing strings and lists of lines to specified paths,
 * with automatic creation of parent directories as needed.
 * 
 * <p>
 * Used throughout the application to persist data such as contact CSV files,
 * logs, and other output files.
 * </p>
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class FileSaver {

    /**
     * Saves a list of text lines to the specified file.
     * Overwrites any existing content.
     *
     * @param path  the destination file path
     * @param lines the lines of text to write
     * @throws IOException if writing fails
     */
    public static void saveLines(Path path, List<String> lines) throws IOException {
        ensureParentDirectory(path);
        Files.write(path, lines);
    }

    /**
     * Saves a single string to the specified file.
     * Overwrites any existing content.
     *
     * @param path    the destination file path
     * @param content the full string content to write
     * @throws IOException if writing fails
     */
    public static void saveString(Path path, String content) throws IOException {
        ensureParentDirectory(path);
        Files.writeString(path, content);
    }

    /**
     * Convenience method to save a list of lines to a file
     * located under the base application directory.
     *
     * @param fileName the name of the file (relative to base directory)
     * @param lines    the lines to write
     * @throws IOException if writing fails
     */
    public static void saveLinesToBase(String fileName, List<String> lines) throws IOException {
        saveLines(AppPaths.getFile(fileName), lines);
    }

    /**
     * Convenience method to save a single string to a file
     * located under the base application directory.
     *
     * @param fileName the name of the file (relative to base directory)
     * @param content  the content to write
     * @throws IOException if writing fails
     */
    public static void saveStringToBase(String fileName, String content) throws IOException {
        saveString(AppPaths.getFile(fileName), content);
    }

    /**
     * Ensures that the parent directory of a given file path exists.
     * Creates any missing directories if needed.
     *
     * @param path the target file path
     * @throws IOException if directory creation fails
     */
    private static void ensureParentDirectory(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}
