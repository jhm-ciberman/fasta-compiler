package com.ciberman.fastacompiler.ir;

import com.ciberman.fastacompiler.out.IRValueStringConverter;

public interface Value {
    ValueType getType();
    String toPrintableString(IRValueStringConverter valueVisitor);
}
