package com.orangeHRM.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {

        private static final Logger logger = LogManager.getLogger(LogUtil.class);

        public static void info(String message) {
            logger.info(message);
        }

        public static void error(String message, Throwable throwable) {
            logger.error(message, throwable);
        }

        public static void warn(String message) {
            logger.warn(message);
        }

        public static void debug(String message) {
            logger.debug(message);
        }
    }

