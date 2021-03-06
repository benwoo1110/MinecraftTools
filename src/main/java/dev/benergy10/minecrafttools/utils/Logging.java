package dev.benergy10.minecrafttools.utils;

import org.bukkit.plugin.Plugin;

import java.util.IllegalFormatException;
import java.util.logging.Logger;

/**
 * Handles console and debug logging for the plugin.
 */
public final class Logging {

    private static Logger logger;
    private static Logger debugLogger;
    private static boolean doDebug = false;

    public static void setup(Plugin plugin) {
        logger = Logger.getLogger(plugin.getName());
        debugLogger = Logger.getLogger(plugin.getName() + "-debug");
    }

    public static void info(String msg, Object... args) {
        logger.info(format(msg, args));
    }

    public static void warning(String msg, Object... args) {
        logger.warning(format(msg, args));
    }

    public static void severe(String msg, Object... args) {
        logger.severe(format(msg, args));
    }

    public static void debug(String msg, Object... args) {
        if (doDebug) {
            debugLogger.info(format(msg, args));
        }
    }

    private static String format(String msg, Object[] args) {
        try {
            return String.format(msg, args);
        }
        catch (IllegalFormatException e) {
            logger.warning("Illegal format in the following message:");
            return msg;
        }
    }

    public static void doDebugLog(boolean state) {
        doDebug = state;
    }

    public static boolean isDoDebug() {
        return doDebug;
    }
}
