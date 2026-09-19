package address_book;

import address_utils.formatter.ContactNormalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Collects contact information from the command line.
 *
 * <p>The handler provides immediate validation feedback while users enter
 * data. Domain-level validation is also enforced independently through
 * AspectJ so correctness does not depend solely on the command-line UI.</p>
 *
 * <p>Users may enter {@code cancel} at a value prompt to abandon contact
 * creation.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public class ContactInputHandler {

    /**
     * Collects the information required to create a contact.
     *
     * <p>Names are required. Addresses, phone numbers, and email addresses
     * are optional and may contain multiple labeled values.</p>
     *
     * @param scanner scanner used for console input
     * @return new contact, or {@code null} if creation is cancelled
     */
    public Contact promptContactDetails(Scanner scanner) {
        String firstName = promptUntilValid(
                scanner,
                "Enter the first name or 'cancel': ",
                "first name",
                FieldValidator::isValidFirstName
        );

        if (firstName == null) {
            return null;
        }

        String lastName = promptUntilValid(
                scanner,
                "Enter the last name or 'cancel': ",
                "last name",
                FieldValidator::isValidLastName
        );

        if (lastName == null) {
            return null;
        }

        List<Address> addresses = promptAddresses(scanner);

        if (addresses == null) {
            return null;
        }

        List<PhoneNumber> phoneNumbers = promptPhoneNumbers(scanner);

        if (phoneNumbers == null) {
            return null;
        }

        List<EmailAddress> emailAddresses = promptEmailAddresses(scanner);

        if (emailAddresses == null) {
            return null;
        }

        return new Contact(
                ContactNormalizer.normalizeName(firstName),
                ContactNormalizer.normalizeName(lastName),
                addresses,
                phoneNumbers,
                emailAddresses
        );
    }

    /**
     * Collects zero or more labeled physical addresses.
     *
     * @param scanner scanner used for console input
     * @return collected addresses, or {@code null} if creation is cancelled
     */
    private List<Address> promptAddresses(Scanner scanner) {
        List<Address> addresses = new ArrayList<>();

        while (confirmAdd(scanner, "Add an address? (y/n): ")) {
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

            String zipCode = promptUntilValid(
                    scanner,
                    "ZIP code or 'cancel': ",
                    "ZIP code",
                    FieldValidator::isValidZipCode
            );

            if (zipCode == null) {
                return null;
            }

            addresses.add(
                    new Address(
                            label,
                            ContactNormalizer.normalizeStreet(street),
                            ContactNormalizer.normalizeCity(city),
                            ContactNormalizer.normalizeState(state),
                            ContactNormalizer.normalizeZip(zipCode)
                    )
            );
        }

        return addresses;
    }

    /**
     * Collects zero or more labeled phone numbers.
     *
     * @param scanner scanner used for console input
     * @return collected phone numbers, or {@code null} if creation is cancelled
     */
    private List<PhoneNumber> promptPhoneNumbers(Scanner scanner) {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();

        while (confirmAdd(scanner, "Add a phone number? (y/n): ")) {
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

            phoneNumbers.add(
                    new PhoneNumber(
                            label,
                            ContactNormalizer.normalizePhone(number)
                    )
            );
        }

        return phoneNumbers;
    }

    /**
     * Collects zero or more labeled email addresses.
     *
     * @param scanner scanner used for console input
     * @return collected email addresses, or {@code null} if creation is cancelled
     */
    private List<EmailAddress> promptEmailAddresses(Scanner scanner) {
        List<EmailAddress> emailAddresses = new ArrayList<>();

        while (confirmAdd(scanner, "Add an email address? (y/n): ")) {
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

            emailAddresses.add(
                    new EmailAddress(
                            label,
                            ContactNormalizer.normalizeEmail(email)
                    )
            );
        }

        return emailAddresses;
    }

    /**
     * Prompts for a standard label by number or allows a validated custom label.
     *
     * @param scanner scanner used for console input
     * @param itemName item type displayed in the menu
     * @param standardLabels predefined labels
     * @return selected label, or {@code null} if creation is cancelled
     */
    private String promptLabel(
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
     * Prompts until valid input is supplied or contact creation is cancelled.
     *
     * @param scanner scanner used for console input
     * @param prompt prompt displayed to the user
     * @param fieldName field name used in validation feedback
     * @param validator validation operation
     * @return validated trimmed input, or {@code null} if cancelled
     */
    private String promptUntilValid(
            Scanner scanner,
            String prompt,
            String fieldName,
            StringValidator validator) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (isCancel(input)) {
                return null;
            }

            if (validator.isValid(input)) {
                return input;
            }

            System.out.println(
                    "Invalid " + fieldName + ". Please try again."
            );
        }
    }

    /**
     * Prompts whether another multi-value item should be entered.
     *
     * @param scanner scanner used for console input
     * @param prompt prompt displayed to the user
     * @return {@code true} when another item should be added
     */
    private boolean confirmAdd(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);

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
     * @param input user input
     * @return {@code true} if cancellation was requested
     */
    private boolean isCancel(String input) {
        return "cancel".equalsIgnoreCase(input);
    }
}
