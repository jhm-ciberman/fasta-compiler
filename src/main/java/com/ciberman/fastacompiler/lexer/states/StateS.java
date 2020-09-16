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

        switch (codePoint) {
            case -1:
                return ctx.yieldToken(new Token(ctx, TokenType.EOF));
            case '+':
                return ctx.yieldToken(new Token(ctx, TokenType.PLUS));
            case '-':
                return ctx.yieldToken(new Token(ctx, TokenType.MINUS));
            case '*':
                return ctx.yieldToken(new Token(ctx, TokenType.MULTIPLY));
            case '{':
                return ctx.yieldToken(new Token(ctx, TokenType.LBRACE));
            case '}':
                return ctx.yieldToken(new Token(ctx, TokenType.RBRACE));
            case '/':
                return ctx.goToState(4);
            case '#':
                return ctx.goToState(6);
            case '(':
                return ctx.yieldToken(new Token(ctx, TokenType.LPAREN));
            case ')':
                return ctx.yieldToken(new Token(ctx, TokenType.RPAREN));
            case ',':
                return ctx.yieldToken(new Token(ctx, TokenType.COLON));
            case ';':
                return ctx.yieldToken(new Token(ctx, TokenType.SEMI));
            case '\'':
                return ctx.goToState(11);
            case '<':
                return ctx.goToState(8);
            case '>':
                return ctx.goToState(7);
            case '=':
                return ctx.goToState(9);
            default:
                throw new LexicalException(ctx, codePoint);
        }

    }
}
