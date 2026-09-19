package address_utils.formatter;

import address_book.FieldValidator;

import java.util.Locale;

/**
 * Provides canonical formatting for validated contact values.
 *
 * <p>Normalization is deliberately strict: values must satisfy the
 * application's validation rules before they are transformed. Invalid
 * input is rejected rather than partially cleaned or silently preserved.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class ContactNormalizer {

    /**
     * Prevents instantiation because this class provides only static
     * normalization operations.
     */
    private ContactNormalizer() {
    }

    /**
     * Converts a validated personal name to canonical title-style casing
     * while preserving spaces, hyphens, and apostrophes as word boundaries.
     *
     * @param name personal name to normalize
     * @return canonical name representation
     * @throws IllegalArgumentException if the name is null or blank
     */
    public static String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name.");
        }

        return normalizeTitleCase(name);
    }

    /**
     * Converts a validated street address to canonical title-style casing.
     *
     * @param street street address to normalize
     * @return canonical street-address representation
     * @throws IllegalArgumentException if the street is null or blank
     */
    public static String normalizeStreet(String street) {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Invalid street address.");
        }

        return normalizeTitleCase(street);
    }

    /**
     * Converts a validated city name to canonical title-style casing.
     *
     * @param city city name to normalize
     * @return canonical city representation
     * @throws IllegalArgumentException if the city is null or blank
     */
    public static String normalizeCity(String city) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("Invalid city.");
        }

        return normalizeTitleCase(city);
    }

    /**
     * Converts a validated state abbreviation to uppercase.
     *
     * @param state state abbreviation to normalize
     * @return uppercase state abbreviation
     * @throws IllegalArgumentException if the state is invalid
     */
    public static String normalizeState(String state) {
        if (!FieldValidator.isValidState(state)) {
            throw new IllegalArgumentException("Invalid state.");
        }

        return state.toUpperCase(Locale.ROOT);
    }

    /**
     * Converts a validated email address to lowercase.
     *
     * @param email email address to normalize
     * @return lowercase email address
     * @throws IllegalArgumentException if the email address is invalid
     */
    public static String normalizeEmail(String email) {
        if (!FieldValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }

        return email.toLowerCase(Locale.ROOT);
    }

    /**
     * Converts a supported phone-number representation to the application's
     * canonical {@code (XXX) XXX-XXXX} format.
     *
     * <p>Only explicitly supported phone formats are accepted. Arbitrary
     * characters are not discarded before validation.</p>
     *
     * @param phone phone number to normalize
     * @return canonical phone-number representation
     * @throws IllegalArgumentException if the phone number is invalid
     */
    public static String normalizePhone(String phone) {
        if (!FieldValidator.isValidPhoneNumberFormatted(phone)) {
            throw new IllegalArgumentException("Invalid phone number.");
        }

        String digits = phone.replaceAll("\\D", "");

        return String.format(
                "(%s) %s-%s",
                digits.substring(0, 3),
                digits.substring(3, 6),
                digits.substring(6, 10)
        );
    }

    /**
     * Converts a supported ZIP code to its canonical representation.
     *
     * <p>Five-digit ZIP codes remain unchanged. ZIP+4 values are returned
     * in {@code 12345-6789} format.</p>
     *
     * @param zipCode ZIP code to normalize
     * @return canonical ZIP-code representation
     * @throws IllegalArgumentException if the ZIP code is invalid
     */
    public static String normalizeZip(String zipCode) {
        if (!FieldValidator.isValidZipCode(zipCode)) {
            throw new IllegalArgumentException("Invalid ZIP code.");
        }

        if (zipCode.length() == 5) {
            return zipCode;
        }

        return zipCode.substring(0, 5)
                + "-"
                + zipCode.substring(6);
    }

    /**
     * Converts text to title-style casing while treating spaces, hyphens,
     * and apostrophes as word boundaries.
     *
     * @param value text to normalize
     * @return title-style representation
     */
    private static String normalizeTitleCase(String value) {
        StringBuilder normalized = new StringBuilder(value.length());
        boolean capitalizeNext = true;

        for (char character : value.toLowerCase(Locale.ROOT).toCharArray()) {
            if (capitalizeNext && Character.isLetter(character)) {
                normalized.append(Character.toUpperCase(character));
                capitalizeNext = false;
            } else {
                normalized.append(character);
            }

            if (character == ' '
                    || character == '-'
                    || character == '\'') {
                capitalizeNext = true;
            }
        }

        return normalized.toString();
    }
}
