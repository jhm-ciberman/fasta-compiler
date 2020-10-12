package com.ciberman.fastacompiler.lexer;

public class MatrixLexerTest extends LexerTest{
    public Automata makeAutomata() {
        return new MatrixAutomata();
    }
}