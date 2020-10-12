package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.logger.ConsoleLogger;
import com.ciberman.fastacompiler.logger.Logger;

public class Fasta {

    /**
     * The current logger implementation
     */
    private static Logger loggerImpl;

    /**
     * Sets the current logger implementation
     * @param logger The logger implementation to use
     */
    public static void setLogger(Logger logger) {
        Fasta.loggerImpl = logger;
    }

    /**
     * Gets the current logger implementation
     * @return The logger
     */
    public static Logger getLogger() {
        if (Fasta.loggerImpl == null) {
            Fasta.loggerImpl = new ConsoleLogger();
        }
        return Fasta.loggerImpl;
    }

    /**
     * Reports a waning to the logger
     * @param exception The exception to log
     */
    public static void warn(Exception exception) {
        Fasta.getLogger().warn(exception);
    }

    /**
     * Reports an error to the logger
     * @param exception The exception to log
     */
    public static void error(Exception exception) {
        Fasta.getLogger().error(exception);
    }
}
