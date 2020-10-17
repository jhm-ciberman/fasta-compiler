package com.ciberman.fastacompiler.ir;

public class IntConst implements Value {
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
    public String toPrintableString(IRValueVisitor constantVisitor) {
        return constantVisitor.intConstString(this);
    }
}
