package com.ciberman.fastacompiler.lexer.states;

import com.ciberman.fastacompiler.Symbol;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.LexerContext;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;

public class State11 implements State {
    @Override
    public State handle(int codePoint, LexerContext ctx) throws LexicalException {
        if (codePoint == -1) {
            throw new LexicalException(ctx, "Unexpected end of file. String not closed.");
        }

        if (codePoint == '\'') {
            String value = ctx.value();
            return ctx.addSymbol(new Symbol(value, Symbol.Type.STRING))
                    .yieldToken(new Token(ctx, TokenType.STR, value));
        }

        if (codePoint != '\n') {
            ctx.save();
        }

        return ctx.goToState(11);
    }
}
