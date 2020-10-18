package com.ciberman.fastacompiler.ir;

public class AssignInst extends UnaryInstr {

    private Symbol symbol;

    public AssignInst(Symbol symbol, Value op) {
        super(op);
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.assignInst(this);
    }
}
