package com.ciberman.fastacompiler.ir;

public abstract class Const implements Value {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getDebugString() {
        return this.getClass().getSimpleName();
    }

    public abstract String getValueDebugString();
}
