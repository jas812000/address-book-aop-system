package address_book;

/**
 * Provides the executable entry point for the Address Book application.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class Main {

    /**
     * Prevents instantiation because this class serves only as the
     * application entry point.
     */
    private Main() {
    }

    /**
     * Starts the interactive Address Book application.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        AddressBookApp app = new AddressBookApp();
        app.run();
    }
}
