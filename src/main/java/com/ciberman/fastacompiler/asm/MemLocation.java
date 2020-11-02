package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.Value;

public class MemLocation implements Location {
    private final String name;
    private final MemTable table;

    public MemLocation(MemTable table, String name) {
        this.table = table;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isReg() {
        return false;
    }

    @Override
    public void setContent(Value content) {
        this.table.updateLocation(content, this);
    }
}
