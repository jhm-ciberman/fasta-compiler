package com.ciberman.fastacompiler.lexer.states;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.LexerContext;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;

public class State7 implements State {

    @Override
    public State handle(int codePoint, LexerContext ctx) throws LexicalException {
        return (codePoint == '=')
            ? ctx.yieldToken(new Token(ctx, TokenType.GTE))
            : ctx.peek().andYieldToken(new Token(ctx, TokenType.GT));
    }
}
