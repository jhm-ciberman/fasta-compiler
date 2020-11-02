package com.ciberman.fastacompiler.ir;

import com.ciberman.fastacompiler.out.IRValueStringConverter;

public abstract class UnaryInstr extends Inst implements Value {

    protected Value op;

    public UnaryInstr(Value op) {
        this.op = op;
    }

    public Value getOp() {
        return this.op;
    }

    @Override
    public ValueType getType() {
        return this.op.getType();
    }

    @Override
    public String toPrintableString(IRValueStringConverter valueVisitor) {
        return valueVisitor.getInstrString(this);
    }
}
