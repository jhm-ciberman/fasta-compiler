package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.InputSource;

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
     * Yields the passed token and returns the finish state
     * @param token The token to yield
     */
    void yieldToken(Token token);

    /**
     *
     * @return The current value in the temp buffer
     */
    String value();

    /**
     * Overrides the value stored in the temp buffer
     * @param value The value to override in the temp buffer
     */
    void setValue(String value);

    /**
     * @return The current line number
     */
    int line();

    /**
     * @return The current column number
     */
    int col();

    /**
     * @return The current input source
     */
    InputSource getInputSource();
}
