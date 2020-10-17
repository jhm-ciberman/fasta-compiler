package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.lexer.Token;

public class UndeclaredVariableException extends SemanticException {

    public UndeclaredVariableException(Token token) {
        super(token, "Attempting to reference an Undeclared variable \"" + token.getValue() + "\".");
    }
}
