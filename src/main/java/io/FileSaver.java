package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Provides shared text-file writing operations.
 *
 * <p>The utility supports both replacement writes used for application
 * persistence and append writes used for logging. Parent directories are
 * created automatically when necessary.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public final class FileSaver {

    /**
     * Prevents instantiation because this class provides only static
     * file-writing operations.
     */
    private FileSaver() {
    }

    /**
     * Writes lines to a file, replacing any existing content.
     *
     * @param path destination file
     * @param lines lines to write
     * @throws IOException if the file cannot be written
     */
    public static void saveLines(
            Path path,
            List<String> lines) throws IOException {

        ensureParentDirectory(path);
        Files.write(path, lines);
    }

    /**
     * Appends lines to a file while preserving existing content.
     *
     * <p>The file is created automatically when it does not already exist.</p>
     *
     * @param path destination file
     * @param lines lines to append
     * @throws IOException if the file cannot be written
     */
    public static void appendLines(
            Path path,
            List<String> lines) throws IOException {

        ensureParentDirectory(path);

        Files.write(
                path,
                lines,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    /**
     * Writes a string to a file, replacing any existing content.
     *
     * @param path destination file
     * @param content content to write
     * @throws IOException if the file cannot be written
     */
    public static void saveString(
            Path path,
            String content) throws IOException {

        ensureParentDirectory(path);
        Files.writeString(path, content);
    }

    /**
     * Writes lines to a file relative to the application base directory.
     *
     * @param fileName relative file name
     * @param lines lines to write
     * @throws IOException if the file cannot be written
     */
    public static void saveLinesToBase(
            String fileName,
            List<String> lines) throws IOException {

        saveLines(AppPaths.getFile(fileName), lines);
    }

    /**
     * Writes a string to a file relative to the application base directory.
     *
     * @param fileName relative file name
     * @param content content to write
     * @throws IOException if the file cannot be written
     */
    public static void saveStringToBase(
            String fileName,
            String content) throws IOException {

        saveString(AppPaths.getFile(fileName), content);
    }

    /**
     * Creates the destination's parent directories when necessary.
     *
     * @param path destination file
     * @throws IOException if required directories cannot be created
     */
    private static void ensureParentDirectory(Path path)
            throws IOException {

        Path parent = path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
