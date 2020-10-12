package com.ciberman.fastacompiler.ir;

public abstract class BinInst implements Inst, Value {

    protected Value op1;

    protected Value op2;

    public BinInst(Value op1, Value op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Type getType() {
        return this.op1.getType();
    }
}
