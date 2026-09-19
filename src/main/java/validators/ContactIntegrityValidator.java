package validators;

import address_book.Address;
import address_book.Contact;
import address_book.EmailAddress;
import address_book.FieldValidator;
import address_book.PhoneNumber;

import java.util.Objects;

/**
 * Validates the complete integrity of a {@link Contact}.
 *
 * <p>This validator delegates individual field validation to
 * {@link FieldValidator}, ensuring that interactive validation and
 * AspectJ-enforced domain validation use the same rules. It validates
 * names together with every labeled address, phone number, and email
 * address associated with the contact.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class ContactIntegrityValidator {

    /**
     * Prevents instantiation because this class provides only static
     * contact-validation operations.
     */
    private ContactIntegrityValidator() {
    }

    /**
     * Validates all information associated with a contact.
     *
     * @param contact contact to validate
     * @throws IllegalArgumentException if any contact data is invalid
     */
    public static void validateContact(Contact contact) {
        Objects.requireNonNull(contact, "Contact cannot be null.");

        validateName(contact);
        validateAddresses(contact);
        validatePhoneNumbers(contact);
        validateEmailAddresses(contact);
    }

    /**
     * Validates the contact's first and last names.
     *
     * @param contact contact being validated
     */
    private static void validateName(Contact contact) {
        if (!FieldValidator.isValidFirstName(contact.getFirstName())) {
            throw new IllegalArgumentException("Invalid first name.");
        }

        if (!FieldValidator.isValidLastName(contact.getLastName())) {
            throw new IllegalArgumentException("Invalid last name.");
        }
    }

    /**
     * Validates every physical address associated with a contact.
     *
     * @param contact contact being validated
     */
    private static void validateAddresses(Contact contact) {
        for (Address address : contact.getAddresses()) {
            if (!FieldValidator.isValidLabel(address.getLabel())) {
                throw new IllegalArgumentException("Invalid address label.");
            }

            if (!FieldValidator.isValidStreetAddress(address.getStreet())) {
                throw new IllegalArgumentException("Invalid street address.");
            }

            if (!FieldValidator.isValidCity(address.getCity())) {
                throw new IllegalArgumentException("Invalid city.");
            }

            if (!FieldValidator.isValidState(address.getState())) {
                throw new IllegalArgumentException("Invalid state.");
            }

            if (!FieldValidator.isValidZipCode(address.getZipCode())) {
                throw new IllegalArgumentException("Invalid ZIP code.");
            }
        }
    }

    /**
     * Validates every phone number associated with a contact.
     *
     * @param contact contact being validated
     */
    private static void validatePhoneNumbers(Contact contact) {
        for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
            if (!FieldValidator.isValidLabel(phoneNumber.getLabel())) {
                throw new IllegalArgumentException("Invalid phone label.");
            }

            if (!FieldValidator.isValidPhoneNumberFormatted(
                    phoneNumber.getNumber())) {

                throw new IllegalArgumentException(
                        "Invalid phone number."
                );
            }
        }
    }

    /**
     * Validates every email address associated with a contact.
     *
     * @param contact contact being validated
     */
    private static void validateEmailAddresses(Contact contact) {
        for (EmailAddress emailAddress : contact.getEmailAddresses()) {
            if (!FieldValidator.isValidLabel(emailAddress.getLabel())) {
                throw new IllegalArgumentException("Invalid email label.");
            }

            if (!FieldValidator.isValidEmail(emailAddress.getEmail())) {
                throw new IllegalArgumentException(
                        "Invalid email address."
                );
            }
        }
    }
}
