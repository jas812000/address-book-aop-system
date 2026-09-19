package address_utils.parser;

import address_book.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests conversion of persisted contact fields into the current multi-value
 * contact model.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
class ContactLineParserTest {

    /**
     * Verifies that unsupported token counts are rejected.
     */
    @Test
    void parse_returnsNullForUnsupportedTokenCount() {
        ContactLineParser parser = new ContactLineParser();

        assertNull(
                parser.parse(
                        new String[]{"James", "Stevens"}
                )
        );
    }

    /**
     * Verifies parsing of the current five-column persistence format.
     */
    @Test
    void parse_readsCurrentMultiValueFormat() {
        ContactLineParser parser = new ContactLineParser();

        Contact contact = parser.parse(
                new String[]{
                        "James",
                        "Stevens",
                        "Home~123 Main St~Fort Worth~TX~76102"
                                + "|Work~456 Office Rd~Dallas~TX~75201",
                        "Mobile~(312) 555-1212"
                                + "|Work~(817) 555-9876",
                        "Personal~james@example.com"
                                + "|Work~james@company.com"
                }
        );

        assertNotNull(contact);
        assertEquals("James", contact.getFirstName());
        assertEquals("Stevens", contact.getLastName());

        assertEquals(2, contact.getAddresses().size());
        assertEquals(
                "Home",
                contact.getAddresses().get(0).getLabel()
        );
        assertEquals(
                "123 Main St",
                contact.getAddresses().get(0).getStreet()
        );

        assertEquals(2, contact.getPhoneNumbers().size());
        assertEquals(
                "(312) 555-1212",
                contact.getPhoneNumbers().get(0).getNumber()
        );

        assertEquals(2, contact.getEmailAddresses().size());
        assertEquals(
                "james@company.com",
                contact.getEmailAddresses().get(1).getEmail()
        );
    }

    /**
     * Verifies decoding of escaped collection separators.
     */
    @Test
    void parse_decodesEscapedComponents() {
        ContactLineParser parser = new ContactLineParser();

        Contact contact = parser.parse(
                new String[]{
                        "James",
                        "Stevens",
                        "Home%7CPrimary~123 Main St~Fort Worth~TX~76102",
                        "",
                        "Personal%7EMain~james@example.com"
                }
        );

        assertNotNull(contact);

        assertEquals(
                "Home|Primary",
                contact.getAddresses().get(0).getLabel()
        );

        assertEquals(
                "Personal~Main",
                contact.getEmailAddresses().get(0).getLabel()
        );
    }

    /**
     * Verifies that legacy eight-column records are migrated into the
     * current domain model.
     */
    @Test
    void parse_convertsLegacyFormat() {
        ContactLineParser parser = new ContactLineParser();

        Contact contact = parser.parse(
                new String[]{
                        " James ",
                        " Stevens ",
                        " 123 Main St ",
                        " Fort Worth ",
                        " TX ",
                        " 76102 ",
                        " 3125551212 ",
                        " james@example.com "
                }
        );

        assertNotNull(contact);
        assertEquals("James", contact.getFirstName());
        assertEquals("Stevens", contact.getLastName());

        assertEquals(1, contact.getAddresses().size());
        assertEquals(
                "123 Main St",
                contact.getAddresses().get(0).getStreet()
        );

        assertEquals(1, contact.getPhoneNumbers().size());
        assertEquals(
                "(312) 555-1212",
                contact.getPhoneNumbers().get(0).getNumber()
        );

        assertEquals(1, contact.getEmailAddresses().size());
        assertEquals(
                "james@example.com",
                contact.getEmailAddresses().get(0).getEmail()
        );
    }

    /**
     * Verifies that malformed current-format collection data is rejected.
     */
    @Test
    void parse_returnsNullForMalformedCurrentFormat() {
        ContactLineParser parser = new ContactLineParser();

        Contact contact = parser.parse(
                new String[]{
                        "James",
                        "Stevens",
                        "Malformed~Address",
                        "",
                        ""
                }
        );

        assertNull(contact);
    }
}
