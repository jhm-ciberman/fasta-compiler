package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.ErrorHandler;

public class ConsoleErrorHandler implements ErrorHandler {
    @Override
    public void warn(Exception warning) {
        warning.printStackTrace();
    }

    @Override
    public void error(Exception error) {
        error.printStackTrace();
    }
}
