package address_book;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests contact ownership, defensive copying, and controlled collection
 * mutation.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2026-09-18
 */
class ContactTest {

    /**
     * Creates a representative contact for domain-model tests.
     *
     * @return populated contact
     */
    private Contact createContact() {
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
     * Verifies basic name behavior.
     */
    @Test
    void contact_exposesNameAndFullName() {
        Contact contact = createContact();

        assertEquals("James", contact.getFirstName());
        assertEquals("Stevens", contact.getLastName());
        assertEquals("James Stevens", contact.getFullName());
    }

    /**
     * Verifies that constructor collections are copied instead of retained.
     */
    @Test
    void constructor_defensivelyCopiesCollections() {
        List<Address> addresses = new ArrayList<>();
        addresses.add(
                new Address(
                        "Home",
                        "123 Main St",
                        "Fort Worth",
                        "TX",
                        "76102"
                )
        );

        Contact contact = new Contact(
                "James",
                "Stevens",
                addresses,
                List.of(),
                List.of()
        );

        addresses.clear();

        assertEquals(1, contact.getAddresses().size());
    }

    /**
     * Verifies that constructor elements are deeply copied.
     */
    @Test
    void constructor_defensivelyCopiesElements() {
        Address original = new Address(
                "Home",
                "123 Main St",
                "Fort Worth",
                "TX",
                "76102"
        );

        Contact contact = new Contact(
                "James",
                "Stevens",
                List.of(original),
                List.of(),
                List.of()
        );

        original.setStreet("999 Other St");

        assertEquals(
                "123 Main St",
                contact.getAddresses().get(0).getStreet()
        );
    }

    /**
     * Verifies that collection getters cannot structurally modify the
     * contact.
     */
    @Test
    void collectionGetters_areUnmodifiable() {
        Contact contact = createContact();

        assertThrows(
                UnsupportedOperationException.class,
                () -> contact.getAddresses().clear()
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> contact.getPhoneNumbers().clear()
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> contact.getEmailAddresses().clear()
        );
    }

    /**
     * Verifies that objects returned from collection getters are independent
     * copies rather than references to internal state.
     */
    @Test
    void collectionGetters_returnDeepCopies() {
        Contact contact = createContact();

        Address returnedAddress = contact.getAddresses().get(0);
        PhoneNumber returnedPhone = contact.getPhoneNumbers().get(0);
        EmailAddress returnedEmail =
                contact.getEmailAddresses().get(0);

        returnedAddress.setStreet("999 Other St");
        returnedPhone.setNumber("(214) 555-9999");
        returnedEmail.setEmail("other@example.com");

        assertEquals(
                "123 Main St",
                contact.getAddresses().get(0).getStreet()
        );

        assertEquals(
                "(817) 555-1212",
                contact.getPhoneNumbers().get(0).getNumber()
        );

        assertEquals(
                "james@example.com",
                contact.getEmailAddresses().get(0).getEmail()
        );
    }

    /**
     * Verifies controlled addition of multi-value contact information.
     */
    @Test
    void addMethods_addIndependentCopies() {
        Contact contact = createContact();

        Address address = new Address(
                "Work",
                "456 Office Rd",
                "Dallas",
                "TX",
                "75201"
        );

        PhoneNumber phone =
                new PhoneNumber("Work", "(214) 555-2222");

        EmailAddress email =
                new EmailAddress("Work", "james@company.com");

        contact.addAddress(address);
        contact.addPhoneNumber(phone);
        contact.addEmailAddress(email);

        address.setStreet("999 Changed Rd");
        phone.setNumber("(972) 555-3333");
        email.setEmail("changed@example.com");

        assertEquals(2, contact.getAddresses().size());
        assertEquals(2, contact.getPhoneNumbers().size());
        assertEquals(2, contact.getEmailAddresses().size());

        assertEquals(
                "456 Office Rd",
                contact.getAddresses().get(1).getStreet()
        );

        assertEquals(
                "(214) 555-2222",
                contact.getPhoneNumbers().get(1).getNumber()
        );

        assertEquals(
                "james@company.com",
                contact.getEmailAddresses().get(1).getEmail()
        );
    }

    /**
     * Verifies replacement of individual multi-value items.
     */
    @Test
    void replaceMethods_replaceSelectedItems() {
        Contact contact = createContact();

        contact.replaceAddress(
                0,
                new Address(
                        "Work",
                        "456 Office Rd",
                        "Dallas",
                        "TX",
                        "75201"
                )
        );

        contact.replacePhoneNumber(
                0,
                new PhoneNumber("Work", "(214) 555-2222")
        );

        contact.replaceEmailAddress(
                0,
                new EmailAddress("Work", "james@company.com")
        );

        assertEquals(
                "Work",
                contact.getAddresses().get(0).getLabel()
        );

        assertEquals(
                "(214) 555-2222",
                contact.getPhoneNumbers().get(0).getNumber()
        );

        assertEquals(
                "james@company.com",
                contact.getEmailAddresses().get(0).getEmail()
        );
    }

    /**
     * Verifies removal of individual multi-value items.
     */
    @Test
    void removeMethods_removeSelectedItems() {
        Contact contact = createContact();

        contact.removeAddress(0);
        contact.removePhoneNumber(0);
        contact.removeEmailAddress(0);

        assertTrue(contact.getAddresses().isEmpty());
        assertTrue(contact.getPhoneNumbers().isEmpty());
        assertTrue(contact.getEmailAddresses().isEmpty());
    }

    /**
     * Verifies that copying a contact creates an independent deep copy.
     */
    @Test
    void copy_createsIndependentDeepCopy() {
        Contact original = createContact();
        Contact copy = original.copy();

        copy.setFirstName("John");

        Address changedAddress = copy.getAddresses().get(0);
        changedAddress.setStreet("999 Other St");
        copy.replaceAddress(0, changedAddress);

        assertEquals("James", original.getFirstName());

        assertEquals(
                "123 Main St",
                original.getAddresses().get(0).getStreet()
        );

        assertEquals("John", copy.getFirstName());

        assertEquals(
                "999 Other St",
                copy.getAddresses().get(0).getStreet()
        );
    }

    /**
     * Verifies that null values cannot be inserted through controlled
     * collection mutation.
     */
    @Test
    void addMethods_rejectNullElements() {
        Contact contact = createContact();

        assertThrows(
                NullPointerException.class,
                () -> contact.addAddress(null)
        );

        assertThrows(
                NullPointerException.class,
                () -> contact.addPhoneNumber(null)
        );

        assertThrows(
                NullPointerException.class,
                () -> contact.addEmailAddress(null)
        );
    }
}
