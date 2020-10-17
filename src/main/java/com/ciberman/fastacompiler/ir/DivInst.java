package com.ciberman.fastacompiler.ir;

public class DivInst extends BinInst {
    public DivInst(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.divInst(this);
    }
}
