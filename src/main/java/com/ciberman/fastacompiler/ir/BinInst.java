package com.ciberman.fastacompiler.ir;

import com.ciberman.fastacompiler.out.IRValueStringConverter;

public abstract class BinInst extends Inst implements Value {

    protected Value op1;

    protected Value op2;

    protected BinInst(Value op1, Value op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    public Value getOp1() {
        return op1;
    }

    public Value getOp2() {
        return op2;
    }

    @Override
    public ValueType getType() {
        return this.op1.getType();
    }

    @Override
    public String toPrintableString(IRValueStringConverter valueVisitor) {
        return valueVisitor.getInstrString(this);
    }
}
