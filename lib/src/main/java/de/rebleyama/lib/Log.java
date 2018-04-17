package de.rebleyama.lib;

import java.io.IOException;
import java.util.logging.*;

/**
 * Exposes a logging api to be used within the project
 */
public class Log {

    /**
     * Initializes the Logger
     * @throws IOException Write to file failed
     */
    static public void setup() throws IOException {
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // set default log level
        logger.setLevel(Level.INFO);
    }

    /**
     * Allows a function to set a new global log level
     * @param newLevel The new log level to be used globally
     */
    public static void setLogLevel(Level newLevel) {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(newLevel);
    }
}