package com.ciberman.fastacompiler.ir;

public abstract class UnaryInstr implements Inst, Value {

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
    public String toPrintableString(IRValueVisitor constantVisitor) {
        return constantVisitor.instrString(this);
    }
}
