package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.asm.AsmGenerator;
import com.ciberman.fastacompiler.asm.MasmOutput;
import com.ciberman.fastacompiler.asm.program.AsmProgram;
import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.ir.IRProgram;
import com.ciberman.fastacompiler.lexer.BasicLexer;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.RecoveryLexer;
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

        IRProgram program = parser.parse();

        //ConsoleDebugOutput debugOutput = new ConsoleDebugOutput();
        //debugOutput.printProgram(program);
        //debugOutput.printSymbolTable(program);

        if (Fasta.getLogger().getErrorCount() == 0) {
            AsmGenerator asmGenerator = new AsmGenerator();
            AsmProgram asm = asmGenerator.generate(program);
            MasmOutput output = new MasmOutput();
            output.generate(asm, input.outputPath);
        }

        Fasta.getLogger().flush();
    }
}
