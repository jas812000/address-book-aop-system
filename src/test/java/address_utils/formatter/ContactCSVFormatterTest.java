package address_utils.formatter;

import address_book.Address;
import address_book.Contact;
import address_book.EmailAddress;
import address_book.PhoneNumber;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests serialization of the multi-value contact model into the persisted
 * CSV representation.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
class ContactCSVFormatterTest {

    /**
     * Creates a contact containing multiple values for serialization tests.
     *
     * @return representative contact
     */
    private Contact createContact() {
        return new Contact(
                "James",
                "Stevens",
                List.of(
                        new Address(
                                "Home",
                                "123 Main St, Apt 4",
                                "Fort Worth",
                                "TX",
                                "76102"
                        ),
                        new Address(
                                "Vacation",
                                "456 Lake Rd",
                                "Austin",
                                "TX",
                                "78701"
                        )
                ),
                List.of(
                        new PhoneNumber(
                                "Mobile",
                                "(312) 555-1212"
                        ),
                        new PhoneNumber(
                                "Work",
                                "(817) 555-9876"
                        )
                ),
                List.of(
                        new EmailAddress(
                                "Personal",
                                "james@example.com"
                        ),
                        new EmailAddress(
                                "Work",
                                "james@company.com"
                        )
                )
        );
    }

    /**
     * Verifies the current five-column persistence schema.
     */
    @Test
    void header_matchesCurrentSchema() {
        assertEquals(
                "First Name,Last Name,Addresses,Phone Numbers,Email Addresses",
                ContactCSVFormatter.header()
        );
    }

    /**
     * Verifies that all multi-value contact information is serialized.
     */
    @Test
    void toCSV_serializesMultipleValues() {
        String csv = ContactCSVFormatter.toCSV(createContact());

        assertTrue(csv.contains("Home~123 Main St"));
        assertTrue(csv.contains("Vacation~456 Lake Rd"));
        assertTrue(csv.contains("Mobile~(312) 555-1212"));
        assertTrue(csv.contains("Work~(817) 555-9876"));
        assertTrue(csv.contains("Personal~james@example.com"));
        assertTrue(csv.contains("Work~james@company.com"));
    }

    /**
     * Verifies that a comma inside an encoded collection causes the CSV
     * field to be quoted rather than being treated as another column.
     */
    @Test
    void toCSV_quotesFieldContainingComma() {
        String csv = ContactCSVFormatter.toCSV(createContact());

        assertTrue(
                csv.contains(
                        "\"Home~123 Main St, Apt 4~Fort Worth~TX~76102"
                )
        );
    }

    /**
     * Verifies escaping of characters reserved by the internal collection
     * encoding.
     */
    @Test
    void toCSV_escapesInternalSeparators() {
        Contact contact = new Contact(
                "James",
                "Stevens",
                List.of(
                        new Address(
                                "Home|Primary",
                                "123 Main St",
                                "Fort Worth",
                                "TX",
                                "76102"
                        )
                ),
                List.of(),
                List.of(
                        new EmailAddress(
                                "Personal~Main",
                                "james@example.com"
                        )
                )
        );

        String csv = ContactCSVFormatter.toCSV(contact);

        assertTrue(csv.contains("Home%7CPrimary"));
        assertTrue(csv.contains("Personal%7EMain"));
    }
}
