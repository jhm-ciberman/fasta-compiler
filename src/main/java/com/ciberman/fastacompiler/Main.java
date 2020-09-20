package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.lexer.functional.AutomataLexer;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.parser.Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws IOException, FastaException {
        Lexer lexer;
        if (args.length > 0) {
            lexer = new AutomataLexer(new FileInputStream(args[0]), args[0]);
        } else {
            String name = "basic.fasta";
            InputStream stream = Main.class.getClassLoader().getResourceAsStream(name);
            lexer = new AutomataLexer(stream, name);
        }

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
