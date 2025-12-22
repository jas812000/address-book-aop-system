package address_utils.formatter;

import address_book.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactCSVFormatterTest {

    @Test
    void header_isCorrect() {
        assertEquals("First Name,Last Name,Street,City,State,Zip,Phone,Email", ContactCSVFormatter.header());
    }

    @Test
    void toCSV_escapesCommasAndQuotes() {
        Contact c = new Contact(
                "James",
                "Stevens",
                "123 \"Main\", St",
                "Chicago",
                "Illinois",
                "60601",
                "3125551212",
                "james@example.com"
        );

        String csv = ContactCSVFormatter.toCSV(c);

        // Street contains quotes + comma -> must be wrapped in quotes and internal quotes doubled
        assertTrue(csv.contains("\"123 \"\"Main\"\", St\""));
    }
}
