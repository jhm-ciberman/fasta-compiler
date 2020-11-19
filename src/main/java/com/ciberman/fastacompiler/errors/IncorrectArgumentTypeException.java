package com.ciberman.fastacompiler.errors;

import com.ciberman.fastacompiler.ir.ValueType;
import com.ciberman.fastacompiler.lexer.Token;

public class IncorrectArgumentTypeException extends SemanticException {

    public IncorrectArgumentTypeException(Token token, ValueType type1, ValueType type2) {
        super(token, getDescription(token, type1, type2));
    }

    private static String getDescription(Token token, ValueType type1, ValueType type2) {
        String t1 = type1.toEnglish();
        String t2 = type2.toEnglish();
        switch (token.getType()) {
            case PLUS:     return "Cannot add " + t1 + " to " + t2 + ". Types must match.";
            case MINUS:    return "Cannot subtract " + t2 + " from a " + t1 + ". Types must match.";
            case MULTIPLY: return "Cannot multiply " + t1 + " and " + t2 + " together. Types must match.";
            case DIVISION: return "Cannot divide " + t1 + " and " + t2 + ". Types must match.";

            case GTE:
            case LTE:
            case LT:
            case GT:
            case EQ:
            case NOTEQ:
            case ASSIGN:   return "Cannot compare " + t1 + " and " + t2 + ". Types must match.";

            default:       return "Types " + type1 + " and " + type2 + " are incompatible.";
        }
    }
}
