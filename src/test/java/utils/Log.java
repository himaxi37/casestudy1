package utils;

import org.testng.log4testng.Logger;

public class Log {
    public static Logger logger = Logger.getLogger(Log.class); // 🔥 Initialize directly

    public static void startLog(String testCaseName) {
        logger.info("Starting Test: " + testCaseName);
    }

    public static void endLog(String testCaseName) {
        logger.info("Ending Test: " + testCaseName);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

}
