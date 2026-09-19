package io;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests generic delimited-file parsing, including CSV quoting and malformed
 * record isolation.
 *
 * @author James Stevens
 * @version 2.0
 * @since 2026-09-18
 */
class FileParserTest {

    /**
     * Verifies ordinary delimited records.
     */
    @Test
    void parseLines_parsesSimpleFields() {
        FileParser<String> parser =
                new FileParser<>(
                        ",",
                        tokens -> String.join("|", tokens)
                );

        List<String> result =
                parser.parseLines(
                        List.of("James,Stevens")
                );

        assertEquals(
                List.of("James|Stevens"),
                result
        );
    }

    /**
     * Verifies commas embedded in quoted CSV fields.
     */
    @Test
    void parseLines_preservesDelimiterInsideQuotedField() {
        FileParser<String> parser =
                new FileParser<>(
                        ",",
                        tokens -> String.join("|", tokens)
                );

        List<String> result =
                parser.parseLines(
                        List.of(
                                "James,\"123 Main St, Apt 4\",Texas"
                        )
                );

        assertEquals(
                List.of(
                        "James|123 Main St, Apt 4|Texas"
                ),
                result
        );
    }

    /**
     * Verifies escaped double quotes inside quoted CSV fields.
     */
    @Test
    void parseLines_decodesEscapedQuotes() {
        FileParser<String> parser =
                new FileParser<>(
                        ",",
                        tokens -> String.join("|", tokens)
                );

        List<String> result =
                parser.parseLines(
                        List.of(
                                "James,\"123 \"\"Main\"\" St\",Texas"
                        )
                );

        assertEquals(
                List.of(
                        "James|123 \"Main\" St|Texas"
                ),
                result
        );
    }

    /**
     * Verifies whitespace after a closing quote is tolerated.
     */
    @Test
    void parseLines_allowsWhitespaceAfterClosingQuote() {
        FileParser<String> parser =
                new FileParser<>(
                        ",",
                        tokens -> String.join("|", tokens)
                );

        List<String> result =
                parser.parseLines(
                        List.of(
                                "\"James\"   ,Stevens"
                        )
                );

        assertEquals(
                List.of("James|Stevens"),
                result
        );
    }

    /**
     * Verifies that blank and null collections are handled safely.
     */
    @Test
    void parseLines_handlesEmptyInput() {
        FileParser<String> parser =
                new FileParser<>(
                        ",",
                        tokens -> String.join("|", tokens)
                );

        assertTrue(parser.parseLines(null).isEmpty());

        assertTrue(
                parser.parseLines(
                        List.of("", "   ")
                ).isEmpty()
        );
    }

    /**
     * Verifies that one malformed record does not prevent subsequent valid
     * records from being parsed.
     */
    @Test
    void parseLines_skipsMalformedRecordAndContinues() {
        FileParser<String> parser =
                new FileParser<>(
                        ",",
                        tokens -> String.join("|", tokens)
                );

        List<String> result =
                parser.parseLines(
                        List.of(
                                "James,Stevens",
                                "\"unterminated",
                                "Ada,Lovelace"
                        )
                );

        assertEquals(
                List.of(
                        "James|Stevens",
                        "Ada|Lovelace"
                ),
                result
        );
    }

    /**
     * Verifies that malformed characters following a closing quote cause
     * only that record to be skipped.
     */
    @Test
    void parseLines_skipsUnexpectedCharacterAfterQuote() {
        FileParser<String> parser =
                new FileParser<>(
                        ",",
                        tokens -> String.join("|", tokens)
                );

        List<String> result =
                parser.parseLines(
                        List.of(
                                "\"James\"x,Stevens",
                                "Ada,Lovelace"
                        )
                );

        assertEquals(
                List.of("Ada|Lovelace"),
                result
        );
    }

    /**
     * Verifies delimiter configuration requirements.
     */
    @Test
    void constructor_requiresSingleCharacterDelimiter() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new FileParser<>(
                        "",
                        tokens -> tokens.length
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new FileParser<>(
                        "::",
                        tokens -> tokens.length
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new FileParser<>(
                        null,
                        tokens -> tokens.length
                )
        );
    }

    /**
     * Verifies that a domain parser returning null simply omits that record.
     */
    @Test
    void parseLines_omitsNullParserResults() {
        FileParser<String> parser =
                new FileParser<>(
                        ",",
                        tokens -> null
                );

        assertTrue(
                parser.parseLines(
                        List.of("James,Stevens")
                ).isEmpty()
        );
    }

    /**
     * Verifies that a domain-parser exception is isolated to the affected
     * record.
     */
    @Test
    void parseLines_isolatesDomainParserException() {
        FileParser<String> parser =
                new FileParser<>(
                        ",",
                        tokens -> {
                            if ("bad".equals(tokens[0])) {
                                throw new IllegalArgumentException(
                                        "Bad record."
                                );
                            }

                            return tokens[0];
                        }
                );

        List<String> result =
                parser.parseLines(
                        List.of(
                                "James,Stevens",
                                "bad,record",
                                "Ada,Lovelace"
                        )
                );

        assertEquals(
                List.of("James", "Ada"),
                result
        );
    }
}
