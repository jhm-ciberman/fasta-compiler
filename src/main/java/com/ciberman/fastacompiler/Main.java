package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.AutomataLexer;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws IOException {

        Lexer lexer;
        if (args.length > 0) {
            lexer = new AutomataLexer(new FileInputStream(args[0]), args[0]);
        } else {
            String name = "test.fasta";
            InputStream stream = Main.class.getClassLoader().getResourceAsStream(name);
            lexer = new AutomataLexer(stream, name);
        }

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
