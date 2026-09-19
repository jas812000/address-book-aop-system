package address_utils.formatter;

import address_book.Address;
import address_book.Contact;
import address_book.EmailAddress;
import address_book.PhoneNumber;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests detailed and compact user-facing contact formatting.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2026-09-18
 */
class ContactFormatterTest {

    /**
     * Creates a contact containing multiple labeled values.
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
     * Verifies detailed formatting of all contact-value categories.
     */
    @Test
    void format_includesAllContactInformation() {
        String formatted =
                ContactFormatter.format(createContact());

        assertTrue(formatted.contains("James Stevens"));
        assertTrue(
                formatted.contains(
                        "Home Address:\n"
                                + "    123 Main St\n"
                                + "    Fort Worth, TX 76102"
                )
        );
        assertTrue(
                formatted.contains(
                        "Mobile Phone:\n"
                                + "    (817) 555-1212"
                )
        );
        assertTrue(
                formatted.contains(
                        "Personal Email:\n"
                                + "    james@example.com"
                )
        );
    }

    /**
     * Verifies compact formatting.
     */
    @Test
    void formatCompact_returnsFullName() {
        assertEquals(
                "James Stevens",
                ContactFormatter.formatCompact(createContact())
        );
    }

    /**
     * Verifies explicit null-contact representations.
     */
    @Test
    void formatMethods_handleNullContact() {
        assertEquals(
                "[Invalid Contact]",
                ContactFormatter.format(null)
        );

        assertEquals(
                "[Invalid Contact]",
                ContactFormatter.formatCompact(null)
        );
    }
}
