package com.ciberman.fastacompiler.ir;

public class PrintInst implements Inst {
    private final StrConst string;

    public PrintInst(StrConst string) {
        this.string = string;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.printInst(this);
    }

    public StrConst getString() {
        return this.string;
    }
}
