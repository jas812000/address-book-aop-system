package io;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Parses delimited text records into domain objects.
 *
 * <p>The parser supports quoted fields and escaped double quotes, allowing
 * delimiters to appear inside quoted values. Domain-specific conversion is
 * delegated to a supplied {@link LineParser}.</p>
 *
 * @param <T> object type produced from each record
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public class FileParser<T> {

    private final char delimiter;
    private final LineParser<T> parser;

    /**
     * Creates a parser for records using the supplied single-character
     * delimiter.
     *
     * @param delimiter delimiter separating fields
     * @param parser domain-specific token parser
     * @throws IllegalArgumentException if the delimiter is not exactly
     *         one character
     */
    public FileParser(String delimiter, LineParser<T> parser) {
        if (delimiter == null || delimiter.length() != 1) {
            throw new IllegalArgumentException(
                    "Delimiter must contain exactly one character."
            );
        }

        this.delimiter = delimiter.charAt(0);
        this.parser = Objects.requireNonNull(
                parser,
                "Line parser cannot be null."
        );
    }

    /**
     * Parses all valid nonblank records.
     *
     * <p>Malformed records are skipped rather than terminating the entire
     * load operation. This allows one damaged persisted record to be
     * isolated without preventing valid contacts from loading.</p>
     *
     * @param lines persisted records
     * @return successfully parsed objects
     */
    public List<T> parseLines(List<String> lines) {
        List<T> result = new ArrayList<>();

        if (lines == null) {
            return result;
        }

        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }

            try {
                String[] tokens = tokenize(line);
                T item = parser.parse(tokens);

                if (item != null) {
                    result.add(item);
                }
            } catch (RuntimeException exception) {
                System.out.println(
                        "Skipping malformed persisted record."
                );
            }
        }

        return result;
    }

    /**
     * Tokenizes one delimited record while respecting CSV-style quoted
     * fields and escaped double quotes.
     *
     * @param line record to tokenize
     * @return parsed fields
     * @throws IllegalArgumentException if quoted-field syntax is malformed
     */
    private String[] tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean inQuotes = false;
        boolean quoteClosed = false;

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            if (inQuotes) {
                if (currentChar == '"') {
                    if (i + 1 < line.length()
                            && line.charAt(i + 1) == '"') {

                        current.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                        quoteClosed = true;
                    }
                } else {
                    current.append(currentChar);
                }

                continue;
            }

            if (quoteClosed) {
                if (currentChar == delimiter) {
                    tokens.add(current.toString());
                    current.setLength(0);
                    quoteClosed = false;
                    continue;
                }

                if (Character.isWhitespace(currentChar)) {
                    continue;
                }

                throw new IllegalArgumentException(
                        "Unexpected character after closing quote."
                );
            }

            if (currentChar == delimiter) {
                tokens.add(current.toString());
                current.setLength(0);
                continue;
            }

            if (currentChar == '"') {
                if (current.length() == 0) {
                    inQuotes = true;
                    continue;
                }

                throw new IllegalArgumentException(
                        "Unexpected quote inside unquoted field."
                );
            }

            current.append(currentChar);
        }

        if (inQuotes) {
            throw new IllegalArgumentException(
                    "Unterminated quoted field."
            );
        }

        tokens.add(current.toString());

        return tokens.toArray(new String[0]);
    }
}
