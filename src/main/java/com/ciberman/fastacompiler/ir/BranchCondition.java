package com.ciberman.fastacompiler.ir;

public class BranchCondition {
    public enum RelOperator {
        GT, LT, GTE, LTE, EQ, NOTEQ,
    }

    private RelOperator operator;

    private final Value op1;

    private final Value op2;

    protected BranchCondition(Value lhs, RelOperator operator, Value rhs) {
        this.op1 = lhs;
        this.op2 = rhs;
        this.operator = operator;
    }

    public Value getOp1() {
        return op1;
    }

    public Value getOp2() {
        return op2;
    }

    public RelOperator getOperator() {
        return operator;
    }

    public void negateOperator() {
        switch (this.operator) {
            case GT:    this.operator = RelOperator.LTE;   break;
            case LT:    this.operator = RelOperator.GTE;   break;
            case GTE:   this.operator = RelOperator.LT;    break;
            case LTE:   this.operator = RelOperator.GT;    break;
            case EQ:    this.operator = RelOperator.NOTEQ; break;
            case NOTEQ: this.operator = RelOperator.EQ;    break;
        }
    }
}
