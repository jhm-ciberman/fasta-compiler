package com.ciberman.fastacompiler.ir;

public abstract class Inst {

    private boolean isLeader = false;

    public boolean isLeader() {
        return isLeader;
    }

    public void markAsLeader() {
        isLeader = true;
    }

    public abstract void accept(IRVisitor visitor);

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
