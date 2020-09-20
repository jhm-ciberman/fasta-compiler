package com.ciberman.fastacompiler.lexer.functional;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.LexerContext;

public class State2 implements State {

    @Override
    public State handle(int codePoint, LexerContext ctx) throws LexicalException {
        if (Character.isDigit(codePoint)) {
            return ctx.save().andGoToState(2);
        }

        if (codePoint == '_') {
            return ctx.goToState(3);
        }

        String value = ctx.value();
        throw new LexicalException(ctx, codePoint, "Expecting integer literal type. Eg: \"" + value + "_i\" or \"" + value + "_l\").");

    }
}
