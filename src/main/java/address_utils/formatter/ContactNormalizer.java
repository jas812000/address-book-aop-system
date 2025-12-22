
/*
 * Author: James Stevens
 * Date: 01 July 2025
 * Course: SWEN 656 - Advanced Software Design and Implementation
 * 
 * Copyright (c) 2025 James Stevens
 * This file is part of the Address Book project and may not be used, copied,
 * modified, or distributed without permission.
 */

package address_utils.formatter;

import address_book.FieldValidator;

/**
 * Utility class for normalizing user-entered contact data into standardized formats.
 * 
 * Specifically supports:
 * - Converting phone numbers to U.S. style (XXX) XXX-XXXX format
 * - Formatting ZIP codes to 12345 or 12345-6789 format
 * 
 * Invalid input is returned unmodified. Logging of invalid entries is handled
 * separately by the {@code ValidationLoggingAspect}.
 * 
 * @author James Stevens
 * @version 1.0
 * @since 2025-07-01
 */
public class ContactNormalizer {

    /**
     * Converts a valid phone number to the format (XXX) XXX-XXXX.
     * 
     * Removes non-digit characters, validates length, and formats output.
     * If the phone number fails validation, the raw input is returned as-is.
     * 
     * @param raw the raw phone number string entered by the user
     * @return formatted phone number or original input if invalid
     */
    public static String normalizePhone(String raw) {
        if (!FieldValidator.isValidPhoneNumberFormatted(raw)) {
            return raw; 
        }
        String digits = raw.replaceAll("\\D", "");
        return String.format("(%s) %s-%s", digits.substring(0, 3), digits.substring(3, 6), digits.substring(6));
    }

    /**
     * Converts a valid ZIP code to standard format.
     * 
     * Supports both 5-digit and ZIP+4 formats (e.g., 12345 or 12345-6789).
     * If input is invalid, the method returns the original string.
     * 
     * @param raw the raw ZIP code string
     * @return formatted ZIP code or original input if invalid
     */
    public static String normalizeZip(String raw) {
        if (!FieldValidator.isValidZipCode(raw)) {
            return raw; 
        }
        String digits = raw.replaceAll("\\D", "");
        return digits.length() == 5
            ? digits
            : digits.substring(0, 5) + "-" + digits.substring(5);
    }
}
