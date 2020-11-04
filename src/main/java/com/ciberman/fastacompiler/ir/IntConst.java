package com.ciberman.fastacompiler.ir;

import com.ciberman.fastacompiler.out.IRValueStringConverter;

public class IntConst extends Const implements Value {
    private final int value;

    public IntConst(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntConst intConst = (IntConst) o;

        return getValue() == intConst.getValue();
    }

    @Override
    public int hashCode() {
        return getValue();
    }

    @Override
    public ValueType getType() {
        return ValueType.INT;
    }

    @Override
    public String toPrintableString(IRValueStringConverter valueVisitor) {
        return valueVisitor.getIntConstString(this);
    }

    @Override
    public String getValueDebugString() {
        return String.valueOf(this.getValue()) + "_i";
    }
}
