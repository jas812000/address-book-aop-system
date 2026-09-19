package address_aspects;

import address_book.Contact;
import utilities.LogUtil;

/**
 * Logs successful contact deletions as a cross-cutting application concern.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public aspect DeleteContactLoggingAspect {

    /**
     * Matches execution of the address-book delete operation.
     */
    pointcut deleteContact():
        execution(Contact address_book.AddressBook.deleteContact(..));

    /**
     * Logs a deletion only when an actual contact was returned.
     *
     * @param deleted deleted contact
     */
    after() returning(Contact deleted): deleteContact() {
        if (deleted != null) {
            LogUtil.logToFile("DELETED", deleted.toString());
        }
    }
}
