package com.ciberman.fastacompiler.ir;

import com.ciberman.fastacompiler.out.IRValueStringConverter;

public abstract class BinInst extends ValueInst {

    protected Value left;

    protected Value right;

    protected BinInst(Value left, Value right) {
        this.left = left;
        this.right = right;
    }

    public Value getLeft() {
        return left;
    }

    public Value getRight() {
        return right;
    }

    @Override
    public ValueType getType() {
        return this.left.getType();
    }

    @Override
    public String toPrintableString(IRValueStringConverter valueVisitor) {
        return valueVisitor.getInstrString(this);
    }
}
