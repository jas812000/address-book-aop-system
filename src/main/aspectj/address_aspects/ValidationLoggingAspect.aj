package address_aspects;

import io.AppPaths;
import io.FileSaver;
import utilities.ErrorUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Logs failed field-validation attempts as a cross-cutting concern.
 *
 * <p>The aspect observes calls to the application's shared
 * {@code FieldValidator.isValid*} methods. Failed validation attempts are
 * appended to a dedicated validation log without adding logging logic to
 * the validator itself.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public aspect ValidationLoggingAspect {

    /**
     * Matches single-string field-validation calls.
     *
     * @param input value being validated
     */
    pointcut fieldValidation(String input):
        call(static boolean address_book.FieldValidator.isValid*(String))
        && args(input);

    /**
     * Executes validation and logs unsuccessful results.
     *
     * @param input value being validated
     * @return original validation result
     */
    boolean around(String input): fieldValidation(input) {
        boolean valid = proceed(input);

        if (!valid) {
            try {
                String methodName =
                    thisJoinPointStaticPart
                        .getSignature()
                        .getName();

                FileSaver.appendLines(
                    AppPaths.getFile(
                        "log/validation_errors.txt"
                    ),
                    List.of(
                        "["
                            + LocalDateTime.now()
                            + "] Invalid "
                            + methodName
                            + ": "
                            + String.valueOf(input)
                    )
                );
            } catch (IOException exception) {
                ErrorUtil.logError(exception);
            }
        }

        return valid;
    }
}
