package com.ciberman.fastacompiler.errors;

public abstract class FastaException extends Exception {

    public enum ErrorLevel {
        ERROR, WARN,
    }

    public FastaException(String message) {
        super(message);
    }

    public abstract ErrorLevel getLevel();
    public abstract String getFileName();
    public abstract int getLine();
    public abstract int getCol();

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.getFileName())
                .append("[")
                .append(this.getLine())
                .append(":")
                .append(this.getCol())
                .append("] ");

        switch (this.getLevel()) {
            case ERROR: s.append("ERROR"); break;
            case WARN:  s.append("WARN");  break;
        }

        String m = this.getMessage();
        if (! m.isBlank()) {
            s.append(" ").append(m);
        }
        return s.toString();
    }

}
