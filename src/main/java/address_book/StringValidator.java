package address_book;

/**
 * Defines a string-validation operation used by command-line input
 * components.
 *
 * <p>The functional interface allows validation methods to be supplied
 * through method references without coupling input-handling code to a
 * particular validation implementation.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
@FunctionalInterface
public interface StringValidator {

    /**
     * Determines whether an input value satisfies a validation rule.
     *
     * @param input value to validate
     * @return {@code true} if valid; otherwise {@code false}
     */
    boolean isValid(String input);
}
