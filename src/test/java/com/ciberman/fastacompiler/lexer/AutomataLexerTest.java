package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.errors.LexicalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

class AutomataLexerTest {

    Lexer makeLexer(String inputString) {
        return new AutomataLexer(new StringReader(inputString));
    }

    void assertToken(String inputString, TokenType tokenType) throws IOException, LexicalException {
        Token token = this.makeLexer(inputString).getNextToken();

        Assertions.assertEquals(tokenType, token.getType());
    }

    void assertTokenWithValue(String inputString, TokenType tokenType, String value) throws IOException, LexicalException {
        Token token = this.makeLexer(inputString).getNextToken();

        Assertions.assertEquals(tokenType, token.getType());
        Assertions.assertEquals(value, token.getValue());
    }

    @Test
    void shouldParseEndOfFile() throws IOException, LexicalException {
        this.assertToken("", TokenType.EOF);
    }

    @Test
    void shouldParseValidInt() throws IOException, LexicalException {
        this.assertTokenWithValue("2256_i", TokenType.INT, "2256");
    }

    @Test
    void shouldThrowOnInvalidDeclaredIntegerLiteral() {
        LexicalException exception = Assertions.assertThrows(LexicalException.class, () -> {
            // "j" is an invalid literal type
            this.makeLexer("32768_j").getNextToken();
        });
    }

    @Test
    void shouldThrowOnIntegerLiteralWithoutType() {
        LexicalException exception = Assertions.assertThrows(LexicalException.class, () -> {
            // Should expect a "_", not a "+"
            this.makeLexer("32768+1").getNextToken();
        });
    }

    @Test
    void shouldThrowOnInvalidInt() {
        LexicalException exception = Assertions.assertThrows(LexicalException.class, () -> {
            // 16 bit Int range is −32768 to 32767
            this.makeLexer("32768_i").getNextToken();
        });
    }

    @Test
    void shouldParseValidLong() throws IOException, LexicalException {
        this.assertTokenWithValue("512_l", TokenType.LONG, "512");
    }

    @Test
    void shouldThrowOnInvalidLong() {
        LexicalException exception = Assertions.assertThrows(LexicalException.class, () -> {
            // 32 bit Int range is -2147483648 to 2147483647
            this.makeLexer("2147483648_i").getNextToken();
        });
    }

    @Test
    void shouldParseBasicArithmeticOperators() throws IOException, LexicalException {
        this.assertToken("+", TokenType.PLUS);
        this.assertToken("-", TokenType.MINUS);
        this.assertToken("*", TokenType.MULTIPLY);
        this.assertToken("/", TokenType.DIVISION);
        this.assertToken("=", TokenType.ASSIGN);
    }

    @Test
    void shouldParseParens() throws IOException, LexicalException {
        this.assertToken("(", TokenType.LPAREN);
        this.assertToken(")", TokenType.RPAREN);
    }

    @Test
    void shouldParseBraces() throws IOException, LexicalException {
        this.assertToken("{", TokenType.LBRACE);
        this.assertToken("}", TokenType.RBRACE);
    }

    @Test
    void shouldParseColon() throws IOException, LexicalException {
        this.assertToken(",", TokenType.COLON);
    }

    @Test
    void shouldParseSemicolon() throws IOException, LexicalException {
        this.assertToken(";", TokenType.SEMI);
    }

    @Test
    void shouldParseLogicOperators() throws IOException, LexicalException {
        this.assertToken(">", TokenType.GT);
        this.assertToken(">=", TokenType.GTE);
        this.assertToken("<", TokenType.LT);
        this.assertToken("<=", TokenType.LTE);
        this.assertToken("<>", TokenType.NOTEQ);
        this.assertToken("==", TokenType.EQ);
    }

    @Test
    void shouldParseKeywords() throws IOException, LexicalException {
        this.assertToken("IF", TokenType.IF);
        this.assertToken("THEN", TokenType.THEN);
        this.assertToken("ELSE", TokenType.ELSE);
        this.assertToken("ENDIF", TokenType.ENDIF);
        this.assertToken("PRINT", TokenType.PRINT);
        this.assertToken("BEGIN", TokenType.BEGIN);
        this.assertToken("END", TokenType.END);
        this.assertToken("INT", TokenType.TYPE_INT);
        this.assertToken("LONG", TokenType.TYPE_LONG);
        this.assertToken("LOOP", TokenType.LOOP);
        this.assertToken("UNTIL", TokenType.UNTIL);
        this.assertToken("ITOL", TokenType.ITOL);
    }

    @Test
    void shouldThrowOnInvalidKeyword() {
        LexicalException exception = Assertions.assertThrows(LexicalException.class, () -> {
            this.makeLexer("INVALIDKEYWORD").getNextToken();
        });
    }

    @Test
    void shouldParseValidIds() throws IOException, LexicalException {
        this.assertTokenWithValue("a", TokenType.ID, "a");
        this.assertTokenWithValue("foo123", TokenType.ID, "foo123");
        this.assertTokenWithValue("f_o_o", TokenType.ID, "f_o_o");
        this.assertTokenWithValue("_", TokenType.ID, "_");
    }

    @Test
    void shouldTrimIdsExceding20Characters() throws IOException, LexicalException {
        this.assertTokenWithValue("abcd_abcd_abcd_abcd_abcd_", TokenType.ID, "abcd_abcd_abcd_abcd_");
    }

    @Test
    void shouldParseStrings() throws IOException, LexicalException {
        this.assertTokenWithValue("''", TokenType.STR, "");
        this.assertTokenWithValue("'a'", TokenType.STR, "a");
        this.assertTokenWithValue("'Lorem Ipsum Dolor Sit Amet'", TokenType.STR, "Lorem Ipsum Dolor Sit Amet");
        this.assertTokenWithValue("'a\nb\nc\n'", TokenType.STR, "abc");
        this.assertTokenWithValue("'abc\n'", TokenType.STR, "abc");
        this.assertTokenWithValue("'\nabc\n'", TokenType.STR, "abc");
    }

    @Test
    void shouldSkipComments() throws IOException, LexicalException {
        this.assertToken("// this is a comment\nINT", TokenType.TYPE_INT);
        this.assertToken("# this is a multi\nline\ncomment#INT", TokenType.TYPE_INT);
    }
}