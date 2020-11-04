package com.ciberman.fastacompiler.ir;

import com.ciberman.fastacompiler.out.IRValueStringConverter;

public interface Value {
    ValueType getType();
    String getDebugString();
    String toPrintableString(IRValueStringConverter valueVisitor);
}
