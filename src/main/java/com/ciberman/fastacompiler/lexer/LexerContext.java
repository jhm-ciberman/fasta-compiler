package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.Symbol;
import com.ciberman.fastacompiler.errors.LexicalWarning;
import com.ciberman.fastacompiler.lexer.functional.State;

public interface LexerContext {

    /**
     * Saves the current symbol (Unicode code point) to the temp buffer
     * @return Itself for fluent chaining
     */
    LexerContext save();

    /**
     * Returns the current symbol (Unicode code point) to the input stream without consuming it
     * @return Itself for fluent chaining
     */
    LexerContext peek();

    /**
     * Returns the state with the specified state number
     * @param stateNumber The state number (zero is the starting state)
     * @return The specified state
     */
    State goToState(int stateNumber);

    /**
     * @see #goToState
     */
    State andGoToState(int stateNumber);

    /**
     * Yields the passed token and returns the finish state
     * @param token The token to yield
     * @return The finish state
     */
    State yieldToken(Token token);

    /**
     * @see #yieldToken
     */
    State andYieldToken(Token token);

    /**
     *
     * @return The current value in the temp buffer
     */
    String value();

    /**
     * @return The current line number
     */
    int line();

    /**
     * @return The current column number
     */
    int col();

    /**
     * @return The current file name
     */
    String fileName();

    /**
     * Reports a warning
     * @param warning The warning
     * @return Itself for fluent chaining
     */
    LexerContext warning(LexicalWarning warning);

    /**
     * Add symbol to symbol table
     */
    LexerContext addSymbol(Symbol symbol);
}
