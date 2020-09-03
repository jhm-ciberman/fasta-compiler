package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.lexer.LexerContext;

import java.util.Arrays;

public class LexicalException extends Exception {

    private final int line;

    private final int col;

    private final int codePoint;

    private final String fileName;

    private final String message;

    public LexicalException(LexerContext ctx, int codePoint, String message) {
        this.line = ctx.line();
        this.col = ctx.col();
        this.codePoint = codePoint;
        this.fileName = ctx.fileName();
        this.message = message;
    }

    public LexicalException(LexerContext ctx, int codePoint) {
        this(ctx, codePoint, "");
    }

    public LexicalException(LexerContext ctx, String message) {
        this(ctx, -1, message);
    }

    public LexicalException(LexerContext ctx) {
        this(ctx, "Unexpected character.");
    }

    public boolean isCritical() {
        return true;
    }

    protected String getErrorLevelStr() {
        return "ERROR";
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getLine() {
        return this.line;
    }

    public int getCol() {
        return this.col;
    }

    public String getMessage() {
        return this.message;
    }

    public int getUnicodeCodePoint() {
        return this.codePoint;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.getFileName())
                .append("[")
                .append(this.getLine())
                .append(":")
                .append(this.getCol())
                .append("] ")
                .append(this.getErrorLevelStr());

        int cp = this.getUnicodeCodePoint();
        if (cp >= 0) {
            s.append(" Unexpected character \"")
                    .append(new String(new int[]{cp}, 0, 1))
                    .append("\".");
        }

        String m = this.getMessage();
        if (! m.isBlank()) {
            s.append(" ").append(m);
        }
        return s.toString();
    }
}
