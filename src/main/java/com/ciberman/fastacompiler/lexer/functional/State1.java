package com.ciberman.fastacompiler.lexer.functional;

import com.ciberman.fastacompiler.Symbol;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.LexicalWarning;
import com.ciberman.fastacompiler.lexer.LexerContext;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;

public class State1 implements State {
    @Override
    public State handle(int codePoint, LexerContext ctx) throws LexicalException {
        if (Character.isDigit(codePoint) || Character.isLowerCase(codePoint) || codePoint == '_') {
            return ctx.save().goToState(1);
        }

        String value = ctx.value();

        if (value.length() > 20) {
            value = value.substring(0, 20);
            ctx.warning(new LexicalWarning(ctx, "The identifier name \""+ ctx.value() + "\" is larger than 20 characters. Using \"" + value + "\"."));
        }

        return ctx.peek()
            .addSymbol(new Symbol(value, Symbol.Type.UNKNOWN))
            .andYieldToken(new Token(ctx, TokenType.ID, value));
    }
}
