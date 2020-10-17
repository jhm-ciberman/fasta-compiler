package com.ciberman.fastacompiler.ir;

public class NoOpInst implements Inst {
    @Override
    public void accept(IRVisitor visitor) {
        visitor.noopInst(this);
    }
}
