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
import java.io.IOException;
//#line 24 "Parser.java"




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
    7,    7,    8,    8,    4,    4,    9,    9,   10,   10,
    6,    6,    6,    6,    6,   11,   11,   17,   17,   17,
   16,   16,   20,   15,   12,   12,   22,   24,   24,   24,
   23,   21,   19,   19,   19,   19,   19,   19,   14,   13,
   18,   18,   18,   25,   25,   25,   26,   26,   26,   26,
   26,   26,
};
final static short yylen[] = {                            2,
    1,    3,    2,    2,    2,    1,    1,    1,    2,    2,
    3,    1,    1,    2,    1,    2,    3,    3,    1,    3,
    1,    1,    1,    1,    1,    4,    2,    1,    5,    3,
    2,    4,    1,    1,    4,    4,    1,    1,    5,    3,
    1,    5,    1,    1,    1,    1,    1,    1,    4,    5,
    3,    3,    1,    3,    3,    1,    1,    1,    1,    2,
    2,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    1,    0,    4,    0,   21,    3,    0,    0,
    0,    0,   37,    0,    0,    0,    6,    0,    0,   22,
   23,   24,   25,    0,    0,   57,    0,    0,    0,   58,
   59,    0,    0,   27,    0,   34,    0,   56,    0,    0,
    0,    0,   10,    2,    5,    9,   16,    0,    0,   12,
    0,    0,   60,   61,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   17,   18,    0,    0,    0,   49,
   43,   44,   45,   46,   47,   48,   30,    0,    0,    0,
    0,   26,    0,    0,   54,   55,    0,   20,   14,   11,
    0,    0,   41,   35,   36,    0,   62,    0,   50,    0,
   42,   29,   32,   40,    0,    0,   39,
};
final static short yydgoto[] = {                          2,
   14,    4,   15,   16,   17,   18,   51,   68,   19,   41,
   20,   21,   22,   23,   33,   58,   34,   35,   78,   81,
   36,   24,   94,   95,   37,   38,
};
final static short yysindex[] = {                      -252,
 -254,    0,    0, -218,    0, -198,    0,    0, -243, -245,
 -232, -232,    0,  -52, -251,  -52,    0,  -52, -273,    0,
    0,    0,    0,  -71, -201,    0, -201, -201, -201,    0,
    0, -224, -228,    0, -187,    0, -165,    0, -215, -188,
 -181, -179,    0,    0,    0,    0,    0, -171,  -49,    0,
 -195, -211,    0,    0,  -16, -201,  -71, -178, -201, -201,
 -201, -201, -154, -232,    0,    0,  -49, -163, -239,    0,
    0,    0,    0,    0,    0,    0,    0, -201, -248,    0,
 -157,    0, -165, -165,    0,    0, -145,    0,    0,    0,
 -201, -187,    0,    0,    0, -250,    0,  -71,    0,   -3,
    0,    0,    0,    0, -201, -236,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -144,    0,    0,    0, -143,  -42,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -97,    0, -180,    0,    0, -135,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -147,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -175,
    0,    0, -148, -116,    0,    0,    0,    0,    0,    0,
    0,  -78,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  127,    0,    0,  119,   47,  -22,  -56,   77,    0,   -9,
    0,    0,    0,    0,    0,    0,    0,  -25,   45,    0,
   83,    0,    0,    0,   53,  -21,
};
final static int YYTABLESIZE=271;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         52,
   80,   50,   42,   55,    1,   53,   54,   59,   60,   59,
   60,   11,   12,   26,   27,   28,    5,   26,   27,   28,
   44,   59,   60,  101,   40,   97,   67,   39,  102,   29,
   79,   30,   31,   91,   50,   30,   31,  101,    6,   85,
   86,  103,  107,   92,   67,   32,   59,   60,   56,   32,
   57,    7,   96,    8,   88,   26,   27,   28,   70,    9,
   43,   63,   45,   10,   46,  100,   11,   12,   13,   25,
   59,   60,    5,   30,   31,   50,   53,   53,   53,  106,
   64,   53,   53,   53,   53,   53,   53,   32,   65,   53,
   66,   53,   69,   53,   61,   62,   25,   53,   53,   53,
   53,   53,   82,   53,   33,   31,   53,   53,   51,   51,
   51,   83,   84,   51,   51,   51,   51,   51,   51,   87,
   90,   51,   98,   51,   99,   51,    3,    8,    7,   51,
   51,   51,   51,   51,   19,   51,   13,   47,   51,   51,
   52,   52,   52,   89,  105,   52,   52,   52,   52,   52,
   52,   93,    0,   52,    0,   52,    0,   52,    0,   28,
    0,   52,   52,   52,   52,   52,    0,   52,    0,    0,
   52,   52,   28,    0,   28,    0,    0,    0,   38,    0,
   28,    0,   28,   28,   28,   48,   28,    0,    0,   28,
   28,   38,    0,   38,    0,    0,    0,    0,    7,   38,
    0,   38,   38,   38,    6,   38,    9,   48,   38,   38,
   10,   49,    0,    0,   15,   13,    0,    7,    0,    0,
    7,    0,    0,    0,    0,    9,    0,   15,    9,   10,
    0,    0,   10,    0,   13,   15,    0,   13,    0,   15,
    0,   59,   60,    0,   15,   71,   72,   73,   74,   75,
   76,    0,    0,    0,   59,   60,    0,   77,   71,   72,
   73,   74,   75,   76,    0,    0,    0,    0,    0,    0,
  104,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         25,
   57,   24,   12,   29,  257,   27,   28,  258,  259,  258,
  259,  285,  286,  257,  258,  259,  271,  257,  258,  259,
  272,  258,  259,  274,  257,  274,   49,  273,  279,  273,
   56,  275,  276,  273,   57,  275,  276,  274,  257,   61,
   62,   98,  279,   69,   67,  289,  258,  259,  273,  289,
  279,  270,   78,  272,   64,  257,  258,  259,  270,  278,
   14,  277,   16,  282,   18,   91,  285,  286,  287,  268,
  258,  259,  271,  275,  276,   98,  257,  258,  259,  105,
  269,  262,  263,  264,  265,  266,  267,  289,  270,  270,
  270,  272,  288,  274,  260,  261,  268,  278,  279,  280,
  281,  282,  281,  284,  280,  281,  287,  288,  257,  258,
  259,   59,   60,  262,  263,  264,  265,  266,  267,  274,
  284,  270,  280,  272,  270,  274,    0,  272,  272,  278,
  279,  280,  281,  282,  270,  284,  284,   19,  287,  288,
  257,  258,  259,   67,  100,  262,  263,  264,  265,  266,
  267,   69,   -1,  270,   -1,  272,   -1,  274,   -1,  257,
   -1,  278,  279,  280,  281,  282,   -1,  284,   -1,   -1,
  287,  288,  270,   -1,  272,   -1,   -1,   -1,  257,   -1,
  278,   -1,  280,  281,  282,  257,  284,   -1,   -1,  287,
  288,  270,   -1,  272,   -1,   -1,   -1,   -1,  270,  278,
   -1,  280,  281,  282,  257,  284,  278,  257,  287,  288,
  282,  283,   -1,   -1,  257,  287,   -1,  270,   -1,   -1,
  270,   -1,   -1,   -1,   -1,  278,   -1,  270,  278,  282,
   -1,   -1,  282,   -1,  287,  278,   -1,  287,   -1,  282,
   -1,  258,  259,   -1,  287,  262,  263,  264,  265,  266,
  267,   -1,   -1,   -1,  258,  259,   -1,  274,  262,  263,
  264,  265,  266,  267,   -1,   -1,   -1,   -1,   -1,   -1,
  274,
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
"scope_statement_list : statement",
"scope_statement_list : scope",
"scope_statement_list : statement scope_statement_list",
"scope_statement_list : scope scope_statement_list",
"block : BEGIN block_statement_list END",
"block : statement",
"block_statement_list : statement",
"block_statement_list : statement block_statement_list",
"var_declaration_list : var_declaration",
"var_declaration_list : var_declaration var_declaration_list",
"var_declaration : TYPE_INT var_list SEMI",
"var_declaration : TYPE_LONG var_list SEMI",
"var_list : ID",
"var_list : ID COMMA var_list",
"statement : SEMI",
"statement : if_statement",
"statement : loop_statement",
"statement : print_statement",
"statement : assign_statement",
"if_statement : IF if_condition if_body ENDIF",
"if_statement : IF if_error_condition",
"if_error_condition : expr",
"if_error_condition : LPAREN expr relational_operator expr THEN",
"if_error_condition : LPAREN expr RPAREN",
"if_body : THEN block",
"if_body : THEN if_then_block ELSE block",
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
"print_statement : PRINT LPAREN STR RPAREN SEMI",
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

//#line 216 "grammar.y"

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

public void parse() throws IOException, FastaException {
	this.yyparse();
}
//#line 381 "Parser.java"
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
//#line 67 "grammar.y"
{ this.exitScope(); }
break;
case 3:
//#line 68 "grammar.y"
{ this.exitScope(); }
break;
case 4:
//#line 71 "grammar.y"
{ this.enterScope(val_peek(1)); }
break;
case 17:
//#line 114 "grammar.y"
{ this.declareIntSymbols(val_peek(1)); }
break;
case 18:
//#line 115 "grammar.y"
{ this.declareLongSymbols(val_peek(1)); }
break;
case 19:
//#line 118 "grammar.y"
{ yyval = this.newIdList(val_peek(0)); }
break;
case 20:
//#line 119 "grammar.y"
{ yyval = this.pushIdToList(val_peek(0), val_peek(2)); }
break;
case 26:
//#line 141 "grammar.y"
{ this.ifInst(); }
break;
case 28:
//#line 145 "grammar.y"
{ this.errorIfWithoutParens(); }
break;
case 29:
//#line 146 "grammar.y"
{ this.errorIfUnclosedParens(); }
break;
case 30:
//#line 147 "grammar.y"
{ this.errorIfWithoutRelationalOperator(); }
break;
case 33:
//#line 154 "grammar.y"
{ this.ifThenBlock(); }
break;
case 34:
//#line 157 "grammar.y"
{ this.ifCondition(val_peek(0)); }
break;
case 37:
//#line 164 "grammar.y"
{ this.loopKeyword(); }
break;
case 38:
//#line 167 "grammar.y"
{ this.errorLoopWithoutParens(); }
break;
case 39:
//#line 168 "grammar.y"
{ this.errorLoopUnclosedParens(); }
break;
case 40:
//#line 169 "grammar.y"
{ this.errorLoopWithoutRelationalOperator(); }
break;
case 41:
//#line 172 "grammar.y"
{ this.loopCondition(val_peek(0)); }
break;
case 42:
//#line 175 "grammar.y"
{ yyval = this.branchCondition(val_peek(3), val_peek(2), val_peek(1)); }
break;
case 43:
//#line 178 "grammar.y"
{ yyval = val_peek(0); }
break;
case 44:
//#line 179 "grammar.y"
{ yyval = val_peek(0); }
break;
case 45:
//#line 180 "grammar.y"
{ yyval = val_peek(0); }
break;
case 46:
//#line 181 "grammar.y"
{ yyval = val_peek(0); }
break;
case 47:
//#line 182 "grammar.y"
{ yyval = val_peek(0); }
break;
case 48:
//#line 183 "grammar.y"
{ yyval = val_peek(0); }
break;
case 49:
//#line 186 "grammar.y"
{ yyval = this.assignOp(val_peek(3), val_peek(2), val_peek(1)); }
break;
case 50:
//#line 189 "grammar.y"
{ yyval = this.printOp(this.strConst(val_peek(2))); }
break;
case 51:
//#line 198 "grammar.y"
{ yyval = this.addOp(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 52:
//#line 199 "grammar.y"
{ yyval = this.subOp(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 53:
//#line 200 "grammar.y"
{ yyval = val_peek(0); }
break;
case 54:
//#line 203 "grammar.y"
{ yyval = this.mulOp(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 55:
//#line 204 "grammar.y"
{ yyval = this.divOp(val_peek(2), val_peek(1), val_peek(0)); }
break;
case 56:
//#line 205 "grammar.y"
{ yyval = val_peek(0); }
break;
case 57:
//#line 208 "grammar.y"
{ yyval = this.id(val_peek(0)); }
break;
case 58:
//#line 209 "grammar.y"
{ yyval = this.intConst(val_peek(0)); }
break;
case 59:
//#line 210 "grammar.y"
{ yyval = this.longConst(val_peek(0)); }
break;
case 60:
//#line 211 "grammar.y"
{ yyval = val_peek(0); }
break;
case 61:
//#line 212 "grammar.y"
{ yyval = this.negOp(val_peek(1)); }
break;
case 62:
//#line 213 "grammar.y"
{ yyval = this.itolOp(val_peek(3), val_peek(1)); }
break;
//#line 686 "Parser.java"
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
