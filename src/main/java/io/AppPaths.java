package io;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Centralizes filesystem locations used by the Address Book application.
 *
 * <p>The application data directory may be overridden through the
 * {@code APP_DATA_DIR} environment variable. When no override is supplied,
 * the application uses the local {@code data} directory.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class AppPaths {

    private static final String ENV_VAR = "APP_DATA_DIR";

    /**
     * Root directory containing application-managed data.
     */
    public static final Path BASE_DIRECTORY;

    /**
     * Persistent address-book CSV file.
     */
    public static final Path ADDRESS_BOOK_FILE;

    static {
        String configuredDirectory = System.getenv(ENV_VAR);

        if (configuredDirectory != null
                && !configuredDirectory.isBlank()) {

            BASE_DIRECTORY = Paths.get(configuredDirectory);
        } else {
            BASE_DIRECTORY = Paths.get("data");
        }

        ADDRESS_BOOK_FILE =
                BASE_DIRECTORY.resolve("address_book.csv");
    }

    /**
     * Prevents instantiation because this class provides only static
     * path configuration and resolution operations.
     */
    private AppPaths() {
    }

    /**
     * Resolves a file relative to the application base directory.
     *
     * @param fileName relative file name
     * @return resolved path
     */
    public static Path getFile(String fileName) {
        return BASE_DIRECTORY.resolve(fileName);
    }

    /**
     * Resolves a subdirectory relative to the application base directory.
     *
     * @param folderName relative directory name
     * @return resolved path
     */
    public static Path getSubDirectory(String folderName) {
        return BASE_DIRECTORY.resolve(folderName);
    }
}
