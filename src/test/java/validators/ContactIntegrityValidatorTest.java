package validators;

import address_book.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactIntegrityValidatorTest {

    private static Contact validContact() {
        return new Contact(
                "James",
                "Stevens",
                "123 Main St",
                "Chicago",
                "Illinois",
                "60601",
                "3125551212",
                "james@example.com"
        );
    }

    @Test
    void validateContact_acceptsValidContact() {
        assertDoesNotThrow(() -> ContactIntegrityValidator.validateContact(validContact()));
    }

    @Test
    void validateContact_rejectsInvalidEmail() {
        Contact c = validContact();
        c.setEmail("bad-email");
        assertThrows(IllegalArgumentException.class, () -> ContactIntegrityValidator.validateContact(c));
    }

    @Test
    void validateContact_rejectsInvalidPhone() {
        Contact c = validContact();
        c.setPhone("123");
        assertThrows(IllegalArgumentException.class, () -> ContactIntegrityValidator.validateContact(c));
    }
}
