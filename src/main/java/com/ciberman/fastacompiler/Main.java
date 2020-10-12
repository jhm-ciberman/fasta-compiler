package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.lexer.BasicLexer;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.RecoveryLexer;
import com.ciberman.fastacompiler.parser.Parser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, FastaException {

        FastaCliParser fastaCliParser = new FastaCliParser();
        FastaCliParser.FastaInputCommand input = fastaCliParser.parse(args);

        System.out.println("File: " + input.fileName);

        Lexer lexer = new BasicLexer(input.automataImpl, input.stream, input.fileName);
        lexer = new RecoveryLexer(lexer);
        Parser parser = new Parser(lexer, null, false);
        try {
            parser.parse();
        } catch (LexicalException | SyntaxException exception) {
            exception.printStackTrace();
        }

        SymbolTable symbolTable = lexer.getSymbolTable();
        System.out.println("=> Symbol Table: ");
        for (Symbol symbol : symbolTable.all()) {
            System.out.println(symbol.toString());
        }
    }
}
