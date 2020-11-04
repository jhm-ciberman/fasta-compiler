package com.ciberman.fastacompiler.parser;

import com.ciberman.fastacompiler.errors.*;
import com.ciberman.fastacompiler.ir.*;
import com.ciberman.fastacompiler.lexer.Lexer;
import com.ciberman.fastacompiler.lexer.Token;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class BaseParser {

    private static class IdList extends LinkedList<String> {
        // nothing. This class is because java do not permit to downcast an Object to a generic type.
    }

    private final Lexer lexer;

    private final IRProgram theProgram;

    private Token currentToken;

    private final Stack<BranchInst> branchesStack = new Stack<BranchInst>();
    private final Stack<Inst> instructionStack = new Stack<Inst>();

    public BaseParser(Lexer lexer) {
        this.lexer = lexer;
        this.theProgram = new IRProgram();
    }

    protected Token next() throws IOException, LexicalException {
        this.currentToken = this.lexer.getNextToken();
        return this.currentToken;
    }

    public IRProgram buildProgram() {
        return this.theProgram;
    }

    private String tokenVal(ParserVal val) {
        return ((Token) val.obj).getValue();
    }

    protected ParserVal intConst(ParserVal val) {
        int intConst = Integer.parseInt(this.tokenVal(val));
        return new ParserVal(this.theProgram.addConst(intConst));
    }

    protected ParserVal longConst(ParserVal val) {
        long longConst = Long.parseLong(this.tokenVal(val));
        return new ParserVal(this.theProgram.addConst(longConst));
    }


    protected ParserVal strConst(ParserVal val) {
        return new ParserVal(this.theProgram.addConst(this.tokenVal(val)));
    }

    protected ParserVal newIdList(ParserVal val) {
        IdList list = new IdList();
        list.add(this.tokenVal(val));
        return new ParserVal(list);
    }

    protected ParserVal pushIdToList(ParserVal list, ParserVal id) {
        List<String> theList = (IdList) list.obj;
        theList.add(this.tokenVal(id));
        return list;
    }

    protected void declareSymbols(IdList list, ValueType type) {
        for (String str : list) {
            this.theProgram.declareSymbol(str, type);
        }
    }

    protected void declareIntSymbols(ParserVal list) {
        this.declareSymbols((IdList) list.obj, ValueType.INT);
    }

    protected void declareLongSymbols(ParserVal list) {
        this.declareSymbols((IdList) list.obj, ValueType.LONG);
    }

    protected ParserVal assignOp(ParserVal lhs, ParserVal tokenAssign, ParserVal rhs) throws UndeclaredVariableException, IncorrectArgumentTypeException {
        Token token = (Token) lhs.obj;
        Value value = (Value) rhs.obj;
        Symbol symbol = this.theProgram.findSymbolByName(token.getValue());
        if (symbol == null) {
            throw new UndeclaredVariableException(token);
        }

        if (symbol.getType() != value.getType()) {
            throw new IncorrectArgumentTypeException((Token) tokenAssign.obj, symbol.getType(), value.getType());
        }

        if (value instanceof Const) {
            // Optimize constants
            symbol.setInitialValue((Const) value);
            return new ParserVal(this.theProgram.createNoOpInst());
        } else {
            AssignInst inst = this.theProgram.createAssignInst(symbol, value);
            if (value instanceof ValueInst) {
                symbol.markAsInitialized();
                return rhs;
            } else {
                symbol.markAsInitialized();
                return new ParserVal(inst);
            }
        }
    }

    protected ParserVal id(ParserVal val) throws UndeclaredVariableException, UninitializedVariableException {
        Token token = (Token) val.obj;
        Symbol symbol = this.theProgram.findSymbolByName(token.getValue());
        if (symbol == null) {
            throw new UndeclaredVariableException(token);
        }
        if (! symbol.isInitialized()) {
            throw new UninitializedVariableException(token);
        }
        return new ParserVal(symbol);
    }

    protected ParserVal addOp(ParserVal lhs, ParserVal token, ParserVal rhs) throws IncorrectArgumentTypeException {
        this.assertTypes(lhs, token, rhs);
        return new ParserVal(this.theProgram.createAddInst((Value) lhs.obj, (Value) rhs.obj));
    }

    protected ParserVal subOp(ParserVal lhs, ParserVal token, ParserVal rhs) throws IncorrectArgumentTypeException {
        this.assertTypes(lhs, token, rhs);
        return new ParserVal(this.theProgram.createSubInst((Value) lhs.obj, (Value) rhs.obj));
    }

    protected ParserVal divOp(ParserVal lhs, ParserVal token, ParserVal rhs) throws IncorrectArgumentTypeException {
        this.assertTypes(lhs, token, rhs);
        return new ParserVal(this.theProgram.createDivInst((Value) lhs.obj, (Value) rhs.obj));
    }

    protected ParserVal mulOp(ParserVal lhs, ParserVal token, ParserVal rhs) throws IncorrectArgumentTypeException {
        this.assertTypes(lhs, token, rhs);
        return new ParserVal(this.theProgram.createMulInst((Value) lhs.obj, (Value) rhs.obj));
    }

    protected ParserVal negOp(ParserVal op) throws IncorrectArgumentTypeException {
        return new ParserVal(this.theProgram.createNegInst((Value) op.obj));
    }

    protected ParserVal itolOp(ParserVal tokenItol, ParserVal op) throws SemanticException {
        Value val = (Value) op.obj;
        if (val.getType() != ValueType.INT) {
            throw new SemanticException((Token) tokenItol.obj, "ITOL function requires an INT parameter type. " + val.getType() + " parameter given.");
        }
        return new ParserVal(this.theProgram.createItolInst(val));
    }

    protected ParserVal printOp(ParserVal op) {
        return new ParserVal(this.theProgram.createPrintInst((Value) op.obj));
    }

    protected ParserVal branchCondition(ParserVal lhs, ParserVal operator, ParserVal rhs) throws SyntaxException, IncorrectArgumentTypeException {
        this.assertTypes(lhs, operator, rhs);
        BranchCondition.RelOperator op = this.getRelOp((Token) operator.obj);
        BranchCondition inst = this.theProgram.createBranchCondition((Value) lhs.obj, op, (Value) rhs.obj);
        return new ParserVal(inst);
    }

    protected void ifInst() {
        this.theProgram.createNoOpInst();
        this.branchesStack.pop().setTarget(this.theProgram.getLastInst());
    }

    protected void ifThenBlock() {
        BranchInst branchInst = this.theProgram.createBranchInst();
        this.theProgram.createNoOpInst();
        this.branchesStack.pop().setTarget(this.theProgram.getLastInst());
        this.branchesStack.push(branchInst);
    }

    protected void ifCondition(ParserVal test) {
        BranchCondition condition = (BranchCondition) test.obj;
        condition.negateOperator();
        BranchInst branchInst = this.theProgram.createBranchInst(condition);
        this.branchesStack.push(branchInst);
    }

    protected void loopKeyword() {
        this.instructionStack.push(this.theProgram.createNoOpInst());
    }

    protected void loopCondition(ParserVal test) {
        BranchCondition condition = (BranchCondition) test.obj;
        condition.negateOperator();
        BranchInst branchInst = this.theProgram.createBranchInst(condition);
        branchInst.setTarget(this.instructionStack.pop());
    }

    protected BranchCondition.RelOperator getRelOp(Token token) throws SyntaxException {
        switch (token.getType()) {
            case EQ:    return BranchCondition.RelOperator.EQ;
            case NOTEQ: return BranchCondition.RelOperator.NOTEQ;
            case GT:    return BranchCondition.RelOperator.GT;
            case GTE:   return BranchCondition.RelOperator.GTE;
            case LT:    return BranchCondition.RelOperator.LT;
            case LTE:   return BranchCondition.RelOperator.LTE;
            default: throw new SyntaxException(token, "Expected ==, <=, <, >=, > or <>.");
        }
    }

    protected void enterScope(ParserVal name) {
        this.theProgram.enterScope(this.tokenVal(name));
    }

    protected void exitScope() {
        this.theProgram.exitScope();
    }

    protected void assertTypes(ParserVal lhs, ParserVal operator, ParserVal rhs) throws IncorrectArgumentTypeException {
        Value op1 = (Value) lhs.obj;
        Value op2 = (Value) rhs.obj;
        Token token = (Token) operator.obj;
        if (op1.getType() != op2.getType()) {
            throw new IncorrectArgumentTypeException(token, op1.getType(), op2.getType());
        }
    }

    protected void error(String s) throws SyntaxException {
        throw new SyntaxException(this.currentToken, s);
    }

    public void errorIfWithoutParens() throws SyntaxException {
        this.error("IF condition should be enclosed in parenthesis. You should add the missing parenthesis in the condition. Example: IF (foo <= bar) THEN ...");
    }

    public void errorIfUnclosedParens() throws SyntaxException {
        this.error("Unclosed parenthesis in IF condition. You should close the right parenthesis in the IF condition. Example: IF (foo <= bar) THEN ...");
    }

    public void errorIfWithoutRelationalOperator() throws SyntaxException {
        this.error("IF does not have relational operator. You should add a valid relational operator. Example: IF (foo <= bar) THEN ...");
    }

    public void errorLoopWithoutParens() throws SyntaxException {
        this.error("LOOP..WHILE condition should be enclosed in parenthesis. You should add the missing parenthesis in the condition. Example: LOOP .. WHILE (foo <= bar)");
    }

    public void errorLoopUnclosedParens() throws SyntaxException {
        this.error("Unclosed parenthesis in IF condition. You should close the right parenthesis in the IF condition. Example: LOOP .. WHILE (foo <= bar)");
    }

    public void errorLoopWithoutRelationalOperator() throws SyntaxException {
        this.error("LOOP..WHILE condition does not have relational operator. You should add a valid relational operator. Example: LOOP .. WHILE (foo <= bar)");
    }
}
