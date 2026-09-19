package address_aspects;

import address_book.Contact;
import utilities.LogUtil;

/**
 * Logs the before-and-after state of successful contact updates.
 *
 * <p>The address-book operation returns snapshots of the original and
 * updated contact. This aspect consumes those snapshots for audit logging
 * without placing logging responsibilities in the domain layer.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public aspect UpdateContactLoggingAspect {

    /**
     * Matches execution of the address-book update operation.
     */
    pointcut updateContact():
        execution(Contact[] address_book.AddressBook.updateContact(..));

    /**
     * Logs both states when an update actually occurred.
     *
     * @param result original and updated contact snapshots
     */
    after() returning(Contact[] result): updateContact() {
        if (result != null && result.length == 2) {
            LogUtil.logToFile(
                "BEFORE UPDATE",
                result[0].toString()
            );

            LogUtil.logToFile(
                "UPDATED",
                result[1].toString()
            );
        }
    }
}
