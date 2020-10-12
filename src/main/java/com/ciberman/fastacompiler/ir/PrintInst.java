package com.ciberman.fastacompiler.ir;

import com.ciberman.fastacompiler.ir.constants.StrConst;

public class PrintInst implements Inst {
    private final StrConst string;

    public PrintInst(StrConst string) {
        this.string = string;
    }
}
