package address_book;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests proving that AspectJ validation protects domain
 * construction and mutation boundaries.
 *
 * <p>These tests intentionally exercise ordinary domain methods rather than
 * invoking the validation aspect directly. A passing suite therefore
 * demonstrates that the validation advice was woven into the application.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2026-09-18
 */
class ValidationAspectIntegrationTest {

    /**
     * Verifies that the aspect rejects invalid contact construction.
     */
    @Test
    void contactConstruction_rejectsInvalidName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Contact(
                        "1James",
                        "Stevens",
                        List.of(),
                        List.of(),
                        List.of()
                )
        );
    }

    /**
     * Verifies that valid compound and hyphenated names survive AspectJ
     * validation.
     */
    @Test
    void contactConstruction_acceptsSupportedCompoundNames() {
        assertDoesNotThrow(
                () -> new Contact(
                        "Mary Jane",
                        "Martinez-Samuel",
                        List.of(),
                        List.of(),
                        List.of()
                )
        );
    }

    /**
     * Verifies that invalid name mutations are rejected before state changes.
     */
    @Test
    void contactNameMutation_rejectsInvalidValueWithoutChangingState() {
        Contact contact = new Contact(
                "James",
                "Stevens",
                List.of(),
                List.of(),
                List.of()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> contact.setFirstName("1")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> contact.setLastName("-")
        );

        assertEquals("James", contact.getFirstName());
        assertEquals("Stevens", contact.getLastName());
    }

    /**
     * Verifies constructor validation for physical addresses.
     */
    @Test
    void addressConstruction_rejectsInvalidFields() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Address(
                        "",
                        "123 Main St",
                        "Fort Worth",
                        "TX",
                        "76102"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Address(
                        "Home",
                        "@@@",
                        "Fort Worth",
                        "TX",
                        "76102"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Address(
                        "Home",
                        "123 Main St",
                        "Fort W0rth",
                        "TX",
                        "76102"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Address(
                        "Home",
                        "123 Main St",
                        "Fort Worth",
                        "T3xas",
                        "76102"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Address(
                        "Home",
                        "123 Main St",
                        "Fort Worth",
                        "TX",
                        "761021234"
                )
        );
    }

    /**
     * Verifies that address setter advice prevents invalid mutations.
     */
    @Test
    void addressMutation_rejectsInvalidValuesWithoutChangingState() {
        Address address = new Address(
                "Home",
                "123 Main St",
                "Fort Worth",
                "TX",
                "76102"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> address.setLabel("")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> address.setStreet("@@@")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> address.setCity("Fort W0rth")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> address.setState("T3xas")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> address.setZipCode("1234")
        );

        assertEquals("Home", address.getLabel());
        assertEquals("123 Main St", address.getStreet());
        assertEquals("Fort Worth", address.getCity());
        assertEquals("TX", address.getState());
        assertEquals("76102", address.getZipCode());
    }

    /**
     * Verifies constructor validation for phone-number values.
     */
    @Test
    void phoneConstruction_rejectsInvalidFields() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PhoneNumber("", "(817) 555-1212")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new PhoneNumber("Mobile", "817.555.1212")
        );
    }

    /**
     * Verifies that phone setter advice protects existing state.
     */
    @Test
    void phoneMutation_rejectsInvalidValuesWithoutChangingState() {
        PhoneNumber phone =
                new PhoneNumber("Mobile", "(817) 555-1212");

        assertThrows(
                IllegalArgumentException.class,
                () -> phone.setLabel("")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> phone.setNumber("abc8175551212")
        );

        assertEquals("Mobile", phone.getLabel());
        assertEquals("(817) 555-1212", phone.getNumber());
    }

    /**
     * Verifies constructor validation for email-address values.
     */
    @Test
    void emailConstruction_rejectsInvalidFields() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EmailAddress("", "james@example.com")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new EmailAddress("Personal", "bad-email")
        );
    }

    /**
     * Verifies that email setter advice protects existing state.
     */
    @Test
    void emailMutation_rejectsInvalidValuesWithoutChangingState() {
        EmailAddress email =
                new EmailAddress("Personal", "james@example.com");

        assertThrows(
                IllegalArgumentException.class,
                () -> email.setLabel("")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> email.setEmail("bad-email")
        );

        assertEquals("Personal", email.getLabel());
        assertEquals("james@example.com", email.getEmail());
    }

    /**
     * Verifies that direct address-book additions accept a complete valid
     * contact through the AspectJ-protected operation boundary.
     */
    @Test
    void addressBookAdd_acceptsValidContact() {
        AddressBook addressBook = new AddressBook();

        Contact contact = new Contact(
                "James",
                "Stevens",
                List.of(),
                List.of(),
                List.of()
        );

        assertDoesNotThrow(
                () -> addressBook.addContact(contact)
        );

        assertEquals(1, addressBook.getContacts().size());
    }
}
