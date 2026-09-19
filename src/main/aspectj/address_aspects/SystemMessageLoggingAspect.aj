package address_aspects;

import utilities.LogUtil;

/**
 * Observes significant console messages and records them through the
 * application's logging infrastructure.
 *
 * <p>This aspect demonstrates AspectJ interception of output behavior without
 * coupling command-line classes to the logging subsystem. Routine prompts and
 * normal contact displays are intentionally ignored so the application log
 * remains focused on operationally meaningful messages.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public aspect SystemMessageLoggingAspect {

    /**
     * Matches calls to {@code System.out.println(String)} originating from
     * the address-book application layer.
     *
     * @param message console message
     */
    pointcut applicationMessage(String message):
        call(void java.io.PrintStream.println(String))
        && args(message)
        && within(address_book..*);

    /**
     * Logs application messages that represent warnings, failures,
     * cancellations, or invalid user input.
     *
     * @param message message written to the console
     */
    after(String message): applicationMessage(message) {
        if (isLoggable(message)) {
            LogUtil.logToFile(
                classify(message),
                message
            );
        }
    }

    /**
     * Determines whether a console message represents an event worth
     * persisting.
     *
     * @param message console message
     * @return {@code true} when the message should be logged
     */
    private boolean isLoggable(String message) {
        if (message == null || message.isBlank()) {
            return false;
        }

        String normalized = message.toLowerCase();

        return normalized.contains("invalid")
            || normalized.contains("error")
            || normalized.contains("failed")
            || normalized.contains("cancelled")
            || normalized.contains("not found")
            || normalized.contains("no matching");
    }

    /**
     * Assigns an appropriate log category to an intercepted console message.
     *
     * @param message console message
     * @return log category
     */
    private String classify(String message) {
        String normalized = message.toLowerCase();

        if (normalized.contains("error")
                || normalized.contains("failed")) {

            return "ERROR MESSAGE";
        }

        if (normalized.contains("invalid")) {
            return "VALIDATION MESSAGE";
        }

        if (normalized.contains("cancelled")) {
            return "CANCELLATION";
        }

        return "APPLICATION MESSAGE";
    }
}
