
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

/**
 * Functional interface for converting an array of string tokens (typically
 * split from a CSV line) into a domain-specific object.
 *
 * <p>
 * Used to support generic file parsing operations. Implementations define
 * how a line of structured text is converted into an instance of a specific type.
 * </p>
 *
 * @param <T> the target object type to produce from the parsed tokens
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
@FunctionalInterface
public interface LineParser<T> {

    /**
     * Parses a line of tokens into an object.
     *
     * @param tokens the split components of a single input line
     * @return a new object parsed from the tokens
     */
    T parse(String[] tokens);
}


