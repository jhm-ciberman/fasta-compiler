package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.Fasta;
import com.ciberman.fastacompiler.SymbolTable;
import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;

import java.io.IOException;

public class RecoveryLexer implements Lexer {

    private final Lexer lexer;

    private boolean ignoreSubsequentErrors;

    public RecoveryLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    @Override
    public Token getNextToken() throws IOException, LexicalException {
        this.ignoreSubsequentErrors = false;

        while (true) {
            try {
                return this.lexer.getNextToken();
            } catch (LexicalException exception) {
                this.handleError(exception);
            }
        }
    }

    @Override
    public SymbolTable getSymbolTable() {
        return this.lexer.getSymbolTable();
    }

    @Override
    public String fileName() {
        return this.lexer.fileName();
    }

    protected void handleError(LexicalException exception) throws LexicalException {
        if (exception.getLevel() == FastaException.ErrorLevel.ERROR) {
            if (! this.ignoreSubsequentErrors) {
                // Report error to the error handler
                Fasta.error(exception);
                this.ignoreSubsequentErrors = true;
            }
        } else {
            Fasta.warn(exception);
        }
    }
}
