package com.ciberman.fastacompiler.ir;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BranchInst extends Inst {
    private @Nullable final BranchCondition condition;
    private @Nullable Inst target;


    protected BranchInst(@Nullable Inst target) {
        this.condition = null;
        this.target = target;
    }

    protected BranchInst(@Nullable BranchCondition condition, @Nullable Inst target) {
        this.condition = condition;
        this.target = target;
    }

    public @Nullable BranchCondition getCondition() {
        return condition;
    }

    public @Nullable Inst getTarget() {
        return target;
    }

    public void setTarget(@NotNull Inst target) {
        this.target = target;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.branchInst(this);
    }
}
