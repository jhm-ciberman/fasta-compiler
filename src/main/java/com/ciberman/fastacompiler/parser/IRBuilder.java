package com.ciberman.fastacompiler.parser;

import com.ciberman.fastacompiler.ir.IRProgram;
import com.ciberman.fastacompiler.ir.Value;
import com.ciberman.fastacompiler.ir.constants.IntConst;
import com.ciberman.fastacompiler.ir.constants.LongConst;
import com.ciberman.fastacompiler.ir.constants.StrConst;

public class IRBuilder {

    protected IRProgram theProgram;

    public IRBuilder() {
        this.theProgram = new IRProgram();
    }

    public ParserVal intConst(ParserVal val) {
        IntConst c = this.theProgram.createIntConst(Integer.parseInt(val.sval));
        return new ParserVal(c);
    }

    public ParserVal longConst(ParserVal val) {
        LongConst c = this.theProgram.createLongConst(Long.parseLong(val.sval));
        return new ParserVal(c);
    }

    public ParserVal strConst(ParserVal val) {
        StrConst c = this.theProgram.createStrConst(val.sval);
        return new ParserVal(c);
    }

    public ParserVal addOp(ParserVal op1, ParserVal op2) {
        Value v = this.theProgram.createAddInst((Value) op1, (Value) op2);
        return new ParserVal(v);
    }
}
