package address_book;

import address_utils.formatter.ContactNormalizer;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Provides interactive editing operations for existing contacts.
 *
 * <p>Users can change names and independently add, replace, or remove
 * physical addresses, phone numbers, and email addresses. All changes
 * pass through explicit domain mutation methods, providing meaningful
 * join points for AspectJ validation, normalization, and logging.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class ContactUpdater {

    /**
     * Prevents instantiation because this class provides only static
     * contact-update operations.
     */
    private ContactUpdater() {
    }

    /**
     * Interactively edits a contact until the user finishes or cancels.
     *
     * @param contact contact to update
     * @param scanner scanner used for console input
     * @return {@code true} if at least one change was made
     */
    public static boolean updateFields(
            Contact contact,
            Scanner scanner) {

        boolean changed = false;

        while (true) {
            displayUpdateMenu();

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (updateFirstName(contact, scanner)) {
                        changed = true;
                        displayUpdatedContact(contact);
                    }
                    break;

                case "2":
                    if (updateLastName(contact, scanner)) {
                        changed = true;
                        displayUpdatedContact(contact);
                    }
                    break;

                case "3":
                    if (editAddresses(contact, scanner)) {
                        changed = true;
                        displayUpdatedContact(contact);
                    }
                    break;

                case "4":
                    if (editPhoneNumbers(contact, scanner)) {
                        changed = true;
                        displayUpdatedContact(contact);
                    }
                    break;

                case "5":
                    if (editEmailAddresses(contact, scanner)) {
                        changed = true;
                        displayUpdatedContact(contact);
                    }
                    break;

                case "6":
                    return changed;

                case "cancel":
                    System.out.println("Update operation finished.");
                    return changed;

                default:
                    System.out.println(
                            "Invalid selection. Please choose 1-6."
                    );
            }
        }
    }

    /**
     * Displays the contact after a successful update.
     *
     * <p>The explicit {@code toString()} call intentionally provides a
     * call join point for {@code DisplayAspect}, keeping detailed contact
     * presentation under AspectJ control.</p>
     *
     * @param contact updated contact
     */
    private static void displayUpdatedContact(Contact contact) {
        System.out.println();
        System.out.println("---------- Updated Contact ----------");
        System.out.println(contact.toString());
        System.out.println("-------------------------------------");
    }

    /**
     * Displays the contact-update menu.
     */
    private static void displayUpdateMenu() {
        System.out.println();
        System.out.println("--- Update Contact ---");
        System.out.println("1. First name");
        System.out.println("2. Last name");
        System.out.println("3. Addresses");
        System.out.println("4. Phone numbers");
        System.out.println("5. Email addresses");
        System.out.println("6. Finish");
        System.out.print("Enter choice: ");
    }

    /**
     * Updates the contact's first name.
     *
     * @param contact contact being edited
     * @param scanner scanner used for console input
     * @return {@code true} if changed
     */
    private static boolean updateFirstName(
            Contact contact,
            Scanner scanner) {

        String value = promptUntilValid(
                scanner,
                "New first name or 'cancel': ",
                "first name",
                FieldValidator::isValidFirstName
        );

        if (value == null) {
            return false;
        }

        contact.setFirstName(ContactNormalizer.normalizeName(value));
        return true;
    }

    /**
     * Updates the contact's last name.
     *
     * @param contact contact being edited
     * @param scanner scanner used for console input
     * @return {@code true} if changed
     */
    private static boolean updateLastName(
            Contact contact,
            Scanner scanner) {

        String value = promptUntilValid(
                scanner,
                "New last name or 'cancel': ",
                "last name",
                FieldValidator::isValidLastName
        );

        if (value == null) {
            return false;
        }

        contact.setLastName(ContactNormalizer.normalizeName(value));
        return true;
    }

    /**
     * Displays address editing operations.
     *
     * @param contact contact being edited
     * @param scanner scanner used for console input
     * @return {@code true} if an address was changed
     */
    private static boolean editAddresses(
            Contact contact,
            Scanner scanner) {

        while (true) {
            System.out.println();
            displayAddresses(contact.getAddresses());
            System.out.println("1. Add address");
            System.out.println("2. Replace address");
            System.out.println("3. Remove address");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            switch (scanner.nextLine().trim()) {
                case "1":
                    Address added = promptAddress(scanner);

                    if (added != null) {
                        contact.addAddress(added);
                        return true;
                    }
                    break;

                case "2":
                    Integer replaceIndex = selectIndex(
                            scanner,
                            contact.getAddresses().size(),
                            "address"
                    );

                    if (replaceIndex != null) {
                        Address selected =
                                contact.getAddresses().get(replaceIndex);

                        System.out.println();
                        System.out.println("Selected address:");
                        System.out.println(
                                selected.getLabel() + " Address:"
                        );
                        System.out.println("    " + selected.getStreet());
                        System.out.println(
                                "    "
                                        + selected.getCity()
                                        + ", "
                                        + selected.getState()
                                                .toUpperCase(Locale.ROOT)
                                        + " "
                                        + selected.getZipCode()
                        );

                        Address replacement = promptAddress(scanner);

                        if (replacement != null) {
                            contact.replaceAddress(
                                    replaceIndex,
                                    replacement
                            );
                            return true;
                        }
                    }
                    break;

                case "3":
                    Integer removeIndex = selectIndex(
                            scanner,
                            contact.getAddresses().size(),
                            "address"
                    );

                    if (removeIndex != null) {
                        Address selected =
                                contact.getAddresses().get(removeIndex);

                        System.out.println();
                        System.out.println("Selected address:");
                        System.out.println(
                                selected.getLabel() + " Address:"
                        );
                        System.out.println("    " + selected.getStreet());
                        System.out.println(
                                "    "
                                        + selected.getCity()
                                        + ", "
                                        + selected.getState()
                                                .toUpperCase(Locale.ROOT)
                                        + " "
                                        + selected.getZipCode()
                        );

                        if (confirm(
                                scanner,
                                "Remove this address? (y/n): ")) {
                            contact.removeAddress(removeIndex);
                            return true;
                        }
                    }
                    break;

                case "4":
                case "cancel":
                    return false;

                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    /**
     * Displays phone-number editing operations.
     *
     * @param contact contact being edited
     * @param scanner scanner used for console input
     * @return {@code true} if a phone number was changed
     */
    private static boolean editPhoneNumbers(
            Contact contact,
            Scanner scanner) {

        while (true) {
            System.out.println();
            displayPhoneNumbers(contact.getPhoneNumbers());
            System.out.println("1. Add phone number");
            System.out.println("2. Replace phone number");
            System.out.println("3. Remove phone number");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            switch (scanner.nextLine().trim()) {
                case "1":
                    PhoneNumber added = promptPhoneNumber(scanner);

                    if (added != null) {
                        contact.addPhoneNumber(added);
                        return true;
                    }
                    break;

                case "2":
                    Integer replaceIndex = selectIndex(
                            scanner,
                            contact.getPhoneNumbers().size(),
                            "phone number"
                    );

                    if (replaceIndex != null) {
                        PhoneNumber selected =
                                contact.getPhoneNumbers().get(replaceIndex);

                        System.out.println();
                        System.out.println("Selected phone number:");
                        System.out.println(
                                selected.getLabel() + " Phone:"
                        );
                        System.out.println("    " + selected.getNumber());

                        PhoneNumber replacement =
                                promptPhoneNumber(scanner);

                        if (replacement != null) {
                            contact.replacePhoneNumber(
                                    replaceIndex,
                                    replacement
                            );
                            return true;
                        }
                    }
                    break;

                case "3":
                    Integer removeIndex = selectIndex(
                            scanner,
                            contact.getPhoneNumbers().size(),
                            "phone number"
                    );

                    if (removeIndex != null) {
                        PhoneNumber selected =
                                contact.getPhoneNumbers().get(removeIndex);

                        System.out.println();
                        System.out.println("Selected phone number:");
                        System.out.println(
                                selected.getLabel() + " Phone:"
                        );
                        System.out.println("    " + selected.getNumber());

                        if (confirm(
                                scanner,
                                "Remove this phone number? (y/n): ")) {
                            contact.removePhoneNumber(removeIndex);
                            return true;
                        }
                    }
                    break;

                case "4":
                case "cancel":
                    return false;

                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    /**
     * Displays email-address editing operations.
     *
     * @param contact contact being edited
     * @param scanner scanner used for console input
     * @return {@code true} if an email address was changed
     */
    private static boolean editEmailAddresses(
            Contact contact,
            Scanner scanner) {

        while (true) {
            System.out.println();
            displayEmailAddresses(contact.getEmailAddresses());
            System.out.println("1. Add email address");
            System.out.println("2. Replace email address");
            System.out.println("3. Remove email address");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            switch (scanner.nextLine().trim()) {
                case "1":
                    EmailAddress added = promptEmailAddress(scanner);

                    if (added != null) {
                        contact.addEmailAddress(added);
                        return true;
                    }
                    break;

                case "2":
                    Integer replaceIndex = selectIndex(
                            scanner,
                            contact.getEmailAddresses().size(),
                            "email address"
                    );

                    if (replaceIndex != null) {
                        EmailAddress selected =
                                contact.getEmailAddresses().get(replaceIndex);

                        System.out.println();
                        System.out.println("Selected email address:");
                        System.out.println(
                                selected.getLabel() + " Email:"
                        );
                        System.out.println("    " + selected.getEmail());

                        EmailAddress replacement =
                                promptEmailAddress(scanner);

                        if (replacement != null) {
                            contact.replaceEmailAddress(
                                    replaceIndex,
                                    replacement
                            );
                            return true;
                        }
                    }
                    break;

                case "3":
                    Integer removeIndex = selectIndex(
                            scanner,
                            contact.getEmailAddresses().size(),
                            "email address"
                    );

                    if (removeIndex != null) {
                        EmailAddress selected =
                                contact.getEmailAddresses().get(removeIndex);

                        System.out.println();
                        System.out.println("Selected email address:");
                        System.out.println(
                                selected.getLabel() + " Email:"
                        );
                        System.out.println("    " + selected.getEmail());

                        if (confirm(
                                scanner,
                                "Remove this email address? (y/n): ")) {
                            contact.removeEmailAddress(removeIndex);
                            return true;
                        }
                    }
                    break;

                case "4":
                case "cancel":
                    return false;

                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    /**
     * Collects one physical address.
     *
     * @param scanner scanner used for console input
     * @return address, or {@code null} if cancelled
     */
    private static Address promptAddress(Scanner scanner) {
        String label = promptLabel(
                scanner,
                "Address",
                List.of("Home", "Work")
        );

        if (label == null) {
            return null;
        }

        String street = promptUntilValid(
                scanner,
                "Street address or 'cancel': ",
                "street address",
                FieldValidator::isValidStreetAddress
        );

        if (street == null) {
            return null;
        }

        String city = promptUntilValid(
                scanner,
                "City or 'cancel': ",
                "city",
                FieldValidator::isValidCity
        );

        if (city == null) {
            return null;
        }

        String state = promptUntilValid(
                scanner,
                "State or 'cancel': ",
                "state",
                FieldValidator::isValidState
        );

        if (state == null) {
            return null;
        }

        String zip = promptUntilValid(
                scanner,
                "ZIP code or 'cancel': ",
                "ZIP code",
                FieldValidator::isValidZipCode
        );

        if (zip == null) {
            return null;
        }

        return new Address(
                label,
                ContactNormalizer.normalizeStreet(street),
                ContactNormalizer.normalizeCity(city),
                ContactNormalizer.normalizeState(state),
                ContactNormalizer.normalizeZip(zip)
        );
    }

    /**
     * Collects one phone number.
     *
     * @param scanner scanner used for console input
     * @return phone number, or {@code null} if cancelled
     */
    private static PhoneNumber promptPhoneNumber(Scanner scanner) {
        String label = promptLabel(
                scanner,
                "Phone",
                List.of("Mobile", "Home", "Work")
        );

        if (label == null) {
            return null;
        }

        String number = promptUntilValid(
                scanner,
                "Phone number or 'cancel': ",
                "phone number",
                FieldValidator::isValidPhoneNumberFormatted
        );

        if (number == null) {
            return null;
        }

        return new PhoneNumber(
                label,
                ContactNormalizer.normalizePhone(number)
        );
    }

    /**
     * Collects one email address.
     *
     * @param scanner scanner used for console input
     * @return email address, or {@code null} if cancelled
     */
    private static EmailAddress promptEmailAddress(Scanner scanner) {
        String label = promptLabel(
                scanner,
                "Email",
                List.of("Personal", "Work")
        );

        if (label == null) {
            return null;
        }

        String email = promptUntilValid(
                scanner,
                "Email address or 'cancel': ",
                "email address",
                FieldValidator::isValidEmail
        );

        if (email == null) {
            return null;
        }

        return new EmailAddress(
                label,
                ContactNormalizer.normalizeEmail(email)
        );
    }

    /**
     * Prompts for a standard label by number or allows a validated custom label.
     *
     * @param scanner scanner used for console input
     * @param itemName item type displayed in the menu
     * @param standardLabels predefined labels
     * @return selected label, or {@code null} if the operation is cancelled
     */
    private static String promptLabel(
            Scanner scanner,
            String itemName,
            List<String> standardLabels) {

        while (true) {
            System.out.println();
            System.out.println("--- " + itemName + " Label ---");

            for (int i = 0; i < standardLabels.size(); i++) {
                System.out.println((i + 1) + ". " + standardLabels.get(i));
            }

            int customChoice = standardLabels.size() + 1;
            System.out.println(customChoice + ". Custom");
            System.out.print("Enter choice or 'cancel': ");

            String input = scanner.nextLine().trim();

            if (isCancel(input)) {
                return null;
            }

            try {
                int selection = Integer.parseInt(input);

                if (selection >= 1 && selection <= standardLabels.size()) {
                    return standardLabels.get(selection - 1);
                }

                if (selection == customChoice) {
                    return promptUntilValid(
                            scanner,
                            "Custom label or 'cancel': ",
                            itemName.toLowerCase(Locale.ROOT) + " label",
                            FieldValidator::isValidLabel
                    );
                }
            } catch (NumberFormatException ignored) {
                // Common validation feedback is printed below.
            }

            System.out.println("Invalid selection. Please try again.");
        }
    }

    /**
     * Displays physical addresses with selection numbers.
     *
     * @param addresses addresses to display
     */
    private static void displayAddresses(List<Address> addresses) {
        if (addresses.isEmpty()) {
            System.out.println("No addresses stored.");
            return;
        }

        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);

            System.out.println(
                    (i + 1)
                            + ". ["
                            + address.getLabel()
                            + "] "
                            + address.getStreet()
                            + ", "
                            + address.getCity()
                            + ", "
                            + address.getState()
                            + " "
                            + address.getZipCode()
            );
        }
    }

    /**
     * Displays phone numbers with selection numbers.
     *
     * @param phoneNumbers phone numbers to display
     */
    private static void displayPhoneNumbers(
            List<PhoneNumber> phoneNumbers) {

        if (phoneNumbers.isEmpty()) {
            System.out.println("No phone numbers stored.");
            return;
        }

        for (int i = 0; i < phoneNumbers.size(); i++) {
            PhoneNumber phone = phoneNumbers.get(i);

            System.out.println(
                    (i + 1)
                            + ". ["
                            + phone.getLabel()
                            + "] "
                            + phone.getNumber()
            );
        }
    }

    /**
     * Displays email addresses with selection numbers.
     *
     * @param emailAddresses email addresses to display
     */
    private static void displayEmailAddresses(
            List<EmailAddress> emailAddresses) {

        if (emailAddresses.isEmpty()) {
            System.out.println("No email addresses stored.");
            return;
        }

        for (int i = 0; i < emailAddresses.size(); i++) {
            EmailAddress email = emailAddresses.get(i);

            System.out.println(
                    (i + 1)
                            + ". ["
                            + email.getLabel()
                            + "] "
                            + email.getEmail()
            );
        }
    }

    /**
     * Prompts the user to select an existing collection item.
     *
     * @param scanner scanner used for console input
     * @param size number of available items
     * @param itemName item description used in prompts
     * @return zero-based index, or {@code null} if cancelled or empty
     */
    private static Integer selectIndex(
            Scanner scanner,
            int size,
            String itemName) {

        if (size == 0) {
            System.out.println("No " + itemName + " entries available.");
            return null;
        }

        while (true) {
            System.out.print(
                    "Select " + itemName
                            + " by number or enter 'cancel': "
            );

            String input = scanner.nextLine().trim();

            if (isCancel(input)) {
                return null;
            }

            try {
                int selection = Integer.parseInt(input);

                if (selection >= 1 && selection <= size) {
                    return selection - 1;
                }
            } catch (NumberFormatException ignored) {
                // Common validation feedback is printed below.
            }

            System.out.println("Invalid selection. Please try again.");
        }
    }

    /**
     * Prompts until valid input is supplied or the operation is cancelled.
     *
     * @param scanner scanner used for console input
     * @param prompt prompt displayed to the user
     * @param fieldName field name used in validation feedback
     * @param validator validation operation
     * @return validated input, or {@code null} if cancelled
     */
    private static String promptUntilValid(
            Scanner scanner,
            String prompt,
            String fieldName,
            StringValidator validator) {

        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (isCancel(value)) {
                return null;
            }

            if (validator.isValid(value)) {
                return value;
            }

            System.out.println(
                    "Invalid " + fieldName + ". Please try again."
            );
        }
    }

    /**
     * Prompts for confirmation.
     *
     * @param scanner scanner used for console input
     * @param prompt confirmation prompt
     * @return {@code true} for yes; {@code false} for no
     */
    private static boolean confirm(
            Scanner scanner,
            String prompt) {

        while (true) {
            System.out.print(prompt);

            String value = scanner.nextLine()
                    .trim()
                    .toLowerCase(Locale.ROOT);

            if (value.equals("y") || value.equals("yes")) {
                return true;
            }

            if (value.equals("n") || value.equals("no")) {
                return false;
            }

            System.out.println("Please enter 'y' or 'n'.");
        }
    }

    /**
     * Determines whether input requests cancellation.
     *
     * @param value user input
     * @return {@code true} when cancellation was requested
     */
    private static boolean isCancel(String value) {
        return "cancel".equalsIgnoreCase(value);
    }
}
