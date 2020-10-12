package com.ciberman.fastacompiler.logger;

public class ConsoleLogger implements Logger {
    public void warn(Exception warning) {
        warning.printStackTrace();
    }

    public void error(Exception error) {
        error.printStackTrace();
    }
}
