package com.ciberman.fastacompiler.lexer.functional;

import com.ciberman.fastacompiler.Symbol;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.LexerContext;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;

public class State3 implements State {

    @Override
    public State handle(int codePoint, LexerContext ctx) throws LexicalException {

        if (codePoint == 'i') {
            String value = this.validateInt(ctx);
            return ctx.addSymbol(new Symbol(value, Symbol.Type.INT))
                    .andYieldToken(new Token(ctx, TokenType.INT, value));
        }

        if (codePoint == 'l') {
            String value = this.validateLong(ctx);
            return ctx.addSymbol(new Symbol(value, Symbol.Type.LONG))
                    .andYieldToken(new Token(ctx, TokenType.LONG, value));
        }

        throw new LexicalException(ctx, codePoint, "Integer literals must terminate with \"i\" or \"l\".");
    }

    private String validateLong(LexerContext ctx) throws LexicalException {
        String value = ctx.value();
        try {
            if (Long.parseLong(value) > 2147483647) {
                throw new LexicalException(ctx, "Long value " + value + " out of range");
            }
        } catch (NumberFormatException e) {
            throw new LexicalException(ctx, "Long value " + value + " out of range");
        }
        return value;
    }

    private String validateInt(LexerContext ctx) throws LexicalException {
        String value = ctx.value();
        try {
            if (Integer.parseInt(value) > 32767) {
                throw new LexicalException(ctx, "Integer value " + value + " out of range");
            }

        } catch (NumberFormatException e) {
            throw new LexicalException(ctx, "Integer value " + value + " out of range");
        }
        return value;
    }
}
