package address_utils.storage;

import address_book.Contact;
import address_utils.formatter.ContactCSVFormatter;
import address_utils.parser.ContactLineParser;
import io.AppPaths;
import io.FileLoader;
import io.FileParser;
import io.FileSaver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides file-based persistence for the address book.
 *
 * <p>Contacts are serialized through {@link ContactCSVFormatter} and
 * reconstructed through {@link ContactLineParser}. The parser supports
 * migration of legacy contact records into the current multi-value
 * domain model.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public class AddressBookStorage {

    /**
     * Saves all contacts to the configured address-book CSV file.
     *
     * @param contacts contacts to persist
     */
    public void save(List<Contact> contacts) {
        List<String> lines = new ArrayList<>();
        lines.add(ContactCSVFormatter.header());

        if (contacts != null) {
            for (Contact contact : contacts) {
                lines.add(ContactCSVFormatter.toCSV(contact));
            }
        }

        try {
            FileSaver.saveLines(
                    AppPaths.ADDRESS_BOOK_FILE,
                    lines
            );
        } catch (IOException e) {
            System.out.println(
                    "Error saving contacts: " + e.getMessage()
            );
        }
    }

    /**
     * Loads contacts from the configured address-book CSV file.
     *
     * <p>The header is removed before records are passed to the generic
     * file parser. Both legacy and current contact representations are
     * supported by {@link ContactLineParser}.</p>
     *
     * @return loaded contacts, or an empty list if loading fails
     */
    public List<Contact> load() {
        try {
            List<String> lines = new ArrayList<>(
                    FileLoader.loadLines(AppPaths.ADDRESS_BOOK_FILE)
            );

            if (!lines.isEmpty()
                    && lines.get(0)
                    .toLowerCase()
                    .contains("first name")) {

                lines.remove(0);
            }

            FileParser<Contact> parser =
                    new FileParser<>(
                            ",",
                            new ContactLineParser()
                    );

            return parser.parseLines(lines);

        } catch (IOException e) {
            System.out.println(
                    "Error loading contacts: " + e.getMessage()
            );

            return new ArrayList<>();
        }
    }
}
