package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.lexer.Automata;
import com.ciberman.fastacompiler.lexer.FunctionalAutomata;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.parser.Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws IOException, FastaException {

        Automata automata = new FunctionalAutomata();

        Lexer lexer;
        if (args.length > 0) {
            lexer = new Lexer(automata, new FileInputStream(args[0]), args[0]);
        } else {
            String name = "basic.fasta";
            InputStream stream = Main.class.getClassLoader().getResourceAsStream(name);
            lexer = new Lexer(automata, stream, name);
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
