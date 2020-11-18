package com.ciberman.fastacompiler.logger;

import com.ciberman.fastacompiler.errors.FastaException;

import java.util.ArrayList;
import java.util.List;

public class ConsoleLogger implements Logger {

    private final List<Exception> messages = new ArrayList<>();

    private int warnCount = 0;

    private int errorCount = 0;

    public void warn(Exception warning) {
        this.messages.add(warning);
        this.warnCount++;
    }

    public void error(Exception error) {
        this.messages.add(error);
        this.errorCount++;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    public int getWarnCount() {
        return this.warnCount;
    }

    public void flush() {
        for (Exception exception : this.messages) {
            if (exception instanceof FastaException && ((FastaException) exception).getLevel() == FastaException.ErrorLevel.WARN) {
                System.out.println(exception.toString());
            } else {
                System.err.println(exception.toString());
            }
        }
    }
}
