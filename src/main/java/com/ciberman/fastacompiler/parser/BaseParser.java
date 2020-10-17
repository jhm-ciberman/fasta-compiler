package com.ciberman.fastacompiler.parser;

import com.ciberman.fastacompiler.errors.LexicalException;
import com.ciberman.fastacompiler.errors.SyntaxException;
import com.ciberman.fastacompiler.errors.UndeclaredVariableException;
import com.ciberman.fastacompiler.errors.UninitializedVariableException;
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

    public BaseParser(Lexer lexer) {
        this.lexer = lexer;
        this.theProgram = new IRProgram();
    }

    protected Token next() throws IOException, LexicalException {
        this.currentToken = this.lexer.getNextToken();
        System.out.println();
        System.out.println("Current token: " + this.currentToken);
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

    protected ParserVal assignOp(ParserVal lhs, ParserVal rhs) throws UndeclaredVariableException {
        Token token = (Token) lhs.obj;
        Symbol symbol = this.theProgram.findSymbolByName(token.getValue());
        if (symbol == null) {
            throw new UndeclaredVariableException(token);
        }

        if (rhs.obj instanceof Inst) {
            symbol.setLastAssignment((Inst) rhs.obj);
        } else {
            AssignInst inst = this.theProgram.createAssignInst((Value) rhs.obj);
            symbol.setLastAssignment(inst);
            return new ParserVal(inst);
        }

        return rhs;
    }

    protected ParserVal id(ParserVal val) throws UndeclaredVariableException, UninitializedVariableException {
        Token token = (Token) val.obj;
        Symbol symbol = this.theProgram.findSymbolByName(token.getValue());
        if (symbol == null) {
            throw new UndeclaredVariableException(token);
        }
        Inst inst = symbol.getLastAssignment();
        if (inst == null) {
            throw new UninitializedVariableException(token);
        }
        return new ParserVal(inst);
    }

    protected ParserVal addOp(ParserVal op1, ParserVal op2) {
        return new ParserVal(this.theProgram.createAddInst((Value) op1.obj, (Value) op2.obj));
    }

    protected ParserVal subOp(ParserVal op1, ParserVal op2) {
        return new ParserVal(this.theProgram.createSubInst((Value) op1.obj, (Value) op2.obj));
    }

    protected ParserVal divOp(ParserVal op1, ParserVal op2) {
        return new ParserVal(this.theProgram.createDivInst((Value) op1.obj, (Value) op2.obj));
    }

    protected ParserVal mulOp(ParserVal op1, ParserVal op2) {
        return new ParserVal(this.theProgram.createMulInst((Value) op1.obj, (Value) op2.obj));
    }

    protected ParserVal negOp(ParserVal op) {
        return new ParserVal(this.theProgram.createNegInst((Value) op.obj));
    }

    protected ParserVal itolOp(ParserVal op) {
        return new ParserVal(this.theProgram.createItolInst((Value) op.obj));
    }

    protected ParserVal printOp(ParserVal op) {
        return new ParserVal(this.theProgram.createPrintInst((StrConst) op.obj));
    }

    protected ParserVal branchCondition(ParserVal lhs, ParserVal operator, ParserVal rhs) throws SyntaxException {
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
        BranchInst branchInst = this.theProgram.createBranchInst((BranchCondition) test.obj);
        this.branchesStack.push(branchInst);
    }

    protected void loopCondition(ParserVal test) {
        BranchInst branchInst = this.theProgram.createBranchInst((BranchCondition) test.obj);
        this.branchesStack.push(branchInst);
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
