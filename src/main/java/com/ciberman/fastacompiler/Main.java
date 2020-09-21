package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.parser.Parser;

import java.io.IOException;

public class Main {




    public static void main(String[] args) throws IOException, FastaException {

        FastaCliParser fastaCliParser = new FastaCliParser();
        FastaCliParser.FastaInputCommand input = fastaCliParser.parse(args);

        System.out.println("File: " + input.fileName);

        Lexer lexer = new Lexer(input.automataImpl, input.stream, input.fileName);
        Parser parser = new Parser(lexer, false);
        try {
            parser.parse();
        } catch (LexicalException exception) {
            exception.printStackTrace();
        } catch (SyntaxException e) {
            e.printStackTrace();
        }

        SymbolTable symbolTable = lexer.getSymbolTable();
        System.out.println("=> Symbol Table: ");
        for (Symbol symbol : symbolTable.all()) {
            System.out.println(symbol.toString());
        }
    }
}
