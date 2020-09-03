package com.ciberman.fastacompiler.lexer.states;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.LexerContext;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;

public class State4 implements State {

    @Override
    public State handle(int codePoint, LexerContext ctx) throws LexicalException {
        if (codePoint == '/') {
            return ctx.goToState(5);
        }

        return ctx.peek().andYieldToken(new Token(ctx, TokenType.DIVISION));
    }
}
