package validators;

import address_book.Address;
import address_book.Contact;
import address_book.EmailAddress;
import address_book.PhoneNumber;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests validation of complete contact aggregates.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
class ContactIntegrityValidatorTest {

    /**
     * Creates a representative valid contact.
     *
     * @return valid contact
     */
    private Contact validContact() {
        return new Contact(
                "James",
                "Stevens",
                List.of(
                        new Address(
                                "Home",
                                "123 Main St",
                                "Fort Worth",
                                "TX",
                                "76102"
                        )
                ),
                List.of(
                        new PhoneNumber(
                                "Mobile",
                                "(312) 555-1212"
                        )
                ),
                List.of(
                        new EmailAddress(
                                "Personal",
                                "james@example.com"
                        )
                )
        );
    }

    /**
     * Verifies that a complete valid contact passes aggregate validation.
     */
    @Test
    void validateContact_acceptsValidContact() {
        assertDoesNotThrow(
                () -> ContactIntegrityValidator.validateContact(
                        validContact()
                )
        );
    }

    /**
     * Verifies that contacts without optional multi-value information are
     * valid when their required names are valid.
     */
    @Test
    void validateContact_acceptsContactWithNamesOnly() {
        Contact contact = new Contact(
                "Ada",
                "Lovelace",
                List.of(),
                List.of(),
                List.of()
        );

        assertDoesNotThrow(
                () -> ContactIntegrityValidator.validateContact(contact)
        );
    }

    /**
     * Verifies that null is not accepted as a contact aggregate.
     */
    @Test
    void validateContact_rejectsNullContact() {
        assertThrows(
                NullPointerException.class,
                () -> ContactIntegrityValidator.validateContact(null)
        );
    }
}
