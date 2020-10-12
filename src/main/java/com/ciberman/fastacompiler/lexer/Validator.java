package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.Fasta;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.LexicalWarning;

public class Validator {

    /**
     * Validates the current long value stored in the temp buffer
     * @param ctx The lexer context
     * @return The correct value to use
     * @throws LexicalException In case of an out of range value
     */
    public static String validateLong(LexerContext ctx) throws LexicalException {
        String value = ctx.value();
        try {
            if (Long.parseLong(value) > 2147483647) {
                throw new LexicalException(ctx, "Long value " + value + " out of range");
            }
        } catch (NumberFormatException e) {
            throw new LexicalException(ctx, "Long value " + value + " out of range");
        }
        return value;
    }

    /**
     * Validates the current int value stored in the temp buffer
     * @param ctx The lexer context
     * @return The correct value to use
     * @throws LexicalException In case of an out of range value
     */
    public static String validateInt(LexerContext ctx) throws LexicalException {
        String value = ctx.value();
        try {
            if (Integer.parseInt(value) > 32767) {
                throw new LexicalException(ctx, "Integer value " + value + " out of range");
            }

        } catch (NumberFormatException e) {
            throw new LexicalException(ctx, "Integer value " + value + " out of range");
        }
        return value;
    }

    /**
     * Validates the current identifier value stored in the temp buffer
     * @param ctx The lexer context
     * @return The correct value to use
     */
    public static String validateId(LexerContext ctx) {
        String value = ctx.value();

        if (value.length() > 20) {
            value = value.substring(0, 20);
            ctx.setValue(value);
            Fasta.warn(new LexicalWarning(ctx, "The identifier name \""+ ctx.value() + "\" is larger than 20 characters. Using \"" + value + "\"."));
        }

        return value;
    }

    /**
     * Validates the current reserved keyword value stored in the temp buffer
     * @param ctx The lexer context
     * @return The correct TokenType to use
     * @throws LexicalException In case of an invalid reserved keyword
     */
    public static TokenType validateReserved(LexerContext ctx) throws LexicalException {
        TokenType type;
        switch (ctx.value()) {
            case "IF":
                type = TokenType.IF;
                break;
            case "THEN":
                type = TokenType.THEN;
                break;
            case "ELSE":
                type = TokenType.ELSE;
                break;
            case "ENDIF":
                type = TokenType.ENDIF;
                break;
            case "PRINT":
                type = TokenType.PRINT;
                break;
            case "BEGIN":
                type = TokenType.BEGIN;
                break;
            case "END":
                type = TokenType.END;
                break;
            case "INT":
                type = TokenType.TYPE_INT;
                break;
            case "LONG":
                type = TokenType.TYPE_LONG;
                break;
            case "LOOP":
                type = TokenType.LOOP;
                break;
            case "UNTIL":
                type = TokenType.UNTIL;
                break;
            case "ITOL":
                type = TokenType.ITOL;
                break;
            default:
                throw new LexicalException(ctx, "Invalid keyword \"" + ctx.value() + "\".");
        }

        return type;
    }
}
