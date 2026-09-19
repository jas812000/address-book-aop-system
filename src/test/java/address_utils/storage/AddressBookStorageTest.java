package address_utils.storage;

import address_book.Address;
import address_book.Contact;
import address_book.EmailAddress;
import address_book.PhoneNumber;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.github.stefanbirkner.systemlambda.SystemLambda
        .withEnvironmentVariable;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests file persistence for the current multi-value contact model.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
class AddressBookStorageTest {

    /**
     * Verifies that multiple addresses, phone numbers, and email addresses
     * survive a complete save-and-load cycle.
     */
    @Test
    void saveThenLoad_preservesCompleteContact() throws Exception {
        Path testDirectory =
                Path.of("target", "test-data", "round-trip");

        Files.createDirectories(testDirectory);

        withEnvironmentVariable(
                "APP_DATA_DIR",
                testDirectory.toString()
        ).execute(() -> {
            AddressBookStorage storage =
                    new AddressBookStorage();

            Contact contact = new Contact(
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

            storage.save(List.of(contact));

            List<Contact> loaded = storage.load();

            assertEquals(1, loaded.size());

            Contact restored = loaded.get(0);

            assertEquals("James", restored.getFirstName());
            assertEquals("Stevens", restored.getLastName());

            assertEquals(2, restored.getAddresses().size());
            assertEquals(
                    "123 Main St, Apt 4",
                    restored.getAddresses().get(0).getStreet()
            );

            assertEquals(2, restored.getPhoneNumbers().size());
            assertEquals(
                    "(817) 555-9876",
                    restored.getPhoneNumbers().get(1).getNumber()
            );

            assertEquals(2, restored.getEmailAddresses().size());
            assertEquals(
                    "james@company.com",
                    restored.getEmailAddresses().get(1).getEmail()
            );
        });
    }
}
