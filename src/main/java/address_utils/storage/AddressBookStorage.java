
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the Address Book project and may not be used, copied,
 * modified, or distributed without permission.
 */

package address_utils.storage;

import java.io.*;
import java.util.*;

import address_book.Contact;
import address_utils.formatter.ContactCSVFormatter;
import address_utils.parser.ContactLineParser;
import io.AppPaths;
import io.FileLoader;
import io.FileParser;
import io.FileSaver;

/**
 * Handles file-based persistence for the Address Book.
 * 
 * Provides methods to:
 * - Save a list of contacts to a CSV file
 * - Load contacts from a CSV file into memory
 * 
 * Uses {@link ContactCSVFormatter} for formatting and
 * {@link ContactLineParser} for parsing contact data.
 * 
 * The file path is centrally managed via {@link AppPaths#ADDRESS_BOOK_FILE}.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class AddressBookStorage {

    /**
     * Saves a list of contacts to a CSV file.
     * 
     * The output includes a header row and each contact formatted into CSV.
     * If the save operation fails, an error message is printed to the console.
     * 
     * @param contacts the list of contacts to save
     */
    public void save(List<Contact> contacts) {
        List<String> lines = new ArrayList<>();
        lines.add(ContactCSVFormatter.header());
        for (Contact c : contacts) {
            lines.add(ContactCSVFormatter.toCSV(c));
        }
        try {
            FileSaver.saveLines(AppPaths.ADDRESS_BOOK_FILE, lines);
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    /**
     * Loads contacts from the CSV file.
     * 
     * If the file exists, it parses the lines (excluding the header if present)
     * and returns a list of valid Contact objects. If loading fails, it
     * returns an empty list.
     * 
     * @return a list of contacts or an empty list on error
     */
    public List<Contact> load() {
        try {
            List<String> lines = FileLoader.loadLines(AppPaths.ADDRESS_BOOK_FILE);
            if (!lines.isEmpty() && lines.get(0).toLowerCase().contains("first name")) {
                lines.remove(0); // Skip header
            }
            FileParser<Contact> parser = new FileParser<>(",", new ContactLineParser());
            return parser.parseLines(lines);
        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}


