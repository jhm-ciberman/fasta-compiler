package com.ciberman.fastacompiler.ir;

import org.jetbrains.annotations.NotNull;

public class StrConst implements Value {
    @NotNull private final String value;

    public StrConst(@NotNull String value) {
        this.value = value;
    }

    public @NotNull String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StrConst strConst = (StrConst) o;

        return getValue().equals(strConst.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public ValueType getType() {
        return ValueType.STR;
    }

    @Override
    public String toPrintableString(IRValueVisitor constantVisitor) {
        return constantVisitor.strConstString(this);
    }
}