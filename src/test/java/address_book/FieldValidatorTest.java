package address_book;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the field-level validation rules shared by the command-line
 * interface and AspectJ domain-validation layer.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
class FieldValidatorTest {

    /**
     * Verifies that conventional and compound personal names are accepted.
     */
    @Test
    void nameValidation_acceptsSupportedNames() {
        assertTrue(FieldValidator.isValidFirstName("James"));
        assertTrue(FieldValidator.isValidFirstName("Mary Jane"));
        assertTrue(FieldValidator.isValidLastName("Martinez-Samuel"));
        assertTrue(FieldValidator.isValidLastName("O'Brien"));
    }

    /**
     * Verifies that malformed names are rejected.
     */
    @Test
    void nameValidation_rejectsInvalidNames() {
        assertFalse(FieldValidator.isValidFirstName("1James"));
        assertFalse(FieldValidator.isValidLastName("-Stevens"));
        assertFalse(FieldValidator.isValidFirstName(""));
        assertFalse(FieldValidator.isValidLastName(null));
    }

    /**
     * Verifies supported physical-address values.
     */
    @Test
    void addressValidation_acceptsSupportedValues() {
        assertTrue(
                FieldValidator.isValidStreetAddress(
                        "123 Main St, Apt 4"
                )
        );

        assertTrue(FieldValidator.isValidCity("Fort Worth"));
        assertTrue(FieldValidator.isValidState("TX"));
        assertTrue(FieldValidator.isValidState("tx"));
        assertFalse(FieldValidator.isValidState("Texas"));
        assertFalse(FieldValidator.isValidState("T"));
        assertFalse(FieldValidator.isValidState("T1"));
        assertTrue(FieldValidator.isValidZipCode("76102"));
        assertTrue(FieldValidator.isValidZipCode("76102-1234"));
    }

    /**
     * Verifies malformed physical-address values are rejected.
     */
    @Test
    void addressValidation_rejectsInvalidValues() {
        assertFalse(FieldValidator.isValidStreetAddress("@@@"));
        assertFalse(FieldValidator.isValidCity("Fort W0rth"));
        assertFalse(FieldValidator.isValidState("T3xas"));
        assertFalse(FieldValidator.isValidZipCode("1234"));
        assertFalse(FieldValidator.isValidZipCode("123456789"));
    }

    /**
     * Verifies that the supported phone-number input formats are accepted.
     */
    @Test
    void phoneValidation_acceptsSupportedFormats() {
        assertTrue(
                FieldValidator.isValidPhoneNumberFormatted(
                        "3125551212"
                )
        );

        assertTrue(
                FieldValidator.isValidPhoneNumberFormatted(
                        "312-555-1212"
                )
        );

        assertTrue(
                FieldValidator.isValidPhoneNumberFormatted(
                        "(312) 555-1212"
                )
        );
    }

    /**
     * Verifies that phone validation does not simply strip arbitrary
     * characters and accept any value containing ten digits.
     */
    @Test
    void phoneValidation_rejectsUnsupportedFormatting() {
        assertFalse(
                FieldValidator.isValidPhoneNumberFormatted(
                        "abc3125551212"
                )
        );

        assertFalse(
                FieldValidator.isValidPhoneNumberFormatted(
                        "312.555.1212"
                )
        );

        assertFalse(
                FieldValidator.isValidPhoneNumberFormatted(
                        "312555121"
                )
        );

        assertFalse(
                FieldValidator.isValidPhoneNumberFormatted(null)
        );
    }

    /**
     * Verifies valid and invalid email-address handling.
     */
    @Test
    void emailValidation_enforcesEmailFormat() {
        assertTrue(
                FieldValidator.isValidEmail(
                        "james.stevens@example.com"
                )
        );

        assertFalse(
                FieldValidator.isValidEmail(
                        "not-an-email"
                )
        );

        assertFalse(FieldValidator.isValidEmail(null));
    }

    /**
     * Verifies that built-in and custom labels can be used while blank
     * labels are rejected.
     */
    @Test
    void labelValidation_supportsCustomLabels() {
        assertTrue(FieldValidator.isValidLabel("Home"));
        assertTrue(FieldValidator.isValidLabel("Emergency Contact"));
        assertTrue(FieldValidator.isValidLabel("Vacation House"));

        assertFalse(FieldValidator.isValidLabel(""));
        assertFalse(FieldValidator.isValidLabel("   "));
        assertFalse(FieldValidator.isValidLabel(null));
    }
}
