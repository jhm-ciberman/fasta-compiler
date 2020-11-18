package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.mem.MemLocation;
import com.ciberman.fastacompiler.asm.mem.MemTable;
import com.ciberman.fastacompiler.asm.reg.Reg;
import com.ciberman.fastacompiler.ir.Value;

public class ConstMemLocation extends MemLocation {

    public ConstMemLocation(MemTable table, String name, Value value) {
        super(table, name, value);
    }

    @Override
    public void setContent(Value content) {
        throw new RuntimeException("Trying to set content to a read only constant memory location");
    }

    @Override
    public boolean isInReg(Reg reg) {
        return false;
    }
}
