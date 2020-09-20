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
    0,    1,    2,    2,    4,    4,    4,    6,    6,    7,
    7,    3,    3,    8,    8,    9,    9,    5,    5,    5,
    5,    5,   10,   10,   11,   14,   16,   16,   16,   16,
   16,   16,   13,   12,   15,   15,   15,   17,   17,   17,
   18,   18,   18,   18,   18,   18,
};
final static short yylen[] = {                            2,
    1,    4,    2,    1,    1,    2,    2,    3,    1,    1,
    2,    1,    2,    3,    3,    1,    3,    1,    1,    1,
    1,    1,    5,    7,    4,    5,    1,    1,    1,    1,
    1,    1,    4,    5,    3,    3,    1,    3,    3,    1,
    1,    1,    1,    2,    2,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    1,    0,    0,   18,    0,    0,    0,    0,
    0,    0,    0,    0,    4,    0,    0,   19,   20,   21,
   22,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    9,    0,    7,    2,    3,    6,   13,   41,    0,    0,
   42,   43,    0,    0,    0,    0,    0,    0,    0,    0,
   14,   15,    0,    0,    0,   44,   45,    0,   33,    0,
    0,    0,    0,   27,   28,   29,   30,   31,   32,    0,
    0,    0,   17,   11,    8,   25,    0,   35,   36,   38,
   39,    0,    0,   23,   34,   46,   26,    0,   24,
};
final static short yydgoto[] = {                          2,
   12,   13,   14,   15,   16,   32,   54,   17,   27,   18,
   19,   20,   21,   24,   44,   70,   45,   46,
};
final static short yysindex[] = {                      -252,
 -249,    0,    0, -255, -242,    0, -257, -245, -239, -239,
 -237, -223, -228, -223,    0, -223, -234,    0,    0,    0,
    0, -251, -251, -244, -221, -220, -210, -204, -200, -217,
    0, -214,    0,    0,    0,    0,    0,    0, -251, -251,
    0,    0, -195, -185, -196, -184, -163, -237, -177, -239,
    0,    0, -217, -179, -257,    0,    0, -251,    0, -251,
 -251, -251, -251,    0,    0,    0,    0,    0,    0, -251,
 -188, -164,    0,    0,    0,    0, -167,    0,    0,    0,
    0, -166, -237,    0,    0,    0,    0, -172,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -162, -203,    0,    0,    0,
    0,    0,    0,    0,    0, -159,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -253, -176,    0,    0,    0,    0,
    0,    0, -171,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  112,    0,   97,   57,  -11,  -44,   62,    0,   -7,    0,
    0,    0,    0,   61,  -22,    0,   35,   18,
};
final static int YYTABLESIZE=116;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         31,
   47,    5,   28,   71,    1,   38,   39,   40,   37,   37,
   37,   37,   37,   37,    6,   23,   37,   26,   53,   29,
   37,    4,    7,   41,   42,   22,    8,   25,    4,    9,
   10,   11,    6,    5,   48,   77,   31,   43,   88,   29,
    7,   53,   73,   34,    8,   30,    6,   82,   50,   11,
    9,   10,    6,   12,    7,   49,   56,   57,    8,   51,
    7,   60,   61,   11,    8,   52,   12,   22,   33,   11,
   35,   31,   36,   55,   12,   62,   63,   58,   12,   80,
   81,   40,   40,   12,   59,   40,   40,   40,   40,   40,
   40,   83,   84,   40,   78,   79,   72,   40,   64,   65,
   66,   67,   68,   69,   75,   85,   86,   87,   89,    5,
   16,    3,   10,   37,   74,   76,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         11,
   23,  257,   10,   48,  257,  257,  258,  259,  262,  263,
  264,  265,  266,  267,  270,  273,  270,  257,   30,  257,
  274,  271,  278,  275,  276,  268,  282,  273,  271,  285,
  286,  287,  270,  257,  279,   58,   48,  289,   83,  257,
  278,   53,   50,  272,  282,  283,  270,   70,  269,  287,
  285,  286,  270,  257,  278,  277,   39,   40,  282,  270,
  278,  258,  259,  287,  282,  270,  270,  268,   12,  287,
   14,   83,   16,  288,  278,  260,  261,  273,  282,   62,
   63,  258,  259,  287,  270,  262,  263,  264,  265,  266,
  267,  280,  281,  270,   60,   61,  274,  274,  262,  263,
  264,  265,  266,  267,  284,  270,  274,  274,  281,  272,
  270,    0,  284,   17,   53,   55,
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
"scope_body : var_declaration_list scope_statement_list",
"scope_body : scope_statement_list",
"scope_statement_list : statement",
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
"loop_statement : LOOP block UNTIL relational_expr",
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

//#line 177 "grammar.y"

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
//#line 327 "Parser.java"
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
//#line 69 "grammar.y"
{ this.debugRule(); }
break;
case 4:
//#line 70 "grammar.y"
{ this.debugRule(); }
break;
case 5:
//#line 73 "grammar.y"
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
case 10:
//#line 90 "grammar.y"
{ this.debugRule(); }
break;
case 11:
//#line 91 "grammar.y"
{ this.debugRule(); }
break;
case 12:
//#line 104 "grammar.y"
{ this.debugRule(); }
break;
case 13:
//#line 105 "grammar.y"
{ this.debugRule(); }
break;
case 14:
//#line 108 "grammar.y"
{ this.debugRule(); }
break;
case 15:
//#line 109 "grammar.y"
{ this.debugRule(); }
break;
case 16:
//#line 112 "grammar.y"
{ this.debugRule(); }
break;
case 17:
//#line 113 "grammar.y"
{ this.debugRule(); }
break;
case 18:
//#line 122 "grammar.y"
{ this.debugRule(); }
break;
case 19:
//#line 123 "grammar.y"
{ this.debugRule(); }
break;
case 20:
//#line 124 "grammar.y"
{ this.debugRule(); }
break;
case 21:
//#line 125 "grammar.y"
{ this.debugRule(); }
break;
case 22:
//#line 126 "grammar.y"
{ this.debugRule(); }
break;
case 23:
//#line 129 "grammar.y"
{ this.debugRule(); }
break;
case 24:
//#line 130 "grammar.y"
{ this.debugRule(); }
break;
case 25:
//#line 133 "grammar.y"
{ this.debugRule(); }
break;
case 26:
//#line 136 "grammar.y"
{ this.debugRule(); }
break;
case 27:
//#line 139 "grammar.y"
{ this.debugRule(); }
break;
case 28:
//#line 140 "grammar.y"
{ this.debugRule(); }
break;
case 29:
//#line 141 "grammar.y"
{ this.debugRule(); }
break;
case 30:
//#line 142 "grammar.y"
{ this.debugRule(); }
break;
case 31:
//#line 143 "grammar.y"
{ this.debugRule(); }
break;
case 32:
//#line 144 "grammar.y"
{ this.debugRule(); }
break;
case 33:
//#line 147 "grammar.y"
{ this.debugRule(); }
break;
case 34:
//#line 150 "grammar.y"
{ this.debugRule(); }
break;
case 35:
//#line 159 "grammar.y"
{ this.debugRule(); }
break;
case 36:
//#line 160 "grammar.y"
{ this.debugRule(); }
break;
case 37:
//#line 161 "grammar.y"
{ this.debugRule(); }
break;
case 38:
//#line 164 "grammar.y"
{ this.debugRule(); }
break;
case 39:
//#line 165 "grammar.y"
{ this.debugRule(); }
break;
case 40:
//#line 166 "grammar.y"
{ this.debugRule(); }
break;
case 41:
//#line 169 "grammar.y"
{ this.debugRule(); }
break;
case 42:
//#line 170 "grammar.y"
{ this.debugRule(); }
break;
case 43:
//#line 171 "grammar.y"
{ this.debugRule(); }
break;
case 44:
//#line 172 "grammar.y"
{ this.debugRule(); }
break;
case 45:
//#line 173 "grammar.y"
{ this.debugRule(); }
break;
case 46:
//#line 174 "grammar.y"
{ this.debugRule(); }
break;
//#line 652 "Parser.java"
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
