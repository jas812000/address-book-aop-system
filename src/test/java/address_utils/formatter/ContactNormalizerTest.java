package address_utils.formatter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactNormalizerTest {

    @Test
    void normalizePhone_formatsValidNumber() {
        assertEquals("(312) 555-1212", ContactNormalizer.normalizePhone("3125551212"));
    }

    @Test
    void normalizePhone_returnsOriginalIfInvalid() {
        assertEquals("123", ContactNormalizer.normalizePhone("123"));
    }

    @Test
    void normalizeZip_formatsZipPlus4WithoutDash() {
        assertEquals("12345-6789", ContactNormalizer.normalizeZip("123456789"));
    }
}
