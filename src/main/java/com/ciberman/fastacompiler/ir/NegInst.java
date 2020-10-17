package com.ciberman.fastacompiler.ir;

public class NegInst extends UnaryInstr {

    public NegInst(Value op) {
        super(op);
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.negInst(this);
    }
}
