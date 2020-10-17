package com.ciberman.fastacompiler.ir;

public interface Inst {
    void accept(IRVisitor visitor);
}
