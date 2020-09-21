package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.ConsoleErrorHandler;
import com.ciberman.fastacompiler.ErrorHandler;
import com.ciberman.fastacompiler.Symbol;
import com.ciberman.fastacompiler.SymbolTable;
import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.LexicalWarning;

import java.io.*;

public class Lexer implements LexerContext {

    private final Automata automata;

    private final Reader reader;

    private final StringBuilder currentValue = new StringBuilder(20);

    private Token token = null;

    private final String fileName;

    private int lineNumber = 1;

    private int colNumber = 0;

    private int currentCodePoint;

    private boolean ignoreSubsequentErrors;

    public ErrorHandler errorHandler = new ConsoleErrorHandler();

    private final SymbolTable symbolTable = new SymbolTable();

    public Lexer(Automata automata, InputStream inputStream, String inputName) {
        this(automata, new InputStreamReader(inputStream), inputName);
    }

    public Lexer(Automata automata, Reader reader) {
        this(automata, reader, "");
    }

    public Lexer(Automata automata, Reader reader, String fileName) {
        this.automata = automata;
        this.fileName = fileName == null ? "" : fileName;
        this.reader = new BufferedReader(reader);
    }

    @Override
    public LexerContext save() {
        this.currentValue.appendCodePoint(this.currentCodePoint);
        return this;
    }

    @Override
    public LexerContext peek() {
        try {
            this.reader.reset();
        } catch (Exception e) {
            //
        }

        return this;
    }

    public void yieldToken(Token token) {
        this.token = token;
    }

    @Override
    public String value() {
        return this.currentValue.toString();
    }

    @Override
    public void setValue(String value) {
        this.currentValue.setLength(0);
        this.currentValue.append(value);
    }

    @Override
    public int line() {
        return this.lineNumber;
    }

    @Override
    public int col() {
        return this.colNumber;
    }

    /**
     * @return The current file name
     */
    @Override
    public String fileName() {
        return this.fileName;
    }

    @Override
    public LexerContext warning(LexicalWarning warning) {
        this.errorHandler.warn(warning);
        return this;
    }

    @Override
    public LexerContext addSymbol(Symbol symbol) {
        this.symbolTable.add(symbol);
        return this;
    }

    /**
     * Resets the lexer to the initial state.
     */
    private void reset() {
        this.automata.reset();
        this.currentValue.setLength(0); // Clear the string builder. It's faster than allocating a new one
        this.token = null;
    }

    /**
     * Returns the next token in the input stream
     * @return THe token
     * @throws IOException If some IO operation has an error
     */
    public Token getNextToken() throws IOException, LexicalException {

        this.reset();
        this.ignoreSubsequentErrors = false;

        while (this.token == null) {

            int codePoint = this.readNextCodePoint();

            try {
                // Handle current state
                this.automata.advance(codePoint, this);

            } catch (LexicalException exception) {
                this.handleError(exception);
            }
        }

        return this.token;
    }

    private void handleError(LexicalException exception) {
        if (exception.getLevel() == FastaException.ErrorLevel.ERROR) {
            if (! this.ignoreSubsequentErrors) {
                // Report error to the error handler
                this.errorHandler.error(exception);

                // Try to recover
                this.reset();

                this.ignoreSubsequentErrors = true;
            }
        } else {
            this.errorHandler.warn(exception);
        }
    }

    /**
     * @return The symbol table
     */
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    /**
     * Reads the next unicode code point in the input stream and update the line
     * and column number.
     * @return The read code point or -1 if end of file
     * @throws IOException In case of an IO problem
     */
    private int readNextCodePoint() throws IOException {
        this.reader.mark(1);
        this.currentCodePoint = this.reader.read();

        if (this.currentCodePoint == '\n') {
            this.lineNumber++;
            this.colNumber = 1;
        } else if (this.currentCodePoint != 0x0D) { // Carriage return
            this.colNumber++;
        }

        return this.currentCodePoint;
    }

}
