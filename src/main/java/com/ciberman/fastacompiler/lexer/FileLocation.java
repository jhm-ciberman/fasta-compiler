package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.InputSource;

public class FileLocation {
    private final InputSource inputSource;
    private final int line;
    private final int col;

    public FileLocation(InputSource inputSource, int line, int col) {
        this.inputSource = inputSource;
        this.line = line;
        this.col = col;
    }

    public InputSource getInputSource() {
        return inputSource;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }
}
