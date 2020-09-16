package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.lexer.LexerContext;

public class LexicalException extends FastaException {

    private final int line;

    private final int col;

    private final int codePoint;

    private final String fileName;

    public LexicalException(LexerContext ctx, int codePoint) {
        this(ctx, codePoint, "");
    }

    public LexicalException(LexerContext ctx, int codePoint, String message) {
        super(LexicalException.buildMessage(codePoint, message));
        this.line = ctx.line();
        this.col = ctx.col();
        this.codePoint = codePoint;
        this.fileName = ctx.fileName();
    }

    private static String buildMessage(int codePoint, String message) {
        StringBuilder s = new StringBuilder();
        if (codePoint >= 0) {
            s.append("Unexpected character \"")
                    .append(new String(new int[]{codePoint}, 0, 1))
                    .append("\".");

        }
        if (! message.isBlank()) {
            s.append(" ").append(message);
        }
        return s.toString();
    }

    public LexicalException(LexerContext ctx, String message) {
        this(ctx, -1, message);
    }

    public ErrorLevel getLevel() {
        return ErrorLevel.ERROR;
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

    public int getUnicodeCodePoint() {
        return this.codePoint;
    }
}
