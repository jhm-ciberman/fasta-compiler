package com.ciberman.fastacompiler.ir;

public interface Value {
    ValueType getType();
    String toPrintableString(IRValueVisitor constantVisitor);
}
