package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.ConsoleErrorHandler;
import com.ciberman.fastacompiler.ErrorHandler;
import com.ciberman.fastacompiler.Symbol;
import com.ciberman.fastacompiler.SymbolTable;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.LexicalWarning;
import com.ciberman.fastacompiler.lexer.states.*;

import java.io.*;

public class AutomataLexer implements Lexer, LexerContext {

    private static final State[] states = new State[] {
            new StateS(),
            new State1(),
            new State2(),
            new State3(),
            new State4(),
            new State5(),
            new State6(),
            new State7(),
            new State8(),
            new State9(),
            new State10(),
            new State11(),
    };

    private final Reader reader;

    private final StringBuilder currentValue = new StringBuilder(20);

    private Token token = null;

    private final String fileName;
    private int lineNumber = 1;
    private int colNumber = 0;

    private int currentCodePoint;

    public ErrorHandler errorHandler = new ConsoleErrorHandler();

    private final SymbolTable symbolTable = new SymbolTable();

    public AutomataLexer(InputStream inputStream, String inputName) {
        this(new InputStreamReader(inputStream), inputName);
    }

    public AutomataLexer(Reader reader) {
        this(reader, "");
    }

    public AutomataLexer(Reader reader, String fileName) {
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

    @Override
    public State goToState(int stateNumber) {
        return AutomataLexer.states[stateNumber];
    }

    @Override
    public State andGoToState(int stateNumber) {
        return this.goToState(stateNumber);
    }

    @Override
    public State yieldToken(Token token) {
        this.token = token;
        return null;
    }

    @Override
    public State andYieldToken(Token token) {
        return this.yieldToken(token);
    }

    @Override
    public String value() {
        return this.currentValue.toString();
    }

    @Override
    public int line() {
        return this.lineNumber;
    }

    @Override
    public int col() {
        return this.colNumber;
    }

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



    @Override
    public Token getNextToken() throws IOException, LexicalException {

        State currentState = AutomataLexer.states[0]; // 0 = StateS

        this.currentValue.setLength(0); // Clear the string builder. It's faster than allocating a new one
        this.token = null;

        while (this.token == null) {

            int codePoint = this.readNextCodePoint();

            try {

                // Handle current state
                currentState = currentState.handle(codePoint, this);

            } catch (LexicalException exception) {
                if (exception.isCritical()) {
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
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    private int readNextCodePoint() throws IOException {
        this.currentCodePoint = this.reader.read();
        this.reader.mark(1);

        if (this.currentCodePoint == '\n') {
            this.lineNumber++;
            this.colNumber = 1;
        } else if (this.currentCodePoint != 0x0D) { // Carriage return
            this.colNumber++;
        }

        return this.currentCodePoint;
    }

}
