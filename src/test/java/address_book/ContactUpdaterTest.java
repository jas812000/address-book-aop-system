package address_book;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests interactive contact editing through the public update workflow.
 *
 * <p>These tests verify mutation results as well as important user-facing
 * confirmation output introduced by the CLI workflow.</p>
 */
class ContactUpdaterTest {

    @Test
    void updatesFirstNameAndReportsChange() {
        Contact contact = contact();

        boolean changed = update(contact, """
                1
                Michelle
                6
                """);

        assertTrue(changed);
        assertEquals("Michelle", contact.getFirstName());
    }

    @Test
    void invalidFirstNameRetriesBeforeUpdating() {
        Contact contact = contact();

        boolean changed = update(contact, """
                1
                1
                Michelle
                6
                """);

        assertTrue(changed);
        assertEquals("Michelle", contact.getFirstName());
    }

    @Test
    void cancelledNameUpdateDoesNotChangeContact() {
        Contact contact = contact();

        boolean changed = update(contact, """
                1
                cancel
                6
                """);

        assertFalse(changed);
        assertEquals("James", contact.getFirstName());
    }

    @Test
    void addsAddressWithUppercaseState() {
        Contact contact = contact();

        boolean changed = update(contact, """
                3
                1
                2
                500 Commerce Street
                Dallas
                tx
                75201
                6
                """);

        assertTrue(changed);
        assertEquals(2, contact.getAddresses().size());
        Address added = contact.getAddresses().get(1);
        assertEquals("Work", added.getLabel());
        assertEquals("TX", added.getState());
    }

    @Test
    void replacesAddress() {
        Contact contact = contact();

        boolean changed = update(contact, """
                3
                2
                1
                2
                500 Commerce Street
                Dallas
                tx
                75201
                6
                """);

        assertTrue(changed);
        assertEquals(1, contact.getAddresses().size());
        assertEquals("500 Commerce Street",
                contact.getAddresses().get(0).getStreet());
        assertEquals("TX", contact.getAddresses().get(0).getState());
    }

    @Test
    void removeAddressShowsSelectedItemBeforeConfirmation() {
        Contact contact = contact();

        CapturedUpdate result = captureUpdate(contact, """
                3
                3
                1
                y
                6
                """);

        assertTrue(result.changed());
        assertTrue(contact.getAddresses().isEmpty());
        assertTrue(result.output().contains("Selected address:"));
        assertTrue(result.output().contains("1234 West Oak Street"));
        assertTrue(result.output().contains("Fort Worth, TX 76102"));
    }

    @Test
    void declinedAddressRemovalLeavesAddressUnchanged() {
        Contact contact = contact();

        boolean changed = update(contact, """
                3
                3
                1
                n
                4
                6
                """);

        assertFalse(changed);
        assertEquals(1, contact.getAddresses().size());
    }

    @Test
    void addsAndNormalizesPhoneNumber() {
        Contact contact = contact();

        boolean changed = update(contact, """
                4
                1
                2
                8175552222
                6
                """);

        assertTrue(changed);
        assertEquals(2, contact.getPhoneNumbers().size());
        assertEquals("(817) 555-2222",
                contact.getPhoneNumbers().get(1).getNumber());
    }

    @Test
    void replacesPhoneAndShowsSelectedPhone() {
        Contact contact = contact();

        CapturedUpdate result = captureUpdate(contact, """
                4
                2
                1
                2
                817-555-3333
                6
                """);

        assertTrue(result.changed());
        assertTrue(result.output().contains("Selected phone number:"));
        assertTrue(result.output().contains("(210) 555-1212"));
        assertEquals("(817) 555-3333",
                contact.getPhoneNumbers().get(0).getNumber());
    }

    @Test
    void removesPhoneAndShowsSelectedPhone() {
        Contact contact = contact();

        CapturedUpdate result = captureUpdate(contact, """
                4
                3
                1
                y
                6
                """);

        assertTrue(result.changed());
        assertTrue(contact.getPhoneNumbers().isEmpty());
        assertTrue(result.output().contains("Selected phone number:"));
        assertTrue(result.output().contains("(210) 555-1212"));
    }

    @Test
    void addsCustomLabeledEmail() {
        Contact contact = contact();

        boolean changed = update(contact, """
                5
                1
                3
                School
                james@school.edu
                6
                """);

        assertTrue(changed);
        assertEquals(2, contact.getEmailAddresses().size());
        assertEquals("School",
                contact.getEmailAddresses().get(1).getLabel());
    }

    @Test
    void replacesEmailAndShowsSelectedEmail() {
        Contact contact = contact();

        CapturedUpdate result = captureUpdate(contact, """
                5
                2
                1
                2
                james@work.com
                6
                """);

        assertTrue(result.changed());
        assertTrue(result.output().contains("Selected email address:"));
        assertTrue(result.output().contains("james@example.com"));
        assertEquals("james@work.com",
                contact.getEmailAddresses().get(0).getEmail());
    }

    @Test
    void removesEmailAndShowsSelectedEmail() {
        Contact contact = contact();

        CapturedUpdate result = captureUpdate(contact, """
                5
                3
                1
                y
                6
                """);

        assertTrue(result.changed());
        assertTrue(contact.getEmailAddresses().isEmpty());
        assertTrue(result.output().contains("Selected email address:"));
        assertTrue(result.output().contains("james@example.com"));
    }

    @Test
    void successfulUpdateDisplaysUpdatedContact() {
        Contact contact = contact();

        CapturedUpdate result = captureUpdate(contact, """
                1
                Michelle
                6
                """);

        assertTrue(result.output().contains(
                "---------- Updated Contact ----------"));
        assertTrue(result.output().contains("Michelle Stevens"));
    }

    @Test
    void cancelFromMainUpdateMenuFinishesWithoutChange() {
        Contact contact = contact();

        CapturedUpdate result = captureUpdate(contact, "cancel\n");

        assertFalse(result.changed());
        assertTrue(result.output().contains("Update operation finished."));
    }

    private boolean update(Contact contact, String input) {
        try (Scanner scanner = new Scanner(input)) {
            return ContactUpdater.updateFields(contact, scanner);
        }
    }

    private CapturedUpdate captureUpdate(Contact contact, String input) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (PrintStream capture =
                     new PrintStream(buffer, true, StandardCharsets.UTF_8);
             Scanner scanner = new Scanner(input)) {
            System.setOut(capture);
            boolean changed = ContactUpdater.updateFields(contact, scanner);
            return new CapturedUpdate(
                    changed,
                    buffer.toString(StandardCharsets.UTF_8)
            );
        } finally {
            System.setOut(original);
        }
    }

    private Contact contact() {
        return new Contact(
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
    }

    private record CapturedUpdate(boolean changed, String output) {
    }
}
