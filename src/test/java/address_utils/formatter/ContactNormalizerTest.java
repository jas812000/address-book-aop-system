package address_utils.formatter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests canonical normalization of validated contact values.
 *
 * <p>The tests verify that names and address components receive consistent
 * presentation casing, state abbreviations are uppercase, email addresses
 * are lowercase, and phone numbers and ZIP codes use their canonical
 * storage formats.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
class ContactNormalizerTest {

    /**
     * Verifies canonical casing of ordinary first and last names.
     */
    @Test
    void normalizeName_normalizesMixedCaseName() {
        assertEquals(
                "Test",
                ContactNormalizer.normalizeName("tEST")
        );

        assertEquals(
                "Contact",
                ContactNormalizer.normalizeName("cOnTaCt")
        );
    }

    /**
     * Verifies that spaces, apostrophes, and hyphens establish new
     * capitalization boundaries in compound names.
     */
    @Test
    void normalizeName_normalizesCompoundNames() {
        assertEquals(
                "Mary Jane",
                ContactNormalizer.normalizeName("mARY jANE")
        );

        assertEquals(
                "O'Connor",
                ContactNormalizer.normalizeName("o'CONNOR")
        );

        assertEquals(
                "Martinez-Samuel",
                ContactNormalizer.normalizeName("mARTINEZ-sAMUEL")
        );
    }

    /**
     * Verifies that null and blank names are rejected.
     */
    @Test
    void normalizeName_rejectsMissingName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ContactNormalizer.normalizeName(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> ContactNormalizer.normalizeName("   ")
        );
    }

    /**
     * Verifies canonical title-style casing of street addresses.
     */
    @Test
    void normalizeStreet_normalizesMixedCaseStreet() {
        assertEquals(
                "123 Main Street, Apt 4",
                ContactNormalizer.normalizeStreet(
                        "123 MAIN STREET, APT 4"
                )
        );
    }

    /**
     * Verifies canonical title-style casing of city names.
     */
    @Test
    void normalizeCity_normalizesMixedCaseCity() {
        assertEquals(
                "Fort Worth",
                ContactNormalizer.normalizeCity("fORT wORTH")
        );
    }

    /**
     * Verifies that state abbreviations are stored in uppercase.
     */
    @Test
    void normalizeState_normalizesLowercaseState() {
        assertEquals(
                "TX",
                ContactNormalizer.normalizeState("tx")
        );
    }

    /**
     * Verifies that invalid state abbreviations are rejected.
     */
    @Test
    void normalizeState_rejectsInvalidState() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ContactNormalizer.normalizeState("Texas")
        );
    }

    /**
     * Verifies that email addresses are stored in lowercase.
     */
    @Test
    void normalizeEmail_normalizesMixedCaseEmail() {
        assertEquals(
                "test@example.com",
                ContactNormalizer.normalizeEmail("TEST@EXAMPLE.COM")
        );
    }

    /**
     * Verifies that an unformatted ten-digit phone number is converted to
     * the application's canonical representation.
     */
    @Test
    void normalizePhone_formatsDigitsOnlyNumber() {
        assertEquals(
                "(312) 555-1212",
                ContactNormalizer.normalizePhone("3125551212")
        );
    }

    /**
     * Verifies that supported formatted phone numbers are normalized to the
     * same canonical representation.
     */
    @Test
    void normalizePhone_normalizesSupportedFormats() {
        assertEquals(
                "(312) 555-1212",
                ContactNormalizer.normalizePhone("312-555-1212")
        );

        assertEquals(
                "(312) 555-1212",
                ContactNormalizer.normalizePhone("(312) 555-1212")
        );
    }

    /**
     * Verifies that malformed phone numbers are rejected instead of having
     * arbitrary characters silently removed.
     */
    @Test
    void normalizePhone_rejectsInvalidNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ContactNormalizer.normalizePhone("abc3125551212")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> ContactNormalizer.normalizePhone("123")
        );
    }

    /**
     * Verifies preservation and canonical formatting of supported ZIP-code
     * representations and rejection of malformed ZIP codes.
     */
    @Test
    void normalizeZip_handlesSupportedAndInvalidValues() {
        assertEquals(
                "12345",
                ContactNormalizer.normalizeZip("12345")
        );

        assertEquals(
                "12345-6789",
                ContactNormalizer.normalizeZip("12345-6789")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> ContactNormalizer.normalizeZip("1234")
        );
    }
}
