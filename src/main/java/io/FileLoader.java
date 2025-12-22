
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
 * Utility class for reading file contents from disk.
 * <p>
 * Supports reading lines as a list or the entire file as a string.
 * Provides helper methods to operate within the application’s base directory.
 */
public class FileLoader {

    /**
     * Loads all lines from a file at the given path.
     *
     * @param path the path to the file
     * @return list of lines from the file
     * @throws IOException if an I/O error occurs
     */
    public static List<String> loadLines(Path path) throws IOException {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Loads the entire contents of a file as a single string.
     *
     * @param path the path to the file
     * @return file contents as a single string
     * @throws IOException if an I/O error occurs
     */
    public static String loadAsString(Path path) throws IOException {
        return Files.readString(path);
    }

    /**
     * Loads lines from a file located under the base data directory.
     *
     * @param fileName the name of the file relative to the base directory
     * @return list of lines from the file
     * @throws IOException if an I/O error occurs
     */
    public static List<String> loadLinesFromBase(String fileName) throws IOException {
        return loadLines(AppPaths.getFile(fileName));
    }

    /**
     * Checks if a file exists at the specified path.
     *
     * @param path the file path to check
     * @return true if the file exists; false otherwise
     */
    public static boolean fileExists(Path path) {
        return Files.exists(path);
    }
}
