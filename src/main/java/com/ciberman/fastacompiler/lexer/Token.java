package com.ciberman.fastacompiler.lexer;

import org.jetbrains.annotations.Nullable;

public class Token {

    private final TokenType type;
    private final String value;
    private final int line;
    private final int col;

    public Token(LexerContext ctx, TokenType type) {
        this(ctx, type, null);
    }

    public Token(LexerContext ctx, TokenType type, @Nullable String value) {
        this.type = type;
        this.value = value;
        this.line = ctx.line();
        this.col = ctx.col();
    }

    public int code()
    {
        return this.type.code();
    }

    public TokenType getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public int getLine() {
        return this.line;
    }

    public int getCol() {
        return (value != null) ? this.col - this.value.length() : this.col;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Token [")
                .append(this.getLine())
                .append(":")
                .append(this.getCol())
                .append("] ")
                .append(this.getType());

        if (value != null) {
            s.append(" = \"")
                    .append(this.getValue())
                    .append("\"");
        }

        return s.toString();
    }
}
