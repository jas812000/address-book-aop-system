package address_book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

/**
 * Manages the contacts stored in the address book.
 *
 * <p>This class owns the in-memory contact collection and coordinates
 * contact selection for update and delete operations. Contact validation,
 * normalization, logging, and notification are applied separately through
 * AspectJ where appropriate.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public class AddressBook {

    private final List<Contact> contacts = new ArrayList<>();

    /**
     * Returns independent copies of all stored contacts.
     *
     * @return unmodifiable list of contact copies
     */
    public List<Contact> getContacts() {
        return Collections.unmodifiableList(
                contacts.stream()
                        .map(Contact::copy)
                        .toList()
        );
    }

    /**
     * Replaces the current contacts with independent copies of the supplied
     * contacts.
     *
     * @param contacts contacts loaded from persistent storage
     */
    public void setContacts(List<Contact> contacts) {
        this.contacts.clear();

        if (contacts != null) {
            contacts.stream()
                    .filter(Objects::nonNull)
                    .map(Contact::copy)
                    .forEach(this.contacts::add);
        }
    }

    /**
     * Adds an independent copy of a contact to the address book.
     *
     * @param contact contact to add
     */
    public void addContact(Contact contact) {
        contacts.add(
                Objects.requireNonNull(contact, "Contact cannot be null.").copy()
        );
    }

    /**
     * Searches for and deletes a contact selected by the user.
     *
     * @param scanner scanner used for console input
     * @return deleted contact, or {@code null} if the operation is cancelled
     *         or no contact is selected
     */
    public Contact deleteContact(Scanner scanner) {
        String field = getFieldBySelection(scanner);

        if (field == null) {
            return null;
        }

        System.out.print("Enter search value or 'cancel': ");
        String value = scanner.nextLine().trim();

        if (isCancel(value)) {
            System.out.println("Delete operation cancelled.");
            return null;
        }

        List<Contact> matches = ContactSearcher.findMatches(contacts, field, value);
        Contact selected = ContactSearcher.selectFromList(matches, scanner);

        if (selected == null) {
            return null;
        }

        System.out.println();
        System.out.println("---------- Contact Details ----------");
        System.out.println(selected.toString());
        System.out.println("-------------------------------------");

        if (!confirm(
                "Delete " + selected.getFullName() + "? (y/n): ",
                scanner)) {

            System.out.println("Deletion cancelled.");
            return null;
        }

        int index = findIdentityIndex(selected);

        if (index < 0) {
            return null;
        }

        Contact deleted = contacts.remove(index);

        System.out.println(
                "The contact, " + deleted.getFullName() + ", has been deleted."
        );

        return deleted.copy();
    }

    /**
     * Searches for and interactively updates a contact.
     *
     * <p>The returned array contains snapshots of the contact before and
     * after modification so AspectJ logging advice can record the change.</p>
     *
     * @param scanner scanner used for console input
     * @return two-element array containing the original and updated contact,
     *         or {@code null} if no update is completed
     */
    public Contact[] updateContact(Scanner scanner) {
        String field = getFieldBySelection(scanner);

        if (field == null) {
            return null;
        }

        System.out.print("Enter search value or 'cancel': ");
        String value = scanner.nextLine().trim();

        if (isCancel(value)) {
            System.out.println("Update operation cancelled.");
            return null;
        }

        List<Contact> matches = ContactSearcher.findMatches(contacts, field, value);
        Contact selected = ContactSearcher.selectFromList(matches, scanner);

        if (selected == null) {
            return null;
        }

        int index = findIdentityIndex(selected);

        if (index < 0) {
            return null;
        }

        Contact target = contacts.get(index);

        System.out.println();
        System.out.println("---------- Contact Details ----------");
        System.out.println(target.toString());
        System.out.println("-------------------------------------");

        Contact original = target.copy();

        boolean changed = ContactUpdater.updateFields(target, scanner);

        if (!changed) {
            return null;
        }

        return new Contact[] {
                original,
                target.copy()
        };
    }

    /**
     * Returns independent contact copies for display.
     *
     * @return unmodifiable list of contact copies
     */
    public List<Contact> getFormattedContacts() {
        return getContacts();
    }

    /**
     * Displays the supported search fields and returns the selected field.
     *
     * @param scanner scanner used for console input
     * @return search-field identifier, or {@code null} if cancelled
     */
    private String getFieldBySelection(Scanner scanner) {
        while (true) {
            System.out.println("Search by:");
            System.out.println("1. First name");
            System.out.println("2. Last name");
            System.out.println("3. Full name");
            System.out.println("4. Email");
            System.out.println("5. Phone");
            System.out.print("Enter choice (1-5) or 'cancel': ");

            String choice = scanner.nextLine().trim();

            if (isCancel(choice)) {
                return null;
            }

            switch (choice) {
                case "1":
                    return "first";
                case "2":
                    return "last";
                case "3":
                    return "full";
                case "4":
                    return "email";
                case "5":
                    return "phone";
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    /**
     * Finds the internal list position of a selected contact.
     *
     * <p>Search results contain references from the internal collection,
     * allowing identity comparison without introducing mutable equality
     * semantics for contacts.</p>
     *
     * @param selected selected contact
     * @return zero-based list index, or {@code -1} if not found
     */
    private int findIdentityIndex(Contact selected) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i) == selected) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Prompts for a yes-or-no response.
     *
     * @param message prompt message
     * @param scanner scanner used for console input
     * @return {@code true} for yes; {@code false} for no
     */
    private boolean confirm(String message, Scanner scanner) {
        while (true) {
            System.out.print(message);
            String response = scanner.nextLine()
                    .trim()
                    .toLowerCase(Locale.ROOT);

            if (response.equals("y") || response.equals("yes")) {
                return true;
            }

            if (response.equals("n") || response.equals("no")) {
                return false;
            }

            System.out.println("Please enter 'y' or 'n'.");
        }
    }

    /**
     * Determines whether input requests cancellation.
     *
     * @param value user input
     * @return {@code true} when the value is {@code cancel}
     */
    private boolean isCancel(String value) {
        return "cancel".equalsIgnoreCase(value);
    }
}
