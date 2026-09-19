package address_aspects;

import address_book.Address;
import address_book.Contact;
import address_book.EmailAddress;
import address_book.FieldValidator;
import address_book.PhoneNumber;
import validators.ContactIntegrityValidator;

/**
 * Enforces domain validation through AspectJ.
 *
 * <p>The command-line layer performs validation for immediate user feedback,
 * while this aspect independently protects contact construction and domain
 * mutation boundaries. This ensures that valid domain state does not depend
 * on a particular user interface.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public aspect ValidationAspect {

    /**
     * Validates a newly constructed contact after construction completes.
     *
     * @param contact constructed contact
     */
    after(Contact contact) returning:
        execution(address_book.Contact.new(
            String,
            String,
            java.util.List,
            java.util.List,
            java.util.List
        ))
        && this(contact) {

        ContactIntegrityValidator.validateContact(contact);
    }

    /**
     * Validates first-name mutations.
     *
     * @param value proposed first name
     */
    before(String value):
        execution(void address_book.Contact.setFirstName(String))
        && args(value) {

        require(
            FieldValidator.isValidFirstName(value),
            "Invalid first name."
        );
    }

    /**
     * Validates last-name mutations.
     *
     * @param value proposed last name
     */
    before(String value):
        execution(void address_book.Contact.setLastName(String))
        && args(value) {

        require(
            FieldValidator.isValidLastName(value),
            "Invalid last name."
        );
    }

    /**
     * Validates newly constructed physical addresses.
     */
    before(
        String label,
        String street,
        String city,
        String state,
        String zipCode
    ):
        execution(address_book.Address.new(
            String,
            String,
            String,
            String,
            String
        ))
        && args(label, street, city, state, zipCode) {

        validateAddress(
            label,
            street,
            city,
            state,
            zipCode
        );
    }

    /**
     * Validates address labels before mutation.
     */
    before(String value):
        execution(void address_book.Address.setLabel(String))
        && args(value) {

        require(
            FieldValidator.isValidLabel(value),
            "Invalid address label."
        );
    }

    /**
     * Validates street addresses before mutation.
     */
    before(String value):
        execution(void address_book.Address.setStreet(String))
        && args(value) {

        require(
            FieldValidator.isValidStreetAddress(value),
            "Invalid street address."
        );
    }

    /**
     * Validates cities before mutation.
     */
    before(String value):
        execution(void address_book.Address.setCity(String))
        && args(value) {

        require(
            FieldValidator.isValidCity(value),
            "Invalid city."
        );
    }

    /**
     * Validates states before mutation.
     */
    before(String value):
        execution(void address_book.Address.setState(String))
        && args(value) {

        require(
            FieldValidator.isValidState(value),
            "Invalid state."
        );
    }

    /**
     * Validates ZIP codes before mutation.
     */
    before(String value):
        execution(void address_book.Address.setZipCode(String))
        && args(value) {

        require(
            FieldValidator.isValidZipCode(value),
            "Invalid ZIP code."
        );
    }

    /**
     * Validates newly constructed phone numbers.
     */
    before(String label, String number):
        execution(address_book.PhoneNumber.new(String, String))
        && args(label, number) {

        validatePhoneNumber(label, number);
    }

    /**
     * Validates phone labels before mutation.
     */
    before(String value):
        execution(void address_book.PhoneNumber.setLabel(String))
        && args(value) {

        require(
            FieldValidator.isValidLabel(value),
            "Invalid phone label."
        );
    }

    /**
     * Validates phone numbers before mutation.
     */
    before(String value):
        execution(void address_book.PhoneNumber.setNumber(String))
        && args(value) {

        require(
            FieldValidator.isValidPhoneNumberFormatted(value),
            "Invalid phone number."
        );
    }

    /**
     * Validates newly constructed email addresses.
     */
    before(String label, String email):
        execution(address_book.EmailAddress.new(String, String))
        && args(label, email) {

        validateEmailAddress(label, email);
    }

    /**
     * Validates email labels before mutation.
     */
    before(String value):
        execution(void address_book.EmailAddress.setLabel(String))
        && args(value) {

        require(
            FieldValidator.isValidLabel(value),
            "Invalid email label."
        );
    }

    /**
     * Validates email addresses before mutation.
     */
    before(String value):
        execution(void address_book.EmailAddress.setEmail(String))
        && args(value) {

        require(
            FieldValidator.isValidEmail(value),
            "Invalid email address."
        );
    }

    /**
     * Validates contacts supplied directly to the address book.
     *
     * @param contact contact being added
     */
    before(Contact contact):
        execution(void address_book.AddressBook.addContact(Contact))
        && args(contact) {

        ContactIntegrityValidator.validateContact(contact);
    }

    private void validateAddress(
        String label,
        String street,
        String city,
        String state,
        String zipCode
    ) {
        require(
            FieldValidator.isValidLabel(label),
            "Invalid address label."
        );

        require(
            FieldValidator.isValidStreetAddress(street),
            "Invalid street address."
        );

        require(
            FieldValidator.isValidCity(city),
            "Invalid city."
        );

        require(
            FieldValidator.isValidState(state),
            "Invalid state."
        );

        require(
            FieldValidator.isValidZipCode(zipCode),
            "Invalid ZIP code."
        );
    }

    private void validatePhoneNumber(
        String label,
        String number
    ) {
        require(
            FieldValidator.isValidLabel(label),
            "Invalid phone label."
        );

        require(
            FieldValidator.isValidPhoneNumberFormatted(number),
            "Invalid phone number."
        );
    }

    private void validateEmailAddress(
        String label,
        String email
    ) {
        require(
            FieldValidator.isValidLabel(label),
            "Invalid email label."
        );

        require(
            FieldValidator.isValidEmail(email),
            "Invalid email address."
        );
    }

    private void require(
        boolean valid,
        String message
    ) {
        if (!valid) {
            throw new IllegalArgumentException(message);
        }
    }
}
