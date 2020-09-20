package com.ciberman.fastacompiler.lexer.matrix;
/*
import com.ciberman.fastacompiler.ConsoleErrorHandler;
import com.ciberman.fastacompiler.ErrorHandler;
import com.ciberman.fastacompiler.SymbolTable;
import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.LexerContext;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;
import com.ciberman.fastacompiler.lexer.functional.AutomataLexer;
import com.ciberman.fastacompiler.lexer.functional.State;
import com.sun.org.apache.xpath.internal.operations.String;

import java.io.*;

public class MatrixLexer implements Lexer {

    private static final int OTHER = 0;
    private static final int EOF = 1;
    private static final int TAB = 2;
    private static final int SPACE = 3;
    private static final int NL = 4;
    private static final int LOWERCASE_LETTERS_EXCEPT_I_AND_L = 5;
    private static final int LOWERCASE_LETTER_I = 6;
    private static final int LOWERCASE_LETTER_L = 7;
    private static final int UPPERCASE_LETTERS = 8;
    private static final int NUMBERS = 9;
    private static final int UNDERSCORE = 10;
    private static final int PLUS = 11;
    private static final int MINUS = 12;
    private static final int MULTIPLY = 13;
    private static final int DIVISION = 14;
    private static final int LBRACE = 15;
    private static final int RBRACE = 16;
    private static final int LPAREN = 17;
    private static final int RPAREN = 18;
    private static final int SEMI = 20;
    private static final int APOSTROPHE = 21;
    private static final int LESS_THAN = 22;
    private static final int GREATER_THAN = 23;
    private static final int EQUAL = 24;
    private static final int HASH = 25;
    private static final int COMMA = 26;

    private static final int NUMBER_OF_SYMBOLS = 26;

    private final Reader reader;

    private final StringBuilder currentValue = new StringBuilder(20);

    private Token token = null;

    private final String fileName;
    private int lineNumber = 1;
    private int colNumber = 0;

    public ErrorHandler errorHandler = new ConsoleErrorHandler();

    private final SymbolTable symbolTable = new SymbolTable();

    public MatrixLexer(InputStream inputStream, String inputName) {
        this(new InputStreamReader(inputStream), inputName);
    }

    public MatrixLexer(Reader reader) {
        this(reader, "");
    }

    public MatrixLexer(Reader reader, String fileName) {
        this.fileName = fileName == null ? "" : fileName;
        this.reader = new BufferedReader(reader);
    }

    private int[][] makeTransitionMatrix() {
        int[][]       trans = new int[10][NUMBER_OF_SYMBOLS];
        TokenType[][] token = new TokenType[10][NUMBER_OF_SYMBOLS];
        String[][]    error = new String[10][NUMBER_OF_SYMBOLS];

        // State "S"
        trans[0][OTHER]                            = 0;
        trans[0][EOF]                              = 0;
        trans[0][TAB]                              = 0;
        trans[0][SPACE]                            = 0;
        trans[0][NL]                               = 0;
        trans[0][LOWERCASE_LETTERS_EXCEPT_I_AND_L] = 0;
        trans[0][LOWERCASE_LETTER_I]               = 0;
        trans[0][LOWERCASE_LETTER_L]               = 0;
        trans[0][UPPERCASE_LETTERS]                = 0;
        trans[0][NUMBERS]                          = 0;
        trans[0][UNDERSCORE]                       = 0;
        trans[0][PLUS]                             = 0;
        trans[0][MINUS]                            = 0;
        trans[0][MULTIPLY]                         = 0;
        trans[0][DIVISION]                         = 0;
        trans[0][LBRACE]                           = 0;
        trans[0][RBRACE]                           = 0;
        trans[0][LPAREN]                           = 0;
        trans[0][RPAREN]                           = 0;
        trans[0][SEMI]                             = 0;
        trans[0][APOSTROPHE]                       = 0;
        trans[0][LESS_THAN]                        = 0;
        trans[0][GREATER_THAN]                     = 0;
        trans[0][EQUAL]                            = 0;
        trans[0][HASH]                             = 0;
        trans[0][COMMA]                            = 0;

        // State "1"

        // State "2"

        // State "3"


    }

    @Override
    public Token getNextToken() throws IOException, LexicalException {

        State currentState = MatrixLexer.states[0]; // 0 = StateS

        this.currentValue.setLength(0); // Clear the string builder. It's faster than allocating a new one
        this.token = null;

        while (this.token == null) {

            try {
                int currentCodePoint = this.readNextCodePoint();
                int symbol = mapCodePointToSimbol(currentCodePoint);

                // Handle current state
                //currentState = currentState.handle(codePoint, this);

            } catch (LexicalException exception) {
                if (exception.getLevel() == FastaException.ErrorLevel.ERROR) {
                    this.errorHandler.error(exception);
                    throw exception; // Rethrow critical exception!
                } else {
                    this.errorHandler.warn(exception);
                }
            }
        }

        return this.token;
    }

    @Override
    public String fileName() {
        return null;
    }

    @Override
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    private int readNextCodePoint() throws IOException {
        int currentCodePoint = this.reader.read();
        this.reader.mark(1);

        if (currentCodePoint == '\n') {
            this.lineNumber++;
            this.colNumber = 1;
        } else if (currentCodePoint != 0x0D) { // Carriage return
            this.colNumber++;
        }

        return currentCodePoint;
    }

    private int mapCodePointToSimbol(int codePoint) {

        if (Character.isLowerCase(codePoint)) {
            if (codePoint == 'i') {
                return LOWERCASE_LETTER_I;
            } else if (codePoint == 'l') {
                return LOWERCASE_LETTER_L;
            } else {
                return LOWERCASE_LETTERS_EXCEPT_I_AND_L;
            }
        }

        if (Character.isUpperCase(codePoint)) {
            return UPPERCASE_LETTERS;
        }

        if (Character.isDigit(codePoint)) {
            return NUMBERS;
        }

        switch (codePoint)
        {
            case '\t':
            case '\u000B':
                return TAB;
            case Character.SPACE_SEPARATOR:
            case Character.LINE_SEPARATOR:
            case Character.PARAGRAPH_SEPARATOR:
                return SPACE;
            case '\n':
                return NL;
            case -1:
                return EOF;
            case '+':
                return PLUS;
            case '-':
                return MINUS;
            case '*':
                return MULTIPLY;
            case '{':
                return LBRACE;
            case '}':
                return RBRACE;
            case '/':
                return DIVISION;
            case '#':
                return HASH;
            case '(':
                return LPAREN;
            case ')':
                return RPAREN;
            case ',':
                return COMMA;
            case ';':
                return SEMI;
            case '\'':
                return APOSTROPHE;
            case '<':
                return LESS_THAN;
            case '>':
                return GREATER_THAN;
            case '=':
                return EQUAL;
            case '_':
                return UNDERSCORE;
            default:
                return OTHER;
        }
    }

}
*/