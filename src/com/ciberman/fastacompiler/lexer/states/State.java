package com.ciberman.fastacompiler.lexer.states;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.LexerContext;

public interface State {
    /**
     * Handles the current state of the automata, performs the corresponding operations,
     * and returns the next State of the automata.
     *
     * @param codePoint The current unicode code point (character)
     * @param ctx The current Lexer context object
     * @return The next state of the automata
     */
    State handle(int codePoint, LexerContext ctx) throws LexicalException;
}

