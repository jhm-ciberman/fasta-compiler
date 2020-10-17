package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.lexer.LexerContext;

public class LexicalException extends FastaException {

    private final int line;

    private final int col;

    private final int codePoint;

    public LexicalException(LexerContext ctx, int codePoint) {
        this(ctx, codePoint, "");
    }

    public LexicalException(LexerContext ctx, int codePoint, String message) {
        super(ctx.getInputSource(), LexicalException.buildMessage(codePoint, message));
        this.line = ctx.line();
        this.col = ctx.col();
        this.codePoint = codePoint;
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

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public int getCol() {
        return this.col;
    }

    public int getUnicodeCodePoint() {
        return this.codePoint;
    }
}
