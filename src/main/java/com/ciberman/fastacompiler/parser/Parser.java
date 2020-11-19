//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package com.ciberman.fastacompiler.parser;



//#line 3 "grammar.y"
import com.ciberman.fastacompiler.errors.FastaException;
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;
import com.ciberman.fastacompiler.ir.IRProgram;
import com.ciberman.fastacompiler.Fasta;
import java.io.IOException;
//#line 26 "Parser.java"




public class Parser
             extends BaseParser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short PLUS=258;
public final static short MINUS=259;
public final static short MULTIPLY=260;
public final static short DIVISION=261;
public final static short GTE=262;
public final static short LTE=263;
public final static short LT=264;
public final static short GT=265;
public final static short EQ=266;
public final static short NOTEQ=267;
public final static short ASSIGN=268;
public final static short COMMA=269;
public final static short SEMI=270;
public final static short LBRACE=271;
public final static short RBRACE=272;
public final static short LPAREN=273;
public final static short RPAREN=274;
public final static short INT=275;
public final static short LONG=276;
public final static short STR=277;
public final static short IF=278;
public final static short THEN=279;
public final static short ELSE=280;
public final static short ENDIF=281;
public final static short PRINT=282;
public final static short BEGIN=283;
public final static short END=284;
public final static short TYPE_INT=285;
public final static short TYPE_LONG=286;
public final static short LOOP=287;
public final static short UNTIL=288;
public final static short ITOL=289;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    3,    3,    5,    5,    5,    5,
    7,    7,    8,    8,    4,    4,    9,    9,    9,    9,
   10,   10,    6,    6,    6,    6,    6,    6,   11,   11,
   17,   17,   17,   16,   16,   16,   21,   21,   20,   15,
   12,   12,   23,   25,   25,   25,   24,   22,   19,   19,
   19,   19,   19,   19,   14,   13,   26,   26,   27,   27,
   18,   18,   18,   28,   28,   28,   29,   29,   29,   29,
   29,   29,
};
final static short yylen[] = {                            2,
    1,    3,    2,    2,    2,    1,    2,    2,    1,    1,
    3,    1,    2,    1,    2,    1,    3,    3,    3,    3,
    3,    1,    1,    1,    1,    1,    1,    2,    3,    2,
    1,    5,    3,    3,    3,    3,    3,    3,    1,    1,
    4,    4,    1,    1,    5,    3,    1,    5,    1,    1,
    1,    1,    1,    1,    4,    5,    1,    3,    1,    1,
    3,    3,    1,    3,    3,    1,    1,    1,    1,    2,
    2,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    1,    0,    4,    0,    0,   23,    3,    0,
    0,    0,    0,   43,    0,    0,    0,    6,    0,    0,
   24,   25,   26,   27,    0,   28,    0,   67,    0,    0,
    0,   68,   69,    0,    0,   30,    0,   40,    0,   66,
    0,    0,    0,    0,    0,    0,    8,    2,    5,    7,
   15,    0,    0,   12,    0,    0,   70,   71,    0,    0,
    0,   29,    0,    0,    0,    0,   59,    0,    0,    0,
   19,    0,   17,   20,   18,    0,    0,    0,   55,   49,
   50,   51,   52,   53,   54,   33,    0,    0,    0,    0,
    0,    0,    0,   64,   65,    0,    0,   21,   13,   11,
    0,    0,   47,   41,   42,    0,   72,   36,   34,    0,
   35,   56,   58,    0,   48,   32,    0,    0,   46,    0,
   38,   37,    0,   45,
};
final static short yydgoto[] = {                          2,
   15,    4,   16,   17,   18,   19,   55,   77,   20,   44,
   21,   22,   23,   24,   35,   62,   36,   68,   87,   91,
  111,   38,   25,  104,  105,   69,   70,   39,   40,
};
final static short yysindex[] = {                      -244,
 -253,    0,    0,  -46,    0, -242, -220,    0,    0,  -95,
 -250, -202, -181,    0,   -6, -237,   -6,    0,   -6, -196,
    0,    0,    0,    0,  -40,    0,  -67,    0,  -67,  -67,
  -67,    0,    0, -235, -219,    0, -161,    0, -148,    0,
  -75, -226, -198, -197, -187, -185,    0,    0,    0,    0,
    0, -176,   12,    0, -183, -239,    0,    0,   48,  -67,
  -22,    0,  -67,  -67,  -67,  -67,    0, -161, -166, -155,
    0, -137,    0,    0,    0,   12, -143,  -70,    0,    0,
    0,    0,    0,    0,    0,    0,  -67, -247, -267, -134,
 -127, -148, -148,    0,    0, -115,  -75,    0,    0,    0,
  -67, -161,    0,    0,    0, -233,    0,    0,    0,  -12,
    0,    0,    0,   61,    0,    0, -265, -125,    0,  -67,
    0,    0, -136,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -112,    0,    0,    0, -107,   22,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -248,    0, -200,    0,
    0,    0, -104,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -259,    0, -106,
    0,    0,    0,    0,    0, -110,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -108,
    0, -163, -130,    0,    0,    0,    0,    0,    0,    0,
    0, -111,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  175,    0,    0,  159,   30,  -24,  -57,  109,    0,  -11,
    0,    0,    0,    0,    0,    0,    0,  -10,   72,    0,
    0,  115,    0,    0,    0,   98,    0,   67,  -23,
};
final static int YYTABLESIZE=335;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         37,
   54,   46,   26,   90,   26,   57,   58,   31,   31,   60,
   63,   64,    1,  108,   60,  121,   56,    5,   63,   64,
   59,   31,   41,   31,   63,   64,  107,   26,   76,   31,
   79,   31,   31,   31,   48,   31,   54,   60,   31,   31,
  115,   94,   95,   71,   47,  116,   49,   27,   50,   88,
    5,   76,  118,   42,   43,   63,   63,   63,   63,   61,
   98,   63,   63,   63,   63,   63,   63,  102,   63,   63,
   72,   63,   73,   63,   45,   43,  106,   63,   63,   63,
   63,   63,   74,   63,   75,   54,   63,   63,   12,   13,
  114,   27,   61,   61,   61,   61,   63,   64,   61,   61,
   61,   61,   61,   61,   78,   61,   61,   96,   61,  123,
   61,   65,   66,   97,   61,   61,   61,   61,   61,   43,
   61,   63,   64,   61,   61,   62,   62,   62,   62,   92,
   93,   62,   62,   62,   62,   62,   62,  115,   62,   62,
  100,   62,  124,   62,   44,   44,  109,   62,   62,   62,
   62,   62,  110,   62,  112,  122,   62,   62,   44,   10,
   44,   28,   29,   30,    9,   22,   44,   57,   44,   44,
   44,   39,   44,   14,    3,   44,   44,   31,   51,   32,
   33,   28,   29,   30,   99,  120,   28,   29,   30,   28,
   29,   30,  103,   34,  113,    0,    0,    0,    0,   32,
   33,   67,  101,    0,   32,   33,    0,   32,   33,    6,
    7,    0,    0,   34,    0,    6,   52,    0,   34,    0,
    0,   34,    0,    8,    0,    9,    0,    0,    0,    8,
    0,   10,    0,   89,   52,   11,    0,   10,   12,   13,
   14,   11,   53,  117,   52,    0,   14,    8,    0,    6,
    7,    0,    0,    0,    0,   10,    0,    8,    0,   11,
   53,    0,    0,    8,   14,   10,    0,    6,   52,   11,
   53,   10,    0,    0,   14,   11,    0,   16,   16,    0,
   14,    8,    0,    0,    0,    0,    0,    0,    0,   10,
    0,   16,    0,   11,    0,    0,    0,    0,   14,   16,
    0,    0,    0,   16,    0,   63,   64,    0,   16,   80,
   81,   82,   83,   84,   85,    0,    0,    0,   63,   64,
    0,   86,   80,   81,   82,   83,   84,   85,    0,    0,
    0,    0,    0,    0,  119,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         10,
   25,   13,  270,   61,  270,   29,   30,  256,  257,  269,
  258,  259,  257,  281,  274,  281,   27,  271,  258,  259,
   31,  270,  273,  272,  258,  259,  274,  270,   53,  278,
  270,  280,  281,  282,  272,  284,   61,  273,  287,  288,
  274,   65,   66,  270,   15,  279,   17,  268,   19,   60,
  271,   76,  110,  256,  257,  256,  257,  258,  259,  279,
   72,  262,  263,  264,  265,  266,  267,   78,  269,  270,
  269,  272,  270,  274,  256,  257,   87,  278,  279,  280,
  281,  282,  270,  284,  270,  110,  287,  288,  285,  286,
  101,  268,  256,  257,  258,  259,  258,  259,  262,  263,
  264,  265,  266,  267,  288,  269,  270,  274,  272,  120,
  274,  260,  261,  269,  278,  279,  280,  281,  282,  257,
  284,  258,  259,  287,  288,  256,  257,  258,  259,   63,
   64,  262,  263,  264,  265,  266,  267,  274,  269,  270,
  284,  272,  279,  274,  256,  257,  281,  278,  279,  280,
  281,  282,  280,  284,  270,  281,  287,  288,  270,  272,
  272,  257,  258,  259,  272,  270,  278,  274,  280,  281,
  282,  280,  284,  284,    0,  287,  288,  273,   20,  275,
  276,  257,  258,  259,   76,  114,  257,  258,  259,  257,
  258,  259,   78,  289,   97,   -1,   -1,   -1,   -1,  275,
  276,  277,  273,   -1,  275,  276,   -1,  275,  276,  256,
  257,   -1,   -1,  289,   -1,  256,  257,   -1,  289,   -1,
   -1,  289,   -1,  270,   -1,  272,   -1,   -1,   -1,  270,
   -1,  278,   -1,  256,  257,  282,   -1,  278,  285,  286,
  287,  282,  283,  256,  257,   -1,  287,  270,   -1,  256,
  257,   -1,   -1,   -1,   -1,  278,   -1,  270,   -1,  282,
  283,   -1,   -1,  270,  287,  278,   -1,  256,  257,  282,
  283,  278,   -1,   -1,  287,  282,   -1,  256,  257,   -1,
  287,  270,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  278,
   -1,  270,   -1,  282,   -1,   -1,   -1,   -1,  287,  278,
   -1,   -1,   -1,  282,   -1,  258,  259,   -1,  287,  262,
  263,  264,  265,  266,  267,   -1,   -1,   -1,  258,  259,
   -1,  274,  262,  263,  264,  265,  266,  267,   -1,   -1,
   -1,   -1,   -1,   -1,  274,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=289;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"ID","PLUS","MINUS","MULTIPLY","DIVISION","GTE","LTE","LT","GT",
"EQ","NOTEQ","ASSIGN","COMMA","SEMI","LBRACE","RBRACE","LPAREN","RPAREN","INT",
"LONG","STR","IF","THEN","ELSE","ENDIF","PRINT","BEGIN","END","TYPE_INT",
"TYPE_LONG","LOOP","UNTIL","ITOL",
};
final static String yyrule[] = {
"$accept : program",
"program : scope",
"scope : scope_start scope_body RBRACE",
"scope : scope_start RBRACE",
"scope_start : ID LBRACE",
"scope_body : var_declaration_list scope_statement_list",
"scope_body : scope_statement_list",
"scope_statement_list : statement scope_statement_list",
"scope_statement_list : scope scope_statement_list",
"scope_statement_list : statement",
"scope_statement_list : scope",
"block : BEGIN block_statement_list END",
"block : statement",
"block_statement_list : statement block_statement_list",
"block_statement_list : statement",
"var_declaration_list : var_declaration var_declaration_list",
"var_declaration_list : var_declaration",
"var_declaration : TYPE_INT var_list SEMI",
"var_declaration : TYPE_LONG var_list SEMI",
"var_declaration : TYPE_INT error SEMI",
"var_declaration : TYPE_LONG error SEMI",
"var_list : ID COMMA var_list",
"var_list : ID",
"statement : SEMI",
"statement : if_statement",
"statement : loop_statement",
"statement : print_statement",
"statement : assign_statement",
"statement : error SEMI",
"if_statement : IF if_condition if_body",
"if_statement : IF if_error_condition",
"if_error_condition : expr",
"if_error_condition : LPAREN expr relational_operator expr THEN",
"if_error_condition : LPAREN expr RPAREN",
"if_body : THEN block ENDIF",
"if_body : THEN if_then_block else_body",
"if_body : THEN error ENDIF",
"else_body : ELSE block ENDIF",
"else_body : ELSE error ENDIF",
"if_then_block : block",
"if_condition : bool_expr",
"loop_statement : loop_keyword block UNTIL loop_condition",
"loop_statement : loop_keyword block UNTIL loop_error_condition",
"loop_keyword : LOOP",
"loop_error_condition : expr",
"loop_error_condition : LPAREN expr relational_operator expr THEN",
"loop_error_condition : LPAREN expr RPAREN",
"loop_condition : bool_expr",
"bool_expr : LPAREN expr relational_operator expr RPAREN",
"relational_operator : GTE",
"relational_operator : LTE",
"relational_operator : LT",
"relational_operator : GT",
"relational_operator : EQ",
"relational_operator : NOTEQ",
"assign_statement : ID ASSIGN expr SEMI",
"print_statement : PRINT LPAREN print_argument_list RPAREN SEMI",
"print_argument_list : print_argument",
"print_argument_list : print_argument COMMA print_argument_list",
"print_argument : STR",
"print_argument : expr",
"expr : expr PLUS term",
"expr : expr MINUS term",
"expr : term",
"term : term MULTIPLY factor",
"term : term DIVISION factor",
"term : factor",
"factor : ID",
"factor : INT",
"factor : LONG",
"factor : PLUS factor",
"factor : MINUS factor",
"factor : ITOL LPAREN expr RPAREN",
};

//#line 234 "grammar.y"

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
//#line 414 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
throws IOException, FastaException
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 2:
//#line 69 "grammar.y"
{ this.exitScope(); }
break;
case 3:
//#line 70 "grammar.y"
{ this.exitScope(); }
break;
case 4:
//#line 73 "grammar.y"
{ this.enterScope(val_peek(1)); }
break;
case 17:
//#line 116 "grammar.y"
{ this.declareIntSymbols(val_peek(1)); }
break;
case 18:
//#line 117 "grammar.y"
{ this.declareLongSymbols(val_peek(1)); }
break;
case 21:
//#line 122 "grammar.y"
{ yyval = this.pushIdToList(val_peek(0), val_peek(2)); }
break;
case 22:
//#line 123 "grammar.y"
{ yyval = this.newIdList(val_peek(0)); }
break;
case 29:
//#line 146 "grammar.y"
{ this.ifInst(); }
break;
case 31:
//#line 150 "grammar.y"
{ this.errorIfWithoutParens(); }
break;
case 32:
//#line 151 "grammar.y"
{ this.errorIfUnclosedParens(); }
break;
case 33:
//#line 152 "grammar.y"
{ this.errorIfWithoutRelationalOperator(); }
break;
case 39:
//#line 164 "grammar.y"
{ this.ifThenBlock(); }
break;
case 40:
//#line 167 "grammar.y"
{ this.ifCondition(val_peek(0)); }
break;
case 43:
//#line 174 "grammar.y"
{ this.loopKeyword(); }
break;
case 44:
//#line 177 "grammar.y"
{ this.errorLoopWithoutParens(); }
break;
case 45:
//#line 178 "grammar.y"
{ this.errorLoopUnclosedParens(); }
break;
case 46:
//#line 179 "grammar.y"
{ this.errorLoopWithoutRelationalOperator(); }
break;
case 47:
//#line 182 "grammar.y"
{ this.loopCondition(val_peek(0)); }
break;
case 48:
//#line 185 "grammar.y"
{ yyval = this.branchCondition(val_peek(3), val_peek(2), val_peek(1)); }
break;
case 49:
//#line 188 "grammar.y"
{ yyval = val_peek(0); }
break;
case 50:
//#line 189 "grammar.y"
{ yyval = val_peek(0); }
break;
case 51:
//#line 190 "grammar.y"
{ yyval = val_peek(0); }
break;
case 52:
//#line 191 "grammar.y"
{ yyval = val_peek(0); }
break;
case 53:
//#line 192 "grammar.y"
{ yyval = val_peek(0); }
break;
case 54:
//#line 193 "grammar.y"
{ yyval = val_peek(0); }
break;
case 55:
//#line 196 "grammar.y"
{ yyval = this.assignOp(val_peek(3), val_peek(2), val_peek(1)); }
break;
case 59:
//#line 206 "grammar.y"
{ this.printOp(this.strConst(val_peek(0))); }
break;
case 60:
//#line 207 "grammar.y"
{ this.printOp(val_peek(0)); }
break;
case 61:
//#line 216 "grammar.y"
{ yyval = this.addOp(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 62:
//#line 217 "grammar.y"
{ yyval = this.subOp(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 63:
//#line 218 "grammar.y"
{ yyval = val_peek(0); }
break;
case 64:
//#line 221 "grammar.y"
{ yyval = this.mulOp(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 65:
//#line 222 "grammar.y"
{ yyval = this.divOp(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 66:
//#line 223 "grammar.y"
{ yyval = val_peek(0); }
break;
case 67:
//#line 226 "grammar.y"
{ yyval = this.id(val_peek(0)); }
break;
case 68:
//#line 227 "grammar.y"
{ yyval = this.intConst(val_peek(0)); }
break;
case 69:
//#line 228 "grammar.y"
{ yyval = this.longConst(val_peek(0)); }
break;
case 70:
//#line 229 "grammar.y"
{ yyval = val_peek(0); }
break;
case 71:
//#line 230 "grammar.y"
{ yyval = this.negOp(val_peek(0)); }
break;
case 72:
//#line 231 "grammar.y"
{ yyval = this.itolOp(val_peek(3), val_peek(1)); }
break;
//#line 723 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
