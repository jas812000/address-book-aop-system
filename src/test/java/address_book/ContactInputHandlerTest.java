package address_book;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests command-line contact creation, including validation, normalization,
 * standard/custom labels, multi-value data, and cancellation.
 */
class ContactInputHandlerTest {

    private final ContactInputHandler handler = new ContactInputHandler();

    @Test
    void createsMinimalContact() {
        Contact contact = prompt("""
                James
                Stevens
                n
                n
                n
                """);

        assertNotNull(contact);
        assertEquals("James", contact.getFirstName());
        assertEquals("Stevens", contact.getLastName());
        assertTrue(contact.getAddresses().isEmpty());
        assertTrue(contact.getPhoneNumbers().isEmpty());
        assertTrue(contact.getEmailAddresses().isEmpty());
    }

    @Test
    void retriesInvalidNamesBeforeCreatingContact() {
        Contact contact = prompt("""
                1
                Mary Jane
                123
                O'Connor-Martinez
                n
                n
                n
                """);

        assertNotNull(contact);
        assertEquals("Mary Jane", contact.getFirstName());
        assertEquals("O'Connor-Martinez", contact.getLastName());
    }

    @Test
    void createsAddressAndNormalizesLowercaseState() {
        Contact contact = prompt("""
                James
                Stevens
                y
                1
                1234 West Oak Street
                Fort Worth
                tx
                76102
                n
                n
                n
                """);

        assertNotNull(contact);
        assertEquals(1, contact.getAddresses().size());

        Address address = contact.getAddresses().get(0);
        assertEquals("Home", address.getLabel());
        assertEquals("TX", address.getState());
        assertEquals("76102", address.getZipCode());
    }

    @Test
    void createsCustomAddressLabelAndZipPlusFour() {
        Contact contact = prompt("""
                Mary
                Martinez
                y
                3
                Vacation
                987 Lake View Drive
                Austin
                tx
                78701-1234
                n
                n
                n
                """);

        Address address = contact.getAddresses().get(0);
        assertEquals("Vacation", address.getLabel());
        assertEquals("TX", address.getState());
        assertEquals("78701-1234", address.getZipCode());
    }

    @Test
    void rejectsFullStateNameThenAcceptsAbbreviation() {
        Contact contact = prompt("""
                James
                Stevens
                y
                1
                1234 West Oak Street
                Fort Worth
                Texas
                tx
                76102
                n
                n
                n
                """);

        assertEquals("TX", contact.getAddresses().get(0).getState());
    }

    @Test
    void acceptsAndNormalizesAllSupportedPhoneFormats() {
        Contact contact = prompt("""
                James
                Stevens
                n
                y
                1
                2105551212
                y
                2
                210-555-3434
                y
                4
                Emergency
                (817) 555-9876
                n
                n
                """);

        assertEquals(3, contact.getPhoneNumbers().size());
        assertEquals("(210) 555-1212",
                contact.getPhoneNumbers().get(0).getNumber());
        assertEquals("(210) 555-3434",
                contact.getPhoneNumbers().get(1).getNumber());
        assertEquals("(817) 555-9876",
                contact.getPhoneNumbers().get(2).getNumber());
        assertEquals("Emergency",
                contact.getPhoneNumbers().get(2).getLabel());
    }

    @Test
    void rejectsInvalidPhoneThenAcceptsValidPhone() {
        Contact contact = prompt("""
                James
                Stevens
                n
                y
                1
                call2105551212
                2105551212
                n
                n
                """);

        assertEquals("(210) 555-1212",
                contact.getPhoneNumbers().get(0).getNumber());
    }

    @Test
    void createsStandardAndCustomEmailLabels() {
        Contact contact = prompt("""
                James
                Stevens
                n
                n
                y
                1
                james@example.com
                y
                3
                School
                james@school.edu
                n
                """);

        assertEquals(2, contact.getEmailAddresses().size());
        assertEquals("Personal",
                contact.getEmailAddresses().get(0).getLabel());
        assertEquals("School",
                contact.getEmailAddresses().get(1).getLabel());
    }

    @Test
    void rejectsInvalidEmailThenAcceptsValidEmail() {
        Contact contact = prompt("""
                James
                Stevens
                n
                n
                y
                1
                invalid-email
                james@example.com
                n
                """);

        assertEquals("james@example.com",
                contact.getEmailAddresses().get(0).getEmail());
    }

    @Test
    void cancellationAtFirstNameReturnsNull() {
        assertNull(prompt("cancel\n"));
    }

    @Test
    void cancellationDuringAddressEntryCancelsWholeCreation() {
        assertNull(prompt("""
                James
                Stevens
                y
                1
                cancel
                """));
    }

    @Test
    void cancellationDuringCustomLabelCancelsWholeCreation() {
        assertNull(prompt("""
                James
                Stevens
                y
                3
                cancel
                """));
    }

    private Contact prompt(String input) {
        try (Scanner scanner = new Scanner(input)) {
            return handler.promptContactDetails(scanner);
        }
    }
}
