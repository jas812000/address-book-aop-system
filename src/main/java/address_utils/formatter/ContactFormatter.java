package address_utils.formatter;

import address_book.Address;
import address_book.Contact;
import address_book.EmailAddress;
import address_book.PhoneNumber;

/**
 * Provides user-facing formatting operations for contacts.
 *
 * <p>The formatter supports detailed multi-line contact displays and
 * compact representations used when identifying contacts in search and
 * selection results. The application's display aspect can apply this
 * formatting without placing presentation responsibilities in the
 * contact domain model.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class ContactFormatter {

    private static final String INDENT = "    ";

    /**
     * Prevents instantiation because this class provides only static
     * formatting operations.
     */
    private ContactFormatter() {
    }

    /**
     * Produces a detailed multi-line representation of a contact.
     *
     * <p>Each labeled contact item is displayed as its own section so the
     * information remains easy to scan when a contact contains multiple
     * addresses, phone numbers, or email addresses.</p>
     *
     * @param contact contact to format
     * @return formatted contact, or an invalid-contact message if null
     */
    public static String format(Contact contact) {
        if (contact == null) {
            return "[Invalid Contact]";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(contact.getFullName());

        appendAddresses(builder, contact);
        appendPhoneNumbers(builder, contact);
        appendEmailAddresses(builder, contact);

        return builder.toString();
    }

    /**
     * Produces a compact representation suitable for search and selection
     * results.
     *
     * @param contact contact to format
     * @return contact's full name, or an invalid-contact message if null
     */
    public static String formatCompact(Contact contact) {
        return contact == null
                ? "[Invalid Contact]"
                : contact.getFullName();
    }

    /**
     * Appends all physical addresses associated with a contact.
     *
     * @param builder destination for formatted output
     * @param contact contact containing the addresses
     */
    private static void appendAddresses(
            StringBuilder builder,
            Contact contact) {

        for (Address address : contact.getAddresses()) {
            appendSectionHeading(
                    builder,
                    address.getLabel() + " Address:"
            );

            builder.append(INDENT)
                    .append(address.getStreet())
                    .append(System.lineSeparator())
                    .append(INDENT)
                    .append(address.getCity())
                    .append(", ")
                    .append(address.getState().toUpperCase())
                    .append(" ")
                    .append(address.getZipCode());
        }
    }

    /**
     * Appends all phone numbers associated with a contact.
     *
     * @param builder destination for formatted output
     * @param contact contact containing the phone numbers
     */
    private static void appendPhoneNumbers(
            StringBuilder builder,
            Contact contact) {

        for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
            appendSectionHeading(
                    builder,
                    phoneNumber.getLabel() + " Phone:"
            );

            builder.append(INDENT)
                    .append(phoneNumber.getNumber());
        }
    }

    /**
     * Appends all email addresses associated with a contact.
     *
     * @param builder destination for formatted output
     * @param contact contact containing the email addresses
     */
    private static void appendEmailAddresses(
            StringBuilder builder,
            Contact contact) {

        for (EmailAddress emailAddress : contact.getEmailAddresses()) {
            appendSectionHeading(
                    builder,
                    emailAddress.getLabel() + " Email:"
            );

            builder.append(INDENT)
                    .append(emailAddress.getEmail());
        }
    }

    /**
     * Starts a new labeled section separated from the previous contact
     * information by a blank line.
     *
     * @param builder destination for formatted output
     * @param heading section heading
     */
    private static void appendSectionHeading(
            StringBuilder builder,
            String heading) {

        builder.append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(heading)
                .append(System.lineSeparator());
    }
}
