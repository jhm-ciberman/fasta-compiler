package com.ciberman.fastacompiler.ir.constants;

import com.ciberman.fastacompiler.ir.Value;

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
    public Type getType() {
        return Type.INT;
    }
}
