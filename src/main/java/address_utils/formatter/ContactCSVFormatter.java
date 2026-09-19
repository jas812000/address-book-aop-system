package address_utils.formatter;

import address_book.Address;
import address_book.Contact;
import address_book.EmailAddress;
import address_book.PhoneNumber;

import java.util.stream.Collectors;

/**
 * Converts contacts into the CSV representation used by address-book
 * persistence.
 *
 * <p>Each contact occupies one CSV record. Multi-value contact information
 * is encoded within dedicated address, phone, and email columns while
 * preserving the label associated with each value.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class ContactCSVFormatter {

    private static final String ITEM_SEPARATOR = "|";
    private static final String VALUE_SEPARATOR = "~";

    /**
     * Prevents instantiation because this class provides only static
     * CSV-formatting operations.
     */
    private ContactCSVFormatter() {
    }

    /**
     * Returns the header used by the current address-book CSV format.
     *
     * @return CSV header
     */
    public static String header() {
        return "First Name,Last Name,Addresses,Phone Numbers,Email Addresses";
    }

    /**
     * Converts a contact into one escaped CSV record.
     *
     * @param contact contact to format
     * @return CSV record, or an empty string if the contact is null
     */
    public static String toCSV(Contact contact) {
        if (contact == null) {
            return "";
        }

        return String.join(",",
                escape(contact.getFirstName()),
                escape(contact.getLastName()),
                escape(formatAddresses(contact)),
                escape(formatPhoneNumbers(contact)),
                escape(formatEmailAddresses(contact))
        );
    }

    /**
     * Encodes all physical addresses while preserving their labels.
     *
     * @param contact contact containing the addresses
     * @return encoded address collection
     */
    private static String formatAddresses(Contact contact) {
        return contact.getAddresses()
                .stream()
                .map(ContactCSVFormatter::formatAddress)
                .collect(Collectors.joining(ITEM_SEPARATOR));
    }

    /**
     * Encodes one physical address.
     *
     * @param address address to encode
     * @return encoded address
     */
    private static String formatAddress(Address address) {
        return String.join(VALUE_SEPARATOR,
                encodeComponent(address.getLabel()),
                encodeComponent(address.getStreet()),
                encodeComponent(address.getCity()),
                encodeComponent(address.getState()),
                encodeComponent(address.getZipCode())
        );
    }

    /**
     * Encodes all phone numbers while preserving their labels.
     *
     * @param contact contact containing the phone numbers
     * @return encoded phone-number collection
     */
    private static String formatPhoneNumbers(Contact contact) {
        return contact.getPhoneNumbers()
                .stream()
                .map(phone -> String.join(VALUE_SEPARATOR,
                        encodeComponent(phone.getLabel()),
                        encodeComponent(phone.getNumber())))
                .collect(Collectors.joining(ITEM_SEPARATOR));
    }

    /**
     * Encodes all email addresses while preserving their labels.
     *
     * @param contact contact containing the email addresses
     * @return encoded email-address collection
     */
    private static String formatEmailAddresses(Contact contact) {
        return contact.getEmailAddresses()
                .stream()
                .map(email -> String.join(VALUE_SEPARATOR,
                        encodeComponent(email.getLabel()),
                        encodeComponent(email.getEmail())))
                .collect(Collectors.joining(ITEM_SEPARATOR));
    }

    /**
     * Escapes characters reserved by the internal multi-value encoding.
     *
     * @param value component value
     * @return encoded component
     */
    private static String encodeComponent(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("%", "%25")
                .replace("|", "%7C")
                .replace("~", "%7E");
    }

    /**
     * Escapes a value according to CSV quoting rules.
     *
     * <p>Values containing commas, quotes, carriage returns, or line feeds
     * are enclosed in double quotes, with embedded quotes doubled.</p>
     *
     * @param value raw CSV field
     * @return escaped CSV field
     */
    private static String escape(String value) {
        if (value == null) {
            return "";
        }

        if (value.contains(",")
                || value.contains("\"")
                || value.contains("\r")
                || value.contains("\n")) {

            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }
}
