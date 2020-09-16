
%{
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;
import java.io.IOException;
%}

/* YACC Declarations */
// %token EOF = 0
%token ID

%token PLUS     // "+"
%token MINUS    // "-"
%token MULTIPLY // "*"
%token DIVISION // "/"

%token GTE      // ">="
%token LTE      // "<="
%token LT       // ">"
%token GT       // "<"
%token EQ       // "=="
%token NOTEQ    // "<>"

%token ASSIGN   // "="
%token COLON    // ":"
%token SEMI     // ";"
%token LPAREN   // "("
%token RPAREN   // ")"

%token INT      // Literal Int (ex: 123_i)
%token LONG     // Literal Long (ex: 123_l)
%token STR      // Literal Str (ex; 'abc')

// Keywords
%token IF
%token THEN
%token ELSE
%token ENDIF
%token PRINT
%token BEGIN
%token END
%token TYPE_INT
%token TYPE_LONG
%token LOOP
%token UNTIL
%token ITOL


%%

additiveExpression
	: term PLUS term           { System.out.println("term PLUS term"); }
	| term MINUS term          { System.out.println("term MINUS term"); }
	| term                     { System.out.println("term"); }

term
	: factor MULTIPLY factor   { System.out.println("factor MULTIPLY factor"); }
	| factor DIVISION factor   { System.out.println("factor DIVISION factor"); }
	| factor                   { System.out.println("factor"); }

factor
	: INT                      { System.out.println("INT"); }
	| LONG                     { System.out.println("LONG"); }
	| PLUS factor              { System.out.println("PLUS factor"); }
	| MINUS factor             { System.out.println("PLUS factor"); }
	;

%%

protected Lexer lexer;

protected Token currentToken;

public Parser(Lexer lexer, boolean debugMode) {
	this.lexer = lexer;
	this.yydebug = debugMode;
}

public Parser(Lexer lexer) {
	this(lexer, false);
}

protected void yyerror(String s) throws SyntaxException {
	throw new SyntaxException(this.currentToken, this.lexer.fileName(), s);
}

protected int yylex() throws IOException, LexicalException {
	this.currentToken = this.lexer.getNextToken();
	String value = this.currentToken.getValue();
	this.yylval = (value == null) ? null : new ParserVal(value);
	return this.currentToken.getType().code();
}

public void parse() throws IOException, LexicalException, SyntaxException {
	this.yyparse();
}