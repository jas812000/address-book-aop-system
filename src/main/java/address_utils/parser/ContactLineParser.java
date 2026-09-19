package address_utils.parser;

import address_book.Address;
import address_book.Contact;
import address_book.EmailAddress;
import address_book.PhoneNumber;
import address_utils.formatter.ContactNormalizer;
import io.LineParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses persisted CSV fields into {@link Contact} objects.
 *
 * <p>The parser supports both the current multi-value contact format and
 * the legacy eight-column format. Legacy records are converted into the
 * richer domain model when loaded and are written in the current format
 * the next time the address book is saved.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public class ContactLineParser implements LineParser<Contact> {

    private static final String ITEM_SEPARATOR = "\\|";
    private static final String VALUE_SEPARATOR = "~";

    /**
     * Parses one tokenized CSV record.
     *
     * @param tokens CSV fields belonging to one contact
     * @return parsed contact, or {@code null} when the record does not
     *         represent a supported format
     */
    @Override
    public Contact parse(String[] tokens) {
        if (tokens == null) {
            return null;
        }

        if (tokens.length == 5) {
            return parseCurrentFormat(tokens);
        }

        if (tokens.length == 8) {
            return parseLegacyFormat(tokens);
        }

        return null;
    }

    /**
     * Parses the current five-column multi-value representation.
     *
     * @param tokens current-format CSV fields
     * @return parsed contact, or {@code null} if encoded data is malformed
     */
    private Contact parseCurrentFormat(String[] tokens) {
        try {
            String firstName = tokens[0].trim();
            String lastName = tokens[1].trim();

            List<Address> addresses = parseAddresses(tokens[2]);
            List<PhoneNumber> phoneNumbers = parsePhoneNumbers(tokens[3]);
            List<EmailAddress> emailAddresses =
                    parseEmailAddresses(tokens[4]);

            return new Contact(
                    firstName,
                    lastName,
                    addresses,
                    phoneNumbers,
                    emailAddresses
            );
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Converts a legacy eight-column record into the current domain model.
     *
     * @param tokens legacy CSV fields
     * @return converted contact
     */
    private Contact parseLegacyFormat(String[] tokens) {
        List<Address> addresses = new ArrayList<>();
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        List<EmailAddress> emailAddresses = new ArrayList<>();

        String street = tokens[2].trim();
        String city = tokens[3].trim();
        String state = tokens[4].trim();
        String zip = tokens[5].trim();
        String phone = tokens[6].trim();
        String email = tokens[7].trim();

        if (!street.isEmpty()
                || !city.isEmpty()
                || !state.isEmpty()
                || !zip.isEmpty()) {

            addresses.add(
                    new Address(
                            "Home",
                            street,
                            city,
                            state,
                            ContactNormalizer.normalizeZip(zip)
                    )
            );
        }

        if (!phone.isEmpty()) {
            phoneNumbers.add(
                    new PhoneNumber(
                            "Primary",
                            ContactNormalizer.normalizePhone(phone)
                    )
            );
        }

        if (!email.isEmpty()) {
            emailAddresses.add(
                    new EmailAddress("Primary", email)
            );
        }

        return new Contact(
                tokens[0].trim(),
                tokens[1].trim(),
                addresses,
                phoneNumbers,
                emailAddresses
        );
    }

    /**
     * Parses the encoded physical-address collection.
     *
     * @param encoded encoded address field
     * @return parsed addresses
     */
    private List<Address> parseAddresses(String encoded) {
        List<Address> addresses = new ArrayList<>();

        if (encoded == null || encoded.isBlank()) {
            return addresses;
        }

        for (String item : encoded.split(ITEM_SEPARATOR, -1)) {
            String[] values = item.split(VALUE_SEPARATOR, -1);

            if (values.length != 5) {
                throw new IllegalArgumentException(
                        "Malformed address data."
                );
            }

            addresses.add(
                    new Address(
                            decodeComponent(values[0]),
                            decodeComponent(values[1]),
                            decodeComponent(values[2]),
                            decodeComponent(values[3]),
                            decodeComponent(values[4])
                    )
            );
        }

        return addresses;
    }

    /**
     * Parses the encoded phone-number collection.
     *
     * @param encoded encoded phone-number field
     * @return parsed phone numbers
     */
    private List<PhoneNumber> parsePhoneNumbers(String encoded) {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();

        if (encoded == null || encoded.isBlank()) {
            return phoneNumbers;
        }

        for (String item : encoded.split(ITEM_SEPARATOR, -1)) {
            String[] values = item.split(VALUE_SEPARATOR, -1);

            if (values.length != 2) {
                throw new IllegalArgumentException(
                        "Malformed phone-number data."
                );
            }

            phoneNumbers.add(
                    new PhoneNumber(
                            decodeComponent(values[0]),
                            decodeComponent(values[1])
                    )
            );
        }

        return phoneNumbers;
    }

    /**
     * Parses the encoded email-address collection.
     *
     * @param encoded encoded email-address field
     * @return parsed email addresses
     */
    private List<EmailAddress> parseEmailAddresses(String encoded) {
        List<EmailAddress> emailAddresses = new ArrayList<>();

        if (encoded == null || encoded.isBlank()) {
            return emailAddresses;
        }

        for (String item : encoded.split(ITEM_SEPARATOR, -1)) {
            String[] values = item.split(VALUE_SEPARATOR, -1);

            if (values.length != 2) {
                throw new IllegalArgumentException(
                        "Malformed email-address data."
                );
            }

            emailAddresses.add(
                    new EmailAddress(
                            decodeComponent(values[0]),
                            decodeComponent(values[1])
                    )
            );
        }

        return emailAddresses;
    }

    /**
     * Decodes characters escaped by {@code ContactCSVFormatter}.
     *
     * @param value encoded component
     * @return decoded component
     */
    private String decodeComponent(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("%7E", "~")
                .replace("%7C", "|")
                .replace("%25", "%");
    }
}
