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

    @Override
    public int getErrorCount() {
        return 0;
    }

    @Override
    public int getWarnCount() {
        return 0;
    }

    @Override
    public void flush() {
        // nothing
    }
}
