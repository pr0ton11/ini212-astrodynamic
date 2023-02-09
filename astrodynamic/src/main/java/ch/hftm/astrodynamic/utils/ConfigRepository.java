package ch.hftm.astrodynamic.utils;

import java.util.HashMap;
import java.util.NoSuchElementException;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Contains configuration of the project
public class ConfigRepository {

    private static final String ENV_PREFIX = "AD_";  // AstroDynamic
    
    private static ConfigRepository instance; // Single instance of the config repository
    private static String[] cmdArgs;  // Contains all command line arguments

    private HashMap<String, String> config = new HashMap<>();  // Contains configuration keys and values as strings

    private ConfigRepository() {}  // Constructor

    // Retrieving and working with the singleton class, used primarly internally
    public static ConfigRepository getInstance() {
        if(instance == null) {
            instance = new ConfigRepository();
            // Load environment variables first
            instance.fromEnv();
            // Load command line parameters, this overrides environment
            instance.fromArgs(cmdArgs);
        }
        return instance;
    }

    // Loads the configuration from environment variables
    // The stettings are prefixed with AD_
    private void fromEnv() {
        System.getenv().forEach((key, value) -> {
            if (key.startsWith(ENV_PREFIX)) {
                // Remove prefix
                String suffix = key.replace(ENV_PREFIX, "");
                // Add configuration to this instance
                addConfiguration(suffix, value);
            }
        });
    }

    // Loads the configuration from command line arguments
    private void fromArgs(String[] args) {
        boolean expectValue = false;
        String lastKey = "";
        // Parse command line parameters
        for (String arg : args) {
            if (arg.startsWith("-") || (arg.startsWith("--"))) {
                // This is a key
                // Remove all "-" from the key
                arg = arg.replace("-", "");
                // Check for direct value assignment
                if (arg.contains("=")) {
                    String[] parts = lastKey.split(arg, 0);
                    // Check if the length is expected
                    if (parts.length != 2) {
                        throw new IllegalArgumentException(String.format("Illegal argument %s contains multiple assignments", arg));
                    }
                    // Update the lastKey value
                    lastKey = parts[0];
                    // Add configuration
                    addConfiguration(lastKey, parts[1]);
                    // Continue with the next element
                    continue;
                }
                // Lastkey does not contain a value assignment
                // Check if we expected a value
                // This would make the lastKey a flag instead
                if (expectValue) {
                    // Update lastKey with a boolean true value
                    addConfiguration(lastKey, "true");
                    // Reset the flag
                    expectValue = false;
                }
                // Update the last key with current argument
                lastKey = arg;
                // Update flag to expect a value next
                expectValue = true;
            } else {  // Values (any string without - prefix)
                if (expectValue) {
                    // Because we expected an value, we can assign with the last key
                    addConfiguration(lastKey, arg);
                } else {
                    // We did not expect a value, throw an exception
                    throw new IllegalArgumentException(String.format("Unexpected command line argument: %s", arg));
                }
            }
        }
    }

    // Updates the current cmdArgs of this class with the arguments from the main method
    public static void registerArgs(String[] args) {
        cmdArgs = args;
    }

    // Adds configuration to the instance
    public void addConfiguration(String key, String value) {
        config.put(key, value);
    }

    // Adds configuration to the instance
    public static void add(String key, String value) {
        getInstance().addConfiguration(key, value);
    }

    // Getter for optional string
    public static String get(String key, String defaultValue) {
        // Key exists in config
        if (getInstance().config.containsKey(key)) {
            return getInstance().config.get(key);
        // Value is not optional, must exist
        } else if (defaultValue == "") {
            throw new NoSuchElementException(String.format("The config value %s is mandatory but did not exist", key));
        }
        // Key not found but default value provided
        return defaultValue;
    }

    // Getter for mandantory string
    public static String get(String key) throws NoSuchElementException {
        return get(key, "");
    }

    // Getter for optional boolean
    public static boolean getBool(String key, Boolean defaultValue) {
        try {
            return getBool(key);
        } catch (NoSuchElementException ex) {
            return defaultValue;
        }
    }

    // Getter for mandatory boolean
    public static boolean getBool(String key) {
        return Boolean.parseBoolean(get(key));
    }

    // Getter for optional int
    public static int getInt(String key, int defaultValue) throws NumberFormatException {
        try {
            return getInt(key);
        } catch (NoSuchElementException ex) {
            return defaultValue;
        }
    }

    // Getter for mandatory int
    public static int getInt(String key) throws NumberFormatException {
        return Integer.parseInt(get(key));
    }

    // Getter for optional float
    public static Float getFloat(String key, Float defaultValue) throws NumberFormatException {
        try {
            return getFloat(key);
        } catch (NoSuchElementException ex) {
            return defaultValue;
        }
    }

    // Getter for mandatory int
    public static Float getFloat(String key) throws NumberFormatException {
        return Float.parseFloat(get(key));
    }

}
