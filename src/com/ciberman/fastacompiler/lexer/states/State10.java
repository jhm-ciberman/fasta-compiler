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

        TokenType type = switch (ctx.value()) {
            case "IF"     -> TokenType.IF;
            case "THEN"   -> TokenType.THEN;
            case "ELSE"   -> TokenType.ELSE;
            case "ENDIF"  -> TokenType.ENDIF;
            case "PRINT"  -> TokenType.PRINT;
            case "BEGIN"  -> TokenType.BEGIN;
            case "END"    -> TokenType.END;
            case "INT"    -> TokenType.TYPE_INT;
            case "LONG"   -> TokenType.TYPE_LONG;
            case "LOOP"   -> TokenType.LOOP;
            case "UNTIL"  -> TokenType.UNTIL;
            case "ITOL"   -> TokenType.ITOL;
            default -> throw new LexicalException(ctx, "Invalid keyword \"" + ctx.value() + "\".");
        };

        return ctx.peek().andYieldToken(new Token(ctx, type));
    }
}
