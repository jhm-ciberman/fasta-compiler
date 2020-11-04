package com.ciberman.fastacompiler.ir;

public class ItolInst extends UnaryInstr {

    protected ItolInst(Value op) {
        super(op);
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.itolInst(this);
    }

    @Override
    public ValueType getType() {
        return ValueType.LONG;
    }
}
