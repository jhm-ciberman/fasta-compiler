package com.ciberman.fastacompiler.lexer.states;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.LexerContext;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;

public class StateS implements State {
    @Override
    public State handle(int codePoint, LexerContext ctx) throws LexicalException {

        if (Character.isLowerCase(codePoint) || codePoint == '_') {
            return ctx.save().andGoToState(1);
        }

        if (Character.isUpperCase(codePoint)) {
            return ctx.save().andGoToState(10);
        }

        if (Character.isDigit(codePoint)) {
            return ctx.save().andGoToState(2);
        }

        if (Character.isWhitespace(codePoint)) {
            return ctx.goToState(0);
        }

        return switch (codePoint) {
            case -1   -> ctx.yieldToken(new Token(ctx, TokenType.EOF));
            case '+'  -> ctx.yieldToken(new Token(ctx, TokenType.PLUS));
            case '-'  -> ctx.yieldToken(new Token(ctx, TokenType.MINUS));
            case '*'  -> ctx.yieldToken(new Token(ctx, TokenType.MULTIPLY));
            case '/'  -> ctx.goToState(4);
            case '{'  -> ctx.goToState(6);
            case '('  -> ctx.yieldToken(new Token(ctx, TokenType.LPAREN));
            case ')'  -> ctx.yieldToken(new Token(ctx, TokenType.RPAREN));
            case ','  -> ctx.yieldToken(new Token(ctx, TokenType.COLON));
            case ';'  -> ctx.yieldToken(new Token(ctx, TokenType.SEMI));
            case '\'' -> ctx.goToState(11);
            case '<'  -> ctx.goToState(8);
            case '>'  -> ctx.goToState(7);
            case '='  -> ctx.goToState(9);
            default   -> throw new LexicalException(ctx, codePoint);
        };

    }
}
