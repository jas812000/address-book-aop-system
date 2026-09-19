package address_aspects;

import address_book.Contact;
import address_utils.formatter.ContactFormatter;

/**
 * Applies the application's detailed contact presentation through AspectJ.
 *
 * <p>The underlying {@link Contact#toString()} remains a simple domain
 * representation. Calls to it are intercepted and replaced with the richer
 * address-book presentation supplied by {@link ContactFormatter}. This
 * deliberately demonstrates separation of presentation as a cross-cutting
 * concern.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public aspect DisplayAspect {

    /**
     * Matches calls to {@link Contact#toString()}.
     *
     * @param contact target contact
     */
    pointcut contactDisplay(Contact contact):
        call(String address_book.Contact.toString())
        && target(contact);

    /**
     * Replaces the basic domain representation with detailed formatted
     * output at woven call sites.
     *
     * @param contact contact being represented
     * @return detailed contact representation
     */
    String around(Contact contact): contactDisplay(contact) {
        return ContactFormatter.format(contact);
    }
}
