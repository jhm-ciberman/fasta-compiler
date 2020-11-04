package com.ciberman.fastacompiler.ir;

public class PrintInst extends Inst {
    private final Value value;

    public PrintInst(Value value) {
        this.value = value;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.printInst(this);
    }

    public Value getValue() {
        return this.value;
    }
}
