package com.ciberman.fastacompiler.parser;

import com.ciberman.fastacompiler.InputSource;
import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.ir.IRProgram;
import com.ciberman.fastacompiler.lexer.FileLocation;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.lexer.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ParserTest {

    static class MockLexer implements Lexer {

        private int index = 0;
        private final Token[] tokens;

        public MockLexer(Token[] tokens) {
            this.tokens = tokens;
        }

        @Override
        public Token getNextToken() throws IOException, LexicalException {
            return this.tokens[this.index++];
        }

        @Override
        public String fileName() {
            return "myfile.fasta";
        }
    }

    static class FakeInputSource implements InputSource {
        @Override
        public String getFileName() {
            return "myfile.fasta";
        }
    }

    private final FileLocation loc = new FileLocation(new FakeInputSource(), 10, 10);

    @Test
    public void testParseEmptyProgram() throws IOException, FastaException {
        Lexer lexer = new MockLexer(new Token[] {
                new Token(loc, TokenType.ID, "my_program"),
                new Token(loc, TokenType.LBRACE),
                new Token(loc, TokenType.RBRACE),
                new Token(loc, TokenType.EOF),
        });
        Parser parser = new Parser(lexer);
        IRProgram program = parser.parse();
        Assertions.assertFalse(program.instructions().iterator().hasNext());
    }
}
