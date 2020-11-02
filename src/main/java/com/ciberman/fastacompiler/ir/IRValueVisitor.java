package com.ciberman.fastacompiler.ir;

public interface IRValueVisitor<T> {
    T intConstValue(IntConst c);
    T longConstValue(LongConst c);
    T strConstValue(StrConst c);
    T instrValue(Inst instr);
    T symbolValue(Symbol symbol);
}
