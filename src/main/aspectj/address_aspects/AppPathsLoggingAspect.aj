package address_aspects;

import utilities.LogUtil;

import java.nio.file.Path;

/**
 * Logs dynamic application-path resolution.
 *
 * <p>This aspect demonstrates infrastructure tracing without coupling
 * {@code AppPaths} directly to the logging subsystem. Path resolution that
 * occurs while the logging utilities themselves are executing is excluded
 * so logging cannot recursively trigger additional path-resolution logs.</p>
 *
 * @author James Stevens
 * @version 2.0
 * @since 2025-07-01
 */
public aspect AppPathsLoggingAspect {

    /**
     * Matches application file-path resolution performed outside the
     * logging infrastructure.
     *
     * @param fileName requested relative file name
     */
    pointcut fileResolution(String fileName):
        execution(Path io.AppPaths.getFile(String))
        && args(fileName)
        && !cflow(execution(* utilities.LogUtil.*(..)))
        && !cflow(execution(* utilities.ErrorUtil.*(..)));

    /**
     * Logs successfully resolved file paths.
     *
     * @param fileName requested relative file name
     * @param resolved resolved path
     */
    after(String fileName) returning(Path resolved):
        fileResolution(fileName) {

        LogUtil.logToFile(
            "PATH",
            "Resolved file '"
                + fileName
                + "' to "
                + resolved
        );
    }

    /**
     * Matches application directory-path resolution performed outside the
     * logging infrastructure.
     *
     * @param folderName requested relative directory name
     */
    pointcut directoryResolution(String folderName):
        execution(Path io.AppPaths.getSubDirectory(String))
        && args(folderName)
        && !cflow(execution(* utilities.LogUtil.*(..)))
        && !cflow(execution(* utilities.ErrorUtil.*(..)));

    /**
     * Logs successfully resolved directory paths.
     *
     * @param folderName requested relative directory name
     * @param resolved resolved path
     */
    after(String folderName) returning(Path resolved):
        directoryResolution(folderName) {

        LogUtil.logToFile(
            "PATH",
            "Resolved directory '"
                + folderName
                + "' to "
                + resolved
        );
    }
}
