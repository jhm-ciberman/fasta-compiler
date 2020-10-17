package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.ir.ValueType;
import com.ciberman.fastacompiler.lexer.Token;

public class IncorrectArgumentTypeException extends SemanticException {

    public IncorrectArgumentTypeException(Token token, ValueType type1, ValueType type2) {
        super(token, "Types " + type1 + " and " + type2 + " are incompatible.");
    }
}
