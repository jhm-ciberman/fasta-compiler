package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.lexer.LexerContext;

public class LexicalWarning extends LexicalException {
    public LexicalWarning(LexerContext ctx, int codePoint, String message) {
        super(ctx, codePoint, message);
    }

    public LexicalWarning(LexerContext ctx, int codePoint) {
        super(ctx, codePoint);
    }

    public LexicalWarning(LexerContext ctx, String message) {
        super(ctx, message);
    }

    @Override
    public boolean isCritical() {
        return false;
    }

    @Override
    protected String getErrorLevelStr() {
        return "WARN";
    }
}
