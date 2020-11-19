package com.ciberman.fastacompiler.parser;

import com.ciberman.fastacompiler.Fasta;
import com.ciberman.fastacompiler.errors.*;
import com.ciberman.fastacompiler.ir.*;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class BaseParser {

    private static final boolean OPTIMIZE_CONSTANTS_INITIALIZATION = true;

    private static class IdList extends LinkedList<String> {
        // nothing. This class is because java do not permit to downcast an Object to a generic type.
    }

    private final Lexer lexer;

    private final IRProgram theProgram;

    private Token currentToken;

    private boolean errorRecoveryMode = false;

    private int ifStatementCount = 0;

    private final Stack<BranchInst> branchesStack = new Stack<>();
    private final Stack<Inst> instructionStack = new Stack<>();

    public BaseParser(Lexer lexer) {
        this.lexer = lexer;
        this.theProgram = new IRProgram();
    }

    protected Token next() throws IOException, LexicalException {
        this.currentToken = this.lexer.getNextToken();
        return this.currentToken;
    }

    protected IRProgram getProgram() {
        return this.theProgram;
    }

    private String tokenVal(ParserVal val) {
        return ((Token) val.obj).getValue();
    }

    protected ParserVal intConst(ParserVal val) {
        try {
            int intConst = Integer.parseInt(this.tokenVal(val));
            return new ParserVal(this.theProgram.addConst(intConst));
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected ParserVal longConst(ParserVal val) {
        try {
            long longConst = Long.parseLong(this.tokenVal(val));
            return new ParserVal(this.theProgram.addConst(longConst));
        } catch (Exception e) {
            return this.error(e);
        }
    }


    protected ParserVal strConst(ParserVal val) {
        try {
            return new ParserVal(this.theProgram.addConst(this.tokenVal(val)));
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected ParserVal newIdList(ParserVal val) {
        try {
            IdList list = new IdList();
            list.add(this.tokenVal(val));
            return new ParserVal(list);
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected ParserVal pushIdToList(ParserVal list, ParserVal id) {
        try {
            List<String> theList = (IdList) list.obj;
            theList.add(this.tokenVal(id));
            return list;
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected void declareSymbols(IdList list, ValueType type) {
        for (String str : list) {
            this.theProgram.declareSymbol(str, type);
        }
    }

    protected void declareIntSymbols(ParserVal list) {
        try {
            this.declareSymbols((IdList) list.obj, ValueType.INT);
        } catch (Exception e) {
            this.error(e);
        }
    }

    protected void declareLongSymbols(ParserVal list) {
        try {
            this.declareSymbols((IdList) list.obj, ValueType.LONG);
        } catch (Exception e) {
            this.error(e);
        }
    }

    protected ParserVal assignOp(ParserVal lhs, ParserVal tokenAssign, ParserVal rhs) {
        try {
            Token token = (Token) lhs.obj;
            Value value = (Value) rhs.obj;
            Symbol symbol = this.theProgram.findSymbolByName(token.getValue());
            if (symbol == null) {
                throw new UndeclaredVariableException(token);
            }

            if (symbol.getType() != value.getType()) {
                throw new IncorrectArgumentTypeException((Token) tokenAssign.obj, symbol.getType(), value.getType());
            }

            if (OPTIMIZE_CONSTANTS_INITIALIZATION && value instanceof Const && this.ifStatementCount <= 0) {
                // Optimize constants
                symbol.setInitialValue((Const) value);
                return new ParserVal(this.theProgram.createNoOpInst());
            }

            AssignInst inst = this.theProgram.createAssignInst(symbol, value);
            symbol.markAsInitialized();
            return (value instanceof ValueInst) ? rhs : new ParserVal(inst);
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected ParserVal id(ParserVal val) {
        try {
            Token token = (Token) val.obj;
            Symbol symbol = this.theProgram.findSymbolByName(token.getValue());
            if (symbol == null) {
                throw new UndeclaredVariableException(token);
            }
            if (! symbol.isInitialized()) {
                throw new UninitializedVariableException(token);
            }
            return new ParserVal(symbol);
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected ParserVal addOp(ParserVal lhs, ParserVal token, ParserVal rhs) throws IncorrectArgumentTypeException {
        try {
            this.assertTypes(lhs, token, rhs);
            return new ParserVal(this.theProgram.createAddInst((Value) lhs.obj, (Value) rhs.obj));
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected ParserVal subOp(ParserVal lhs, ParserVal token, ParserVal rhs) throws IncorrectArgumentTypeException {
        try {
            this.assertTypes(lhs, token, rhs);
            return new ParserVal(this.theProgram.createSubInst((Value) lhs.obj, (Value) rhs.obj));
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected ParserVal divOp(ParserVal lhs, ParserVal token, ParserVal rhs) throws IncorrectArgumentTypeException {
        try {
            this.assertTypes(lhs, token, rhs);
            return new ParserVal(this.theProgram.createDivInst((Value) lhs.obj, (Value) rhs.obj));
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected ParserVal mulOp(ParserVal lhs, ParserVal token, ParserVal rhs) throws IncorrectArgumentTypeException {
        try {
            this.assertTypes(lhs, token, rhs);
            return new ParserVal(this.theProgram.createMulInst((Value) lhs.obj, (Value) rhs.obj));
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected ParserVal negOp(ParserVal op) {
        try {
            return new ParserVal(this.theProgram.createNegInst((Value) op.obj));
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected ParserVal itolOp(ParserVal tokenItol, ParserVal op) {
        try {
            Value val = (Value) op.obj;
            if (val.getType() != ValueType.INT) {
                throw new SemanticException((Token) tokenItol.obj, "ITOL function requires an INT parameter type. " + val.getType() + " parameter given.");
            }
            return new ParserVal(this.theProgram.createItolInst(val));
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected void printOp(ParserVal op) {
        try {
            this.theProgram.createPrintInst((Value) op.obj);
        } catch (Exception e) {
            this.error(e);
        }
    }

    protected ParserVal branchCondition(ParserVal lhs, ParserVal operator, ParserVal rhs) {
        try {
            this.assertTypes(lhs, operator, rhs);
            BranchCondition.RelOperator op = this.getRelOp((Token) operator.obj);
            BranchCondition inst = this.theProgram.createBranchCondition((Value) lhs.obj, op, (Value) rhs.obj);
            return new ParserVal(inst);
        } catch (Exception e) {
            return this.error(e);
        }
    }

    protected void ifInst() {
        try {
            this.theProgram.createNoOpInst();
            this.branchesStack.pop().setTarget(this.theProgram.getLastInst());
            this.ifStatementCount--;
        } catch (Exception e) {
            this.error(e);
        }
    }

    protected void ifThenBlock() {
        try {
            BranchInst branchInst = this.theProgram.createBranchInst();
            this.theProgram.createNoOpInst();
            this.branchesStack.pop().setTarget(this.theProgram.getLastInst());
            this.branchesStack.push(branchInst);
        } catch (Exception e) {
            this.error(e);
        }
    }

    protected void ifCondition(ParserVal test) {
        try {
            BranchCondition condition = (BranchCondition) test.obj;
            condition.negateOperator();
            BranchInst branchInst = this.theProgram.createBranchInst(condition);
            this.branchesStack.push(branchInst);
            this.ifStatementCount++;
        } catch (Exception e) {
            this.error(e);
        }
    }

    protected void loopKeyword() {
        try {
            this.instructionStack.push(this.theProgram.createNoOpInst());
        } catch (Exception e) {
            this.error(e);
        }
    }

    protected void loopCondition(ParserVal test) {
        try {
            BranchCondition condition = (BranchCondition) test.obj;
            condition.negateOperator();
            BranchInst branchInst = this.theProgram.createBranchInst(condition);
            branchInst.setTarget(this.instructionStack.pop());
        } catch (Exception e) {
            this.error(e);
        }
    }

    protected BranchCondition.RelOperator getRelOp(Token token) throws SyntaxException {
        switch (token.getType()) {
            case EQ:    return BranchCondition.RelOperator.EQ;
            case NOTEQ: return BranchCondition.RelOperator.NOTEQ;
            case GT:    return BranchCondition.RelOperator.GT;
            case GTE:   return BranchCondition.RelOperator.GTE;
            case LT:    return BranchCondition.RelOperator.LT;
            case LTE:   return BranchCondition.RelOperator.LTE;
            default:
                this.error(new SyntaxException(token, "Expected ==, <=, <, >=, > or <>."));
                return BranchCondition.RelOperator.EQ;
        }
    }

    protected void enterScope(ParserVal name) {
        try {
            this.theProgram.enterScope(this.tokenVal(name));
        } catch (Exception e) {
            this.error(e);
        }
    }

    protected void exitScope() {
        try {
            this.theProgram.exitScope();
        } catch (Exception e) {
            this.error(e);
        }
    }

    protected void assertTypes(ParserVal lhs, ParserVal operator, ParserVal rhs) throws IncorrectArgumentTypeException {
        Value op1 = (Value) lhs.obj;
        Value op2 = (Value) rhs.obj;
        Token token = (Token) operator.obj;
        if (op1.getType() != op2.getType()) {
            throw new IncorrectArgumentTypeException(token, op1.getType(), op2.getType());
        }
    }

    protected void error(String s) {
        Fasta.getLogger().error(new SyntaxException(this.currentToken, s));
        this.errorRecoveryMode = true;
    }

    private ParserVal error(Exception e) {
        if (e instanceof FastaException) {
            // Intended error. Report to the user and activate the error recovery mode
            Fasta.getLogger().error(e);
            this.errorRecoveryMode = true;
        } else if (!this.errorRecoveryMode) {
            // We are not in error recovery mode, and it is not a FastaException, it means
            // it is a regular Java exception and it is not the intended behaviour, we should
            // rethrow this and stop the application from continuing.
            throw new RuntimeException(e);
        }
        return new ParserVal(null);
    }

    public void errorIfWithoutParens() {
        this.error("IF condition should be enclosed in parenthesis. You should add the missing parenthesis in the condition. Example: IF (foo <= bar) THEN ...");
    }

    public void errorIfUnclosedParens() {
        this.error("Unclosed parenthesis in IF condition. You should close the right parenthesis in the IF condition. Example: IF (foo <= bar) THEN ...");
    }

    public void errorIfWithoutRelationalOperator() {
        this.error("IF does not have relational operator. You should add a valid relational operator. Example: IF (foo <= bar) THEN ...");
    }

    public void errorLoopWithoutParens() {
        this.error("LOOP..WHILE condition should be enclosed in parenthesis. You should add the missing parenthesis in the condition. Example: LOOP .. WHILE (foo <= bar)");
    }

    public void errorLoopUnclosedParens() {
        this.error("Unclosed parenthesis in IF condition. You should close the right parenthesis in the IF condition. Example: LOOP .. WHILE (foo <= bar)");
    }

    public void errorLoopWithoutRelationalOperator() {
        this.error("LOOP..WHILE condition does not have relational operator. You should add a valid relational operator. Example: LOOP .. WHILE (foo <= bar)");
    }
}
