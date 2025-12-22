package address_utils.parser;

import address_book.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactLineParserTest {

    @Test
    void parse_returnsNullWhenTooFewTokens() {
        ContactLineParser parser = new ContactLineParser();
        assertNull(parser.parse(new String[]{"a","b"}));
    }

    @Test
    void parse_trimsTokens() {
        ContactLineParser parser = new ContactLineParser();
        Contact c = parser.parse(new String[]{
                " James ",
                " Stevens ",
                " 123 Main St ",
                " Chicago ",
                " Illinois ",
                " 60601 ",
                " 3125551212 ",
                " james@example.com "
        });

        assertNotNull(c);
        assertEquals("James", c.getFirstName());
        assertEquals("Stevens", c.getLastName());
        assertEquals("123 Main St", c.getStreet());
        assertEquals("Chicago", c.getCity());
        assertEquals("Illinois", c.getState());
        assertEquals("60601", c.getZipCode());
        assertEquals("3125551212", c.getPhone());
        assertEquals("james@example.com", c.getEmail());
    }
}
