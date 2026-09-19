package address_book;

import java.util.regex.Pattern;

/**
 * Centralizes field-level validation rules for contact information.
 *
 * <p>The validator defines the rules for names, labels, physical addresses,
 * phone numbers, and email addresses. AspectJ aspects apply these rules at
 * relevant application and domain join points so validation can be enforced
 * consistently as a cross-cutting concern.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class FieldValidator {

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[\\p{L}][\\p{L}' -]{0,49}$");

    private static final Pattern STREET_PATTERN =
            Pattern.compile("^[\\p{L}\\p{N}][\\p{L}\\p{N} .,'#/-]{4,99}$");

    private static final Pattern CITY_PATTERN =
            Pattern.compile("^[\\p{L}][\\p{L} .'\\-]{1,49}$");

    private static final Pattern STATE_PATTERN =
            Pattern.compile("^[A-Za-z]{2}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile(
                    "^(?:\\d{10}|\\d{3}-\\d{3}-\\d{4}|\\(\\d{3}\\) ?\\d{3}-\\d{4})$"
            );

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+"
                            + "@[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?"
                            + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$"
            );

    /**
     * Prevents instantiation because this class provides only static
     * validation operations.
     */
    private FieldValidator() {
    }

    /**
     * Validates a first name.
     *
     * <p>Names may contain letters, apostrophes, hyphens, and spaces,
     * supporting values such as {@code Martinez-Samuel}.</p>
     *
     * @param name first name
     * @return {@code true} if valid; otherwise {@code false}
     */
    public static boolean isValidFirstName(String name) {
        return matches(name, NAME_PATTERN);
    }

    /**
     * Validates a last name.
     *
     * <p>Names may contain letters, apostrophes, hyphens, and spaces.</p>
     *
     * @param name last name
     * @return {@code true} if valid; otherwise {@code false}
     */
    public static boolean isValidLastName(String name) {
        return matches(name, NAME_PATTERN);
    }

    /**
     * Validates a label used to identify an address, phone number,
     * or email address.
     *
     * @param label label to validate
     * @return {@code true} if the label is nonblank and no longer than
     *         30 characters
     */
    public static boolean isValidLabel(String label) {
        return label != null
                && !label.trim().isEmpty()
                && label.trim().length() <= 30;
    }

    /**
     * Validates a street address.
     *
     * <p>Letters, numbers, spaces, and common address punctuation are
     * supported.</p>
     *
     * @param street street address
     * @return {@code true} if valid; otherwise {@code false}
     */
    public static boolean isValidStreetAddress(String street) {
        return matches(street, STREET_PATTERN);
    }

    /**
     * Validates a city name.
     *
     * <p>Letters, spaces, apostrophes, periods, and hyphens are supported,
     * allowing names such as {@code Winston-Salem}.</p>
     *
     * @param city city
     * @return {@code true} if valid; otherwise {@code false}
     */
    public static boolean isValidCity(String city) {
        return matches(city, CITY_PATTERN);
    }

    /**
     * Validates a two-letter U.S. state abbreviation.
     *
     * <p>The validation rule accepts exactly two alphabetic characters.
     * Interactive input normalizes accepted abbreviations to uppercase
     * before they enter the domain model.</p>
     *
     * @param state two-letter state abbreviation
     * @return {@code true} if valid; otherwise {@code false}
     */
    public static boolean isValidState(String state) {
        return matches(state, STATE_PATTERN);
    }

    /**
     * Determines whether a ZIP code uses a supported format.
     *
     * <p>Accepted formats are a five-digit ZIP code or a ZIP+4 value
     * containing the required hyphen.</p>
     *
     * @param zipCode ZIP code to validate
     * @return {@code true} when the ZIP code is valid
     */
    public static boolean isValidZipCode(String zipCode) {
        return matches(
                zipCode,
                Pattern.compile("^\\d{5}(-\\d{4})?$")
        );
    }

    /**
     * Validates a U.S. phone number using supported common representations.
     *
     * <p>Accepted examples include {@code 2105551212},
     * {@code 210-555-1212}, and {@code (210) 555-1212}. Characters are
     * validated before normalization so arbitrary text cannot be hidden
     * by simply stripping all nondigit characters.</p>
     *
     * @param phone phone number
     * @return {@code true} if valid; otherwise {@code false}
     */
    public static boolean isValidPhoneNumberFormatted(String phone) {
        return matches(phone, PHONE_PATTERN);
    }

    /**
     * Validates an email address using the application's supported
     * email-address format.
     *
     * @param email email address
     * @return {@code true} if valid; otherwise {@code false}
     */
    public static boolean isValidEmail(String email) {
        return matches(email, EMAIL_PATTERN);
    }

    /**
     * Applies a regular-expression validation rule to trimmed,
     * non-null input.
     *
     * @param value value to validate
     * @param pattern validation pattern
     * @return {@code true} if the value matches the pattern
     */
    private static boolean matches(String value, Pattern pattern) {
        return value != null && pattern.matcher(value.trim()).matches();
    }
}
