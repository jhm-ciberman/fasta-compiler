package com.ciberman.fastacompiler.ir;

public class MulInst extends BinInst {

    public MulInst(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.mulInst(this);
    }
}
