package address_book;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests contact searching across names and multi-value contact information.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2026-09-18
 */
class ContactSearcherTest {

    /**
     * Creates contacts containing multiple searchable values.
     *
     * @return representative contacts
     */
    private List<Contact> contacts() {
        return List.of(
                new Contact(
                        "James",
                        "Stevens",
                        List.of(),
                        List.of(
                                new PhoneNumber(
                                        "Mobile",
                                        "(817) 555-1212"
                                ),
                                new PhoneNumber(
                                        "Work",
                                        "(214) 555-2222"
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
                ),
                new Contact(
                        "Mary Jane",
                        "Martinez-Samuel",
                        List.of(),
                        List.of(
                                new PhoneNumber(
                                        "Mobile",
                                        "(972) 555-3333"
                                )
                        ),
                        List.of(
                                new EmailAddress(
                                        "Personal",
                                        "mary@example.com"
                                )
                        )
                )
        );
    }

    /**
     * Verifies case-insensitive partial first-name searching.
     */
    @Test
    void findMatches_searchesFirstNameCaseInsensitively() {
        List<Contact> matches =
                ContactSearcher.findMatches(
                        contacts(),
                        "first",
                        "jam"
                );

        assertEquals(1, matches.size());
        assertEquals("James", matches.get(0).getFirstName());
    }

    /**
     * Verifies partial last-name searching including hyphenated names.
     */
    @Test
    void findMatches_searchesHyphenatedLastName() {
        List<Contact> matches =
                ContactSearcher.findMatches(
                        contacts(),
                        "last",
                        "samuel"
                );

        assertEquals(1, matches.size());

        assertEquals(
                "Martinez-Samuel",
                matches.get(0).getLastName()
        );
    }

    /**
     * Verifies full-name searching.
     */
    @Test
    void findMatches_searchesFullName() {
        List<Contact> matches =
                ContactSearcher.findMatches(
                        contacts(),
                        "full",
                        "mary jane martinez"
                );

        assertEquals(1, matches.size());
        assertEquals("Mary Jane", matches.get(0).getFirstName());
    }

    /**
     * Verifies that every email address belonging to a contact is searched.
     */
    @Test
    void findMatches_searchesAllEmailAddresses() {
        List<Contact> matches =
                ContactSearcher.findMatches(
                        contacts(),
                        "email",
                        "company.com"
                );

        assertEquals(1, matches.size());
        assertEquals("James", matches.get(0).getFirstName());
    }

    /**
     * Verifies normalized phone searching regardless of punctuation.
     */
    @Test
    void findMatches_normalizesPhoneSearch() {
        List<Contact> matches =
                ContactSearcher.findMatches(
                        contacts(),
                        "phone",
                        "2145552222"
                );

        assertEquals(1, matches.size());
        assertEquals("James", matches.get(0).getFirstName());
    }

    /**
     * Verifies that an unsupported search field produces no matches.
     */
    @Test
    void findMatches_unknownFieldReturnsEmptyList() {
        assertTrue(
                ContactSearcher.findMatches(
                        contacts(),
                        "unknown",
                        "James"
                ).isEmpty()
        );
    }

    /**
     * Verifies null-safe search behavior.
     */
    @Test
    void findMatches_handlesNullInputs() {
        assertTrue(
                ContactSearcher.findMatches(
                        null,
                        "first",
                        "James"
                ).isEmpty()
        );

        assertTrue(
                ContactSearcher.findMatches(
                        contacts(),
                        null,
                        "James"
                ).isEmpty()
        );

        assertTrue(
                ContactSearcher.findMatches(
                        contacts(),
                        "first",
                        null
                ).isEmpty()
        );
    }

    /**
     * Verifies automatic selection when exactly one contact matches.
     */
    @Test
    void selectFromList_returnsOnlyMatchWithoutPrompting() {
        Contact contact = contacts().get(0);

        Contact selected =
                ContactSearcher.selectFromList(
                        List.of(contact),
                        new Scanner("")
                );

        assertSame(contact, selected);
    }

    /**
     * Verifies cancellation while choosing among multiple matches.
     */
    @Test
    void selectFromList_allowsCancellation() {
        Contact selected =
                ContactSearcher.selectFromList(
                        contacts(),
                        new Scanner("cancel\n")
                );

        assertNull(selected);
    }
}
