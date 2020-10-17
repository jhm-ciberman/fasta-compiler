package com.ciberman.fastacompiler.ir;

public class SubInst extends BinInst {
    public SubInst(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.subInst(this);
    }
}
