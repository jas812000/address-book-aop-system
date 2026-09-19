package address_aspects;

import utilities.ErrorUtil;

/**
 * Provides centralized exception logging through AspectJ.
 *
 * <p>This aspect observes exceptions thrown from the application's
 * controller, persistence, parsing, and file-I/O layers. Exception logging
 * therefore remains separate from the classes performing those operations.</p>
 *
 * <p>The aspect deliberately does not suppress exceptions or replace failure
 * results. After an exception is logged, normal Java exception propagation
 * continues unchanged.</p>
 *
 * <p>Logging infrastructure is excluded from observation so a failure that
 * occurs while writing a log cannot recursively trigger exception logging.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public aspect ExceptionHandlingAspect {

    /**
     * Matches application operations whose unexpected exceptions should be
     * centrally logged.
     *
     * <p>Log-writing operations and execution occurring within the logging
     * utilities are excluded to prevent recursive logging failures.</p>
     */
    pointcut monitoredOperation():
        (
            execution(* address_book.AddressBookController.*(..))
            || execution(* address_utils.storage..*(..))
            || execution(* address_utils.parser..*(..))
            || execution(* io..*(..))
        )
        && !execution(* io.FileSaver.appendLines(..))
        && !cflow(execution(* utilities.LogUtil.*(..)))
        && !cflow(execution(* utilities.ErrorUtil.*(..)));

    /**
     * Logs an exception thrown by a monitored operation.
     *
     * <p>Because this is {@code after throwing} advice, the original
     * exception continues through the application's normal control flow.</p>
     *
     * @param exception exception thrown by the intercepted operation
     */
    after() throwing(Exception exception): monitoredOperation() {
        ErrorUtil.logError(exception);
    }
}
