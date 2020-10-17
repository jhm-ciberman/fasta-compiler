package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.lexer.LexerContext;

public class LexicalWarning extends LexicalException {

    public LexicalWarning(LexerContext ctx, String message) {
        super(ctx, message);
    }

    @Override
    public ErrorLevel getLevel() {
        return ErrorLevel.WARN;
    }
}
