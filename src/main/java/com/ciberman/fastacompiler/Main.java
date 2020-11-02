package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.ir.*;
import com.ciberman.fastacompiler.lexer.BasicLexer;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.RecoveryLexer;
import com.ciberman.fastacompiler.out.ConsoleDebugOutput;
import com.ciberman.fastacompiler.asm.IntelAsmOutput;
import com.ciberman.fastacompiler.parser.Parser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, FastaException {

        FastaCliParser fastaCliParser = new FastaCliParser();
        FastaCliParser.FastaCommandConfig input = fastaCliParser.parse(args);

        System.out.println("File: " + input.inputSource.getFileName());

        Lexer lexer = new BasicLexer(input.automataImpl, input.inputSource);
        lexer = new RecoveryLexer(lexer);
        Parser parser = new Parser(lexer, false);
        try {
            parser.parse();
        } catch (LexicalException | SyntaxException exception) {
            exception.printStackTrace();
        }

        IRProgram program = parser.buildProgram();

        ConsoleDebugOutput debugOutput = new ConsoleDebugOutput();
        debugOutput.printProgram(program);
        debugOutput.printSymbolTable(program);

        IntelAsmOutput masmOutput = new IntelAsmOutput();
        masmOutput.generate(program);

    }
}
