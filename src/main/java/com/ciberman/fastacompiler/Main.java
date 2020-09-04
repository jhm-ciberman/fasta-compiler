package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.lexer.AutomataLexer;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Lexer lexer = new AutomataLexer("./input/test.fasta");

        try {
            Token token = lexer.getNextToken();


            while (token.getType() != TokenType.EOF) {
                System.out.println(token);

                token = lexer.getNextToken();
            }

        } catch (LexicalException e) {
            e.printStackTrace();
        }
    }
}
