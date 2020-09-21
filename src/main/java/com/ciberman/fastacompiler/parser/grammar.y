
%{
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;
import java.io.IOException;
%}

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
%token COMMA    // ","
%token SEMI     // ";"
%token LBRACE   // "{"
%token RBRACE   // "}"
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

%start program
%%

/**
 * ----------------------------------------------------------------
 * Scopes
 * ----------------------------------------------------------------
 * Scopes can contain regular statements or another scope.
 * The main program is also a scope.
 */

program
	: scope                            { this.debugRule(); }

scope
	: ID LBRACE scope_body RBRACE      { this.debugRule(); }

scope_body
	: var_declaration_list scope_statement_list        { this.debugRule(); }
	| scope_statement_list                             { this.debugRule(); }

scope_statement_list
	: statement                        { this.debugRule(); }
	| statement scope_statement_list   { this.debugRule(); }
	| scope scope_statement_list       { this.debugRule(); }


/**
 * ----------------------------------------------------------------
 * Blocks
 * ----------------------------------------------------------------
 * Blocks cannot contain scopes. Only regular statements
 */

block
	: BEGIN block_statement_list END
	| statement

block_statement_list
	: statement                          { this.debugRule(); }
	| statement block_statement_list     { this.debugRule(); }

/**
 * ----------------------------------------------------------------
 * Variable declaration
 * ----------------------------------------------------------------
 * Variable declarations are only allowd at the begining
 * of a scope. Declarations must have a type (INT/LONG) followed
 * by a comma separated list of identifiers.
 *
 */

var_declaration_list
	: var_declaration                       { this.debugRule(); }
	| var_declaration var_declaration_list  { this.debugRule(); }

var_declaration
	: TYPE_INT var_list SEMI                { this.debugRule(); }
	| TYPE_LONG var_list SEMI               { this.debugRule(); }

var_list
	: ID                                    { this.debugRule(); }
	| ID COMMA var_list                     { this.debugRule(); }

/**
 * ----------------------------------------------------------------
 * Executable statements
 * ----------------------------------------------------------------
 * IF..THEN, IF..THEN..ELSE and LOOP..WHILE are all considered
 * executable constrol statements. Control statements cannot have
 * scopes inside, that's why they use the special non-terminal
 * "scope".
 *
 * Print and assignments are also considered executable statements.
 */

statement
	: SEMI                              { this.debugRule(); }
	| if_statement                      { this.debugRule(); }
        | loop_statement                    { this.debugRule(); }
        | print_statement                   { this.debugRule(); }
        | assign_statement                  { this.debugRule(); }

if_statement
	: IF relational_expr THEN block ENDIF             { this.debugRule(); }
	| IF relational_expr THEN block ELSE block ENDIF  { this.debugRule(); }
	// Error handling:
	| IF expr                                         { this.yyerror("IF condition should be enclosed in parenthesis. You should add the missing parenthesis in the condition. Example: IF (foo <= bar) THEN ..."); }
	| IF LPAREN expr relational_operator expr THEN    { this.yyerror("Unclosed parenthesis in IF condition. You should close the right parenthesis in the IF condition. Example: IF (foo <= bar) THEN ..."); }
	| IF LPAREN expr RPAREN                           { this.yyerror("IF does not have relational operator. You should add a valid relational operator. Example: IF (foo <= bar) THEN ..."); }

loop_statement
	: LOOP block UNTIL relational_expr                { this.debugRule(); }
	// Error handling:
	| LOOP block UNTIL expr                                         { this.yyerror("LOOP..WHILE condition should be enclosed in parenthesis. You should add the missing parenthesis in the condition. Example: LOOP .. WHILE (foo <= bar)"); }
	| LOOP block UNTIL LPAREN expr relational_operator expr THEN    { this.yyerror("Unclosed parenthesis in IF condition. You should close the right parenthesis in the IF condition. Example: LOOP .. WHILE (foo <= bar)"); }
	| LOOP block UNTIL LPAREN expr RPAREN                           { this.yyerror("LOOP..WHILE condition does not have relational operator. You should add a valid relational operator. Example: LOOP .. WHILE (foo <= bar)"); }

relational_expr
	:  LPAREN expr relational_operator expr RPAREN    { this.debugRule(); }

relational_operator
	: GTE                               { this.debugRule(); }
	| LTE                               { this.debugRule(); }
	| LT                                { this.debugRule(); }
	| GT                                { this.debugRule(); }
	| EQ                                { this.debugRule(); }
	| NOTEQ                             { this.debugRule(); }

assign_statement
	: ID ASSIGN expr SEMI               { this.debugRule(); }

print_statement
	: PRINT LPAREN STR RPAREN SEMI      { this.debugRule(); }

/**
 * ----------------------------------------------------------------
 * Expression
 * ----------------------------------------------------------------
 */

expr
	: term PLUS term                    { this.debugRule(); }
	| term MINUS term                   { this.debugRule(); }
	| term                              { this.debugRule(); }

term
	: factor MULTIPLY factor            { this.debugRule(); }
	| factor DIVISION factor            { this.debugRule(); }
	| factor                            { this.debugRule(); }

factor
	: ID                                { this.debugRule(); }
	| INT                               { this.debugRule(); }
	| LONG                              { this.debugRule(); }
	| PLUS factor                       { this.debugRule(); }
	| MINUS factor                      { this.debugRule(); }
	| ITOL LPAREN expr RPAREN           { this.debugRule(); }

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

protected void debugRule() {
	System.out.println(" => RULE        " + this.yyrule[this.yyn]);
}

protected void yyerror(String s) throws SyntaxException {
	throw new SyntaxException(this.currentToken, this.lexer.fileName(), s);
}

protected int yylex() throws IOException, LexicalException {
	this.currentToken = this.lexer.getNextToken();
	System.out.println("");
	System.out.println("Current token: " + this.currentToken);
	String value = this.currentToken.getValue();
	this.yylval = new ParserVal(value);
	return this.currentToken.getType().code();
}

public void parse() throws IOException, LexicalException, SyntaxException {
	this.yyparse();
}