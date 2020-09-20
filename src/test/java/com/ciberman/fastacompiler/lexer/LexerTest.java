package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.errors.LexicalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;

class LexerTest {

    static abstract class TestAutomata {

        private final Automata automata;

        public TestAutomata(Automata automata) {
            this.automata = automata;
        }

        public Lexer makeLexer(String inputString) {
            return new Lexer(this.automata, new StringReader(inputString));
        }

        public void assertToken(String inputString, TokenType tokenType) throws IOException, LexicalException {
            Token token = this.makeLexer(inputString).getNextToken();
            Assertions.assertEquals(tokenType, token.getType());
        }

        public void assertTokenWithValue(String inputString, TokenType tokenType, String value) throws IOException, LexicalException {
            Token token = this.makeLexer(inputString).getNextToken();

            Assertions.assertEquals(tokenType, token.getType());
            Assertions.assertEquals(value, token.getValue());
        }
    }

    static class FunctionalTestAutomata extends TestAutomata {
        public FunctionalTestAutomata() {
            super(new FunctionalAutomata());
        }
    }

    static class MatrixTestAutomata extends TestAutomata {
        public MatrixTestAutomata() {
            super(new MatrixAutomata());
        }
    }

    public static Stream<TestAutomata> getAutomata() {
        return Stream.of(new FunctionalTestAutomata(), new MatrixTestAutomata());
    }


    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseEndOfFile(TestAutomata automata) throws IOException, LexicalException {
        automata.assertToken("", TokenType.EOF);
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseValidInt(TestAutomata automata) throws IOException, LexicalException {
        automata.assertTokenWithValue("2256_i", TokenType.INT, "2256");
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldThrowOnInvalidDeclaredIntegerLiteral(TestAutomata automata) {
        LexicalException exception = Assertions.assertThrows(LexicalException.class, () -> {
            // "j" is an invalid literal type
            automata.makeLexer("32768_j").getNextToken();
        });
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldThrowOnIntegerLiteralWithoutType(TestAutomata automata) {
        LexicalException exception = Assertions.assertThrows(LexicalException.class, () -> {
            // Should expect a "_", not a "+"
            automata.makeLexer("32768+1").getNextToken();
        });
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldThrowOnInvalidInt(TestAutomata automata) {
        LexicalException exception = Assertions.assertThrows(LexicalException.class, () -> {
            // 16 bit Int range is −32768 to 32767
            automata.makeLexer("32768_i").getNextToken();
        });
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseValidLong(TestAutomata automata) throws IOException, LexicalException {
        automata.assertTokenWithValue("512_l", TokenType.LONG, "512");
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldThrowOnInvalidLong(TestAutomata automata) {
        LexicalException exception = Assertions.assertThrows(LexicalException.class, () -> {
            // 32 bit Int range is -2147483648 to 2147483647
            automata.makeLexer("2147483648_i").getNextToken();
        });
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseBasicArithmeticOperators(TestAutomata automata) throws IOException, LexicalException {
        automata.assertToken("+", TokenType.PLUS);
        automata.assertToken("-", TokenType.MINUS);
        automata.assertToken("*", TokenType.MULTIPLY);
        automata.assertToken("/", TokenType.DIVISION);
        automata.assertToken("=", TokenType.ASSIGN);
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseParens(TestAutomata automata) throws IOException, LexicalException {
        automata.assertToken("(", TokenType.LPAREN);
        automata.assertToken(")", TokenType.RPAREN);
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseBraces(TestAutomata automata) throws IOException, LexicalException {
        automata.assertToken("{", TokenType.LBRACE);
        automata.assertToken("}", TokenType.RBRACE);
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseComma(TestAutomata automata) throws IOException, LexicalException {
        automata.assertToken(",", TokenType.COMMA);
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseSemicolon(TestAutomata automata) throws IOException, LexicalException {
        automata.assertToken(";", TokenType.SEMI);
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseLogicOperators(TestAutomata automata) throws IOException, LexicalException {
        automata.assertToken(">", TokenType.GT);
        automata.assertToken(">=", TokenType.GTE);
        automata.assertToken("<", TokenType.LT);
        automata.assertToken("<=", TokenType.LTE);
        automata.assertToken("<>", TokenType.NOTEQ);
        automata.assertToken("==", TokenType.EQ);
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseKeywords(TestAutomata automata) throws IOException, LexicalException {
        automata.assertToken("IF", TokenType.IF);
        automata.assertToken("THEN", TokenType.THEN);
        automata.assertToken("ELSE", TokenType.ELSE);
        automata.assertToken("ENDIF", TokenType.ENDIF);
        automata.assertToken("PRINT", TokenType.PRINT);
        automata.assertToken("BEGIN", TokenType.BEGIN);
        automata.assertToken("END", TokenType.END);
        automata.assertToken("INT", TokenType.TYPE_INT);
        automata.assertToken("LONG", TokenType.TYPE_LONG);
        automata.assertToken("LOOP", TokenType.LOOP);
        automata.assertToken("UNTIL", TokenType.UNTIL);
        automata.assertToken("ITOL", TokenType.ITOL);
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldThrowOnInvalidKeyword(TestAutomata automata) {
        LexicalException exception = Assertions.assertThrows(LexicalException.class, () -> {
            automata.makeLexer("INVALIDKEYWORD").getNextToken();
        });
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseValidIds(TestAutomata automata) throws IOException, LexicalException {
        automata.assertTokenWithValue("a", TokenType.ID, "a");
        automata.assertTokenWithValue("foo123", TokenType.ID, "foo123");
        automata.assertTokenWithValue("f_o_o", TokenType.ID, "f_o_o");
        automata.assertTokenWithValue("_", TokenType.ID, "_");
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldTrimIdsExceding20Characters(TestAutomata automata) throws IOException, LexicalException {
        automata.assertTokenWithValue("abcd_abcd_abcd_abcd_abcd_", TokenType.ID, "abcd_abcd_abcd_abcd_");
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseStrings(TestAutomata automata) throws IOException, LexicalException {
        automata.assertTokenWithValue("''", TokenType.STR, "");
        automata.assertTokenWithValue("'a'", TokenType.STR, "a");
        automata.assertTokenWithValue("'Lorem Ipsum Dolor Sit Amet'", TokenType.STR, "Lorem Ipsum Dolor Sit Amet");
        automata.assertTokenWithValue("'a\nb\nc\n'", TokenType.STR, "abc");
        automata.assertTokenWithValue("'abc\n'", TokenType.STR, "abc");
        automata.assertTokenWithValue("'\nabc\n'", TokenType.STR, "abc");
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldSkipComments(TestAutomata automata) throws IOException, LexicalException {
        automata.assertToken("// this is a comment\nINT", TokenType.TYPE_INT);
        automata.assertToken("# this is a multi\nline\ncomment#INT", TokenType.TYPE_INT);
    }

    @ParameterizedTest
    @MethodSource("getAutomata")
    void shouldParseParensInPrintStatements(TestAutomata automata) throws IOException, LexicalException {
        Lexer lexer = automata.makeLexer("PRINT('my string');");
        Assertions.assertEquals(TokenType.PRINT, lexer.getNextToken().getType());
        Assertions.assertEquals(TokenType.LPAREN, lexer.getNextToken().getType());
        Assertions.assertEquals(TokenType.STR, lexer.getNextToken().getType());
        Assertions.assertEquals(TokenType.RPAREN, lexer.getNextToken().getType());
        Assertions.assertEquals(TokenType.SEMI, lexer.getNextToken().getType());
    }
}