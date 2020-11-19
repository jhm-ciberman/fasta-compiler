package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.lexer.Token;

public class SyntaxException extends FastaException {

    protected final Token token;

    public SyntaxException(Token token, String message) {
        super(token.getInputSource(), SyntaxException.buildMessage(token, message));
        this.token = token;
    }

    private static String buildMessage(Token token, String message) {
        StringBuilder s = new StringBuilder();

        s.append("Unexpected token ")
                .append(token.getType().toString())
                .append(".");

        if (! message.isEmpty()) {
            s.append(" ").append(message);
        }
        return s.toString();
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
