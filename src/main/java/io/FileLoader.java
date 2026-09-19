package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Provides shared text-file reading operations.
 *
 * <p>The utility supports reading complete files or individual lines and
 * provides convenience operations for files relative to the application's
 * configured base directory.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class FileLoader {

    /**
     * Prevents instantiation because this class provides only static
     * file-reading operations.
     */
    private FileLoader() {
    }

    /**
     * Reads all lines from a file.
     *
     * @param path source file
     * @return file contents as individual lines
     * @throws IOException if the file cannot be read
     */
    public static List<String> loadLines(Path path) throws IOException {
        return Files.readAllLines(path);
    }

    /**
     * Reads an entire file as one string.
     *
     * @param path source file
     * @return complete file contents
     * @throws IOException if the file cannot be read
     */
    public static String loadAsString(Path path) throws IOException {
        return Files.readString(path);
    }

    /**
     * Reads lines from a file relative to the application base directory.
     *
     * @param fileName relative file name
     * @return file contents as individual lines
     * @throws IOException if the file cannot be read
     */
    public static List<String> loadLinesFromBase(String fileName)
            throws IOException {

        return loadLines(AppPaths.getFile(fileName));
    }

    /**
     * Determines whether a file exists.
     *
     * @param path file to check
     * @return {@code true} if the file exists
     */
    public static boolean fileExists(Path path) {
        return Files.exists(path);
    }
}
