package com.ciberman.fastacompiler.lexer;

import com.ciberman.fastacompiler.errors.LexicalException;

import static com.ciberman.fastacompiler.lexer.TokenType.*;

public class MatrixAutomata implements Automata {

    interface SemanticAction {
        void performAction(LexerContext ctx) throws LexicalException;
    }

    enum MatrixCol {
        OTHER,                            //  0
        EOF,                              //  1
        TAB,                              //  2
        SPACE,                            //  3
        NL,                               //  4
        LOWERCASE_LETTERS_EXCEPT_I_AND_L, //  5
        UPPERCASE_LETTERS,                //  6
        NUMBERS,                          //  7
        LOWERCASE_LETTER_I,               //  8
        LOWERCASE_LETTER_L,               //  9
        UNDERSCORE,                       // 10
        PLUS,                             // 11
        MINUS,                            // 12
        MULTIPLY,                         // 13
        DIVISION,                         // 14
        LBRACE,                           // 15
        RBRACE,                           // 16
        LPAREN,                           // 17
        RPAREN,                           // 18
        COMMA,                            // 19
        SEMI,                             // 20
        APOSTROPHE,                       // 21
        LESS_THAN,                        // 22
        GREATER_THAN,                     // 23
        EQUAL,                            // 24
        HASH,                             // 25
    }

    public static final int S   =  0;
    public static final int F   =  0; // Final state also goes to initial state
    public static final int ERR = -1; // Error state is a special state

    private final int[][] transitions;
    private final TokenType[][] tokenTypes;
    private final SemanticAction[][] semanticActions;

    private int currentStateNumber = S;

    public MatrixAutomata() {

        SemanticAction Peek = LexerContext::peek;
        SemanticAction Save = LexerContext::save;
        SemanticAction CheckInt = Validator::validateInt;
        SemanticAction CheckLong = Validator::validateLong;
        SemanticAction ChekId = ctx -> { ctx.peek(); Validator.validateId(ctx); };
        SemanticAction Reserved = ctx -> ctx.peek().yieldToken(new Token(ctx, Validator.validateReserved(ctx)));

        this.transitions = new int[][] {
            // 0      , 1       , 2       , 3,      , 4       , 5       , 6       , 7       , 8        , 9         , 10      , 11      , 12      , 13      , 14      , 15      , 16      , 17      , 18      , 19      , 20      , 21      , 22      , 23      , 24      , 25      ,
            // OTHER  , EOF     , TAB     , SPACE   , NEWLINE , LOWER   , UPPER   , NUMBER , "i"      , "l"       , "_"     , "+"     , "-"     , "*"     , "/"     , "{"     , "}"     , "("     , ")"     , ","     , ";"     , "'"     , "<"     , ">"     , "="     , "#"     ,
            { ERR     , F       , S       , S       , S       , 1       , 10      , 2       , 1        , 1         , 1       , F       , F       , F       , 4       , F       , F       , F       , F       , F       , F       , 11      , 8       , 7       , 9       , 6        },
            { F       , F       , F       , F       , F       , 1       , F       , 1       , 1        , 1         , 1       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F        },
            { ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , 2       , ERR      , ERR       , 3       , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR      },
            { ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , F        , F         , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR     , ERR      },
            { F       , F       , F       , F       , F       , F       , F       , F       , F        , F         , F       , F       , F       , F       , 5       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F        },
            { 5       , S       , 5       , 5       , S       , 5       , 5       , 5       , 5        , 5         , 5       , 5       , 5       , 5       , 5       , 5       , 5       , 5       , 5       , 5       , 5       , 5       , 5       , 5       , 5       , 5        },
            { 6       , S       , 6       , 6       , 6       , 6       , 6       , 6       , 6        , 6         , 6       , 6       , 6       , 6       , 6       , 6       , 6       , 6       , 6       , 6       , 6       , 6       , 6       , 6       , 6       , S        },
            { F       , F       , F       , F       , F       , F       , F       , F       , F        , F         , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F        },
            { F       , F       , F       , F       , F       , F       , F       , F       , F        , F         , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F        },
            { F       , F       , F       , F       , F       , F       , F       , F       , F        , F         , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F        },
            { F       , F       , F       , F       , F       , F       , 10      , F       , F        , F         , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F       , F        },
            { 11      , ERR     , 11      , 11      , 11      , 11      , 11      , 11      , 11       , 11        , 11      , 11      , 11      , 11      , 11      , 11      , 11      , 11      , 11      , 11      , 11      , F       , 11      , 11      , 11      , 11       },
        };

        this.tokenTypes = new TokenType[][] {
            // 0      , 1       , 2       , 3,      , 4       , 5       , 6       , 7       , 8        , 9         , 10      , 11      , 12      , 13      , 14      , 15      , 16      , 17      , 18      , 19      , 20      , 21      , 22      , 23      , 24      , 25      ,
            // OTHER  , EOF     , TAB     , SPACE   , NEWLINE , LOWER   , UPPER   , NUMBER  , "i"      , "l"       , "_"     , "+"     , "-"     , "*"     , "/"     , "{"     , "}"     , "("     , ")"     , ","     , ";"     , "'"     , "<"     , ">"     , "="     , "#"     ,
            { null    , EOF     , null    , null    , null    , null    , null    , null    , null     , null      , null    , PLUS    , MINUS   , MULTIPLY, null    , LBRACE  , RBRACE  , LPAREN  , RPAREN  , COMMA   , SEMI    , null    , null    , null    , null    , null     },
            { ID      , ID      , ID      , ID      , ID      , null    , ID      , null    , null     , null      , null    , ID      , ID      , ID      , ID      , ID      , ID      , ID      , ID      , ID      , ID      , ID      , ID      , ID      , ID      , ID       },
            { null    , null    , null    , null    , null    , null    , null    , null    , null     , null      , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null     },
            { null    , null    , null    , null    , null    , null    , null    , null    , INT      , LONG      , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null     },
            { DIVISION, DIVISION, DIVISION, DIVISION, DIVISION, DIVISION, DIVISION, DIVISION, DIVISION , DIVISION  , DIVISION, DIVISION, DIVISION, DIVISION, null    , DIVISION, DIVISION, DIVISION, DIVISION, DIVISION, DIVISION, DIVISION, DIVISION, DIVISION, DIVISION, DIVISION },
            { null    , null    , null    , null    , null    , null    , null    , null    , null     , null      , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null     },
            { null    , null    , null    , null    , null    , null    , null    , null    , null     , null      , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null     },
            { GT      , GT      , GT      , GT      , GT      , GT      , GT      , GT      , GT       , GT        , GT      , GT      , GT      , GT      , GT      , GT      , GT      , GT      , GT      , GT      , GT      , GT      , GT      , GT      , GTE     , GT       },
            { GT      , LT      , LT      , LT      , LT      , LT      , LT      , LT      , LT       , LT        , LT      , LT      , LT      , LT      , LT      , LT      , LT      , LT      , LT      , LT      , LT      , LT      , LT      , NOTEQ   , LTE     , LT       },
            { ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN   , ASSIGN    , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , ASSIGN  , EQ      , ASSIGN   },
            { null    , null    , null    , null    , null    , null    , null    , null    , null     , null      , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null     },
            { null    , null    , null    , null    , null    , null    , null    , null    , null     , null      , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , STR     , null    , null    , null    , null     },
        };

        this.semanticActions = new SemanticAction[][] {
            // 0      , 1       , 2       , 3,      , 4       , 5       , 6       , 7       , 8        , 9         , 10      , 11      , 12      , 13      , 14      , 15      , 16      , 17      , 18      , 19      , 20      , 21      , 22      , 23      , 24      , 25      ,
            // OTHER  , EOF     , TAB     , SPACE   , NEWLINE , LOWER   , UPPER   , NUMBER  , "i"      , "l"       , "_"     , "+"     , "-"     , "*"     , "/"     , "{"     , "}"     , "("     , ")"     , ","     , ";"     , "'"     , "<"     , ">"     , "="     , "#"     ,
            { null    , null    , null    , null    , null    , Save    , Save    , Save    , Save     , Save      , Save    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null     },
            { ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , Save    , ChekId  , Save    , Save     , Save      , Save    , ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , ChekId  , ChekId   },
            { null    , null    , null    , null    , null    , null    , null    , Save    , null     , null      , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null     },
            { null    , null    , null    , null    , null    , null    , null    , null    , CheckInt , CheckLong , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null     },
            { Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek     , Peek      , Peek    , Peek    , Peek    , Peek    , null    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek     },
            { null    , Peek    , null    , null    , null    , null    , null    , null    , null     , null      , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null     },
            { null    , Peek    , null    , null    , null    , null    , null    , null    , null     , null      , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null    , null     },
            { Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek     , Peek      , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , null    , Peek     },
            { Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek     , Peek      , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , null    , null    , Peek     },
            { Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek     , Peek      , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , Peek    , null    , Peek     },
            { Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Save    , Reserved, Reserved , Reserved  , Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Reserved, Reserved },
            { Save    , null    , Save    , Save    , null    , Save    , Save    , Save    , Save     , Save      , Save    , Save    , Save    , Save    , Save    , Save    , Save    , Save    , Save    , Save    , Save    , null    , Save    , Save    , Save    , Save     },
        };
    }

    @Override
    public void reset() {
        this.currentStateNumber = S;
    }

    @Override
    public void advance(int codePoint, LexerContext ctx) throws LexicalException {

        int matrixRow = this.currentStateNumber;
        int matrixColumn = this.mapCodePointToMatrixColumn(codePoint).ordinal();

        int stateNumber               = this.transitions[matrixRow][matrixColumn];
        TokenType tokenType           = this.tokenTypes[matrixRow][matrixColumn];
        SemanticAction semanticAction = this.semanticActions[matrixRow][matrixColumn];

        if (stateNumber == ERR) {
            throw new LexicalException(ctx, codePoint);
        }

        if (semanticAction != null) {
            semanticAction.performAction(ctx);
        }

        if (tokenType != null) {
            ctx.yieldToken(new Token(ctx, tokenType, ctx.value()));
            this.currentStateNumber = S;
            return;
        }

        this.currentStateNumber = stateNumber;
    }


    private MatrixCol mapCodePointToMatrixColumn(int codePoint) {

        if (Character.isLowerCase(codePoint)) {
            if (codePoint == 'i') {
                return MatrixCol.LOWERCASE_LETTER_I;
            } else if (codePoint == 'l') {
                return MatrixCol.LOWERCASE_LETTER_L;
            } else {
                return MatrixCol.LOWERCASE_LETTERS_EXCEPT_I_AND_L;
            }
        }

        if (Character.isUpperCase(codePoint)) {
            return MatrixCol.UPPERCASE_LETTERS;
        }

        if (Character.isDigit(codePoint)) {
            return MatrixCol.NUMBERS;
        }

        switch (codePoint)
        {
            case '\t':
            case '\u000B':
                return MatrixCol.TAB;
            case ' ':
            case Character.SPACE_SEPARATOR:
            case Character.LINE_SEPARATOR:
            case Character.PARAGRAPH_SEPARATOR:
                return MatrixCol.SPACE;
            case '\n':
                return MatrixCol.NL;
            case -1:
                return MatrixCol.EOF;
            case '+':
                return MatrixCol.PLUS;
            case '-':
                return MatrixCol.MINUS;
            case '*':
                return MatrixCol.MULTIPLY;
            case '{':
                return MatrixCol.LBRACE;
            case '}':
                return MatrixCol.RBRACE;
            case '/':
                return MatrixCol.DIVISION;
            case '#':
                return MatrixCol.HASH;
            case '(':
                return MatrixCol.LPAREN;
            case ')':
                return MatrixCol.RPAREN;
            case ',':
                return MatrixCol.COMMA;
            case ';':
                return MatrixCol.SEMI;
            case '\'':
                return MatrixCol.APOSTROPHE;
            case '<':
                return MatrixCol.LESS_THAN;
            case '>':
                return MatrixCol.GREATER_THAN;
            case '=':
                return MatrixCol.EQUAL;
            case '_':
                return MatrixCol.UNDERSCORE;
            default:
                return MatrixCol.OTHER;
        }
    }

}
