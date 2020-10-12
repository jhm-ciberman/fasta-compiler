package com.ciberman.fastacompiler.logger;

public interface Logger {
    void warn(Exception warning);

    void error(Exception error);
}
