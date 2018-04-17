package de.rebleyama.lib;

import java.io.IOException;
import java.util.logging.*;

/**
 * Exposes a logging api to be used within the project
 */
public class Log {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    /**
     * Initializes the Logger
     * @throws IOException Write to file failed
     */
    static public void setup() throws IOException {
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // set default log level
        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler("log.txt");

        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);
    }
}