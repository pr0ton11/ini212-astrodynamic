package ch.hftm.astrodynamic.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Logger Factory
public class Log {

    private static FileHandler fileHandler;  // Contains the handler for the log file
    private static boolean fileError = false; // Flag to check if file errors happened
    private static ConsoleHandler consoleHandler = new ConsoleHandler();  // Contains the console handler
    private static Level logLevel = Level.INFO;  // Sets the default log level

    // Configures the logger based on the configuration repository
    public static void configure() {
        // Parse log level from configuration
        String logConfigLevel = ConfigRepository.get("log_level", "INFO").toUpperCase();
        try {
            logLevel = Level.parse(logConfigLevel);
        } catch (IllegalArgumentException ex) {
            System.out.println(String.format("Illegal configuration value for log_level: %s; using default: INFO", logConfigLevel)); 
        }
        // Parse log file
        if (useFile()) {
            // Read the path of the file
            String filePath = ConfigRepository.get("log_file");
            // Check if FileHandler is already initialized
            if (fileHandler == null) {
                try {
                    fileHandler = new FileHandler(filePath, true);
                    fileHandler.setFormatter(new java.util.logging.SimpleFormatter());
                } catch (Exception ex) {
                    fileError = true;
                    System.out.println(String.format("Error initializing file logger with path: %s; falling back to console", filePath)); 
                }
            }
        }
    }

    // Returns true if a file should be used for logging
    public static boolean useFile() {
        // if a FileError happened before, do not try to use file again
        if (fileError) {
            return false;
        }
        return ConfigRepository.keyExists("log_file");
    }

    // Returns true if the console should be used for logging
    public static boolean useConsole() {
        return ConfigRepository.getBool("log_disable_console", true);
    }

    // Builds a logger
    // Returns the logger for use
    public static Logger build() {
        Logger logger = Logger.getLogger("ch.hftm.astrodynamic.log");
        logger.setUseParentHandlers(false);
        // Logger configuration
        configure();  // Ensures that everything is properly initialized
        // Log level
        logger.setLevel(logLevel);
        // Logger sink
        if (useConsole()) {
            logger.addHandler(consoleHandler);
        }
        if (useFile()) {
            logger.addHandler(fileHandler);
        }
        // Return finished logger
        return logger;
    }

}
