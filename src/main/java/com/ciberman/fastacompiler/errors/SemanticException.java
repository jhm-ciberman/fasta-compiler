package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.lexer.Token;

public class SemanticException extends FastaException {
    protected final Token token;

    public SemanticException(Token token, String message) {
        super(token.getInputSource(), message);
        this.token = token;
    }

    @Override
    public ErrorLevel getLevel() {
        return ErrorLevel.ERROR;
    }

    @Override
    public int getLine() {
        return this.token.getLine();
    }

    @Override
    public int getCol() {
        return this.token.getCol();
    }
}
