package com.ciberman.fastacompiler.ir;

public abstract class ValueInst extends Inst implements Value {
    @Override
    public String getDebugString() {
        return this.getClass().getSimpleName();
    }
}
