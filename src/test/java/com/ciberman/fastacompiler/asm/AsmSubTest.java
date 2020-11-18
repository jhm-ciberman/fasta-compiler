package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.program.AsmCode;
import com.ciberman.fastacompiler.asm.program.AsmOp;
import com.ciberman.fastacompiler.ir.IntConst;
import org.junit.jupiter.api.Test;

class AsmSubTest extends AsmBase {

    @Test
    void subInstMemMem() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createSubInst(a, b);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), resolver.saveInMem(a)),
                new AsmOp("sub", resolver.REG_AX.loc16(), resolver.saveInMem(b)),
                new AsmOp("ret"),
        });
    }

    @Test
    void subInstMemReg() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createSubInst(a, b);

        resolver.REG_AX.setContent(b);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_BX.loc16(), resolver.saveInMem(a)),
                new AsmOp("sub", resolver.REG_BX.loc16(), resolver.REG_AX.loc16()),
                new AsmOp("ret"),
        });
    }

    @Test
    void subInstRegMem() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createSubInst(a, b);

        resolver.REG_AX.setContent(a);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_BX.loc16(), resolver.saveInMem(b)),
                new AsmOp("sub", resolver.REG_AX.loc16(), resolver.REG_BX.loc16()),
                new AsmOp("ret"),
        });
    }

    @Test
    void subInstRegReg() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createSubInst(a, b);

        resolver.REG_AX.setContent(a);
        resolver.REG_BX.setContent(b);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("sub", resolver.REG_AX.loc16(), resolver.REG_BX.loc16()),
                new AsmOp("ret"),
        });
    }
}
