package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.Value;

public abstract class MemLocation implements Location {
    protected final String name;
    protected final MemTable table;
    protected final Value value;

    public MemLocation(MemTable table, String name, Value value) {
        this.table = table;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public boolean isReg() {
        return false;
    }

    @Override
    public abstract void setContent(Value content);

    @Override
    public void markAsFree() {
        // Nothing. This method is only implemented in RegLocation
        // But it is also here to make easier to manage Locations
    }

    public String toString() {
        return this.getName();
    }
}
