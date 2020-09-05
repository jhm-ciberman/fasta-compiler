package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.AutomataLexer;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String inputFile = (args.length > 0) ? args[0] : "./input/test.fasta";

        Lexer lexer = new AutomataLexer(inputFile);

        try {
            Token token = lexer.getNextToken();


            while (token.getType() != TokenType.EOF) {
                System.out.println(token);

                token = lexer.getNextToken();
            }

        } catch (LexicalException e) {
            e.printStackTrace();
        }

        SymbolTable symbolTable = lexer.getSymbolTable();
        System.out.println("=> Symbol Table: ");
        for (Symbol symbol : symbolTable.all()) {
            System.out.println(symbol.toString());
        }
    }
}
