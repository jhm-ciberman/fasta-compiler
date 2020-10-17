package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.InputSource;

public abstract class FastaException extends Exception {

    public enum ErrorLevel {
        ERROR, WARN,
    }

    protected final InputSource inputSource;

    public FastaException(InputSource inputSource, String message) {
        super(message);
        this.inputSource = inputSource;
    }

    public abstract int getLine();

    public abstract int getCol();

    public ErrorLevel getLevel() {
        return ErrorLevel.ERROR;
    }

    public String getFileName() {
        return this.inputSource.getFileName();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        switch (this.getLevel()) {
            case ERROR: s.append("ERROR "); break;
            case WARN:  s.append("WARN ");  break;
        }

        s.append(this.getFileName())
                .append(" (Line ")
                .append(this.getLine())
                .append(":")
                .append(this.getCol())
                .append(")");

        String m = this.getMessage();
        if (! m.isBlank()) {
            s.append(" ").append(m);
        }
        return s.toString();
    }

}
