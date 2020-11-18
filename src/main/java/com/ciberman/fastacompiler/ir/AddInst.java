package com.ciberman.fastacompiler.ir;

public class AddInst extends BinInst {
    public AddInst(Value op1, Value op2) {
        super(op1, op2);
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.addInst(this);
    }
}
