package com.ciberman.fastacompiler.asm.reg;

import com.ciberman.fastacompiler.ir.IntConst;
import com.ciberman.fastacompiler.ir.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RegTableTest {

    @Test
    void createReg() {
        RegTable regTable = new RegTable();
        Reg reg = regTable.createReg("reg32", "reg16");
        Assertions.assertEquals("reg32", reg.loc32().getName());
        Assertions.assertEquals("reg16", reg.loc16().getName());
    }

    @Test
    void requestReg() {
        RegTable regTable = new RegTable();
        Reg reg = regTable.createReg("reg32", "reg16");
        Value value = new IntConst(100);
        RegLocation location = regTable.requestReg(value);
        Assertions.assertNotNull(location);
        Assertions.assertEquals("reg16", location.getName());
    }

    @Test
    void findReg() {
    }

    @Test
    void updateRegContent() {
    }
}