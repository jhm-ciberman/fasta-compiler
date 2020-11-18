package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.InputSource;
import org.jetbrains.annotations.Nullable;

public class Token {

    private final TokenType type;
    private final String value;
    private final FileLocation fileLocation;

    public Token(LexerContext ctx, TokenType type) {
        this(ctx, type, null);
    }

    public Token(LexerContext ctx, TokenType type, @Nullable String value) {
        this(new FileLocation(ctx.getInputSource(), ctx.line(), ctx.col()), type, value);
    }

    public Token(FileLocation location, TokenType type) {
        this.fileLocation = location;
        this.type = type;
        this.value = null;
    }

    public Token(FileLocation location, TokenType type, @Nullable String value) {
        this.fileLocation = location;
        this.type = type;
        this.value = value;
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
        return this.fileLocation.getLine();
    }

    public int getCol() {
        return (value != null) ? this.fileLocation.getCol() - this.value.length() : this.fileLocation.getCol();
    }

    public InputSource getInputSource() {
        return this.fileLocation.getInputSource();
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
