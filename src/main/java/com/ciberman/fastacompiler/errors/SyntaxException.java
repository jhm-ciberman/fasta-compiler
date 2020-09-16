package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.lexer.Token;

public class SyntaxException extends FastaException {

    protected final Token token;

    protected final String fileName;

    public SyntaxException(Token token, String fileName, String message) {
        super(SyntaxException.buildMessage(token, message));
        this.token = token;
        this.fileName = fileName;
    }

    private static String buildMessage(Token token, String message) {
        StringBuilder s = new StringBuilder();

        s.append("Unexpected token \"")
                .append(token.getType().toString())
                .append(" (")
                .append(token.getType().code())
                .append(")")
                .append("\".");

        if (! message.isBlank()) {
            s.append(" ").append(message);
        }
        return s.toString();
    }

    @Override
    public ErrorLevel getLevel() {
        return ErrorLevel.ERROR;
    }

    @Override
    public String getFileName() {
        return this.fileName;
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
