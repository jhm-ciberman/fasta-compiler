package com.ciberman.fastacompiler.ir;

public interface IRValueVisitor {
    String intConstString(IntConst c);
    String longConstString(LongConst c);
    String strConstString(StrConst c);
    String instrString(Inst instr);
}
