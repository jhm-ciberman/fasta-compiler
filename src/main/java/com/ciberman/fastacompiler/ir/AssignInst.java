package com.ciberman.fastacompiler.ir;

public class AssignInst extends UnaryInstr {

    public AssignInst(Value op) {
        super(op);
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.assignInst(this);
    }
}
