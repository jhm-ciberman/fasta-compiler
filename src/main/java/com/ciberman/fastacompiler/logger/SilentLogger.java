package com.ciberman.fastacompiler.logger;

public class SilentLogger implements Logger {
    @Override
    public void warn(Exception warning) {
        // nothing
    }

    @Override
    public void error(Exception error) {
        // nothing
    }
}
