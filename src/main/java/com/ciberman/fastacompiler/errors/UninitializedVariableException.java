package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.lexer.Token;

public class UninitializedVariableException extends SemanticException {

    public UninitializedVariableException(Token token) {
        super(token, "The variable \"" + token.getValue() + "\" has not been initialized.");
    }
}
