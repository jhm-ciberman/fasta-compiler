package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.mem.MemLocation;
import com.ciberman.fastacompiler.asm.mem.MemTable;
import com.ciberman.fastacompiler.asm.reg.Reg;
import com.ciberman.fastacompiler.ir.Value;

public class DataMemLocation extends MemLocation {
    public DataMemLocation(MemTable table, String name, Value value) {
        super(table, name, value);
    }

    @Override
    public void setContent(Value content) {
        this.table.update(content, this);
    }

    @Override
    public boolean isInReg(Reg reg) {
        return false;
    }
}
