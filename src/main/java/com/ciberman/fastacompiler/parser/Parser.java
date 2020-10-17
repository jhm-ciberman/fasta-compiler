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
    0,    1,    1,    2,    2,    4,    4,    4,    4,    6,
    6,    7,    7,    3,    3,    8,    8,    9,    9,    5,
    5,    5,    5,    5,   10,   10,   10,   10,   15,   15,
   18,   14,   11,   11,   11,   11,   20,   19,   17,   17,
   17,   17,   17,   17,   13,   12,   16,   16,   16,   21,
   21,   21,   22,   22,   22,   22,   22,   22,
};
final static short yylen[] = {                            2,
    1,    4,    3,    2,    1,    1,    1,    2,    2,    3,
    1,    1,    2,    1,    2,    3,    3,    1,    3,    1,
    1,    1,    1,    1,    4,    2,    6,    4,    2,    4,
    1,    1,    4,    4,    8,    6,    1,    5,    1,    1,
    1,    1,    1,    1,    4,    5,    3,    3,    1,    3,
    3,    1,    1,    1,    1,    2,    2,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    1,    0,    0,   20,    3,    0,    0,    0,
    0,    0,    0,    0,    0,    5,    0,    0,   21,   22,
   23,   24,    0,   53,    0,    0,    0,   54,   55,    0,
    0,    0,   32,    0,   52,    0,    0,    0,    0,    0,
    0,   11,    0,    9,    2,    4,    8,   15,    0,   56,
   57,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   16,   17,    0,    0,    0,   45,   39,   40,   41,
   42,   43,   44,   28,    0,    0,    0,    0,   25,    0,
    0,   50,   51,    0,   19,   13,   10,    0,    0,   37,
   33,    0,   58,    0,   46,    0,   38,   27,   30,   36,
    0,    0,   35,
};
final static short yydgoto[] = {                          2,
   13,   14,   15,   16,   17,   43,   65,   18,   38,   19,
   20,   21,   22,   31,   55,   32,   75,   78,   33,   91,
   34,   35,
};
final static short yysindex[] = {                      -255,
 -263,    0,    0,  -85, -223,    0,    0, -248, -247, -210,
 -210,  -66, -220, -238, -220,    0, -220, -265,    0,    0,
    0,    0, -240,    0, -240, -240, -240,    0,    0, -222,
 -202, -198,    0, -186,    0, -221, -188, -187, -181, -170,
  -63,    0, -179,    0,    0,    0,    0,    0, -246,    0,
    0,  -26, -240,  -66, -182, -240, -240, -240, -240, -163,
 -210,    0,    0,  -63, -171, -243,    0,    0,    0,    0,
    0,    0,    0,    0, -240, -236,    0, -166,    0, -186,
 -186,    0,    0, -155,    0,    0,    0, -240, -198,    0,
    0,  -95,    0,  -66,    0,  -13,    0,    0,    0,    0,
 -240,  -76,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -151,    0,    0,    0, -149,  -52,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -111,    0, -194,    0,    0, -146,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -154,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -189,    0,    0, -162,
 -130,    0,    0,    0,    0,    0,    0,    0,  -92,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  131,    0,  120,   42,  -12,  -51,   75,    0,   -7,    0,
    0,    0,    0,    0,    0,  -22,   45,    0,   77,    0,
   50,  -19,
};
final static int YYTABLESIZE=261;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         42,
   49,    1,   77,   39,   52,   50,   51,    4,   24,   25,
   26,   56,   57,   24,   25,   26,   24,   25,   26,   10,
   11,   56,   57,   67,   27,   36,   28,   29,   64,   88,
   76,   28,   29,   45,   28,   29,    5,   93,   82,   83,
   30,   42,   99,   89,   23,   30,   37,    4,   30,    6,
   53,   64,   92,   85,   44,   60,   46,    8,   47,   56,
   57,    9,   49,   49,   49,   96,   12,   49,   49,   49,
   49,   49,   49,   58,   59,   49,   54,   49,  102,   49,
   61,   42,   62,   49,   49,   49,   49,   49,   63,   49,
   31,   29,   49,   49,   47,   47,   47,   23,   79,   47,
   47,   47,   47,   47,   47,   80,   81,   47,   66,   47,
   84,   47,   87,   94,   95,   47,   47,   47,   47,   47,
    7,   47,    6,   18,   47,   47,   48,   48,   48,   12,
    3,   48,   48,   48,   48,   48,   48,   48,   86,   48,
  101,   48,   90,   48,    0,   26,    0,   48,   48,   48,
   48,   48,    0,   48,    0,    0,   48,   48,   26,    0,
   26,    0,   56,   57,   34,    0,   26,    0,   26,   26,
   26,    5,   26,    0,    0,   26,   26,   34,   97,   34,
    0,   56,   57,   98,    6,   34,    7,   34,   34,   34,
   40,   34,    8,   40,   34,   34,    9,   97,    0,   10,
   11,   12,  103,    6,   14,    0,    6,    0,    0,    0,
    0,    8,    0,    0,    8,    9,   41,   14,    9,    0,
   12,    0,    0,   12,    0,   14,    0,    0,    0,   14,
    0,   56,   57,    0,   14,   68,   69,   70,   71,   72,
   73,    0,    0,    0,   56,   57,    0,   74,   68,   69,
   70,   71,   72,   73,    0,    0,    0,    0,    0,    0,
  100,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         12,
   23,  257,   54,   11,   27,   25,   26,  271,  257,  258,
  259,  258,  259,  257,  258,  259,  257,  258,  259,  285,
  286,  258,  259,  270,  273,  273,  275,  276,   41,  273,
   53,  275,  276,  272,  275,  276,  257,  274,   58,   59,
  289,   54,   94,   66,  268,  289,  257,  271,  289,  270,
  273,   64,   75,   61,   13,  277,   15,  278,   17,  258,
  259,  282,  257,  258,  259,   88,  287,  262,  263,  264,
  265,  266,  267,  260,  261,  270,  279,  272,  101,  274,
  269,   94,  270,  278,  279,  280,  281,  282,  270,  284,
  280,  281,  287,  288,  257,  258,  259,  268,  281,  262,
  263,  264,  265,  266,  267,   56,   57,  270,  288,  272,
  274,  274,  284,  280,  270,  278,  279,  280,  281,  282,
  272,  284,  272,  270,  287,  288,  257,  258,  259,  284,
    0,  262,  263,  264,  265,  266,  267,   18,   64,  270,
   96,  272,   66,  274,   -1,  257,   -1,  278,  279,  280,
  281,  282,   -1,  284,   -1,   -1,  287,  288,  270,   -1,
  272,   -1,  258,  259,  257,   -1,  278,   -1,  280,  281,
  282,  257,  284,   -1,   -1,  287,  288,  270,  274,  272,
   -1,  258,  259,  279,  270,  278,  272,  280,  281,  282,
  257,  284,  278,  257,  287,  288,  282,  274,   -1,  285,
  286,  287,  279,  270,  257,   -1,  270,   -1,   -1,   -1,
   -1,  278,   -1,   -1,  278,  282,  283,  270,  282,   -1,
  287,   -1,   -1,  287,   -1,  278,   -1,   -1,   -1,  282,
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
"scope : ID LBRACE scope_body RBRACE",
"scope : ID LBRACE RBRACE",
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
"if_statement : IF expr",
"if_statement : IF LPAREN expr relational_operator expr THEN",
"if_statement : IF LPAREN expr RPAREN",
"if_body : THEN block",
"if_body : THEN if_then_block ELSE block",
"if_then_block : block",
"if_condition : bool_expr",
"loop_statement : LOOP block UNTIL loop_condition",
"loop_statement : LOOP block UNTIL expr",
"loop_statement : LOOP block UNTIL LPAREN expr relational_operator expr THEN",
"loop_statement : LOOP block UNTIL LPAREN expr RPAREN",
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

//#line 206 "grammar.y"

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
//#line 373 "Parser.java"
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
case 1:
//#line 64 "grammar.y"
{ this.debugRule(); }
break;
case 2:
//#line 67 "grammar.y"
{ this.debugRule(); }
break;
case 3:
//#line 68 "grammar.y"
{ this.debugRule(); }
break;
case 4:
//#line 71 "grammar.y"
{ this.debugRule(); }
break;
case 5:
//#line 72 "grammar.y"
{ this.debugRule(); }
break;
case 6:
//#line 75 "grammar.y"
{ this.debugRule(); }
break;
case 7:
//#line 76 "grammar.y"
{ this.debugRule(); }
break;
case 8:
//#line 77 "grammar.y"
{ this.debugRule(); }
break;
case 9:
//#line 78 "grammar.y"
{ this.debugRule(); }
break;
case 12:
//#line 93 "grammar.y"
{ this.debugRule(); }
break;
case 13:
//#line 94 "grammar.y"
{ this.debugRule(); }
break;
case 16:
//#line 111 "grammar.y"
{ this.declareIntSymbols(val_peek(1)); }
break;
case 17:
//#line 112 "grammar.y"
{ this.declareLongSymbols(val_peek(1)); }
break;
case 18:
//#line 115 "grammar.y"
{ yyval = this.newIdList(val_peek(0)); }
break;
case 19:
//#line 116 "grammar.y"
{ yyval = this.pushIdToList(val_peek(0), val_peek(2)); }
break;
case 25:
//#line 138 "grammar.y"
{ this.ifInst(); }
break;
case 26:
//#line 140 "grammar.y"
{ this.errorIfWithoutParens(); }
break;
case 27:
//#line 141 "grammar.y"
{ this.errorIfUnclosedParens(); }
break;
case 28:
//#line 142 "grammar.y"
{ this.errorIfWithoutRelationalOperator(); }
break;
case 31:
//#line 149 "grammar.y"
{ this.ifThenBlock(); }
break;
case 32:
//#line 152 "grammar.y"
{ this.ifCondition(val_peek(0)); }
break;
case 33:
//#line 155 "grammar.y"
{ this.debugRule(); }
break;
case 34:
//#line 157 "grammar.y"
{ this.errorLoopWithoutParens(); }
break;
case 35:
//#line 158 "grammar.y"
{ this.errorLoopUnclosedParens(); }
break;
case 36:
//#line 159 "grammar.y"
{ this.errorLoopWithoutRelationalOperator(); }
break;
case 37:
//#line 162 "grammar.y"
{ this.loopCondition(val_peek(0)); }
break;
case 38:
//#line 165 "grammar.y"
{ yyval = this.branchCondition(val_peek(3), val_peek(2), val_peek(1)); }
break;
case 39:
//#line 168 "grammar.y"
{ yyval = val_peek(0); }
break;
case 40:
//#line 169 "grammar.y"
{ yyval = val_peek(0); }
break;
case 41:
//#line 170 "grammar.y"
{ yyval = val_peek(0); }
break;
case 42:
//#line 171 "grammar.y"
{ yyval = val_peek(0); }
break;
case 43:
//#line 172 "grammar.y"
{ yyval = val_peek(0); }
break;
case 44:
//#line 173 "grammar.y"
{ yyval = val_peek(0); }
break;
case 45:
//#line 176 "grammar.y"
{ yyval = this.assignOp(val_peek(3), val_peek(1)); }
break;
case 46:
//#line 179 "grammar.y"
{ yyval = this.printOp(this.strConst(val_peek(2))); }
break;
case 47:
//#line 188 "grammar.y"
{ yyval = this.addOp(val_peek(2), val_peek(0)); }
break;
case 48:
//#line 189 "grammar.y"
{ yyval = this.subOp(val_peek(2), val_peek(0)); }
break;
case 49:
//#line 190 "grammar.y"
{ yyval = val_peek(0); }
break;
case 50:
//#line 193 "grammar.y"
{ yyval = this.mulOp(val_peek(2), val_peek(0)); }
break;
case 51:
//#line 194 "grammar.y"
{ yyval = this.divOp(val_peek(2), val_peek(0)); }
break;
case 52:
//#line 195 "grammar.y"
{ yyval = val_peek(0); }
break;
case 53:
//#line 198 "grammar.y"
{ yyval = this.id(val_peek(0)); }
break;
case 54:
//#line 199 "grammar.y"
{ yyval = this.intConst(val_peek(0)); }
break;
case 55:
//#line 200 "grammar.y"
{ yyval = this.longConst(val_peek(0)); }
break;
case 56:
//#line 201 "grammar.y"
{ yyval = val_peek(0); }
break;
case 57:
//#line 202 "grammar.y"
{ yyval = this.negOp(val_peek(1)); }
break;
case 58:
//#line 203 "grammar.y"
{ yyval = this.itolOp(val_peek(3)); }
break;
//#line 710 "Parser.java"
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
