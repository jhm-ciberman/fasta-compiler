package com.ciberman.fastacompiler;

public interface ErrorHandler {

    void warn(Exception warning);

    void error(Exception error);
}
