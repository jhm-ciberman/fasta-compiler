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
import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;
import java.io.IOException;
//#line 23 "Parser.java"




public class Parser
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
    5,    5,    5,    5,   10,   10,   10,   10,   10,   11,
   11,   11,   11,   14,   16,   16,   16,   16,   16,   16,
   13,   12,   15,   15,   15,   17,   17,   17,   18,   18,
   18,   18,   18,   18,
};
final static short yylen[] = {                            2,
    1,    4,    3,    2,    1,    1,    1,    2,    2,    3,
    1,    1,    2,    1,    2,    3,    3,    1,    3,    1,
    1,    1,    1,    1,    5,    7,    2,    6,    4,    4,
    4,    8,    6,    5,    1,    1,    1,    1,    1,    1,
    4,    5,    3,    3,    1,    3,    3,    1,    1,    1,
    1,    2,    2,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    1,    0,    0,   20,    3,    0,    0,    0,
    0,    0,    0,    0,    0,    5,    0,    0,   21,   22,
   23,   24,    0,   49,    0,    0,    0,   50,   51,    0,
    0,   27,    0,    0,    0,    0,    0,    0,    0,    0,
   11,    0,    9,    2,    4,    8,   15,    0,   52,   53,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   16,
   17,    0,    0,    0,   41,   35,   36,   37,   38,   39,
   40,   29,    0,    0,    0,   43,   44,   46,   47,    0,
   19,   13,   10,    0,   30,   31,    0,   54,    0,   25,
   42,    0,   34,   28,    0,   33,    0,   26,    0,   32,
};
final static short yydgoto[] = {                          2,
   13,   14,   15,   16,   17,   42,   63,   18,   37,   19,
   20,   21,   22,   31,   32,   73,   33,   34,
};
final static short yysindex[] = {                      -254,
 -261,    0,    0, -213, -228,    0,    0, -250, -251, -245,
 -245, -135, -224, -225, -224,    0, -224, -265,    0,    0,
    0,    0, -241,    0, -241, -241, -241,    0,    0, -246,
 -230,    0, -203, -200, -211, -216, -206, -194, -172, -132,
    0, -199,    0,    0,    0,    0,    0, -173,    0,    0,
  -99, -241, -135, -241, -241, -241, -241, -176, -245,    0,
    0, -132, -180, -244,    0,    0,    0,    0,    0,    0,
    0,    0, -241, -167, -210,    0,    0,    0,    0, -160,
    0,    0,    0, -241,    0,    0, -268,    0, -135,    0,
    0,  -93,    0,    0, -164,    0, -241,    0, -255,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -154,    0,    0,    0, -152, -125,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -151, -179,    0, -146,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -158,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  134,    0,  121,   77,  -12,  -51,   78,    0,   -7,    0,
    0,    0,    0,   80,  -22,   49,   27,   11,
};
final static int YYTABLESIZE=181;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         41,
   48,   75,    1,   38,   51,   93,   24,   25,   26,    4,
   94,   36,   24,   25,   26,   24,   25,   26,   93,   10,
   11,   35,   27,  100,   28,   29,   52,   62,   84,   74,
   28,   29,    5,   28,   29,   49,   50,   95,   30,   23,
   41,   86,    4,    5,   30,    6,   44,   30,   53,   62,
   87,   81,   59,    8,   54,   55,    6,    9,    7,   56,
   57,   92,   12,   60,    8,   58,   78,   79,    9,   89,
   90,   10,   11,   12,   99,   61,   41,   48,   48,   48,
   76,   77,   48,   48,   48,   48,   48,   48,   64,   43,
   48,   45,   48,   46,   48,   23,   65,   80,   48,   48,
   48,   48,   48,   83,   48,   45,   88,   48,   48,   91,
   45,   45,   45,   45,   45,   45,   98,    7,   45,    6,
   45,   39,   45,   18,   39,   12,   45,   45,   45,   45,
   45,   14,   45,    3,    6,   45,   45,    6,   47,   82,
   97,    0,    8,   85,   14,    8,    9,   40,    0,    9,
    0,   12,   14,    0,   12,    0,   14,    0,    0,    0,
    0,   14,   66,   67,   68,   69,   70,   71,   66,   67,
   68,   69,   70,   71,   72,    0,    0,    0,    0,    0,
   96,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         12,
   23,   53,  257,   11,   27,  274,  257,  258,  259,  271,
  279,  257,  257,  258,  259,  257,  258,  259,  274,  285,
  286,  273,  273,  279,  275,  276,  273,   40,  273,   52,
  275,  276,  257,  275,  276,   25,   26,   89,  289,  268,
   53,   64,  271,  257,  289,  270,  272,  289,  279,   62,
   73,   59,  269,  278,  258,  259,  270,  282,  272,  260,
  261,   84,  287,  270,  278,  277,   56,   57,  282,  280,
  281,  285,  286,  287,   97,  270,   89,  257,  258,  259,
   54,   55,  262,  263,  264,  265,  266,  267,  288,   13,
  270,   15,  272,   17,  274,  268,  270,  274,  278,  279,
  280,  281,  282,  284,  284,  257,  274,  287,  288,  270,
  262,  263,  264,  265,  266,  267,  281,  272,  270,  272,
  272,  257,  274,  270,  257,  284,  278,  279,  280,  281,
  282,  257,  284,    0,  270,  287,  288,  270,   18,   62,
   92,   -1,  278,   64,  270,  278,  282,  283,   -1,  282,
   -1,  287,  278,   -1,  287,   -1,  282,   -1,   -1,   -1,
   -1,  287,  262,  263,  264,  265,  266,  267,  262,  263,
  264,  265,  266,  267,  274,   -1,   -1,   -1,   -1,   -1,
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
"if_statement : IF relational_expr THEN block ENDIF",
"if_statement : IF relational_expr THEN block ELSE block ENDIF",
"if_statement : IF expr",
"if_statement : IF LPAREN expr relational_operator expr THEN",
"if_statement : IF LPAREN expr RPAREN",
"loop_statement : LOOP block UNTIL relational_expr",
"loop_statement : LOOP block UNTIL expr",
"loop_statement : LOOP block UNTIL LPAREN expr relational_operator expr THEN",
"loop_statement : LOOP block UNTIL LPAREN expr RPAREN",
"relational_expr : LPAREN expr relational_operator expr RPAREN",
"relational_operator : GTE",
"relational_operator : LTE",
"relational_operator : LT",
"relational_operator : GT",
"relational_operator : EQ",
"relational_operator : NOTEQ",
"assign_statement : ID ASSIGN expr SEMI",
"print_statement : PRINT LPAREN STR RPAREN SEMI",
"expr : term PLUS term",
"expr : term MINUS term",
"expr : term",
"term : factor MULTIPLY factor",
"term : factor DIVISION factor",
"term : factor",
"factor : ID",
"factor : INT",
"factor : LONG",
"factor : PLUS factor",
"factor : MINUS factor",
"factor : ITOL LPAREN expr RPAREN",
};

//#line 193 "grammar.y"

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
//#line 354 "Parser.java"
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
throws IOException, LexicalException, SyntaxException
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
//#line 63 "grammar.y"
{ this.debugRule(); }
break;
case 2:
//#line 66 "grammar.y"
{ this.debugRule(); }
break;
case 3:
//#line 67 "grammar.y"
{ this.debugRule(); }
break;
case 4:
//#line 70 "grammar.y"
{ this.debugRule(); }
break;
case 5:
//#line 71 "grammar.y"
{ this.debugRule(); }
break;
case 6:
//#line 74 "grammar.y"
{ this.debugRule(); }
break;
case 7:
//#line 75 "grammar.y"
{ this.debugRule(); }
break;
case 8:
//#line 76 "grammar.y"
{ this.debugRule(); }
break;
case 9:
//#line 77 "grammar.y"
{ this.debugRule(); }
break;
case 12:
//#line 92 "grammar.y"
{ this.debugRule(); }
break;
case 13:
//#line 93 "grammar.y"
{ this.debugRule(); }
break;
case 14:
//#line 106 "grammar.y"
{ this.debugRule(); }
break;
case 15:
//#line 107 "grammar.y"
{ this.debugRule(); }
break;
case 16:
//#line 110 "grammar.y"
{ this.debugRule(); }
break;
case 17:
//#line 111 "grammar.y"
{ this.debugRule(); }
break;
case 18:
//#line 114 "grammar.y"
{ this.debugRule(); }
break;
case 19:
//#line 115 "grammar.y"
{ this.debugRule(); }
break;
case 20:
//#line 130 "grammar.y"
{ this.debugRule(); }
break;
case 21:
//#line 131 "grammar.y"
{ this.debugRule(); }
break;
case 22:
//#line 132 "grammar.y"
{ this.debugRule(); }
break;
case 23:
//#line 133 "grammar.y"
{ this.debugRule(); }
break;
case 24:
//#line 134 "grammar.y"
{ this.debugRule(); }
break;
case 25:
//#line 137 "grammar.y"
{ this.debugRule(); }
break;
case 26:
//#line 138 "grammar.y"
{ this.debugRule(); }
break;
case 27:
//#line 140 "grammar.y"
{ this.yyerror("IF condition should be enclosed in parenthesis. You should add the missing parenthesis in the condition. Example: IF (foo <= bar) THEN ..."); }
break;
case 28:
//#line 141 "grammar.y"
{ this.yyerror("Unclosed parenthesis in IF condition. You should close the right parenthesis in the IF condition. Example: IF (foo <= bar) THEN ..."); }
break;
case 29:
//#line 142 "grammar.y"
{ this.yyerror("IF does not have relational operator. You should add a valid relational operator. Example: IF (foo <= bar) THEN ..."); }
break;
case 30:
//#line 145 "grammar.y"
{ this.debugRule(); }
break;
case 31:
//#line 147 "grammar.y"
{ this.yyerror("LOOP..WHILE condition should be enclosed in parenthesis. You should add the missing parenthesis in the condition. Example: LOOP .. WHILE (foo <= bar)"); }
break;
case 32:
//#line 148 "grammar.y"
{ this.yyerror("Unclosed parenthesis in IF condition. You should close the right parenthesis in the IF condition. Example: LOOP .. WHILE (foo <= bar)"); }
break;
case 33:
//#line 149 "grammar.y"
{ this.yyerror("LOOP..WHILE condition does not have relational operator. You should add a valid relational operator. Example: LOOP .. WHILE (foo <= bar)"); }
break;
case 34:
//#line 152 "grammar.y"
{ this.debugRule(); }
break;
case 35:
//#line 155 "grammar.y"
{ this.debugRule(); }
break;
case 36:
//#line 156 "grammar.y"
{ this.debugRule(); }
break;
case 37:
//#line 157 "grammar.y"
{ this.debugRule(); }
break;
case 38:
//#line 158 "grammar.y"
{ this.debugRule(); }
break;
case 39:
//#line 159 "grammar.y"
{ this.debugRule(); }
break;
case 40:
//#line 160 "grammar.y"
{ this.debugRule(); }
break;
case 41:
//#line 163 "grammar.y"
{ this.debugRule(); }
break;
case 42:
//#line 166 "grammar.y"
{ this.debugRule(); }
break;
case 43:
//#line 175 "grammar.y"
{ this.debugRule(); }
break;
case 44:
//#line 176 "grammar.y"
{ this.debugRule(); }
break;
case 45:
//#line 177 "grammar.y"
{ this.debugRule(); }
break;
case 46:
//#line 180 "grammar.y"
{ this.debugRule(); }
break;
case 47:
//#line 181 "grammar.y"
{ this.debugRule(); }
break;
case 48:
//#line 182 "grammar.y"
{ this.debugRule(); }
break;
case 49:
//#line 185 "grammar.y"
{ this.debugRule(); }
break;
case 50:
//#line 186 "grammar.y"
{ this.debugRule(); }
break;
case 51:
//#line 187 "grammar.y"
{ this.debugRule(); }
break;
case 52:
//#line 188 "grammar.y"
{ this.debugRule(); }
break;
case 53:
//#line 189 "grammar.y"
{ this.debugRule(); }
break;
case 54:
//#line 190 "grammar.y"
{ this.debugRule(); }
break;
//#line 711 "Parser.java"
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
