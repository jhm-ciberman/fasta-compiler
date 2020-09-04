package com.ciberman.fastacompiler.lexer;

public enum TokenType {
    EOF,
    ID,

    PLUS,     // "+"
    MINUS,    // "-"
    MULTIPLY, // "*"
    DIVISION, // "/"

    GTE,      // ">="
    LTE,      // "<="
    LT,       // ">"
    GT,       // "<"
    EQ,       // "=="
    NOTEQ,    // "<>"

    ASSIGN,   // "="
    COLON,    // ":"
    SEMI,     // ";"
    LPAREN,   // "("
    RPAREN,   // ")"

    INT,      // Literal Int (ex: 123_i)
    LONG,     // Literal Long (ex: 123_l)
    STR,      // Literal Str (ex; 'abc')

    // Keywords
    IF,
    THEN,
    ELSE,
    ENDIF,
    PRINT,
    BEGIN,
    END,
    TYPE_INT,
    TYPE_LONG,
    LOOP,
    UNTIL,
    ITOL;

    public int code() {
        return this == TokenType.EOF ? 0 : this.ordinal() + 255;
    }
}
