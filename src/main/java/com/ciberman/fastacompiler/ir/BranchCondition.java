package com.ciberman.fastacompiler.ir;

public class BranchCondition {
    public enum RelOperator {
        GT, LT, GTE, LTE, EQ, NOTEQ,
    }

    private final RelOperator operator;

    private final Value op1;

    private final Value op2;

    protected BranchCondition(Value lhs, RelOperator operator, Value rhs) {
        this.op1 = lhs;
        this.op2 = rhs;
        this.operator = this.negateOperator(operator);
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

    private RelOperator negateOperator(RelOperator operator) {
        switch (operator) {
            case GT:    return RelOperator.LTE;
            case LT:    return RelOperator.GTE;
            case GTE:   return RelOperator.LT;
            case LTE:   return RelOperator.GT;
            case EQ:    return RelOperator.NOTEQ;
            case NOTEQ: return RelOperator.EQ;
        }
        return null;
    }
}
