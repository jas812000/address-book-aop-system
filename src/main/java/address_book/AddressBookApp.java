package address_book;

/**
 * Runs the interactive Address Book application.
 *
 * <p>This class owns the main command-line loop and delegates contact
 * operations to {@link AddressBookController}.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public class AddressBookApp {

    private final AddressBookController controller = new AddressBookController();

    /**
     * Loads persisted contacts and starts the main application loop.
     */
    public void run() {
        controller.load();

        while (true) {
            displayMenu();

            String choice = controller.getScanner().nextLine().trim();

            switch (choice) {
                case "1":
                    controller.add();
                    break;
                case "2":
                    controller.delete();
                    break;
                case "3":
                    controller.update();
                    break;
                case "4":
                    controller.display();
                    break;
                case "5":
                    controller.save();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please select 1-5.");
            }
        }
    }

    /**
     * Displays the primary application menu.
     */
    private void displayMenu() {
        System.out.println();
        System.out.println("--- Address Book Menu ---");
        System.out.println("1. Add Contact");
        System.out.println("2. Delete Contact");
        System.out.println("3. Update Contact");
        System.out.println("4. Display Contacts");
        System.out.println("5. Exit");
        System.out.print("Enter choice: ");
    }
}

