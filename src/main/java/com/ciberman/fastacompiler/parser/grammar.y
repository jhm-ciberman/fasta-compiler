
%{
import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.ir.IRProgram;
import com.ciberman.fastacompiler.Fasta;
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
	: scope

scope
	: scope_start scope_body RBRACE      { this.exitScope(); }
	| scope_start RBRACE                 { this.exitScope(); }

scope_start
	: ID LBRACE                          { this.enterScope($1); }

scope_body
	: var_declaration_list scope_statement_list
	| scope_statement_list

scope_statement_list
	: statement scope_statement_list
	| scope scope_statement_list
	| statement
	| scope


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
	: statement block_statement_list
	| statement

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
	: var_declaration var_declaration_list
	| var_declaration

var_declaration
	: TYPE_INT var_list SEMI                { this.declareIntSymbols($2); }
	| TYPE_LONG var_list SEMI               { this.declareLongSymbols($2); }
	| TYPE_INT error SEMI
	| TYPE_LONG error SEMI

var_list
	: ID COMMA var_list                     { $$ = this.pushIdToList($3, $1); }
	| ID                                    { $$ = this.newIdList($1); }

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
	: SEMI
	| if_statement
        | loop_statement
        | print_statement
        | assign_statement
        | error SEMI

if_statement
	: IF if_condition if_body                                      { this.ifInst(); }
	| IF if_error_condition

if_error_condition
	: expr                                                          { this.errorIfWithoutParens(); }
	| LPAREN expr relational_operator expr THEN                     { this.errorIfUnclosedParens(); }
	| LPAREN expr RPAREN                                            { this.errorIfWithoutRelationalOperator(); }

if_body
	: THEN block ENDIF
	| THEN if_then_block else_body
	| THEN error ENDIF

else_body
	: ELSE block ENDIF
	| ELSE error ENDIF

if_then_block
	: block                                                         { this.ifThenBlock(); }

if_condition
	: bool_expr                                                     { this.ifCondition($1); }

loop_statement
	: loop_keyword block UNTIL loop_condition
	| loop_keyword block UNTIL loop_error_condition

loop_keyword
	: LOOP                                                          { this.loopKeyword(); }

loop_error_condition
	: expr                                                          { this.errorLoopWithoutParens(); }
	| LPAREN expr relational_operator expr THEN                     { this.errorLoopUnclosedParens(); }
	| LPAREN expr RPAREN                                            { this.errorLoopWithoutRelationalOperator(); }

loop_condition
	: bool_expr                                                     { this.loopCondition($1); }

bool_expr
	:  LPAREN expr relational_operator expr RPAREN                  { $$ = this.branchCondition($2, $3, $4); }

relational_operator
	: GTE                               { $$ = $1; }
	| LTE                               { $$ = $1; }
	| LT                                { $$ = $1; }
	| GT                                { $$ = $1; }
	| EQ                                { $$ = $1; }
	| NOTEQ                             { $$ = $1; }

assign_statement
	: ID ASSIGN expr SEMI               { $$ = this.assignOp($1, $2, $3); }

print_statement
	: PRINT LPAREN print_argument_list RPAREN SEMI

print_argument_list
	: print_argument
	| print_argument COMMA print_argument_list

print_argument
	: STR                               { this.printOp(this.strConst($1)); }
	| expr                              { this.printOp($1); }

/**
 * ----------------------------------------------------------------
 * Expression
 * ----------------------------------------------------------------
 */

expr
	: expr PLUS term                    { $$ = this.addOp($1, $2, $3); }
	| expr MINUS term                   { $$ = this.subOp($1, $2, $3); }
	| term                              { $$ = $1; }

term
	: term MULTIPLY factor              { $$ = this.mulOp($1, $2, $3); }
	| term DIVISION factor              { $$ = this.divOp($1, $2, $3); }
	| factor                            { $$ = $1; }

factor
	: ID                                { $$ = this.id($1); }
	| INT                               { $$ = this.intConst($1); }
	| LONG                              { $$ = this.longConst($1); }
	| PLUS factor                       { $$ = $2; }
	| MINUS factor                      { $$ = this.negOp($2); }
	| ITOL LPAREN expr RPAREN           { $$ = this.itolOp($1, $3); }

%%

public Parser(Lexer lexer, boolean debugMode) {
	super(lexer);
	this.yydebug = debugMode;
}

public Parser(Lexer lexer) {
	this(lexer, false);
}

protected void debugRule() {
	System.out.println(" => RULE        " + this.yyrule[this.yyn]);
}

protected void yyerror(String s) throws SyntaxException {
	this.error(s);
}

protected int yylex() throws IOException, LexicalException {
	Token token = this.next();
	this.yylval = new ParserVal(token);
	return token.getType().code();
}

public IRProgram parse() throws IOException, FastaException {
	this.yyparse();
	return this.getProgram();
}