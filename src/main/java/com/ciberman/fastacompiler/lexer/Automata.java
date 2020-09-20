package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.errors.LexicalException;

public interface Automata {

    void reset();

    void advance(int currentCodePoint, LexerContext ctx) throws LexicalException;
}
