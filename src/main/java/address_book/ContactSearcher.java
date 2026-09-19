package address_book;

import address_utils.formatter.ContactFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Provides contact searching and interactive result selection.
 *
 * <p>Searches are case-insensitive and support partial matching. Phone
 * searches compare digits rather than presentation formatting so common
 * phone-number representations produce equivalent results.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class ContactSearcher {

    /**
     * Prevents instantiation because this class provides only static
     * search operations.
     */
    private ContactSearcher() {
    }

    /**
     * Finds contacts matching a search value in the requested field.
     *
     * @param contacts contacts to search
     * @param field field identifier
     * @param value search value
     * @return matching contacts
     */
    public static List<Contact> findMatches(
            List<Contact> contacts,
            String field,
            String value) {

        if (contacts == null || field == null || value == null) {
            return Collections.emptyList();
        }

        String normalizedField = field.trim().toLowerCase(Locale.ROOT);
        String searchTerm = value.trim().toLowerCase(Locale.ROOT);

        if (searchTerm.isEmpty()) {
            return Collections.emptyList();
        }

        return contacts.stream()
                .filter(contact -> matches(
                        contact,
                        normalizedField,
                        searchTerm
                ))
                .toList();
    }

    /**
     * Determines whether a contact matches a search request.
     *
     * @param contact contact being evaluated
     * @param field normalized field identifier
     * @param searchTerm normalized search term
     * @return {@code true} when the contact matches
     */
    private static boolean matches(
            Contact contact,
            String field,
            String searchTerm) {

        if (contact == null) {
            return false;
        }

        switch (field) {
            case "first":
                return containsIgnoreCase(
                        contact.getFirstName(),
                        searchTerm
                );

            case "last":
                return containsIgnoreCase(
                        contact.getLastName(),
                        searchTerm
                );

            case "full":
            case "name":
                return containsIgnoreCase(
                        contact.getFullName(),
                        searchTerm
                );

            case "email":
                return contact.getEmailAddresses()
                        .stream()
                        .anyMatch(email ->
                                containsIgnoreCase(
                                        email.getEmail(),
                                        searchTerm
                                )
                        );

            case "phone":
                return matchesPhone(contact, searchTerm);

            default:
                return false;
        }
    }

    /**
     * Determines whether any phone number contains the supplied search
     * digits.
     *
     * @param contact contact being searched
     * @param searchTerm raw search term
     * @return {@code true} when a phone number matches
     */
    private static boolean matchesPhone(
            Contact contact,
            String searchTerm) {

        String searchDigits = digitsOnly(searchTerm);

        if (searchDigits.isEmpty()) {
            return false;
        }

        return contact.getPhoneNumbers()
                .stream()
                .map(PhoneNumber::getNumber)
                .map(ContactSearcher::digitsOnly)
                .anyMatch(number -> number.contains(searchDigits));
    }

    /**
     * Performs null-safe, case-insensitive partial matching.
     *
     * @param value stored value
     * @param searchTerm normalized search term
     * @return {@code true} when the stored value contains the search term
     */
    private static boolean containsIgnoreCase(
            String value,
            String searchTerm) {

        return value != null
                && value.toLowerCase(Locale.ROOT).contains(searchTerm);
    }

    /**
     * Removes phone-number presentation characters for searching.
     *
     * @param value phone search value
     * @return numeric characters only
     */
    private static String digitsOnly(String value) {
        return value == null
                ? ""
                : value.replaceAll("\\D", "");
    }

    /**
     * Selects one contact from search results.
     *
     * <p>A single result is returned immediately. Multiple results are
     * displayed for explicit selection. Entering {@code cancel} abandons
     * the operation.</p>
     *
     * @param matches matching contacts
     * @param scanner scanner used for console input
     * @return selected contact, or {@code null} if none is selected
     */
    public static Contact selectFromList(
            List<Contact> matches,
            Scanner scanner) {

        if (matches == null || matches.isEmpty()) {
            System.out.println("No matching contacts found.");
            return null;
        }

        if (matches.size() == 1) {
            return matches.get(0);
        }

        System.out.println("Multiple matches found:");

        for (int i = 0; i < matches.size(); i++) {
            System.out.println(
                    (i + 1)
                            + ". "
                            + ContactFormatter.formatCompact(matches.get(i))
            );
        }

        while (true) {
            System.out.print(
                    "Select contact by number or enter 'cancel': "
            );

            String input = scanner.nextLine().trim();

            if ("cancel".equalsIgnoreCase(input)) {
                System.out.println("Selection cancelled.");
                return null;
            }

            try {
                int choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= matches.size()) {
                    return matches.get(choice - 1);
                }
            } catch (NumberFormatException ignored) {
                // The common validation message below handles bad input.
            }

            System.out.println("Invalid selection. Please try again.");
        }
    }
}
