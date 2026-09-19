package address_book;

import address_utils.storage.AddressBookStorage;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests contact-display workflows through the address-book controller.
 *
 * <p>The injected scanner keeps CLI tests deterministic while production
 * execution continues to use the controller's standard no-argument
 * constructor.</p>
 */
class AddressBookControllerTest {

    @Test
    void displayReportsEmptyAddressBook() {
        AddressBook addressBook = new AddressBook();

        String output = captureDisplay(addressBook, "1\n");

        assertTrue(output.contains("Address book is empty."));
    }

    @Test
    void displayOneShowsNumberedContactsAndSelectedDetails() {
        AddressBook addressBook = addressBook();

        String output = captureDisplay(addressBook, """
                1
                1
                """);

        assertTrue(output.contains("--- Display Contacts ---"));
        assertTrue(output.contains("1. View One Contact"));
        assertTrue(output.contains("1. James Stevens"));
        assertTrue(output.contains(
                "---------- Contact Details ----------"));
        assertTrue(output.contains("James Stevens"));
        assertTrue(output.contains("Home Address:"));
        assertTrue(output.contains("1234 West Oak Street"));
        assertTrue(output.contains("Mobile Phone:"));
        assertTrue(output.contains("(210) 555-1212"));
        assertTrue(output.contains("Personal Email:"));
        assertTrue(output.contains("james@example.com"));
    }

    @Test
    void displayOneRetriesInvalidSelection() {
        AddressBook addressBook = addressBook();

        String output = captureDisplay(addressBook, """
                1
                999
                1
                """);

        assertTrue(output.contains(
                "Invalid selection. Please try again."));
        assertTrue(output.contains(
                "---------- Contact Details ----------"));
    }

    @Test
    void displayOneCanBeCancelled() {
        AddressBook addressBook = addressBook();

        String output = captureDisplay(addressBook, """
                1
                cancel
                """);

        assertTrue(output.contains("---------- Select Contact ----------"));
        assertFalse(output.contains(
                "---------- Contact Details ----------"));
    }

    @Test
    void displayAllShowsCompleteContactDetails() {
        AddressBook addressBook = addressBook();

        String output = captureDisplay(addressBook, "2\n");

        assertTrue(output.contains(
                "---------- Address Book Contacts ----------"));
        assertTrue(output.contains("James Stevens"));
        assertTrue(output.contains("1234 West Oak Street"));
        assertTrue(output.contains("(210) 555-1212"));
        assertTrue(output.contains("james@example.com"));
    }

    @Test
    void displayMenuRetriesInvalidChoice() {
        AddressBook addressBook = addressBook();

        String output = captureDisplay(addressBook, """
                999
                3
                """);

        assertTrue(output.contains(
                "Invalid selection. Please choose 1-3."));
    }

    @Test
    void displayMenuCanReturnWithBackChoice() {
        AddressBook addressBook = addressBook();

        String output = captureDisplay(addressBook, "3\n");

        assertTrue(output.contains("--- Display Contacts ---"));
        assertFalse(output.contains(
                "---------- Contact Details ----------"));
    }

    @Test
    void displayMenuCanReturnWithCancel() {
        AddressBook addressBook = addressBook();

        String output = captureDisplay(addressBook, "cancel\n");

        assertTrue(output.contains("--- Display Contacts ---"));
        assertFalse(output.contains(
                "---------- Contact Details ----------"));
    }

    private String captureDisplay(AddressBook addressBook, String input) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (PrintStream capture =
                     new PrintStream(buffer, true, StandardCharsets.UTF_8);
             Scanner scanner = new Scanner(input)) {
            System.setOut(capture);

            AddressBookController controller =
                    new AddressBookController(
                            scanner,
                            addressBook,
                            new AddressBookStorage(),
                            new ContactInputHandler()
                    );

            controller.display();
            return buffer.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(original);
        }
    }

    private AddressBook addressBook() {
        AddressBook addressBook = new AddressBook();

        Contact contact = new Contact(
                "James",
                "Stevens",
                new ArrayList<>(List.of(
                        new Address(
                                "Home",
                                "1234 West Oak Street",
                                "Fort Worth",
                                "TX",
                                "76102"
                        )
                )),
                new ArrayList<>(List.of(
                        new PhoneNumber("Mobile", "(210) 555-1212")
                )),
                new ArrayList<>(List.of(
                        new EmailAddress("Personal", "james@example.com")
                ))
        );

        addressBook.setContacts(List.of(contact));
        return addressBook;
    }
}
