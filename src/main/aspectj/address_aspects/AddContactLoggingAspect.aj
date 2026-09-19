package address_aspects;

import address_book.Contact;
import utilities.LogUtil;

/**
 * Logs successful contact additions as a cross-cutting application concern.
 *
 * <p>The aspect observes successful executions of
 * {@code AddressBook.addContact(Contact)} so logging remains separate from
 * the address-book domain logic.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public aspect AddContactLoggingAspect {

    /**
     * Matches execution of the address-book add operation and exposes the
     * contact supplied to it.
     *
     * @param contact contact being added
     */
    pointcut addContact(Contact contact):
        execution(void address_book.AddressBook.addContact(Contact))
        && args(contact);

    /**
     * Logs the contact only after the add operation completes successfully.
     *
     * @param contact contact that was added
     */
    after(Contact contact) returning: addContact(contact) {
        if (contact != null) {
            LogUtil.logToFile("ADDED", contact.toString());
        }
    }
}
