
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

import java.util.ArrayList;
import java.util.List;

/**
 * Generic file parser for converting delimited text lines into objects.
 * <p>
 * Uses a provided {@link LineParser} to convert each line of tokens into an object
 * of type {@code T}. Skips blank lines and ignores lines that fail parsing.
 * </p>
 *
 * @param <T> the object type produced from each line
 */
public class FileParser<T> {

    private final String delimiterRegex;
    private final LineParser<T> parser;

    /**
     * Constructs a FileParser with a specific delimiter and parser.
     *
     * @param delimiterRegex regular expression to split each line
     * @param parser         logic for converting token arrays into objects
     */
    public FileParser(String delimiterRegex, LineParser<T> parser) {
        this.delimiterRegex = delimiterRegex;
        this.parser = parser;
    }

    /**
     * Parses each line into an object of type {@code T}.
     * <p>
     * Lines that are blank or result in {@code null} objects are skipped.
     * Parsing exceptions are printed and re-thrown.
     * </p>
     *
     * @param lines list of lines to parse
     * @return a list of successfully parsed objects
     */
    public List<T> parseLines(List<String> lines) {
        List<T> result = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) continue;
            String[] tokens = line.split(delimiterRegex, -1);
            try {
                T item = parser.parse(tokens);
                if (item != null) result.add(item);
            } catch (RuntimeException e) {
                System.out.println("Failed to parse line: " + line);
                throw e;
            }
        }
        return result;
    }
}
