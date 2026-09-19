package utilities;

import io.AppPaths;
import io.FileSaver;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Provides centralized exception logging for the application.
 *
 * <p>Error entries include a timestamp, exception type, exception message,
 * and complete stack trace. Error-log persistence uses the application's
 * shared path and file utilities rather than maintaining an independent
 * hardcoded logging location.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class ErrorUtil {

    /**
     * Prevents instantiation because this class provides only static
     * error-logging operations.
     */
    private ErrorUtil() {
    }

    /**
     * Appends detailed exception information to the application error log.
     *
     * @param exception exception to log
     */
    public static void logError(Exception exception) {
        if (exception == null) {
            return;
        }

        try {
            FileSaver.appendLines(
                    AppPaths.getFile("log/error.txt"),
                    List.of(
                            "[" + LocalDateTime.now() + "] ERROR",
                            "Exception: "
                                    + exception.getClass().getSimpleName(),
                            "Message: "
                                    + String.valueOf(exception.getMessage()),
                            "Stack Trace:",
                            stackTraceOf(exception),
                            "--------------------------------------------------"
                    )
            );
        } catch (IOException loggingException) {
            System.err.println(
                    "[ERROR] Unable to write to the application error log."
            );
        }
    }

    /**
     * Converts an exception stack trace into text suitable for persistence.
     *
     * @param exception exception whose stack trace is required
     * @return complete stack trace
     */
    private static String stackTraceOf(Exception exception) {
        StringWriter buffer = new StringWriter();

        try (PrintWriter writer = new PrintWriter(buffer)) {
            exception.printStackTrace(writer);
        }

        return buffer.toString().stripTrailing();
    }
}
