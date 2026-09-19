package address_book;

import address_utils.storage.AddressBookStorage;

import java.util.List;
import java.util.Scanner;

/**
 * Coordinates command-line operations between the application, domain model,
 * input handling, and persistent storage.
 *
 * <p>The controller deliberately remains focused on application workflow.
 * Cross-cutting concerns such as validation enforcement, logging,
 * notification, and exception reporting are handled through AspectJ
 * where appropriate.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public class AddressBookController {

    private final Scanner scanner;
    private final AddressBook addressBook;
    private final AddressBookStorage storage;
    private final ContactInputHandler inputHandler;

    /**
     * Creates a controller using the application's standard console,
     * address book, storage, and input components.
     */
    public AddressBookController() {
        this(
                new Scanner(System.in),
                new AddressBook(),
                new AddressBookStorage(),
                new ContactInputHandler()
        );
    }

    /**
     * Creates a controller with explicitly supplied collaborators.
     *
     * <p>This constructor supports deterministic testing of command-line
     * workflows without replacing global console input or weakening the
     * application's AspectJ design.</p>
     *
     * @param scanner scanner used for console input
     * @param addressBook address book managed by the controller
     * @param storage persistent storage implementation
     * @param inputHandler contact-input handler
     */
    AddressBookController(
            Scanner scanner,
            AddressBook addressBook,
            AddressBookStorage storage,
            ContactInputHandler inputHandler) {
        this.scanner = scanner;
        this.addressBook = addressBook;
        this.storage = storage;
        this.inputHandler = inputHandler;
    }

    /**
     * Returns the application's shared console scanner.
     *
     * @return shared scanner
     */
    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Loads persisted contacts into the address book.
     */
    public void load() {
        addressBook.setContacts(storage.load());
    }

    /**
     * Saves the current address book to persistent storage.
     */
    public void save() {
        storage.save(addressBook.getContacts());
    }

    /**
     * Collects information for a new contact, saves it when creation
     * completes successfully, and displays the completed contact.
     *
     * <p>The explicit {@code toString()} call is intentional because it
     * provides the call join point used by {@code DisplayAspect} to weave
     * the formatted contact presentation.</p>
     */
    public void add() {
        Contact contact = inputHandler.promptContactDetails(scanner);
        if (contact == null) {
            System.out.println("Add operation cancelled.");
            return;
        }

        addressBook.addContact(contact);
        save();

        System.out.println();
        System.out.println("---------- Contact Added ----------");
        System.out.println(contact.toString());
        System.out.println("-----------------------------------");
    }

    /**
     * Deletes a selected contact and saves the address book when deletion
     * completes successfully.
     */
    public void delete() {
        Contact deleted = addressBook.deleteContact(scanner);

        if (deleted != null) {
            save();
        }
    }

    /**
     * Updates a selected contact and saves the address book when at least
     * one change is completed.
     */
    public void update() {
        Contact[] result = addressBook.updateContact(scanner);

        if (result != null) {
            save();
        }
    }

    /**
     * Displays the contact-view menu and allows the user to view either one
     * selected contact or every contact in the address book.
     */
    public void display() {
        List<Contact> contacts = addressBook.getFormattedContacts();

        if (contacts.isEmpty()) {
            System.out.println("Address book is empty.");
            return;
        }

        while (true) {
            System.out.println();
            System.out.println("--- Display Contacts ---");
            System.out.println("1. View One Contact");
            System.out.println("2. View All Contacts");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayOne(contacts);
                    return;
                case "2":
                    displayAll(contacts);
                    return;
                case "3":
                case "cancel":
                    return;
                default:
                    System.out.println(
                            "Invalid selection. Please choose 1-3."
                    );
            }
        }
    }

    /**
     * Displays a numbered contact list and then the complete representation
     * of the selected contact.
     *
     * <p>The explicit {@code toString()} call is intentional. It provides the
     * call join point used by {@code DisplayAspect}, allowing detailed
     * presentation to remain an AspectJ cross-cutting concern.</p>
     *
     * @param contacts contacts available for selection
     */
    private void displayOne(List<Contact> contacts) {
        System.out.println();
        System.out.println("---------- Select Contact ----------");

        for (int i = 0; i < contacts.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + contacts.get(i).getFullName()
            );
        }

        while (true) {
            System.out.print(
                    "Select contact by number or enter 'cancel': "
            );

            String input = scanner.nextLine().trim();

            if ("cancel".equalsIgnoreCase(input)) {
                return;
            }

            try {
                int selection = Integer.parseInt(input);

                if (selection >= 1 && selection <= contacts.size()) {
                    Contact contact = contacts.get(selection - 1);

                    System.out.println();
                    System.out.println(
                            "---------- Contact Details ----------"
                    );
                    System.out.println(contact.toString());
                    System.out.println(
                            "-------------------------------------"
                    );
                    return;
                }
            } catch (NumberFormatException ignored) {
                // Common validation feedback is printed below.
            }

            System.out.println("Invalid selection. Please try again.");
        }
    }

    /**
     * Displays the complete representation of every stored contact.
     *
     * <p>The explicit {@code toString()} calls provide the join points used by
     * {@code DisplayAspect} for detailed presentation.</p>
     *
     * @param contacts contacts to display
     */
    private void displayAll(List<Contact> contacts) {
        System.out.println();
        System.out.println(
                "---------- Address Book Contacts ----------"
        );

        for (Contact contact : contacts) {
            System.out.println(
                    "-------------------------------------------"
            );
            System.out.println(contact.toString());
        }

        System.out.println(
                "-------------------------------------------"
        );
    }

}