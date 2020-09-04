package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.errors.LexicalException;

import java.io.IOException;

public interface Lexer {

    /**
     * Returns the next token in the input stream
     * @return THe token
     * @throws IOException If some IO operation has an error
     */
    Token getNextToken() throws IOException, LexicalException;
}
