package address_book;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests in-memory address-book ownership and defensive-copy behavior.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2026-09-18
 */
class AddressBookTest {

    /**
     * Creates a representative contact.
     *
     * @return valid contact
     */
    private Contact createContact() {
        return new Contact(
                "James",
                "Stevens",
                List.of(),
                List.of(
                        new PhoneNumber(
                                "Mobile",
                                "(817) 555-1212"
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
     * Verifies that added contacts are copied before storage.
     */
    @Test
    void addContact_storesIndependentCopy() {
        AddressBook addressBook = new AddressBook();
        Contact original = createContact();

        addressBook.addContact(original);
        original.setFirstName("John");

        assertEquals(
                "James",
                addressBook.getContacts().get(0).getFirstName()
        );
    }

    /**
     * Verifies that returned contacts cannot mutate internal address-book
     * state.
     */
    @Test
    void getContacts_returnsIndependentCopies() {
        AddressBook addressBook = new AddressBook();
        addressBook.addContact(createContact());

        Contact returned = addressBook.getContacts().get(0);
        returned.setFirstName("John");

        assertEquals(
                "James",
                addressBook.getContacts().get(0).getFirstName()
        );
    }

    /**
     * Verifies that the returned contact collection is unmodifiable.
     */
    @Test
    void getContacts_returnsUnmodifiableList() {
        AddressBook addressBook = new AddressBook();
        addressBook.addContact(createContact());

        assertThrows(
                UnsupportedOperationException.class,
                () -> addressBook.getContacts().clear()
        );
    }

    /**
     * Verifies that setContacts copies both the supplied list and its
     * contact elements.
     */
    @Test
    void setContacts_defensivelyCopiesInput() {
        AddressBook addressBook = new AddressBook();
        Contact original = createContact();

        List<Contact> supplied = new ArrayList<>();
        supplied.add(original);

        addressBook.setContacts(supplied);

        supplied.clear();
        original.setFirstName("John");

        assertEquals(1, addressBook.getContacts().size());

        assertEquals(
                "James",
                addressBook.getContacts().get(0).getFirstName()
        );
    }

    /**
     * Verifies null-list handling during replacement.
     */
    @Test
    void setContacts_nullClearsAddressBook() {
        AddressBook addressBook = new AddressBook();
        addressBook.addContact(createContact());

        addressBook.setContacts(null);

        assertTrue(addressBook.getContacts().isEmpty());
    }

    /**
     * Verifies null contacts cannot be added.
     */
    @Test
    void addContact_rejectsNull() {
        AddressBook addressBook = new AddressBook();

        assertThrows(
                NullPointerException.class,
                () -> addressBook.addContact(null)
        );
    }
}
