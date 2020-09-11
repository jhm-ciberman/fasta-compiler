package com.ciberman.fastacompiler.lexer.states;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.LexerContext;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;

public class State10 implements State {
    @Override
    public State handle(int codePoint, LexerContext ctx) throws LexicalException {
       if (Character.isUpperCase(codePoint)) {
            return ctx.save().andGoToState(10);
        }

        TokenType type;
        switch (ctx.value()) {
            case "IF":
                type = TokenType.IF;
                break;
            case "THEN":
                type = TokenType.THEN;
                break;
            case "ELSE":
                type = TokenType.ELSE;
                break;
            case "ENDIF":
                type = TokenType.ENDIF;
                break;
            case "PRINT":
                type = TokenType.PRINT;
                break;
            case "BEGIN":
                type = TokenType.BEGIN;
                break;
            case "END":
                type = TokenType.END;
                break;
            case "INT":
                type = TokenType.TYPE_INT;
                break;
            case "LONG":
                type = TokenType.TYPE_LONG;
                break;
            case "LOOP":
                type = TokenType.LOOP;
                break;
            case "UNTIL":
                type = TokenType.UNTIL;
                break;
            case "ITOL":
                type = TokenType.ITOL;
                break;
            default:
                throw new LexicalException(ctx, "Invalid keyword \"" + ctx.value() + "\".");
        }

        return ctx.peek().andYieldToken(new Token(ctx, type));
    }
}
