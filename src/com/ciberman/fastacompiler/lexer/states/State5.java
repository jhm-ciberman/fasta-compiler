package com.ciberman.fastacompiler.lexer.states;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.LexerContext;

public class State5 implements State {
    @Override
    public State handle(int codePoint, LexerContext ctx) throws LexicalException {
        return ctx.goToState((codePoint == '\n') ? 0 : 5);
    }
}
