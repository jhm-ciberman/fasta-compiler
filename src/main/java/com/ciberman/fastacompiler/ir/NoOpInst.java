package com.ciberman.fastacompiler.ir;

public class NoOpInst extends Inst {
    @Override
    public void accept(IRVisitor visitor) {
        visitor.noopInst(this);
    }
}
