package io;

/**
 * Defines an operation that converts tokenized file data into a
 * domain-specific object.
 *
 * <p>The interface allows the generic file-parsing infrastructure to remain
 * independent of the domain object represented by each persisted record.</p>
 *
 * @param <T> object type produced by the parser
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
@FunctionalInterface
public interface LineParser<T> {

    /**
     * Converts one tokenized record into an object.
     *
     * @param tokens fields belonging to one persisted record
     * @return parsed object, or {@code null} when the record cannot be parsed
     */
    T parse(String[] tokens);
}
