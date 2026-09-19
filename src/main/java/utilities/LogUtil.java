package utilities;

import io.AppPaths;
import io.FileSaver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Provides centralized application event logging.
 *
 * <p>Log entries are appended to the application's general log and contain
 * a timestamp, descriptive label, and associated content. Logging failures
 * are delegated to {@link ErrorUtil}.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class LogUtil {

    /**
     * Prevents instantiation because this class provides only static
     * logging operations.
     */
    private LogUtil() {
    }

    /**
     * Appends a labeled event to the application log.
     *
     * @param label short description of the event type
     * @param content event details
     */
    public static void logToFile(String label, String content) {
        try {
            FileSaver.appendLines(
                    AppPaths.getFile("log/log.txt"),
                    List.of(
                            "[" + LocalDateTime.now() + "] "
                                    + String.valueOf(label),
                            String.valueOf(content),
                            "--------------------------------------------------"
                    )
            );
        } catch (IOException exception) {
            ErrorUtil.logError(exception);
        }
    }
}
