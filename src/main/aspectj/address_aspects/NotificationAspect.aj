package address_aspects;

import address_book.Contact;
import utilities.LogUtil;

/**
 * Logs successful address-book operation notifications.
 *
 * <p>This aspect separates high-level business-operation notifications from
 * detailed audit logging. Add, update, and delete logging aspects record the
 * affected contact data, while this aspect records whether the corresponding
 * operation completed successfully.</p>
 *
 * <p>Notifications are emitted only when an operation actually succeeds.
 * Cancelled delete and update operations therefore do not produce false
 * success notifications.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public aspect NotificationAspect {

    /**
     * Matches successful execution of the contact-add operation.
     */
    pointcut addOperation():
        execution(void address_book.AddressBook.addContact(Contact));

    /**
     * Records successful contact creation.
     */
    after() returning: addOperation() {
        LogUtil.logToFile(
            "NOTIFICATION",
            "Contact added successfully."
        );
    }

    /**
     * Matches execution of the contact-delete operation.
     */
    pointcut deleteOperation():
        execution(Contact address_book.AddressBook.deleteContact(..));

    /**
     * Records successful contact deletion only when a contact was actually
     * deleted.
     *
     * @param deleted contact returned by the delete operation
     */
    after() returning(Contact deleted): deleteOperation() {
        if (deleted != null) {
            LogUtil.logToFile(
                "NOTIFICATION",
                "Contact deleted successfully."
            );
        }
    }

    /**
     * Matches execution of the contact-update operation.
     */
    pointcut updateOperation():
        execution(Contact[] address_book.AddressBook.updateContact(..));

    /**
     * Records successful contact modification only when an update actually
     * occurred.
     *
     * @param result original and updated contact snapshots
     */
    after() returning(Contact[] result): updateOperation() {
        if (result != null && result.length == 2) {
            LogUtil.logToFile(
                "NOTIFICATION",
                "Contact updated successfully."
            );
        }
    }
}
