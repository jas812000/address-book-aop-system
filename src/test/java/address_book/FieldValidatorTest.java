package address_book;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldValidatorTest {

    @Test
    void isValidZipCode_acceptsFiveDigitZip() {
        assertTrue(FieldValidator.isValidZipCode("12345"));
    }

    @Test
    void isValidZipCode_acceptsZipPlus4() {
        assertTrue(FieldValidator.isValidZipCode("12345-6789"));
    }

    @Test
    void isValidEmail_rejectsInvalidEmail() {
        assertFalse(FieldValidator.isValidEmail("not-an-email"));
    }

    @Test
    void isValidPhoneNumberFormatted_acceptsCommonFormats() {
        assertTrue(FieldValidator.isValidPhoneNumberFormatted("(312) 555-1212"));
        assertTrue(FieldValidator.isValidPhoneNumberFormatted("312-555-1212"));
        assertTrue(FieldValidator.isValidPhoneNumberFormatted("3125551212"));
    }
}
