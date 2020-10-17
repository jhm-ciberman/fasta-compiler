package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.errors.LexicalException;

public class FunctionalAutomata implements Automata {

    interface State {
        /**
         * Handles the current state of the automata, performs the corresponding operations,
         * and returns the next State of the automata.
         *
         * @param codePoint The current unicode code point (character)
         * @param ctx The current Lexer context object
         * @throws LexicalException In case of a critical lexical exception
         */
        void handle(int codePoint, LexerContext ctx) throws LexicalException;
    }


    private final State[] states = new State[] {
            this::stateS,
            this::state1,
            this::state2,
            this::state3,
            this::state4,
            this::state5,
            this::state6,
            this::state7,
            this::state8,
            this::state9,
            this::state10,
            this::state11,
    };

    private State currentState = this.states[0];

    @Override
    public void reset() {
        this.currentState = this.states[0]; // 0 = StateS
    }

    @Override
    public void advance(int currentCodePoint, LexerContext ctx) throws LexicalException {
        this.currentState.handle(currentCodePoint, ctx);
    }


    private void goToState(int stateNumber) {
        this.currentState = this.states[stateNumber];
    }

    private void saveAndGoToState(LexerContext ctx, int stateNumber) {
        ctx.save();
        this.currentState = this.states[stateNumber];
    }

    private void state1(int codePoint, LexerContext ctx) throws LexicalException {
        if (Character.isDigit(codePoint) || Character.isLowerCase(codePoint) || codePoint == '_') {
            ctx.save();
            this.goToState(1);
        } else {
            String value = Validator.validateId(ctx);
            ctx.peek().yieldToken(new Token(ctx, TokenType.ID, value));
        }
    }

    private void state2(int codePoint, LexerContext ctx) throws LexicalException {
        if (Character.isDigit(codePoint)) {
            ctx.save();
            this.goToState(2);
        } else if (codePoint == '_') {
            this.goToState(3);
        } else {
            String value = ctx.value();
            throw new LexicalException(ctx, codePoint, "Expecting integer literal type. Eg: \"" + value + "_i\" or \"" + value + "_l\").");
        }
    }

    private void state3(int codePoint, LexerContext ctx) throws LexicalException {
        if (codePoint == 'i') {
            String value = Validator.validateInt(ctx);
            ctx.yieldToken(new Token(ctx, TokenType.INT, value));
        } else if (codePoint == 'l') {
            String value = Validator.validateLong(ctx);
            ctx.yieldToken(new Token(ctx, TokenType.LONG, value));
        } else {
            throw new LexicalException(ctx, codePoint, "Integer literals must terminate with \"i\" or \"l\".");
        }
    }

    private void state4(int codePoint, LexerContext ctx) throws LexicalException {
        if (codePoint == '/') {
            this.goToState(5);
        } else {
            ctx.peek().yieldToken(new Token(ctx, TokenType.DIVISION));
        }
    }

    private void state5(int codePoint, LexerContext ctx) throws LexicalException {
        if (codePoint == -1) {
            ctx.peek();
            this.goToState(0);
        } else {
            this.goToState((codePoint == '\n') ? 0 : 5);
        }
    }

    private void state6(int codePoint, LexerContext ctx) throws LexicalException {
        if (codePoint == -1) {
            ctx.peek();
            this.goToState(0);
        } else {
            this.goToState((codePoint == '#') ? 0 : 6);
        }
    }

    private void state7(int codePoint, LexerContext ctx) throws LexicalException {
        if (codePoint == '=') {
            ctx.yieldToken(new Token(ctx, TokenType.GTE));
        } else {
            ctx.peek().yieldToken(new Token(ctx, TokenType.GT));
        }
    }

    private void state8(int codePoint, LexerContext ctx) throws LexicalException {
        if (codePoint == '=') {
            ctx.yieldToken(new Token(ctx, TokenType.LTE));
        } else if (codePoint == '>') {
            ctx.yieldToken(new Token(ctx, TokenType.NOTEQ));
        } else {
            ctx.peek().yieldToken(new Token(ctx, TokenType.LT));
        }
    }

    private void state9(int codePoint, LexerContext ctx) throws LexicalException {
        if (codePoint == '=') {
            ctx.yieldToken(new Token(ctx, TokenType.EQ));
        } else {
            ctx.peek().yieldToken(new Token(ctx, TokenType.ASSIGN));
        }
    }

    private void state10(int codePoint, LexerContext ctx) throws LexicalException {
        if (Character.isUpperCase(codePoint)) {
            ctx.save();
            this.goToState(10);
        } else {
            TokenType type = Validator.validateReserved(ctx);
            ctx.peek().yieldToken(new Token(ctx, type));
        }

    }

    private void state11(int codePoint, LexerContext ctx) throws LexicalException {
        if (codePoint == -1) {
            throw new LexicalException(ctx, "Unexpected end of file. String not closed.");
        }

        if (codePoint == '\'') {
            String value = ctx.value();
            ctx.yieldToken(new Token(ctx, TokenType.STR, value));
        } else {
            if (codePoint != '\n') {
                ctx.save();
            }

            this.goToState(11);
        }

    }

    private void stateS(int codePoint, LexerContext ctx) throws LexicalException {
        if (Character.isLowerCase(codePoint) || codePoint == '_') {
            ctx.save();
            this.goToState(1);
        } else if (Character.isUpperCase(codePoint)) {
            ctx.save();
            this.goToState(10);
        } else if (Character.isDigit(codePoint)) {
            ctx.save();
            this.goToState(2);
        } else if (Character.isWhitespace(codePoint)) {
            this.goToState(0);
        } else {
            switch (codePoint) {
                case -1:   ctx.yieldToken(new Token(ctx, TokenType.EOF)); break;
                case '+':  ctx.yieldToken(new Token(ctx, TokenType.PLUS)); break;
                case '-':  ctx.yieldToken(new Token(ctx, TokenType.MINUS)); break;
                case '*':  ctx.yieldToken(new Token(ctx, TokenType.MULTIPLY)); break;
                case '{':  ctx.yieldToken(new Token(ctx, TokenType.LBRACE)); break;
                case '}':  ctx.yieldToken(new Token(ctx, TokenType.RBRACE)); break;
                case '/':  this.goToState(4); break;
                case '#':  this.goToState(6); break;
                case '(':  ctx.yieldToken(new Token(ctx, TokenType.LPAREN)); break;
                case ')':  ctx.yieldToken(new Token(ctx, TokenType.RPAREN)); break;
                case ',':  ctx.yieldToken(new Token(ctx, TokenType.COMMA)); break;
                case ';':  ctx.yieldToken(new Token(ctx, TokenType.SEMI)); break;
                case '\'': this.goToState(11); break;
                case '<':  this.goToState(8); break;
                case '>':  this.goToState(7); break;
                case '=':  this.goToState(9); break;
                default:   throw new LexicalException(ctx, codePoint);
            }
        }
    }
}
