package com.ciberman.fastacompiler.ir.constants;

import com.ciberman.fastacompiler.ir.Value;

public class LongConst implements Value {
    private final long value;

    public LongConst(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongConst longConst = (LongConst) o;

        return getValue() == longConst.getValue();
    }

    @Override
    public int hashCode() {
        return (int) (getValue() ^ (getValue() >>> 32));
    }

    @Override
    public Type getType() {
        return Type.LONG;
    }
}
